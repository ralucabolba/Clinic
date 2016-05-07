package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;

public class ClientController {
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Socket socket;
	
	private Window w;
	
	public ClientController(Window w, Socket socket, ObjectInputStream in, ObjectOutputStream out){
		this.w = w;
		this.socket = socket;
		this.in = in;
		this.out = out;
		
		w.addListenerLoginButton(new LoginButtonListener());
		w.addListenerAddUserButton(new AddUserButtonListener());
		w.addListenerDeleteUserButton(new DeleteUserButtonListener());
		w.addListenerUsernameUser(new UsernameUserListener());
		w.addListenerLogout(new LogoutListener());
		w.addListenerPatientButton(new PatientButtonListener());
		w.addListenerConsultButton(new ConsultButtonListener());
		
		w.addListenerAddPatientButton(new AddPatientButtonListener());
		w.addListenerNamePatient(new NamePatientListener());
		w.addListenerCardNoPatient(new CardNoPatientListener());
		w.addListenerCnpPatient(new CnpPatientListener());
		w.addListenerBirthDatePatient(new BirthDatePatientListener());
		w.addListenerAddressPatient(new AddressPatientListener());
		
		w.addListenerAddConsultationButton(new AddConsultationButtonListener());
		w.addListenerCheckAvailability(new CheckAvailabilityListener());
		w.addListenerDeleteConsultationButton(new DeleteConsultationButtonListener());
		w.addListenerCheckin(new CheckinListener());
		
		w.addListenerAddConsByDoctor(new AddConsByDoctorListener());
		w.addListenerStatus(new StatusListener());
		w.addListenerResult(new ResultListener());
	}
	
	public static void sendObjectToServer(ObjectOutputStream out, Object o) throws IOException{
		out.writeObject(o);
		out.flush();
	}

	public static Object getObjectFromServer(ObjectInputStream in) throws IOException, ClassNotFoundException{
		Object o = in.readObject();
		return o;
	}
	
	class LoginButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			try {
				

				String username = w.getUsername();
				String password = w.getPassword();

				sendObjectToServer(out, "login");
				sendObjectToServer(out, username);
				sendObjectToServer(out, password);

				String result = (String) getObjectFromServer(in);

				if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed logging in", "Username or password incorrect");
				}
				else if("administrator".equals(result)){
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setUsersTable(table);
					w.adminWindow();
				}
				else if("secretary".equals(result)){
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
					
					List<List<String>> tableC = (List<List<String>>) getObjectFromServer(in);
					w.setConsultationsTable(tableC);
					
					List<String> cnps = (List<String>) getObjectFromServer(in);
					w.setCnpData(cnps);
					 
					List<String> usernames = (List<String>) getObjectFromServer(in);
					w.setUsernameData(usernames);
					
					List<Integer> ids = (List<Integer>) getObjectFromServer(in);
					w.setComboConsultation(ids);
					
					w.secretaryChoiceWindow();
				}
				else if("doctor".equals(result)){
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setDoctorTable(table);
					
					List<String> cnps = (List<String>) getObjectFromServer(in);
					w.setCnpData(cnps);
					
					List<String> dates = (List<String>) getObjectFromServer(in);
					w.setAvailableDate(dates);
					
					w.doctorWindow();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	class AddUserButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			try{
				String username = w.getUsernameUserField();
				String password = w.getPasswordUserField();
				String typeUser = w.getTypes();
				
				sendObjectToServer(out, "adduser");
				sendObjectToServer(out, username);
				sendObjectToServer(out, password);
				sendObjectToServer(out, typeUser);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "User added", "User has been added successfully");
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setUsersTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed adding user",  "Username already exists.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class DeleteUserButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			try{
				//Integer idUser = Integer.valueOf(w.getSelectedUserId());
				List<String> user =w.getSelectedUser();
				
				if(user == null){
					w.ErrorMessage("Error", "Error deleting user", "You must select an user.");
				}
				else{
					String idUser = user.get(0); 
					
					sendObjectToServer(out, "deleteuser");
					sendObjectToServer(out, idUser);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						w.SuccesMessage("Success", "User deleted", "User has been deleted successfully");
						
						@SuppressWarnings("unchecked")
						
						List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
						w.setUsersTable(table);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed deleting user",  "User could not be deleted.");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	class UsernameUserListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> user = w.getSelectedUser();
			String newUsername = event.getNewValue();
			
			try{
				sendObjectToServer(out, "updateuser");
				sendObjectToServer(out, user.get(0));
				sendObjectToServer(out, newUsername);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "User updated", "User has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setUsersTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating user",  "User could not be updated. Username already exists.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class LogoutListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			try{
				sendObjectToServer(out, "logout");
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.clearTables();
					w.loginWindow();
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed logging out",  "");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	class PatientButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			w.patientInfoWindow();
		}
		
	}
	
	class ConsultButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			w.consultationInfoWindow();
		}
	}
	
	class AddPatientButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			try{
				String name = w.getNamePatientField();
				String cardNo = w.getCardNoPatientField();
				String cnp = w.getCnpPatientField();
				Date date  = w.getBirthDatePatientField();
				String address = w.getAddressPatientField();
				
				if(date == null || name.equals("") || cardNo.equals("") || cnp.equals("") || address.equals("") ){
					w.ErrorMessage("Error", "Failed adding patient",  "You must fill in all the fields");
				}
				else{
					sendObjectToServer(out, "addpatient");
					sendObjectToServer(out, name);
					sendObjectToServer(out, cardNo);
					sendObjectToServer(out, cnp);
					sendObjectToServer(out, date);
					sendObjectToServer(out, address);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						w.SuccesMessage("Success", "Patient added", "Patient has been added successfully");
						List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
						w.setPatientsTable(table);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed adding patient",  "Patient already exists in database.");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class NamePatientListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> patient = w.getSelectedPatient();
			String name = event.getNewValue();
			
			try{
				if (name.equals("")){
					throw new Exception("Invalid name");
				}
				sendObjectToServer(out, "updatenamepatient");
				sendObjectToServer(out, patient.get(0));
				sendObjectToServer(out, name);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Patient updated", "Patient has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating patient",  "Patient could not be updated.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating patient",  e.getMessage());
			}
		}
		
	}
	
	class CardNoPatientListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> patient = w.getSelectedPatient();
			String cardno = event.getNewValue();
			
			try{
				
				if (cardno.equals("")){
					throw new Exception("Invalid card number");
				}
				
				sendObjectToServer(out, "updatecardnopatient");
				sendObjectToServer(out, patient.get(0));
				sendObjectToServer(out, cardno);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Patient updated", "Patient has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating patient",  "Patient could not be updated.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating patient",  e.getMessage());
			}
		}
		
	}
	class CnpPatientListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> patient = w.getSelectedPatient();
			String cnp = event.getNewValue();
			
			try{
				if (cnp.equals("")){
					throw new Exception("Invalid cnp");
				}
				
				sendObjectToServer(out, "updatecnppatient");
				sendObjectToServer(out, patient.get(0));
				sendObjectToServer(out, cnp);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Patient updated", "Patient has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating patient",  "Patient could not be updated. A pacient with the same cnp already exists in database.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating patient",  e.getMessage());
			}
		}
		
	}
	class BirthDatePatientListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> patient = w.getSelectedPatient();
			String date = event.getNewValue();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			try{
				Date d = dateFormat.parse(date);
				
				sendObjectToServer(out, "updatedatepatient");
				sendObjectToServer(out, patient.get(0));
				sendObjectToServer(out, d);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Patient updated", "Patient has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating patient",  "Patient could not be updated.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				w.ErrorMessage("Error", "Error updating patient", "Invalid date");
				//e.printStackTrace();
			}
		}
		
	}
	class AddressPatientListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> patient = w.getSelectedPatient();
			String address = event.getNewValue();
			
			try{
				if (address.equals("")){
					throw new Exception("Invalid address");
				}
				
				sendObjectToServer(out, "updateaddresspatient");
				sendObjectToServer(out, patient.get(0));
				sendObjectToServer(out, address);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Patient updated", "Patient has been updated successfully");
					
					@SuppressWarnings("unchecked")
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setPatientsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating patient",  "Patient could not be updated.");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating patient",  e.getMessage());
			}
		}
		
	}
	
	class AddConsultationButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			String cnp = w.getCnp();
			String username = w.getDoctorUsername();
			String date = w.getSelectedDate();
			
			
			try{
				
				if(date.equals("") || cnp.equals("") || username.equals("")){
					w.ErrorMessage("Error", "Failed scheduling consultation",  "You must fill in all the fields");
				}
				else{
					sendObjectToServer(out, "addconsultation");
					sendObjectToServer(out, cnp);
					sendObjectToServer(out, username);
					sendObjectToServer(out, date);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						w.SuccesMessage("Success", "Consultation scheduled", "");
						List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
						w.setConsultationsTable(table);
						
						List<Integer> ids = (List<Integer>) getObjectFromServer(in);
						w.setComboConsultation(ids);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed scheduling consultation",  "");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	class CheckAvailabilityListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			String username = w.getDoctorUsername();
			
			try{
				if(username.equals("")){
					w.ErrorMessage("Error", "Failed scheduling consultation",  "Invalid doctor username");
				}
				else{
					sendObjectToServer(out, "checkavailability");
					sendObjectToServer(out, username);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						//w.SuccesMessage("Success", "Consultation schedules", "");
						List<String> dates = (List<String>) getObjectFromServer(in);
						w.setAvailableDate(dates);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed checking availability",  "");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}

	class DeleteConsultationButtonListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			try{
				List<String> c = w.getSelectedConsultation();
				
				if(c == null){
					w.ErrorMessage("Error", "Error canceling consultation", "You must select a consultation.");
				}
				else{
					String idC = c.get(0); 
					
					sendObjectToServer(out, "deleteconsultation");
					sendObjectToServer(out, idC);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						w.SuccesMessage("Success", "Consultation canceled", "");
						
						@SuppressWarnings("unchecked")
						
						List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
						w.setConsultationsTable(table);
						
						List<Integer> ids = (List<Integer>) getObjectFromServer(in);
						w.setComboConsultation(ids);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed canceling consultatoion",  "");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	class CheckinListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			Integer idC = w.getSelectedIdConsultation();
			
			try{
				sendObjectToServer(out, "checkin");
				sendObjectToServer(out, idC);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Status updated", "");
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setConsultationsTable(table);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating status",  "");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class AddConsByDoctorListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			String cnp = w.getCnp();
			//String username = w.getDoctorUsername();
			String date = w.getSelectedDate();
			
			
			try{
				
				if(date.equals("") || cnp.equals("")){
					w.ErrorMessage("Error", "Failed scheduling consultation",  "You must fill in all the fields");
				}
				else{
					sendObjectToServer(out, "addconsultationbydoctor");
					sendObjectToServer(out, cnp);
					//sendObjectToServer(out, username);
					sendObjectToServer(out, date);
					
					String result = (String) getObjectFromServer(in);
					
					if("success".equals(result)){
						w.SuccesMessage("Success", "Consultation scheduled", "");
						
						List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
						w.setDoctorTable(table);
						
						List<String> cnps = (List<String>) getObjectFromServer(in);
						w.setCnpData(cnps);
						
						List<String> dates = (List<String>) getObjectFromServer(in);
						w.setAvailableDate(dates);
					}
					else if("fail".equals(result)){
						w.ErrorMessage("Error", "Failed scheduling consultation",  "");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class StatusListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> c = w.getSelectedPastConsultation();
			String status = event.getNewValue();
			
			try{
				if (!(status.equals("complete") || status.equals("checked in") || status.equals("scheduled"))){
					throw new Exception("Invalid status");
				}
				
				sendObjectToServer(out, "updatestatus");
				sendObjectToServer(out, c.get(0));
				sendObjectToServer(out, status);
				
				String result = (String) getObjectFromServer(in);
				
				if("success".equals(result)){
					w.SuccesMessage("Success", "Status updated", "");
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setDoctorTable(table);
					
					List<String> cnps = (List<String>) getObjectFromServer(in);
					w.setCnpData(cnps);
					
					List<String> dates = (List<String>) getObjectFromServer(in);
					w.setAvailableDate(dates);
				}
				else if("fail".equals(result)){
					w.ErrorMessage("Error", "Failed updating status",  "");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating status",  e.getMessage());
			}
		}
	}
	
	class ResultListener implements EventHandler<CellEditEvent<List<String>, String>>{

		@Override
		public void handle(CellEditEvent<List<String>, String> event) {
			List<String> c = w.getSelectedPastConsultation();
			String result = event.getNewValue();
			
			try{
				if (result.equals("")){
					throw new Exception("Invalid result");
				}
				
				sendObjectToServer(out, "updateresult");
				sendObjectToServer(out, c.get(0));
				sendObjectToServer(out, result);
				
				String serverResult = (String) getObjectFromServer(in);
				
				if("success".equals(serverResult)){
					w.SuccesMessage("Success", "Result updated", "");
					
					List<List<String>> table = (List<List<String>>) getObjectFromServer(in);
					w.setDoctorTable(table);
					
					List<String> cnps = (List<String>) getObjectFromServer(in);
					w.setCnpData(cnps);
					
					List<String> dates = (List<String>) getObjectFromServer(in);
					w.setAvailableDate(dates);
				}
				else if("fail".equals(serverResult)){
					w.ErrorMessage("Error", "Failed updating result",  "");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while connecting to the server: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				w.ErrorMessage("Error", "Failed updating result",  e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
