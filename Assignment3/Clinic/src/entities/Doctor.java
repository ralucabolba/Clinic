package entities;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import observer.Observer;

public class Doctor extends User implements Observer {
	//private int idDoctor;
	//private String name;
	
	public Doctor(String username, String password) {
		super(username, password);
	}

	@Override
	public void update(String message) {
		Message.setMessage("Doctor " + super.username + ", " + message + " is waiting for you");
		Message.launch(Message.class);
		//System.out.println("Doctor " + super.username + ", " + message + " has checked in");
	}
	
	public String getType(){
		return "doctor";
	}

}
