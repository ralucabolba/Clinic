package entities;

public class Secretary extends User{

	public Secretary(String username, String password) {
		super(username, password);
	}

	public String getType(){
		return "secretary";
	}
	
	public void checkinPatient(Patient p){
		p.checkin();
	}
}
