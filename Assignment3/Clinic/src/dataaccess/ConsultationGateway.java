package dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.Consultation;
import entities.Doctor;
import entities.Patient;

public class ConsultationGateway {
	private static Connection connection = DBConnection.connect();;


	public static void deleteConsultation(int id){
		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("DELETE FROM Consultation WHERE idConsultation = " + id);
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
			pst = connection.prepareStatement("SELECT MAX(idConsultation) AS maxid FROM Consultation;");
			rs = pst.executeQuery();

			if(rs.next()){
				id = rs.getInt("maxid");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	public static void insertConsultation(Consultation consultation){
		int idPatient = consultation.getPatient().getIdPatient();
		int idDoctor = consultation.getDoctor().getIdUser();
		Date date = consultation.getDate();
		String status = consultation.getStatus();
		String result = consultation.getResult();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		PreparedStatement pst;
		int id = getMaxId() + 1;
		consultation.setIdConsultation(id);

		try{
			pst = connection.prepareStatement("INSERT INTO Consultation VALUES (" + id + ", " + idPatient + ", " + idDoctor + ", '" 
												+ dateFormat.format(date) + "', '" + status + "', '" + result + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static ArrayList<Consultation> readConsultations(){
		ArrayList<Consultation> consultations = new ArrayList<>();

		PreparedStatement pst;
		ResultSet rs;

		try{
			//pst = connection.prepareStatement("SELECT * FROM Consultation");
			pst = connection.prepareStatement("SELECT * FROM "
					+ "Consultation c "
					+ "INNER JOIN "
					+ "Patient p "
					+ "ON c.idPatient = p.idPatient "
					+ "INNER JOIN "
					+ "ClinicUser u "
					+ "ON c.idDoctor = u.idUser;"
					);
			rs = pst.executeQuery();

			while(rs.next()){
				int id = rs.getInt("c.idConsultation");
				int idPatient = rs.getInt("c.idPatient");
				int idUser = rs.getInt("c.idDoctor");
				Date date = rs.getDate("c.dateConsultation");
				String status = rs.getString("c.statusConsultation");
				String result = rs.getString("c.result");
				
				String name = rs.getString("p.namePatient");
				String cardNo = rs.getString("p.cardNo");
				String cnp = rs.getString("p.cnp");
				Date birthDate = rs.getDate("p.birthDate");
				String address = rs.getString("p.address");

				Patient patient = new Patient(name, cardNo, cnp, birthDate, address);
				patient.setIdPatient(idPatient);
				
				String username = rs.getString("u.username");
				String password = rs.getString("u.passwrd");
				
				
				Doctor doctor = new Doctor(username, password);
				doctor.setIdUser(idUser);

				Consultation c = new Consultation(patient, doctor, date);
				c.setIdConsultation(id);
				c.setStatus(status);
				c.setResult(result);
				
				consultations.add(c);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return consultations;
	}


	public static void updateConsultation(Consultation c){
		int idC = c.getIdConsultation();
		int idP = c.getPatient().getIdPatient();
		int idD = c.getDoctor().getIdUser();
		Date date = c.getDate();
		String status = c.getStatus();
		String result = c.getResult();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		PreparedStatement pst;

		try{
			pst = connection.prepareStatement("UPDATE Consultation SET "
					+ "								idPatient = " + idP + ", "
					+ "								idDoctor = " + idD + ", "
					+ "								dateConsultation = '" + dateFormat.format(date) + "', "
					+ "								statusConsultation = '" + status + "', "
					+ "								result = '" + result + "' WHERE idConsultation = " + idC); 
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
}
