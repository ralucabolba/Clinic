package entities;
import java.util.Date;

import dataaccess.PatientGateway;
import observer.Observer;
import observer.Subject;

public class Patient extends Subject{
	private int idPatient;
	

	private String name;
	private String cardNo;
	private String cnp;
	private Date birthDate;
	private String address;
	
	public Patient(String name, String cardNo, String cnp, Date birthDate, String address) {
		super();
		this.name = name;
		this.cardNo = cardNo;
		this.cnp = cnp;
		this.birthDate = birthDate;
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Patient [idPatient=" + idPatient + ", name=" + name + ", cardNo=" + cardNo + ", cnp=" + cnp
				+ ", birthDate=" + birthDate + ", adddress=" + address + "]";
	}

	/**
	 * @return the idPatient
	 */
	public int getIdPatient() {
		return idPatient;
	}

	/**
	 * @param idPatient the idPatient to set
	 */
	public void setIdPatient(int idPatient) {
		this.idPatient = idPatient;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		PatientGateway.updatePatient(this);
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
		PatientGateway.updatePatient(this);
	}

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
		PatientGateway.updatePatient(this);
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		PatientGateway.updatePatient(this);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		PatientGateway.updatePatient(this);
	}

	public void checkin(){
		updateAllObservers();
	}
	
	@Override
	public void updateAllObservers() {
		for(Observer o : super.observers){
			o.update(toString());
		}
	}
	
	
	
}
