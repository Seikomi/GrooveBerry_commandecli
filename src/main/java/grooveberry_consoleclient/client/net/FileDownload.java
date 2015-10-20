package grooveberry_consoleclient.client.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileDownload implements Runnable {
	private ObjectInputStream in;

	public FileDownload(ObjectInputStream in) {
		this.in = in;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				File fileReceive = (File) this.in.readObject();
				System.out.println(fileReceive.getName());
				
				File newFile = new File("src/main/resources/ClientFiles/" + fileReceive.getName());
				
				if (!newFile.exists()) {
					fileCopy(fileReceive, newFile);
					System.out.println("[System] Download of " + newFile.getName() + "... done");
				} else {
					System.out.println("[Warning] The file " + newFile.getName() + " already exists");
				}	
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
	}
	
	private void fileCopy(File source, File destination) {
		FileInputStream sourceStream = null;
		FileOutputStream destinationStream = null;
		try {
			destination.createNewFile();
		
			sourceStream = new FileInputStream(source);
			destinationStream = new FileOutputStream(destination);
	
			byte buffer[] = new byte[1024];
			int numberOfReadingByte;
			while((numberOfReadingByte = sourceStream.read(buffer)) != -1) {
				destinationStream.write(buffer, 0, numberOfReadingByte);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				sourceStream.close();
				destinationStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
