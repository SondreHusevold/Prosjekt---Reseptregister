/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen utvider AbstractTableModel og definerer modellen for tabellene til programmet, klassen har tre konstruktører, en for resepttabell,
en for pasienttabell og en for pasienttabell. Konstruktørene tar imot et PrescriptionReg, DoctorReg eller PatientReg og bruker
dette til å opprette modellen. Klassen har også en parameterløs konstruktør som oppretter en tom modell.

*/

import java.util.*;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.table.*;

public class TModel extends AbstractTableModel
{
	private String[] names;
	private Object[][] data;
	private final String[] preNames = {"Reseptnummer", "Skrevet ut", "Hentet", "Pasient", "Lege",
									"Medisin navn", "Medisin kategori", "Medisin gruppe"};//kolonnenavn for tabellen
	private final String[] docNames = {"Personnummer", "Fornavn", "Etternavn", "Telefon", "Ansatt ved", "A", "B", "C"};//kollonnenavn for tabellen
	private final String[] patNames = {"Personnummer", "Fornavn", "Etternavn", "Telefon", "Adresse"};//kolonnenavn for tabellen
	public static final int PERSON_NR = 0, FIRSTNAME = 1, LASTNAME = 2, PHONE = 3, ADR = 4, A = 5, B = 6, C = 7,
							PRESCRIPTION_NR = 0, PRINTED = 1, RECIVED = 2, PATIENT = 3, DOCTOR = 4, MED_NAME = 5, MED_CAT = 6, MED_GROUP = 7;
							//nummer på kolonnene
	private int searchFor;
	private boolean editable;
	private Doctor[] doc;
	private Patient[] pat;
	private Prescription[] pre;//arrayer for objecter i tabellen
	public TModel()//oppretter en modell for en tom tabell
	{
		names = new String[0];
		data = new Object[0][0];
		editable = false;
	}
	public TModel(PatientReg reg)//oppretter en model for en pasient tabell
	{
		names = patNames;
		int length = reg.size(), width = names.length;
		data = new Object[length][width];
		pat = new Patient[length];
		Iterator<Patient> iter= reg.iterator();
		Patient temp;
		for(int i = 0; i < length; i++)
		{
			temp = iter.next();
			int j = 0;
			data[i][j++] = temp.getPersonNr();
			data[i][j++] = temp.getfirstName();
			data[i][j++] = temp.getlastName();
			data[i][j++] = temp.getTelephoneNr();
			data[i][j++] = temp.getAddress();
			pat[i] = temp;
		}
		editable = true;
		searchFor = AdminGUI.SEARCH_PATIENT;
	}
	public TModel(DoctorReg reg)//oppretter en model for en doktor tabell
	{
		names = docNames;
		int length = reg.size(), width = names.length;
		data = new Object[length][width];
		doc = new Doctor[length];
		Iterator<Doctor> iter= reg.iterator();
		Doctor temp;
		for(int i = 0; i < length; i++)
		{
			temp = iter.next();
			int j = 0;
			data[i][j++] = temp.getPersonNr();
			data[i][j++] = temp.getfirstName();
			data[i][j++] = temp.getlastName();
			data[i][j++] = temp.getTelephoneNr();
			data[i][j++] = temp.getWorkplace();
			data[i][j++] = temp.getLicenseA();
			data[i][j++] = temp.getLicenseB();
			data[i][j++] = temp.getLicenseC();;
			doc[i] = temp;
		}
		editable = true;
		searchFor = AdminGUI.SEARCH_DOCTOR;
	}
	public TModel(PrescriptionReg reg)//oppretter en tabell for resept tabell
	{
		if(reg == null)
		{
			names = preNames;
			data = new Object[0][0];
			return;
		}
		names = preNames;
		int length = reg.size(), width = names.length;
		data = new Object[length][width];
		pre = new Prescription[length];
		Iterator<Prescription> iter= reg.iterator();
		Prescription temp;
		DateFormat df = DateFormat.getDateInstance();
		for(int i = 0; i < length; i++)
		{
			temp = iter.next();
			int j = 0;
			data[i][j++] = temp.getPrescriptionNr();
			data[i][j++] = df.format(temp.getPrinted().getTime() );
			data[i][j++] = temp.getRecived() == null ? "" : df.format(temp.getRecived().getTime() );
			data[i][j++] = temp.getPatient().getlastName();
			data[i][j++] = temp.getDoctor().getlastName();
			data[i][j++] = temp.getMedName();
			data[i][j++] = temp.getCategory();
			data[i][j++] = PrescriptionReg.getGroupString(temp.getGroup() );
			pre[i] = temp;
		}
		editable = false;
		searchFor = AdminGUI.SEARCH_PRESCRIPTION;
	}

	public int getSearchFor()
	{
		return searchFor;
	}

	public Doctor[] getDoctors()
	{
		return doc;
	}

	public Patient[] getPatients()
	{
		return pat;
	}

	public Prescription[] getPrescriptions()
	{
		return pre;
	}

	public String getColumnName(int c)
	{
		return names[c];
	}
	public int getRowCount()
	{
		return data.length;
	}
	public int getColumnCount()
	{
		return names.length;
	}
	public Object getValueAt(int r, int c)
	{
		return data[r][c];
	}
    public Class<?> getColumnClass(int c)
    {
		return data[0][c].getClass();
	}
	public boolean isCellEditable(int r, int c)
	{
		if(!editable)
			return false;
		if(c == PERSON_NR)
			return false;
		if(c == FIRSTNAME || c == LASTNAME || c == PHONE || c == ADR || c == A || c == B || c == C)
			return true;
		return false;
	}

	public void setValueAt(Object value, int r, int c)
	{
		data[r][c] = value;
	}
	public void setCellEditable(boolean b)
	{
		editable = false;
	}
	public void saveChanges()//metode lagrer dataen i tabellen slik den står
	{
		if(searchFor == AdminGUI.SEARCH_DOCTOR)
		{
			for(int r = 0; r < getRowCount(); r++)
				for(int c = 0; c < getColumnCount(); c++)
				{
					if(c == FIRSTNAME)
						doc[r].setfirstName( (String)getValueAt(r, c) );
					else if(c == LASTNAME)
						doc[r].setlastName( (String)getValueAt(r, c) );
					else if(c == PHONE)
						doc[r].setTelephoneNr( (String)getValueAt(r, c) );
					else if(c == ADR)
						doc[r].setWorkplace( (String)getValueAt(r, c) );
					else if(c == A)
						doc[r].setLicenseA( (boolean)getValueAt(r, c) );
					else if(c == B)
						doc[r].setLicenseB( (boolean)getValueAt(r, c) );
					else if(c == C)
						doc[r].setLicenseC( (boolean)getValueAt(r, c) );
				}
		}//end of if(search doctor)
		else if(searchFor == AdminGUI.SEARCH_PATIENT)
		{
			for(int r = 0; r < getRowCount(); r++)
				for(int c = 0; c < getColumnCount(); c++)
				{
					if(c == FIRSTNAME)
						pat[r].setfirstName( (String)getValueAt(r, c) );
					else if(c == LASTNAME)
						pat[r].setlastName( (String)getValueAt(r, c) );
					else if(c == PHONE)
						pat[r].setTelephoneNr( (String)getValueAt(r, c) );
					else if(c == ADR)
						pat[r].setAddress( (String)getValueAt(r, c) );
				}
		}//end of if(search patient)
	}

	public void setTableCellEditor(JTable table)//setter JTextField som editor for String felter som kan endres
	{
		for(int i = 0; i < table.getRowCount(); i++)
		{
			TableCellEditor textEdit = new DefaultCellEditor(new JTextField() );
			TableColumnModel columnModel = table.getColumnModel();
			columnModel.getColumn(FIRSTNAME).setCellEditor(textEdit);
			columnModel.getColumn(LASTNAME).setCellEditor(textEdit);
			columnModel.getColumn(PHONE).setCellEditor(textEdit);
			columnModel.getColumn(ADR).setCellEditor(textEdit);
		}//end of for(row)
	}//end of method
}//end of class