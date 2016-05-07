package entities;
import dataaccess.UserGateway;

public abstract class User {
	protected int idUser;
	protected String username;
	protected String password;
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the idUser
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		UserGateway.updateUser(this);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean login(String username, String password){
		return this.username.equals(username) && this.password.equals(password);
	}
	
	
	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", username=" + username + ", password=" + password + ", type=" + getType() + "]";
	}

	public abstract String getType();
}
