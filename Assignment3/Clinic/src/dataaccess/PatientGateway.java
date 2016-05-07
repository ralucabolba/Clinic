package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.Patient;

public class PatientGateway {
	private static Connection connection = DBConnection.connect();;


	public static void deletePatient(int idPatient){
		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("DELETE FROM Patient WHERE idPatient = " + idPatient);
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

	private static int getMaxId(){
		PreparedStatement pst;
		ResultSet rs;

		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idPatient) AS maxid FROM Patient;");
			rs = pst.executeQuery();

			if(rs.next()){
				id = rs.getInt("maxid");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	public static void insertPatient(Patient patient){
		String name = patient.getName();
		String cardNo = patient.getCardNo();
		String cnp = patient.getCnp();
		Date birthDate = patient.getBirthDate();
		String address = patient.getAddress();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		PreparedStatement pst;
		int id = getMaxId() + 1;
		patient.setIdPatient(id);

		try{
			pst = connection.prepareStatement("INSERT INTO Patient VALUES (" + id + ", '" + name + "', '" + cardNo + "', '" + cnp + "', '" + dateFormat.format(birthDate) + "', '" + address + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static ArrayList<Patient> readPatients(){
		ArrayList<Patient> patients = new ArrayList<>();

		PreparedStatement pst;
		ResultSet rs;

		try{
			pst = connection.prepareStatement("SELECT * FROM Patient");
			rs = pst.executeQuery();

			while(rs.next()){
				int idPatient = rs.getInt("idPatient");
				String name = rs.getString("namePatient");
				String cardNo = rs.getString("cardNo");
				String cnp = rs.getString("cnp");
				Date date = rs.getDate("birthDate");
				String address = rs.getString("address");

				Patient patient = new Patient(name, cardNo, cnp, date, address);
				patient.setIdPatient(idPatient);

				patients.add(patient);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return patients;
	}


	public static void updatePatient(Patient patient){
		int id = patient.getIdPatient();
		String name = patient.getName();
		String cardNo = patient.getCardNo();
		String cnp = patient.getCnp();
		Date birthDate = patient.getBirthDate();
		String address = patient.getAddress();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("UPDATE Patient SET "
					+ "								namePatient = '" + name + "', "
					+ "								cardNo = '" + cardNo + "', "
					+ "								cnp = '" + cnp + "', "
					+ "								birthDate = '" + dateFormat.format(birthDate) + "', "
					+ "								address = '" + address + "' WHERE idPatient = " + id); 
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
}
