/*Skrevet av Magnus Tønsager, s198761. Sist endret 13.05.14
Klassen utvider AbstractTableModel og definerer modellen for statestikk tabellene til programmet. Konstruktørene tar imot et PrescriptionReg
og bruker dette til å opprette modellen. Klassen har metode for å telle over antall resepter per legemiddel per måned.

*/

import java.util.*;
import javax.swing.table.*;

public class StatisticTableModel extends AbstractTableModel
{
	private final String[] statNames = {"Medisin", "Jan", "Feb", "Mar", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des"};//kolonnenavn for statestikk visning
	private final int MED_NAME = 0, JAN = 1, FEB = 2, MAR = 3, APR = 4, MAI = 5,
						JUN = 6, JUL = 7, AUG = 8, SEP = 9, OKT = 10, NOV = 11, DES = 12;//kolonnekonstanter
	private int jan, feb, mar, apr, mai, jun, jul, aug, sep, oct, nov, dec;//variabler for antall resepter
	private Object[][] data;//data arrayen
	private PrescriptionReg pReg;
	private Register reg;
	private TreeSet<String> medNames;

	public StatisticTableModel(PrescriptionReg pRegPara)
	{
		pReg = pRegPara;
		medNames = new TreeSet<>();
		reg = new Register(null);
		findNames();//legger inn alle medikamentnavnene i medNames
		data = new Object[medNames.size()][statNames.length];
		setUpData();//setter opp dataen i data
	}
	public void findNames()//metode legger inn alle medikamentnavnene i medNames
	{
		Iterator<Prescription> iter = pReg.iterator();
		while(iter.hasNext() )
		{
			medNames.add(iter.next().getMedName().toUpperCase() );
		}
	}
	public void setUpData()//setter opp statistikken
	{
		Iterator<String> iter = medNames.iterator();
		Iterator<Prescription> preIter;
		PrescriptionReg preReg;
		Prescription temp;
		String medName;
		for(int i = 0; i < data.length; i++)//går gjennom alle medikamentnavnene
		{
			medName = iter.next();
			preReg = reg.getPrescriptionsByMedName(pReg, medName);
			preIter = preReg.iterator();
			jan = 0; feb = 0; mar = 0; apr = 0; mai = 0; jun = 0; jul = 0; aug = 0; sep = 0; oct = 0; nov = 0; dec = 0;
			while(preIter.hasNext() )//finner antall resepter per måned
			{
				temp = preIter.next();
				int m = temp.getPrinted().get(Calendar.MONTH);
				switch(m)
				{
					case Calendar.JANUARY:
						jan++;
					break;
					case Calendar.FEBRUARY:
						feb++;
					break;
					case Calendar.MARCH:
						mar++;
					break;
					case Calendar.APRIL:
						apr++;
					break;
					case Calendar.MAY:
						mai++;
					break;
					case Calendar.JUNE:
						jun++;
					break;
					case Calendar.JULY:
						jul++;
					break;
					case Calendar.AUGUST:
						aug++;
					break;
					case Calendar.SEPTEMBER:
						sep++;
					break;
					case Calendar.OCTOBER:
						oct++;
					break;
					case Calendar.NOVEMBER:
						nov++;
					break;
					case Calendar.DECEMBER:
						dec++;
					break;
				}//end of switch(month)
			}//end of while(hasNext)
			setUpValues(i, medName);//setter dataen inn i data arrayen
		}//end of for(data.length)
	}
	public void setUpValues(int i, String name)//metode setter data inn på plass i i data arrayen
	{
		data[i][MED_NAME] = name;
		data[i][JAN] = new Integer(jan);
		data[i][FEB] = new Integer(feb);
		data[i][MAR] = new Integer(mar);
		data[i][APR] = new Integer(apr);
		data[i][MAI] = new Integer(mai);
		data[i][JUN] = new Integer(jun);
		data[i][JUL] = new Integer(jul);
		data[i][AUG] = new Integer(aug);
		data[i][SEP] = new Integer(sep);
		data[i][OKT] = new Integer(oct);
		data[i][NOV] = new Integer(nov);
		data[i][DES] = new Integer(dec);
	}
	public String getColumnName(int c)
	{
		return statNames[c];
	}
	public int getRowCount()
	{
		return data.length;
	}
	public int getColumnCount()
	{
		return statNames.length;
	}
	public Object getValueAt(int r, int c)
	{
		return data[r][c];
	}
	public Class<?> getColumnClass(int c)
	{
			return data[0][c].getClass();
	}
	public void setValueAt(Object value, int r, int c)
	{
		data[r][c] = value;
	}
}//end of class