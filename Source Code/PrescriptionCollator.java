/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen definerer en Comparator for resepter, sorterer reseptene på reseptnummeret

*/

import java.io.Serializable;
import java.util.Comparator;


public class PrescriptionCollator implements Comparator<Prescription>, Serializable
{
	private static final long serialVersionUID = 42L;

	public int compare(Prescription a, Prescription b)
	{
		return a.getPrescriptionNr() - b.getPrescriptionNr();
	}

}
