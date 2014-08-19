/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen tar seg av all kommunikasjon mellom GUI klassene og listeklassene, de fleste søke og sorterings metodene ligger her.

*/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class Register implements Serializable
{
	private static final long serialVersionUID = 42L;
	private PrescriptionReg preReg;
	private PatientReg patReg;
	private DoctorReg docReg;
	private Datamanager data;

	public Register(Datamanager dataPara)
	{
		data = dataPara;
		preReg = new PrescriptionReg();
		patReg = new PatientReg();
		docReg = new DoctorReg();
	}
	//get metoder:
	public PrescriptionReg getPrescriptions()
	{
		return preReg;
	}

	public DoctorReg getDoctors()
	{
		return docReg;
	}

	public PatientReg getPatients()
	{
		return patReg;
	}
	public Doctor getDoctorByNumber(String nr)
	{
		DoctorReg dReg = docReg.findDoctorByPersonNr(nr);
		if(dReg == null)
			return null;
		Iterator<Doctor> iter = dReg.iterator();
		return iter.next();

	}
	public Patient getPatientByNumber(String nr)
	{
		PatientReg pReg = patReg.findPatientByPersonNr(nr);
		if(pReg == null)
			return null;
		Iterator<Patient> iter = pReg.iterator();
		return iter.next();
	}

	public Prescription getPrescriptionByNr(int nr)//metode returnerer et reseptregister med reseptnr lik nr
	{
		Prescription res;
		Iterator<Prescription> iter = preReg.iterator();
		while(iter.hasNext() )
		{
			res = iter.next();
			if(res.getPrescriptionNr() == nr)
				return res;
		}
		return null;
	}
	public PrescriptionReg getPrescriptionByNr(PrescriptionReg reg, int nr)/*metode returnerer et reseptregister med reseptnr lik nr,
																			tar imot et register eller bruker preReg*/
	{
		if(reg == null)
			reg = preReg;
		Prescription temp;
		PrescriptionReg res = new PrescriptionReg();
		Iterator<Prescription> iter = reg.iterator();
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getPrescriptionNr() == nr)
				res.add(temp);
		}
		return res;
	}

	//add metoder:
	public void addPrescription(Prescription pre)
	{
		pre.getDoctor().addPrescription(pre);
		pre.getPatient().addPrescription(pre);
		preReg.add(pre);
	}

	public void addDoctor(Doctor doc)
	{
		docReg.add(doc);//endre til add
	}

	public void addPatient(Patient pat)
	{
		patReg.add(pat);//endre til add
	}

	//Filtreringsmetoder:

	//Filtreringsmetoder for reseptsøk:
	public PrescriptionReg getPrescriptionsBeforeDate(PrescriptionReg reg, Calendar date)/*Returnerer et reseptregister over
																						resepter skrevet før date
																						tar imot et register eller bruker preReg*/
	{
		if(reg == null)
			reg = preReg;
		Iterator<Prescription> iter = reg.iterator();
		PrescriptionReg res = new PrescriptionReg();
		Prescription temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getPrinted().before(date) )
				res.add(temp);
		}
		return res;
	}
	public PrescriptionReg getPrescriptionsAfterDate(PrescriptionReg reg, Calendar date)/*Returnerer et reseptregister over
																						resepter skrevet metter date
																						tar imot et register eller bruker preReg*/
	{
		if(reg == null)
			reg = preReg;
		Iterator<Prescription> iter = reg.iterator();
		PrescriptionReg res = new PrescriptionReg();
		Prescription temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getPrinted().after(date) )
				res.add(temp);
		}
		return res;
	}

	public PrescriptionReg getPrescriptionsByRecived(PrescriptionReg reg, boolean recived)/*returnerer er reseptregister med alle mottatte resepter,
																							tar imot et reseptliste eller bruker preReg*/
	{
		if(reg == null)
			reg = preReg;
		Iterator<Prescription> iter = reg.iterator();
		Prescription temp;
		PrescriptionReg res = new PrescriptionReg();
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.isRecived() == recived)
				res.add(temp);
		}
		return res;
	}
	public PrescriptionReg getPrescriptionsByPatient(PrescriptionReg reg, String name)/*Returnerer en liste over alle resepter forskrevet til en
																						pasient, tar imot en reseptliste eller bruker preReg*/
	{
		PatientReg pReg = patReg.findPatients(name);
		Iterator<Patient> patIter = pReg.iterator();
		Patient pat;
		if(patIter.hasNext() )//tester om det ble funnet noen pasienter
			pat = patIter.next();
		else
			return new PrescriptionReg();
		if(patIter.hasNext() )//tester om det ble funnet flere pasienter
			JOptionPane.showMessageDialog(null, "Flere personer funner, venligst spesifiser s?ket bedre");/*kan utvides med et vindu til å velge
																											mellom resultatene*/
		PrescriptionReg pr = pat.getPrescriptions();
		if(reg == null)
			return pr;

		PrescriptionReg res = new PrescriptionReg();
		Iterator<Prescription> iter = pr.iterator();
		Prescription temp;
		while(iter.hasNext() )//søker gjennom reseptregsteret
		{
			temp = iter.next();
			if(reg.contains(temp) )//tester om reg inneholder resepten
				res.add(temp);
		}
		return res;
	}

	public PrescriptionReg getPrescriptionsByDoctor(PrescriptionReg reg, String name)/*Returnerer en liste over alle resepter forskrevet fra en
																						doktor, tar imot en reseptliste eller bruker preReg*/
	{
		DoctorReg dReg = docReg.findDoctors(name);
		Iterator<Doctor> docIter = dReg.iterator();
		Doctor doc;
		if(docIter.hasNext() )
			doc = docIter.next();
		else
			return new PrescriptionReg();
		if(docIter.hasNext() )
			JOptionPane.showMessageDialog(null, "Flere personer funner, venligst spesifiser s?ket bedre");/*kan utvides med et vindu til å velge
																											mellom resultatene*/
		PrescriptionReg pr = doc.getPrescriptions();
		if(reg == null)
			return pr;

		PrescriptionReg res = new PrescriptionReg();
		Iterator<Prescription> iter = pr.iterator();
		Prescription temp;
		while(iter.hasNext() )//søker gjennom pr
		{
			temp = iter.next();
			if(reg.contains(temp) )//tester om reg inneholder resepten
				res.add(temp);
		}
		return res;
	}

	public PrescriptionReg getPrescriptionsByMedName(PrescriptionReg reg, String med)/*metode returnerer en liste over resepter med navn lik med,
																					tar imot en liste eller bruker klassens egene liste*/
	{
		if(reg == null)
			reg = preReg;

		Iterator<Prescription> iter = reg.iterator();
		PrescriptionReg res = new PrescriptionReg();
		Prescription temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getMedName().toLowerCase().equals(med.toLowerCase() ) )//kan legge til en matches her som ikke er case sensitive
				res.add(temp);
		}
		return res;
	}


	public PrescriptionReg getPrescriptionsByGroup(PrescriptionReg reg, boolean a, boolean b, boolean c)/*returnerer en liste over alle resepter
																										på medikament i gruppene lik true,
																										tar imot en liste eller bruker klassens egen liste*/
	{
		if(reg == null)
			reg = preReg;
		if(!(a || b || c) )
			return reg;
		Iterator<Prescription> iter = reg.iterator();
		PrescriptionReg res = new PrescriptionReg();
		Prescription temp;
		while(iter.hasNext() )//søker gjennom reg
		{
			temp = iter.next();
			if(a && temp.getGroup() == Prescription.GROUP_A)
				res.add(temp);
			if(b && temp.getGroup() == Prescription.GROUP_B)
				res.add(temp);
			if(c && temp.getGroup() == Prescription.GROUP_C)
				res.add(temp);
		}
		return res;
	}

	public PrescriptionReg getPrescriptionsByCategory(PrescriptionReg reg, String cat)/*metode returnerer en liste over resepter med kategori lik cat,
																						tar imot et PrescriptionReg eller bruker klassens egene liste*/
	{
		if(reg == null)
			reg = preReg;

		Iterator<Prescription> iter = reg.iterator();
		PrescriptionReg res = new PrescriptionReg();
		Prescription temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getCategory().toLowerCase().equals(cat.toLowerCase() ) )
				res.add(temp);
		}
		return res;
	}

	//Filtreringsmetoder for pasientsøk
	public PatientReg getPatientsByName(PatientReg reg, String n)//returnerer en liste over alle pasienter med navn eller personnummer lik n
	{
		if(reg == null)
			return patReg.findPatients(n);
		return reg.findPatients(n);
	}

	public PatientReg getPatientsByDoctor(PatientReg reg, String n)/*returnerer en liste over alle pasientene til en doktor,
																	tar imot et PatientReg eller bruker klassens egen liste*/
	{
		Iterator<Prescription> iter = getPrescriptionsByDoctor(null, n).iterator();
		PatientReg res = new PatientReg();
		Patient temp;
		if(reg == null)//legger til alle pasientene til doktoren dersom innkommende reg = null
		{
			while(iter.hasNext() )
				res.add(iter.next().getPatient() );
			return res;
		}
		while(iter.hasNext() )//legger til alle pasientene til doktoren som er i reg
		{
			temp = iter.next().getPatient();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}

	public PatientReg getPatientsByCat(PatientReg reg, String c)/*returnerer en liste over alle pasientene som har fått resept på en med medisin
																i kategori c, tar imot en liste eller bruker klassens egen liste*/
	{
		if(reg == null)
			reg = patReg;
		PatientReg res = new PatientReg();
		Iterator<Prescription> preIter = getPrescriptionsByCategory(null, c).iterator();//finner alle resepter med kategori lik c
		Patient temp;
		while(preIter.hasNext() )
		{
			temp = preIter.next().getPatient();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}
	public PatientReg getPatientsByMed(PatientReg reg, String med)/*Returnerer en liste over alle pasienter som er blitt foreskrevet en bestemt medisin.
																	Tar imot en liste eller bruker patReg listen*/
	{
		if(reg == null)
			reg = patReg;
		Iterator<Prescription> iter = getPrescriptionsByMedName(null, med).iterator();
		PatientReg res = new PatientReg();
		Prescription temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getMedName().toLowerCase().equals(med.toLowerCase() ) && reg.contains(temp.getPatient() ) )
				res.add(temp.getPatient() );
		}
		return res;
	}
	public PatientReg getPatientsByGroup(PatientReg reg, boolean a, boolean b, boolean c)/*returnerer en liste over alle pasienter som har fått
																						resept på et medikament i gruppene lik true,
																						tar imot en liste eller bruker klassens egen liste*/
	{
		if(reg == null)
			reg = patReg;
		if(!(a || b || c) )
			return reg;
		PatientReg res = new PatientReg();
		Iterator<Prescription> preIter = getPrescriptionsByGroup(null, a, b, c).iterator();
		Patient temp;
		while(preIter.hasNext() )
		{
			temp = preIter.next().getPatient();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}

	//Filtreringsmetoder for doktorsøk
	public DoctorReg getDoctorsByName(DoctorReg reg, String n)//returnerer en liste over alle doktorer med navn eller personnummer lik n
	{
		if(reg == null)
			return docReg.findDoctors(n);
		return reg.findDoctors(n);
	}

	public DoctorReg getDoctorsByPatient(DoctorReg reg, String n)/*returnerer en liste over alle doktorene til en pasient,
																	tar imot en liste eller bruker patReg*/
	{
		Iterator<Prescription> iter = getPrescriptionsByPatient(null, n).iterator();
		DoctorReg res = new DoctorReg();
		Doctor temp;
		if(reg == null)//legger til alle doktorene til denne pasienten hvis reg = null
		{
			while(iter.hasNext() )
				res.add(iter.next().getDoctor() );
			return res;
		}
		while(iter.hasNext() )//søker gjennom reseptene til pasienten og legger docorene som finnes i reg til i res
		{
			temp = iter.next().getDoctor();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}

	public DoctorReg getDoctorsByCat(DoctorReg reg, String c)/*returnerer en liste over alle doktorer som har skrevet resept på en med medisinn
																i kategori c, tar imot en liste eller bruker klassens egen liste*/
	{
		if(reg == null)
			reg = docReg;
		DoctorReg res = new DoctorReg();
		Iterator<Prescription> preIter = getPrescriptionsByCategory(null, c).iterator();
		Doctor temp;
		while(preIter.hasNext() )
		{
			temp = preIter.next().getDoctor();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}

	public DoctorReg getDoctorsByMed(DoctorReg reg, String med)/*Returnerer en liste over alle leger som har skrevet ut resepter på en bestemt medisin.
																Tar imot en liste eller bruker docReg listen*/
	{
		if(reg == null)
			reg = docReg;
		Iterator<Prescription> iter = getPrescriptionsByMedName(null, med).iterator();
		DoctorReg res = new DoctorReg();
		Prescription temp;

		while(iter.hasNext() )
		{
			temp = iter.next();
			if(temp.getMedName().toLowerCase().equals(med.toLowerCase() ) && reg.contains(temp.getDoctor() ) )
				res.add(temp.getDoctor() );
		}
		return res;
	}

	public DoctorReg getDoctorsByGroup(DoctorReg reg, boolean a, boolean b, boolean c)/*returnerer en liste over alle doktorer som har forskrevet
																						resepter i gruppene lik true*/
	{
		if(reg == null)
			reg = docReg;
		if(!(a || b || c) )
			return reg;
		DoctorReg res = new DoctorReg();
		Iterator<Prescription> preIter = getPrescriptionsByGroup(null, a, b, c).iterator();
		Doctor temp;
		while(preIter.hasNext() )
		{
			temp = preIter.next().getDoctor();
			if(reg.contains(temp) )
				res.add(temp);
		}
		return res;
	}

	public DoctorReg getDoctorsByHasGroup(DoctorReg reg, boolean a, boolean b, boolean c)/*returnerer en liste over alle doktorer som har rettighet
																						resepter i gruppene lik true*/
	{
		if(reg == null)
			reg = docReg;
		if(!(a || b || c) )
			return reg;
		DoctorReg res = new DoctorReg();
		Iterator<Doctor> iter = reg.iterator();
		Doctor temp;
		while(iter.hasNext() )
		{
			temp = iter.next();
			if(a && temp.hasLicense(Prescription.GROUP_A) )
				res.add(temp);
			else if(b && temp.hasLicense(Prescription.GROUP_B))
				res.add(temp);
			else if(c && temp.hasLicense(Prescription.GROUP_C) )
				res.add(temp);
		}
		return res;
	}
	//slutt på filtreringsmetoder for doktor

	//slutt på filtreringsmetoder

	public void setCurrentPrescriptionNumber()//nødvendig for skriving/lesing til fil
	{
		preReg.setCurrentNumber();
	}

	/*En oversikt over hvordan forbruket av forskjellige typer medisiner varierer i løpet av et år.
	Ut fra en liste av medisinnavn, for eksempel inntil 10 stykker, og et årstall,
	kan det være ønskelig at programmet kan skrive ut en tabell der hver linje representerer en medisin,
	kolonnene representerer årets tolv måneder og hvert felt i tabellen representerer antall resepter skrevet ut
	på den tilhørende medisinen (linjen) i den aktuelle måneden (kolonnen).*/

	public void exit()//avslutter programmet og lagrer all data til fil
	{
		preReg.saveCurrentNumber();
		data.writeRegister(this);
		System.exit(0);
	}
}
