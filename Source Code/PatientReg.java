/*

Laget av Sondre Husevold (s198755)

Dette er lista til pasientene. Denne tar hånd om mye av pasientene og søkingen til pasientene.
Det er her alle pasientene blir lagret i en liste.

*/

import java.util.*;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class PatientReg implements Serializable{
	static final long serialVersionUID = 42L;
	private TreeSet<Patient> patList;

	public PatientReg(){
		patList = new TreeSet<Patient>(InitCollator());
	}

	public void add(Patient holmes){
		patList.add(holmes);
	}

	public void remove(Patient holmes){
		patList.remove(holmes);
	}

	public boolean contains(Patient patient){
		return patList.contains(patient);
	}

	public boolean isEmpty(){
		return patList.isEmpty();
	}

	public int size(){
		return patList.size();
	}

	public Iterator<Patient> iterator(){
		return patList.iterator();
	}

	public PatientReg findPatients(String criteria){
		PatientReg searchedPatList = new PatientReg();

		searchedPatList = findPatientByPersonNr(criteria);
		if(searchedPatList == null || searchedPatList.isEmpty() ){
			searchedPatList = findPatientByName(criteria, ""); // Søker kun på helt navn, ikke delt fornavn og etternavn.
			if(searchedPatList == null || searchedPatList.isEmpty()){
				searchedPatList = findPatientByTelephone(criteria);
				if(searchedPatList == null || searchedPatList.isEmpty()){
					searchedPatList = findPatientByAddress(criteria);
				}
			}
		}
		return searchedPatList;
	}

	public PatientReg findPatientByPersonNr(String personNr) {
		if(personNr.isEmpty())
			return null;
		Iterator<Patient> theIterator = iterator();
		Patient pat;
		PatientReg searchedPatReg = new PatientReg();
		try{
			while(theIterator.hasNext()) {
				pat = theIterator.next();
				if(pat.getPersonNr().matches(personNr + ".*")) {
					searchedPatReg.add(pat);
				}
			}
			return searchedPatReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i PatientReg: findPatientByPersonNr fikk NoSuchElementException.",
										"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public PatientReg findPatientByName(String firstname, String lastname) {
		if(firstname.isEmpty() && lastname.isEmpty())
			return null;
		Iterator<Patient> theIterator = iterator();
		Patient pat;
		PatientReg searchedPatReg = new PatientReg();
		try{
			while(theIterator.hasNext()) {
				pat = theIterator.next();
				if(((pat.getfirstName().toLowerCase() + pat.getlastName()).toLowerCase().matches((firstname.toLowerCase() + ".*") + (lastname.toLowerCase() + ".*")))) {
					searchedPatReg.add(pat);
				}
				else if(pat.getfirstName().toLowerCase().equals(firstname.toLowerCase() )
						|| pat.getlastName().toLowerCase().equals(firstname.toLowerCase() ) )
					searchedPatReg.add(pat);
			}
			return searchedPatReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i PatientReg: findPatientByName fikk NoSuchElementException.",
					"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public PatientReg findPatientByTelephone(String telephone) {
		if(telephone.isEmpty())
			return null;
		Iterator<Patient> theIterator = iterator();
		Patient pat;
		PatientReg searchedPatReg = new PatientReg();
		try{
			while(theIterator.hasNext()) {
				pat = theIterator.next();
				if(pat.getTelephoneNr().matches(telephone + ".*")) {
					searchedPatReg.add(pat);
				}
			}
			return searchedPatReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i PatientReg: findPatientByTelephone fikk NoSuchElementException.",
					"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public PatientReg findPatientByAddress(String address) {
		if(address.isEmpty())
			return null;
		Iterator<Patient> theIterator = iterator();
		Patient pat;
		PatientReg searchedPatReg = new PatientReg();
		try{
			while(theIterator.hasNext()) {
				pat = theIterator.next();
				if(pat.getAddress().toLowerCase().matches(address.toLowerCase() + ".*")) {
					searchedPatReg.add(pat);
				}
			}
			return searchedPatReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i PatientReg: findPatientByAddress fikk NoSuchElementException.",
					"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public Comparator<Person> InitCollator(){ // Initialiserer kollatoren til lista.
		Comparator<Person> collator;
		return collator = new PersonCollator();
	}

	public String toString(){
		Patient pat = null;
		Iterator<Patient> iter = iterator();
		StringBuilder info = new StringBuilder(1000);
		while(iter.hasNext()){
			pat = iter.next();
			info.append(pat.toString() + "\n-----------------------------------\n\n");
		}
		return info.toString();
	}
}
