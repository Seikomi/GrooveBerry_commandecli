package grooveberry_server.client.net;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;

public class FileUpload implements Runnable {
	private ObjectOutputStream out;
	
	private PipedInputStream in;
	
	public FileUpload(ObjectOutputStream out, PipedInputStream input) {
		this.out = out;
		this.in = input;
	}


	@Override
	public void run() {
		int data = 0;
		try {
			StringBuilder stringBuilder = new StringBuilder();
			data = in.read();
			while (data != -1) {
				
				if(data == Character.valueOf('\n')) {
					String fileToSendName = stringBuilder.toString();
					/*
					 *  TODO Vérifier l'existance du fichier à envoyer
					 *  NEED la liste des fichiers présent sur le client
					 */
	            	sendFile(fileToSendName);
	            	stringBuilder.delete(0, stringBuilder.length());
	        	} else {
	        		stringBuilder.append((char) data);
	        	}
				data = in.read();
			}
		} catch (IOException e) {
			System.out.println("ERREUR PIPE");
			e.printStackTrace();
		}
	}


	private void sendFile(String path) {
		File file = new File(path);
		try {
			this.out.writeObject(file);
			this.out.flush();
			this.out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
