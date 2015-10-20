package grooveberry_consoleclient;

import grooveberry_consoleclient.client.net.ClientTreatment;
import grooveberry_consoleclient.client.net.FileDownload;
import grooveberry_consoleclient.client.net.FileUpload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class ClientApp {
	public static final int SERVER_COMMANDE_PORT = 2009;
	public static final int SERVER_TRANSFERT_PORT = 3009;
	
	public static void main(String[] args) {		
		try {
			Socket socketCommande = new Socket(InetAddress.getLocalHost(), ClientApp.SERVER_COMMANDE_PORT);
			
			PipedOutputStream commandeOutput = new PipedOutputStream();
			PipedInputStream  fileInput  = new PipedInputStream(commandeOutput);
			
			if (socketCommande.isConnected() && socketCommande.isBound()) {

				
				System.out.print("[System] Init thread commandes... ");
				ObjectOutputStream out = new ObjectOutputStream(socketCommande.getOutputStream());
				out.flush();
				out.reset();
                System.out.print("1 ");
				ObjectInputStream in = new ObjectInputStream(socketCommande.getInputStream());
                System.out.print("2 ");
				
				Thread treatmentThread = new Thread(new ClientTreatment(in, out , commandeOutput));
				treatmentThread.start();
				System.out.println("OK");
			} else {
                System.out.println("ERREUR");
            }
			
			Socket socketFile = new Socket(InetAddress.getLocalHost(), ClientApp.SERVER_TRANSFERT_PORT);
			if (socketFile.isConnected() && socketFile.isBound()) {
				System.out.print("[System] Init thread download... ");
				ObjectInputStream fileIn = new ObjectInputStream(socketFile.getInputStream());

				Thread fileDownloadThread = new Thread(new FileDownload(fileIn));
				fileDownloadThread.start();
				System.out.println("OK");
				
				System.out.print("[System] Init thread upload... ");
				ObjectOutputStream fileOut = new ObjectOutputStream(socketFile.getOutputStream());
				fileOut.flush();
				fileOut.reset();
				
				Thread fileUploadThread = new Thread(new FileUpload(fileOut, fileInput));
				fileUploadThread.start();
				System.out.println("OK");
			} else {
                System.out.println("ERREUR");
            }
			
			
			
			//socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
