package server;
import java.util.Date;

import dataaccess.UserGateway;
import entities.Clinic;
import java.io.*;
import java.net.*;

public class ServerMain {
	
    
	public static void main(String[] args){	    
		ServerSocket serverSocket = null;
		
		boolean listeningSocket = true;
		
		try{
			serverSocket = new ServerSocket(12);
			
			System.out.println("Waiting for a client...");
			
			while(listeningSocket){
				Socket clientSocket = serverSocket.accept();
				ClientThread ct = new ClientThread(clientSocket);
				ct.start();
			}
			
			serverSocket.close();
		}
		catch(IOException e){
			System.exit(1);
		}
		
		
	    
	}
	
	
}
