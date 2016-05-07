package server;

import entities.*;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class ServerController {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private Clinic clinic;
	private User currentUser;
	
	public ServerController(ObjectInputStream in, ObjectOutputStream out){
		this.in = in;
		this.out = out;
		this.clinic = Clinic.getInstance();
	}
	
	public void processInput(String input) throws IOException{
		if(input.equals("login")){
			if(loginUser()){
				Communication.sendObjectToClient(out, currentUser.getType());
				if(currentUser.getType().equals("administrator")){
					Communication.sendObjectToClient(out, getUsers());
				}
				else if(currentUser.getType().equals("secretary")){
					Communication.sendObjectToClient(out, getPatients());
					Communication.sendObjectToClient(out, getConsultations());
					Communication.sendObjectToClient(out, getPatientCnps());
					Communication.sendObjectToClient(out, getDoctorsUsernames());
					Communication.sendObjectToClient(out, getConsultationIds());
				}
				else if(currentUser.getType().equals("doctor")){
					Communication.sendObjectToClient(out, getPastConsultation(currentUser));
					Communication.sendObjectToClient(out, getPatientCnps());
					Communication.sendObjectToClient(out, getDoctorAvailability(currentUser.getUsername()));
				}
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("adduser")){
			if(addUser()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getUsers());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("deleteuser")){
			if(deleteUser()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getUsers());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updateuser")){
			if(updateUser()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getUsers());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("logout")){
			currentUser = null;
			Communication.sendObjectToClient(out, "success");
		}
		else if(input.equals("addpatient")){
			if(addPatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updatenamepatient")){
			if(updateNamePatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updatecardnopatient")){
			if(updateCardNoPatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updatecnppatient")){
			if(updateCnpPatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updatedatepatient")){
			if(updateBirthDatePatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updateaddresspatient")){
			if(updateAddressPatient()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPatients());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		
		else if(input.equals("addconsultation")){
			if(addConsultation()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getConsultations());
				Communication.sendObjectToClient(out, getConsultationIds());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("deleteconsultation")){
			if(deleteConsultation()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getConsultations());
				Communication.sendObjectToClient(out, getConsultationIds());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("checkavailability")){
			Doctor d = checkAvailable();
			if(d != null){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getDoctorAvailability(d.getUsername()));
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("addconsultationbydoctor")){
			if(addConsultationByDoctor()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPastConsultation(currentUser));
				Communication.sendObjectToClient(out, getPatientCnps());
				Communication.sendObjectToClient(out, getDoctorAvailability(currentUser.getUsername()));
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updatestatus")){
			if(updateStatus()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPastConsultation(currentUser));
				Communication.sendObjectToClient(out, getPatientCnps());
				Communication.sendObjectToClient(out, getDoctorAvailability(currentUser.getUsername()));
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("updateresult")){
			if(updateResult()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getPastConsultation(currentUser));
				Communication.sendObjectToClient(out, getPatientCnps());
				Communication.sendObjectToClient(out, getDoctorAvailability(currentUser.getUsername()));
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		else if(input.equals("checkin")){
			if(checkin()){
				Communication.sendObjectToClient(out, "success");
				Communication.sendObjectToClient(out, getConsultations());
			}
			else{
				Communication.sendObjectToClient(out, "fail");
			}
		}
		
	}

	private boolean checkin(){
		Integer id = -1;
		
		try{
			id = (Integer) Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Consultation c = clinic.getConsultationById(id);
		if(c == null){
			return false;
		}
		
		c.setStatus("checked in");
		
		return true;
	}
	
	private boolean updateResult() {
		String idC = null;
		int id = -1;
		String result = null;
		
		try{
			
			idC = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idC);
			
			result = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Consultation c = clinic.getConsultationById(id);
		if(c == null){
			return false;
		}
		
		c.setResult(result);
		
		return true;
	}

	private boolean updateStatus() {
		String idC = null;
		int id = -1;
		String status = null;
		
		try{
			
			idC = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idC);
			
			status = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Consultation c = clinic.getConsultationById(id);
		if(c == null){
			return false;
		}
		
		c.setStatus(status);
		
		return true;
	}

	private boolean addConsultationByDoctor() {
		String cnp = null;
		String date = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateC = null;
		
		try{
			cnp = (String) Communication.getObjectFromClient(in);
			date = (String) Communication.getObjectFromClient(in);
			
			dateC = dateFormat.parse(date);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clinic.addConsultation(cnp, currentUser.getUsername(), dateC);
	}

	private List<List<String>> getPastConsultation(User user) {
		Doctor d = (Doctor)user;
		Date currentDate = new Date();
		
		List<List<String>> cTable = new ArrayList<>();
		ArrayList<Consultation> cs = clinic.getConsultations();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if(cs!=null){
			for(Consultation c : cs){
				if(c.getDoctor().getIdUser() == d.getIdUser() && c.getDate().before(currentDate)){
					List<String> cRow = new ArrayList<>();
					
					cRow.add(String.valueOf(c.getIdConsultation()));
					cRow.add(c.getPatient().getName());
					cRow.add(c.getPatient().getCnp());
					cRow.add(dateFormat.format(c.getDate()));
					cRow.add(c.getStatus());
					cRow.add(c.getResult());
					
					cTable.add(cRow);
				}
			}
		}
		
		return cTable;
	}

	private boolean deleteConsultation() {
		String idC = null;
		int id = -1;
		try{
			
			idC = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idC);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return clinic.deleteConsultation(id);
	}

	private Doctor checkAvailable() {
		String username = null;
		
		try{
			username = (String) Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return (Doctor) clinic.getUserByUsername(username);
	}

	private boolean addConsultation() {
		String cnp = null;
		String username = null;
		String date = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateC = null;
		
		try{
			cnp = (String) Communication.getObjectFromClient(in);
			username = (String) Communication.getObjectFromClient(in);
			date = (String) Communication.getObjectFromClient(in);
			
			dateC = dateFormat.parse(date);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clinic.addConsultation(cnp, username, dateC);
	}

	private boolean updateAddressPatient() {
		String idPatient = null;
		String address = null;
		
		int id = -1;
		try{
			
			idPatient = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idPatient);
			
			address = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Patient patient = clinic.getPatientById(id);
		if(patient == null){
			return false;
		}
		
		patient.setAddress(address);
		
		return true;
	}

	private boolean updateBirthDatePatient() {
		String idPatient = null;
		int id = -1;
		Date birthDate = null;
		
		try{
			
			idPatient = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idPatient);
			
			birthDate = (Date)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Patient patient = clinic.getPatientById(id);
		if(patient == null){
			return false;
		}
		
		patient.setBirthDate(birthDate);
		
		return true;
	}

	private boolean updateCnpPatient() {
		String idPatient = null;
		String cnp = null;
		
		int id = -1;
		try{
			
			idPatient = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idPatient);
			
			cnp = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Patient patient = clinic.getPatientById(id);
		if(patient == null || clinic.getPatientByCnp(cnp) != null){
			return false;
		}
		
		patient.setCnp(cnp);
		
		return true;
	}

	private boolean updateCardNoPatient() {
		String idPatient = null;
		String cardNo = null;
		
		int id = -1;
		try{
			
			idPatient = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idPatient);
			
			cardNo = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Patient patient = clinic.getPatientById(id);
		if(patient == null){
			return false;
		}
		
		patient.setCardNo(cardNo);
		
		return true;
	}

	private boolean updateNamePatient() {
		String idPatient = null;
		String name = null;
		
		int id = -1;
		try{
			
			idPatient = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idPatient);
			
			name = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Patient patient = clinic.getPatientById(id);
		if(patient == null){
			return false;
		}
		
		patient.setName(name);
		
		return true;
	}

	public boolean loginUser(){
		User user = null;
		
		try{
			String username = (String) Communication.getObjectFromClient(in);
			String password = (String) Communication.getObjectFromClient(in);
			
			user = clinic.searchUser(username, password);
			
			if(user == null){
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		currentUser = user;
		return true;
	}
	
	public boolean addUser(){
		String username = null;
		String password = null;
		String type = null;
		
		try{
			username = (String) Communication.getObjectFromClient(in);
			password = (String) Communication.getObjectFromClient(in);
			type = (String) Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clinic.addUser(username, password, type);
	}
	
	public boolean addPatient(){
		String name = null;
		String cardNo = null;
		String cnp = null;
		Date date = null;
		String address = null;
		
		try{
			name = (String) Communication.getObjectFromClient(in);
			cardNo = (String) Communication.getObjectFromClient(in);
			cnp = (String) Communication.getObjectFromClient(in);
			date = (Date) Communication.getObjectFromClient(in);
			address = (String) Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clinic.addPatient(name, cardNo, cnp, date, address);
	}
	
	public boolean deleteUser(){
		String idUser = null;
		int id = -1;
		try{
			
			idUser = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idUser);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return clinic.deleteUser(id);
	}
	
	public boolean updateUser(){
		String idUser = null;
		String username = null;
		
		int id = -1;
		try{
			
			idUser = (String) Communication.getObjectFromClient(in);
			id = Integer.valueOf(idUser);
			
			username = (String)Communication.getObjectFromClient(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		User user = clinic.getUserById(id);
		if(clinic.getUserByUsername(username) != null){
			return false;
		}
		
		user.setUsername(username);
		
		return true;
	}
	
	public List<List<String>> getUsers(){
		List<List<String>> userTable = new ArrayList<>();
		ArrayList<User> users = clinic.getUsers();
		
		if(users!=null){
			for(User u : users){
				List<String> userRow = new ArrayList<>();
				
				userRow.add(String.valueOf(u.getIdUser()));
				userRow.add(u.getUsername());
				userRow.add(u.getType());
				
				userTable.add(userRow);
			}
		}
		
		return userTable;
	}
	
	public List<List<String>> getPatients(){
		List<List<String>> pTable = new ArrayList<>();
		ArrayList<Patient> ps = clinic.getPatients();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if(ps!=null){
			for(Patient p : ps){
				List<String> pRow = new ArrayList<>();
				
				pRow.add(String.valueOf(p.getIdPatient()));
				pRow.add(p.getName());
				pRow.add(p.getCardNo());
				pRow.add(p.getCnp());
				pRow.add(dateFormat.format(p.getBirthDate()));
				pRow.add(p.getAddress());
				
				pTable.add(pRow);
			}
		}
		
		return pTable;
	}
	
	public List<List<String>> getConsultations(){
		List<List<String>> cTable = new ArrayList<>();
		ArrayList<Consultation> cs = clinic.getConsultations();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if(cs!=null){
			for(Consultation c : cs){
				List<String> cRow = new ArrayList<>();
				
				cRow.add(String.valueOf(c.getIdConsultation()));
				cRow.add(c.getPatient().getName());
				cRow.add(c.getPatient().getCnp());
				cRow.add(c.getDoctor().getUsername());
				cRow.add(dateFormat.format(c.getDate()));
				cRow.add(c.getStatus());
				cRow.add(c.getResult());
				
				cTable.add(cRow);
			}
		}
		
		return cTable;
	}
	
	private List<String> getPatientCnps(){
		List<String> cnps = new ArrayList<>();
		
		ArrayList<Patient> patients = clinic.getPatients();
		
		if(patients!=null){
			for(Patient p : patients){
				cnps.add(p.getCnp());
			}
		}
		
		return cnps;
	}
	
	private List<String> getDoctorsUsernames(){
		List<String> usernames = new ArrayList<>();
		
		ArrayList<User> users = clinic.getUsers();
		
		if(users!=null){
			for(User u : users){
				if(u.getType().equals("doctor")){
					usernames.add(u.getUsername());
				}
			}
		}
		
		return usernames;
	}
	
	private List<String> getDoctorAvailability(String username){
		Doctor d = (Doctor) clinic.getUserByUsername(username);
		List<Date> av = clinic.getDoctorAvailability(d);
		List<String> dates = new ArrayList<>();
		
		for(Date date : av){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dates.add(dateFormat.format(date));
		}
		
		return dates;
	}
	
	private List<Integer> getConsultationIds(){
		ArrayList<Consultation> cs = clinic.getConsultations();
		List<Integer> list = new ArrayList<>();
		
		if(cs != null){
			for(Consultation c : cs){
				list.add(c.getIdConsultation());
			}
		}
		
		return list;
	}
}
