package entities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import dataaccess.ConsultationGateway;
import dataaccess.PatientGateway;
import dataaccess.UserGateway;

public class Clinic {
	private ArrayList<User> users;
	private ArrayList<Patient> patients;
	private ArrayList<Consultation> consultations;
	
	private static Clinic instance = new Clinic();
	
	private Clinic(){
		users = new ArrayList<>();
		patients = new ArrayList<>();
		consultations = new ArrayList<>();
		
		setUsers(UserGateway.readUsers());
		setPatients(PatientGateway.readPatients());
		setConsultations(ConsultationGateway.readConsultations());
	}

	public static Clinic getInstance(){
		return instance;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}

	public ArrayList<Consultation> getConsultations() {
		return consultations;
	}

	public void setConsultations(ArrayList<Consultation> consultations) {
		this.consultations = consultations;
	}
	
	public User getUserByUsername(String username){
		if(users == null){
			return null;
		}
		
		for(User user : users){
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		
		return null;
	}
	
//	public boolean existsPatient(String cnp){
//		if (patients == null){
//			return false;
//		}
//		
//		for(Patient patient : patients){
//			if(patient.getCnp().equals(cnp)){
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
	public User getUserById(int idUser){
		if(users == null){
			return null;
		}
		
		for(User user : users){
			if(user.getIdUser() == idUser){
				
				return user;
			}
		}
		
		return null;
	}
	
	public Patient getPatientById(int id){
		if(patients == null){
			return null;
		}
		
		for(Patient p : patients){
			if(p.getIdPatient() == id){
				
				return p;
			}
		}
		
		return null;
	}
	
	public Consultation getConsultationById(int id){
		if(consultations == null){
			return null;
		}
		
		for(Consultation c : consultations){
			if(c.getIdConsultation() == id){
				
				return c;
			}
		}
		
		return null;
	}
	
	public boolean addUser(String username, String password, String type){
		if(this.getUserByUsername(username) != null){
			return false;
		}
		
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
		
		UserGateway.insertUser(user);
		
		return users.add(user);
	}
	
	public boolean addPatient(String name, String cardNo, String cnp, Date birthDate, String address){
		if(getPatientByCnp(cnp) != null){
			return false;
		}
		
		Patient p = new Patient(name, cardNo, cnp, birthDate, address);
		PatientGateway.insertPatient(p);
		
		return patients.add(p);
	}
	
	public boolean addConsultation(String cnp, String username, Date date){
		Patient p = getPatientByCnp(cnp);
		User u = getUserByUsername(username);
		
		Consultation c = new Consultation(p, (Doctor)u, date);
		ConsultationGateway.insertConsultation(c);
		
		return consultations.add(c);
	}
	
	public boolean deleteUser(int idUser){
		User user = getUserById(idUser);
		if(user == null){
			return false;
		}
		
		UserGateway.deleteUser(idUser);
		return users.remove(user);
	}
	
	public boolean deletePatient(int idPatient){
		Patient p = getPatientById(idPatient);
		if(p == null){
			return false;
		}
		
		PatientGateway.deletePatient(idPatient);
		return patients.remove(p);
	}
	
	public boolean deleteConsultation(int idConsultation){
		Consultation c = getConsultationById(idConsultation);
		if(c == null){
			return false;
		}
		
		ConsultationGateway.deleteConsultation(idConsultation);
		return consultations.remove(c);
	}
	
	public User searchUser(String username, String password){
		
		if(users == null){
			return null;
		}
		
		for(User user: this.users){
			if(user.login(username, password)){
				return user;
			}
		}
		
		return null;
	}
	
	public Patient getPatientByCnp(String cnp){
		if(users == null){
			return null;
		}
		
		for(Patient p : patients){
			if(p.getCnp().equals(cnp)){
				return p;
			}
		}
		
		return null;
	}
	
	public List<Consultation> getDoctorConsultation(Doctor doctor){
		List<Consultation> list = new ArrayList<>();
		if(consultations != null){
			for(Consultation c : consultations){
				if(c.getDoctor().getIdUser() == doctor.getIdUser()){
					list.add(c);
				}
			}
		}
		
		return list;
	}
	public List<Date> getDoctorAvailability(Doctor doctor){
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(today);
		
		List<Date> dateList = new ArrayList<>();
		try {
			dateList.add(sdf.parse(dt));
			for(int i=1;i<=27;i++){	
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dt));
				c.add(Calendar.DATE, 1);  // number of days to add
				dt = sdf.format(c.getTime());  // dt is now the new date
				dateList.add(sdf.parse(dt));
			}
			
			List<Consultation> list = getDoctorConsultation(doctor);
			
			for(Consultation c : list){
				Date cd = c.getDate();
				dateList.remove(sdf.parse(sdf.format(cd)));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		return dateList;
	}
}
