package entities;

public class Admin extends User{

	public Admin(String username, String password) {
		super(username, password);
	}

	public String getType(){
		return "administrator";
	}
}
