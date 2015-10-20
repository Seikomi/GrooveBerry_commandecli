package grooveberry_consoleclient.client.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientTreatment implements Runnable {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private PipedOutputStream pipedOutput;
	
	public ClientTreatment(ObjectInputStream in, ObjectOutputStream out, PipedOutputStream pipedOutput) {
		this.in = in;
		this.out = out;
		this.pipedOutput = pipedOutput;
	}

	@Override
	public void run() {
		try {
			
			while(true) {
				Scanner sc = new Scanner(System.in);
				Object receivingObject = this.in.readObject();
			
				if (receivingObject instanceof String) {
					System.out.println("[Server] " + (String) receivingObject);
				} else {
					System.out.println("SEVERE received data type is invalide");
				}
			
				System.out.print(">> ");
				
				String sendingMessage = sc.nextLine();
				
				StringTokenizer stringTokeniser = new StringTokenizer(sendingMessage);
				String commandeSend = stringTokeniser.nextToken();
				if (commandeSend.equals("#UPLOAD")) {
					while (stringTokeniser.hasMoreTokens()) {
						String name = stringTokeniser.nextToken();
						uploadFile(name);
					}
				}
				
				
				try {
					this.out.writeObject(sendingMessage);
					this.out.flush();
					this.out.reset();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}

	private void uploadFile(String path) {
		try {
			this.pipedOutput.write((path + "\n").getBytes());
			this.pipedOutput.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
