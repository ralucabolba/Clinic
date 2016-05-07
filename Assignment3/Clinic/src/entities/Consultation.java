package entities;
import java.util.*;

import dataaccess.ConsultationGateway;

public class Consultation {
	
	private int idConsultation;
	private Patient patient;
	private Doctor doctor;
	private Date date;
	private String status;
	private String result;
	
	public Consultation(Patient patient, Doctor doctor, Date date) {
		this.patient = patient;
		this.doctor = doctor;
		this.date = date;
		
		this.status = "scheduled";
		this.result = "none";
		
		this.patient.registerObserver(doctor);
	}
	
	
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
		//ConsultationGateway.updateConsultation(this);
		if(status.equals("checked in")){
			patient.checkin();
		}
	}
	
	public void setResult(String result){
		this.result = result;
		//ConsultationGateway.updateConsultation(this);
	}
	
	public String getResult(){
		return this.result;
	}
	
	public int getIdConsultation() {
		return idConsultation;
	}
	
	public void setIdConsultation(int idConsultation) {
		this.idConsultation = idConsultation;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient.removeObserver(this.doctor);
		this.patient = patient;
		this.patient.registerObserver(this.doctor);
		//ConsultationGateway.updateConsultation(this);
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.patient.removeObserver(this.doctor);
		this.doctor = doctor;
		this.patient.registerObserver(this.doctor);
		//ConsultationGateway.updateConsultation(this);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
		//ConsultationGateway.update(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Consultation [idConsultation=" + idConsultation + ", patient=" + patient + ", doctor=" + doctor
				+ ", date=" + date + "]";
	}
}
