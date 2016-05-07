package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Admin;
import entities.Doctor;
import entities.Secretary;
import entities.User;

public class UserGateway {
	private static Connection connection = DBConnection.connect();;


	/**
	 * Method that deletes an user from database
	 * @param idUser
	 */
	public static void deleteUser(int idUser){
		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("DELETE FROM ClinicUser WHERE idUser = " + idUser);
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

	private static int getMaxUserId(){
		PreparedStatement pst;
		ResultSet rs;

		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idUser) AS maxid FROM ClinicUser;");
			rs = pst.executeQuery();

			if(rs.next()){
				id = rs.getInt("maxid");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	public static void insertUser(User user){
		String username = user.getUsername();
		String password = user.getPassword();
		String type = user.getType();

		PreparedStatement pst;
		int idUser = getMaxUserId() + 1;
		user.setIdUser(idUser);

		try{
			pst = connection.prepareStatement("INSERT INTO ClinicUser VALUES (" + idUser + ", '" + username + "', '" + password + "', '" + type + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static ArrayList<User> readUsers(){
		ArrayList<User> users = new ArrayList<>();

		PreparedStatement pst;
		ResultSet rs;

		try{
			pst = connection.prepareStatement("SELECT * FROM ClinicUser");
			rs = pst.executeQuery();

			while(rs.next()){
				int idUser = rs.getInt("idUser");
				String username = rs.getString("username");
				String password = rs.getString("passwrd");
				String type = rs.getString("typeUser");

				User user = null;

				if("administrator".equals(type)){
					user = new Admin(username, password);
				}
				else if("doctor".equals(type)){
					user = new Doctor(username, password);
				}
				else if("secretary".equals(type)){
					user = new Secretary(username, password);
				}

				user.setIdUser(idUser);

				users.add(user);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return users;
	}

	/**
	 * Method that updates an user
	 * @param idUser
	 * @param username
	 * @param password
	 * @param type
	 */
	public static void updateUser(User user){
		int idUser = user.getIdUser();
		String username = user.getUsername();
		String type = user.getType();


		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("UPDATE ClinicUser SET "
					+ "								username = '" + username + "', "
					+ "								typeUser = '" + type + "' WHERE idUser = " + idUser); 
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

}
