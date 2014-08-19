/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen ineholder en liste over alle reseptene, har metoder for behandling av listen

*/

import java.io.Serializable;
import java.util.*;
import javax.swing.*;

public class PrescriptionReg implements Serializable
{
	private static final long serialVersionUID = 42L;
	private TreeSet<Prescription> list;//listen
	private int currentNumber;

	public PrescriptionReg()
	{
		list = new TreeSet<>(new PrescriptionCollator() );//Oppretter reseptlisten
	}

	public boolean add(Prescription pre)
	{
		return list.add(pre);
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	public Iterator<Prescription> iterator()
	{
		return list.iterator();
	}

	public boolean contains(Prescription in)
	{
		return list.contains(in);
	}

	public int size()
	{
		return list.size();
	}

	public static int getGroupInt(String in)//metode gjør om en String til int for medikamentgruppene
	{
		if(in.equals("a") || in.equals("A") )
			return Prescription.GROUP_A;
		else if(in.equals("b") || in.equals("B") )
			return Prescription.GROUP_B;
		else if(in.equals("c") || in.equals("C") )
			return Prescription.GROUP_C;
		JOptionPane.showMessageDialog(null, "Ugyldig gruppe for medikament");
		return -1;
	}

	public static String getGroupString(int in)//metode gjør om en int til String for medikamentgruppene
	{
		if(in == Prescription.GROUP_A)
			return "A";
		else if(in == Prescription.GROUP_B)
			return "B";
		else if(in == Prescription.GROUP_C)
			return "C";
		return "Error";
	}

	public void saveCurrentNumber()//nødvendig for skriving/lagring til fil
	{
		currentNumber = Prescription.getCurrentNumber();
	}

	public void setCurrentNumber()//nødvendig for skriving/lagring til fil
	{
		Prescription.setCurrentNumber(currentNumber);
	}

	public String toString()
	{
		StringBuilder res = new StringBuilder();
		Iterator<Prescription> ite = list.iterator();
		while(ite.hasNext() )
			res.append(ite.next().toString() );
			res.append("\n");
		return res.toString();
	}
}
