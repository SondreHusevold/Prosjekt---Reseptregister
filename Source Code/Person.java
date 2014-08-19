/*
 Laget av Sondre Husevold (s198755)

 Denne klassen omhandler datafeltene til leger og pasienter. Den inneholder også en toString for å få ut
 informasjon om personen. Den godkjenner også reseptbevilgningen til legene.

 */
import java.io.Serializable;
import java.util.*;

public abstract class Person implements Serializable{
	private static final long serialVersionUID = 42L;
	protected String firstName, lastName, personNr, password, telephoneNr;
	protected PrescriptionReg prescriptionList;

	protected Person(String firstNamePara, String lastNamePara, String personNrPara, String telephoneNrPara){
		firstName = firstNamePara;
		lastName = lastNamePara;
		personNr = personNrPara;
		telephoneNr = telephoneNrPara;
		prescriptionList = new PrescriptionReg();
	}

	public String getfirstName(){
		return firstName;
	}

	public String getlastName(){
		return lastName;
	}

	public String getPersonNr(){
		return personNr;
	}

	public String getTelephoneNr(){
		return telephoneNr;
	}

	public void setfirstName(String firstNamePara){
		firstName = firstNamePara;
	}

	public void setlastName(String lastNamePara){
		lastName = lastNamePara;
	}

	public void setTelephoneNr(String telephoneNrPara){
		telephoneNr = telephoneNrPara;
	}

	public PrescriptionReg getPrescriptions(){
		return prescriptionList;
	}

	public Iterator<Prescription> iterator(){
		return prescriptionList.iterator();
	}

	public void addPrescription(Prescription prescriptionPara){
		prescriptionList.add(prescriptionPara);
	}

} // End abstract class (Person).

class Patient extends Person implements Serializable{
	static final long serialVersionUID = 42L;
	private String address;

	public Patient(String firstNamePara, String lastNamePara, String personNrPara, String telephoneNrPara, String addressPara){
		super(firstNamePara, lastNamePara, personNrPara, telephoneNrPara);
		address = addressPara;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String addressPara){
		address = addressPara;
	}

	public String toString(){
		String info = "Fornavn: " + getfirstName() + "\nPersonnummer: " + getPersonNr() + "\nAdresse: " + getAddress() + "\nTelefon: " + getTelephoneNr();
		return info;
	}
} // End subclass (Patient).

class Doctor extends Person implements Serializable{
	static final long serialVersionUID = 42L;
	private boolean licenseA;
	private boolean licenseB;
	private boolean licenseC;
	private String password;
	private String employeeAt;

	public Doctor(String firstNamePara, String lastNamePara, String personNrPara, String passwordPara, String telephoneNrPara, boolean licenseAPara,
					boolean licenseBPara, boolean licenseCPara, String employeeAtPara){
		super(firstNamePara, lastNamePara, personNrPara, telephoneNrPara);
		licenseA = licenseAPara;
		licenseB = licenseBPara;
		licenseC = licenseCPara;
		password = passwordPara;
		employeeAt = employeeAtPara;
	}

	public String getWorkplace(){
		return employeeAt;
	}
	
	public void setHashedPass(String passwordPara){
		password = passwordPara;
	}
	
	public String getHashedPass(){
		return password;
	}

	public boolean getLicenseA(){
		return licenseA;
	}

	public boolean getLicenseB(){
		return licenseB;
	}

	public boolean getLicenseC(){
		return licenseC;
	}

	public boolean hasLicense(int group){
		if(group == Prescription.GROUP_A){
			if(getLicenseA())
				return true;
		}
		else if(group == Prescription.GROUP_B){
			if(getLicenseB())
				return true;
		}
		else if(group == Prescription.GROUP_C){
			if(getLicenseC())
				return true;
		}
		return false;
	}

	public void setWorkplace(String employeeAtPara){
		employeeAt = employeeAtPara;
	}

	public void setLicenseA(boolean licenseAPara){
		licenseA = licenseAPara;
	}

	public void setLicenseB(boolean licenseBPara){
		licenseB = licenseBPara;
	}

	public void setLicenseC(boolean licenseCPara){
		licenseC = licenseCPara;
	}

	public String toString(){
		StringBuilder info = new StringBuilder(80);
		info.append("Fornavn: " + getfirstName() + "\nPersonnummer: " + getPersonNr() + 
					"\nTelefon: " + getTelephoneNr() + "\nReseptbevilgning(er): ");
		if(getLicenseA()){
			info.append("A ");
		}

		if(getLicenseB()){
			info.append("B");
		}

		if(getLicenseC()){
			info.append("C");
		}
		return info.toString();
	}
} // End subclass (Doctor).