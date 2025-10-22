import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SpliterJoiner {
	public void split(String source , int psize) {
		File sourceFile = new File(source);
		if (!sourceFile.exists()) {
			System.out.println("Khong ton tai File");
			return;
			
		}
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
			byte[] buffer = new byte[psize];
			int bytesRead;
			int partNum = 1;
			int index = source.lastIndexOf('.');
			
			String baseName = ( index > 0 )? source.substring(0,index) : source;
			
			while ((bytesRead = bis.read(buffer))>0) {
				String partName = String.format(" " , baseName, partNum++);
				try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(partName))){
					bos.write(buffer, 0, bytesRead);
				}
				System.out.println("Da tao file"+partName);
			}
			System.out.println("Hoan tat chia file");
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}
	public void join(String partFileName) {
		File firstPart = new File(partFileName);
		if (!firstPart.exists()) {
			System.out.println("File thanh phan dau tien khong ton tai");
			return;
			
		}
		String baseName = partFileName.substring(0,partFileName.lastIndexOf('.'));
				
		File parentDir = firstPart.getParentFile() ;
		if(parentDir == null) parentDir = new File(".");
		
		File[]partFiles = parentDir.listFiles((dir, name)
				-> name.matches(baseName.replace("\\", "\\\\")+""));
		
		if (partFiles == null || partFiles.length == 0) {
			System.out.println("khong tim thay cac file thanh phan!");
			return;
			
		}
		Arrays.sort(partFiles, Comparator.comparing(File::getName));
		
		File outputFile = new File(baseName+"_joined" + getExtension(baseName));
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			for (File part : partFiles) {
				try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(part))) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = bis.read(buffer))>0) {
						bos.write(buffer, 0 , bytesRead);
					}
			}
				System.out.println("Da ghep" + part.getName());
		}
		System.out.println("Da tao file ghep hoan chinh"+outputFile.getName());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private static String getExtension(String fileName) {
		int idx = fileName.lastIndexOf('.');
		return(idx>=0)? fileName.substring((idx)):"";
	}
	public static void main(String[] args) {
		//SpliterJoiner()
	}

}
