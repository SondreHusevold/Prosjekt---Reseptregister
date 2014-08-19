/*

Laget av Sondre Husevold (s198755)

Dette er lista til doktorene. Den fjerner, legger til og finner doktorere i lista.

*/

import java.util.*;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class DoctorReg implements Serializable {
	private static final long serialVersionUID = 42L;
	private TreeSet<Doctor> docList;

	public DoctorReg(){
		docList = new TreeSet<Doctor>(InitCollator());
	}

	public void add(Doctor watson) {
		docList.add(watson);
	}

	public void remove(Doctor watson) {
		docList.remove(watson);
	}

	public boolean contains(Doctor doc){
		return docList.contains(doc);
	}

	public boolean isEmpty(){
		return docList.isEmpty();
	}

	public int size(){
		return docList.size();
	}

	public Iterator<Doctor> iterator(){
		return docList.iterator();
	}

	public DoctorReg findDoctors(String criteria){
		DoctorReg searchedDocList = new DoctorReg();
		searchedDocList = findDoctorByPersonNr(criteria);
		if(searchedDocList == null){
			searchedDocList = findDoctorByName(criteria);
			if(searchedDocList == null){
				searchedDocList = findDoctorByTelephone(criteria);
				if(searchedDocList == null){
					return null;
				}
			}
		}
		return searchedDocList;
	}

	public DoctorReg findDoctorByPersonNr(String personNr) {
		Iterator<Doctor> theIterator = iterator();
		Doctor doc;
		DoctorReg searchedDocReg = new DoctorReg();
		try{
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getPersonNr().matches(personNr)) {
					searchedDocReg.add(doc);
					return searchedDocReg;
				}
			}
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i DoctorReg (findDoctorByPersonNr): No Such Element Exception.",
											"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public DoctorReg findDoctorByName(String name) {
		Iterator<Doctor> theIterator = iterator();
		Doctor doc;
		DoctorReg searchedDocReg = new DoctorReg();
		try{
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getfirstName().equals(name) || doc.getlastName().equals(name)) {
					searchedDocReg.add(doc);
				}
			}
			return searchedDocReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i DoctorReg (findDoctorByName): No Such Element Exception.",
					"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public DoctorReg findDoctorByTelephone(String telephone) {
		Iterator<Doctor> theIterator = iterator();
		Doctor doc;
		DoctorReg searchedDocReg = new DoctorReg();
		try{
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getTelephoneNr().matches(telephone)) {
					searchedDocReg.add(doc);
				}
			}
			return searchedDocReg;
		}
		catch(NoSuchElementException nsee){
			JOptionPane.showMessageDialog(null, "Feil i DoctorReg (findDoctorByTelephone): No Such Element Exception.",
					"FEIL", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public DoctorReg findDoctorByLicense(int group) {
		DoctorReg listofDoctors = new DoctorReg();
		Doctor doc;
		Iterator<Doctor> theIterator = iterator();

		if(group == Prescription.GROUP_A) {
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getLicenseA()) {
					listofDoctors.add(doc);
				}
			}
		}

		else if(group == Prescription.GROUP_B) {
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getLicenseB()) {
					listofDoctors.add(doc);
				}
			}
		}

		else if(group == Prescription.GROUP_C) {
			while(theIterator.hasNext()) {
				doc = theIterator.next();
				if(doc.getLicenseC()) {
					listofDoctors.add(doc);
				}
			}
		}
		return listofDoctors;
	}

	public Comparator<Person> InitCollator(){ // Initialiserer kollator
		Comparator<Person> collator;
		return collator = new PersonCollator();
	}

	public String toString() {
		StringBuilder info = new StringBuilder();
		Iterator<Doctor> iter = iterator();
		while(iter.hasNext() )
		{
			info.append(iter.next().toString() + "\n-----------------------------------\n\n");
		}
		return info.toString();
	}
} // End of class
