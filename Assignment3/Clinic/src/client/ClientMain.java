package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application{

	private Window window;
	private ClientController controller;
	
	private Stage primaryStage;
	
	public ClientMain(){
		window = new Window(this);
	}
	
	public static void main(String[] args){
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Socket socket = null;
		
		try {
			socket = new Socket("localhost", 12);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to server");

		
		primaryStage = stage;
		window.start(primaryStage);
		controller = new ClientController(window, socket, in, out);
	}

}
