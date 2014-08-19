/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen definerer en resept, inneholder datafelter datafelter for lagring av relevant informasjon
Har set og get metoder for de fleste feltene

*/

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;



public class Prescription implements Serializable
{
	private static final long serialVersionUID = 42L;
	public static final int GROUP_A = 1, GROUP_B = 2, GROUP_C = 3;
	private static int newNr;
	private int prescriptionNr;
	private Calendar printed;
	private Calendar recived;
	private Patient patient;
	private Doctor doctor;
	private String medName;
	private String category;
	private int group;
	private String dosage;
	private String useage;

	public Prescription(Patient pat, Doctor doc, String med, String dos, int gro, String cat, String use)
	{
		prescriptionNr = ++newNr;
		printed = Calendar.getInstance();
		patient = pat;
		doctor = doc;
		medName = med;
		dosage = dos;
		group = gro;
		category = cat;
		useage = use;
	}

	public void setRecived()
	{
		recived = Calendar.getInstance();
	}

	public Patient getPatient()
	{
		return patient;
	}

	public Doctor getDoctor()
	{
		return doctor;
	}

	public String getMedName()
	{
		return medName;
	}

	public int getGroup()
	{
		return group;
	}

	public String getCategory()
	{
		return category;
	}

	public int getPrescriptionNr()
	{
		return prescriptionNr;
	}

	public static int getCurrentNumber()//nødvendig for skriving/lagring til fil
	{
		return newNr;
	}

	public Calendar getPrinted()
	{
		return printed;
	}

	public Calendar getRecived()
	{
		return recived;
	}

	public boolean isRecived()
	{
		if(recived == null)
			return false;
		return true;
	}

	public static void setCurrentNumber(int n)//nødvendig for skriving/lagring til fil
	{
		if(n > newNr)
			newNr = n;
	}

	public String toString()
	{
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM);
		String out = "\nReseptnummer: " + prescriptionNr + "\nSkrevet ut: " + df.format(printed.getTime() ) +
		"\nPasient: " + patient.getfirstName() + " " + patient.getlastName() +
		"\nLege: " + doctor.getfirstName() + " " + doctor.getlastName() +
		"\nMedikament navn: " + medName + "\nGruppe: " + PrescriptionReg.getGroupString(group) + "\nKategori: " + category +
		"\nDosering: " + dosage + "\nAnvisning: " + useage + "\nResept hentet: ";
		if(recived != null)
		out += df.format(recived.getTime() );
		else
		out += "Ikke hentet";
		return out;
	}
}
