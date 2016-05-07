package entities;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Message extends Application{

	private static String message;
	
	@Override
	public void start(Stage arg0) throws Exception {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("A pacient has checked in.");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static void setMessage(String newmessage){
		message = newmessage;
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
