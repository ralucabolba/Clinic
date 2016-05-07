package server;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
	private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    
	private Socket socket;
	private ServerController controller;
	
	public ClientThread(Socket socket){
		this.socket = socket;
		try{
	        in = new ObjectInputStream(this.socket.getInputStream());
	        out = new ObjectOutputStream(this.socket.getOutputStream());
	        controller = new ServerController(in, out);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
    
	public void run(){ 
		boolean connected = true;
	    while(connected){
	    	try{
	    		String input = (String) Communication.getObjectFromClient(in);
	    		//Communication.sendToClient(out, controller.processInput(input));
	    		controller.processInput(input);
	    		//String u = getDataFromClient(in);
		        //String p = getDataFromClient(in);
		        
		        //sendToClient(out, "log in");
	    	}
	    	catch(SocketException se){
	    		connected = false;
	    		//se.printStackTrace();
	    	}
	    	catch(Exception e){
	    		connected = false;
		    	//e.printStackTrace();
		    }
	    }
	}
}
