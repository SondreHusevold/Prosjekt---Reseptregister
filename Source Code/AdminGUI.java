/*Skrevet av Magnus Tønsager, s198761. Sist endret 08.05.14
Klassen definerer brukergrensesnittet for administrator, klassen utvider JPanel med felter og knapper for søkefunksjoner
brukeren kan bruke til å søke seg frem til øsnket data. Panelet har også funskjoner for å endre data der dette er tillat

*/
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class AdminGUI extends JPanel
{
	public static final int SEARCH_PRESCRIPTION = 1, SEARCH_PATIENT = 2, SEARCH_DOCTOR = 3, DATA_FIELD_LENGTH = 20;
	private boolean la, lb, lc, hla, hlb, hlc;
	private int searchFor;
	private MainWindow win;
	private JLabel searchForLabel, patNameLabel, docNameLabel, medNameLabel, medCatLabel, medGroupLabel, hasMedGroupLabel;
	private JTextArea info, showPrescriptionInfo;
	private JTextField patName, docName, medName, medCat;
	private ButtonGroup groupButtons;
	private JCheckBox groupA, groupB, groupC, hasGroupA, hasGroupB, hasGroupC;
	private JRadioButton pre, pat, doc;
	private JButton searchButton, newDoc, showPrescriptionButton, hidePrescriptionButton, save, statButton;
	private RadioButtonListener radioButtonListener;
	private Listener listener;
	private LicenceListener licenceListener;
	private JPanel hasLicChoose, grid, searchGrid, bord, licChoose, flow, showPrescription, showPrescriptionFlow;
	private Register register;
	private TModel tableModel;
	private JTable table;

	public AdminGUI(MainWindow w)
	{
		win = w;
		register = win.getRegister();
		//oppretter lyttere
		radioButtonListener = new RadioButtonListener();
		listener = new Listener();
		licenceListener = new LicenceListener();
		//oppretter avkryssningsbokser
		groupButtons = new ButtonGroup();
		pre = new JRadioButton("Resepter", true);
		pat = new JRadioButton("Pasienter", false);
		doc = new JRadioButton("Doktorer", false);
		groupA = new JCheckBox("Gruppe A", true);
		groupB = new JCheckBox("Gruppe B", true);
		groupC = new JCheckBox("Gruppe C", true);
		hasGroupA = new JCheckBox("Gruppe A", true);
		hasGroupB = new JCheckBox("Gruppe B", true);
		hasGroupC = new JCheckBox("Gruppe C", true);
		pre.addItemListener(radioButtonListener);
		pat.addItemListener(radioButtonListener);
		doc.addItemListener(radioButtonListener);
		groupButtons.add(pre);
		groupButtons.add(pat);
		groupButtons.add(doc);

		setLayout(new BorderLayout() );
		//oppretter JPaneler med uike LayoutManagere
		showPrescription = new JPanel(new BorderLayout() );
		grid = new JPanel(new GridLayout(4, 1) );
		searchGrid = new JPanel(new GridLayout(15, 1) );
		bord = new JPanel(new BorderLayout() );
		licChoose = new JPanel(new GridLayout(1, 3) );
		hasLicChoose = new JPanel(new GridLayout(1, 3) );
		flow = new JPanel(new FlowLayout() );
		showPrescriptionFlow = new JPanel(new FlowLayout() );

		info = new JTextArea("Her kan du finne og vise ulik info om resepter, leger og pasienter. " +
								"Under søk etter kan du velge om du vil søke etter resept, lege eller pasient. " +
								"Du bestemmer søkekriteriene ved å fylle ut feltene under, tomme felter blir ikke tatt med i søket");
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		showPrescriptionInfo = new JTextArea();
		showPrescriptionInfo.setEditable(false);
		showPrescriptionInfo.setVisible(false);
		patName = new JTextField(DATA_FIELD_LENGTH);
		docName = new JTextField(DATA_FIELD_LENGTH);
		medName = new JTextField(DATA_FIELD_LENGTH);
		medCat = new JTextField(DATA_FIELD_LENGTH);
		searchForLabel = new JLabel("Søk etter:");
		patNameLabel = new JLabel("Pasient navn/personnummer:");//legge til tooltip på disse
		patNameLabel.setToolTipText("Søk på peronnummer for mest presise søk, evt. etternavn");
		docNameLabel = new JLabel("Doktor navn/personnummer:");
		docNameLabel.setToolTipText("Søk på peronnummer for mest presise søk, evt. etternavn");
		medNameLabel = new JLabel("Medikament navn:");
		medGroupLabel = new JLabel("Forskrevet medikament i gruppe:");
		hasMedGroupLabel = new JLabel("Rettighet til medikament i gruppe");
		medCatLabel = new JLabel("Medikament kategori:");
		searchButton = new JButton("Søk");
		newDoc = new JButton("Legg til en ny doktor");
		save = new JButton("Lagre endringer");
		showPrescriptionButton = new JButton("Vis resept");
		hidePrescriptionButton = new JButton("Skjul Resept");
		statButton = new JButton("Vis statistikk");
		hidePrescriptionButton.setVisible(false);
		//legger til lyttere
		searchButton.addActionListener(listener);
		patName.addActionListener(listener);
		docName.addActionListener(listener);
		medName.addActionListener(listener);
		medCat.addActionListener(listener);
		newDoc.addActionListener(listener);
		save.addActionListener(listener);
		showPrescriptionButton.addActionListener(listener);
		hidePrescriptionButton.addActionListener(listener);
		statButton.addActionListener(listener);
		groupA.addItemListener(licenceListener);
		groupB.addItemListener(licenceListener);
		groupC.addItemListener(licenceListener);
		hasGroupA.addItemListener(licenceListener);
		hasGroupB.addItemListener(licenceListener);
		hasGroupC.addItemListener(licenceListener);
		//legger elementer til i GUIen
		licChoose.add(groupA);
		licChoose.add(groupB);
		licChoose.add(groupC);
		hasLicChoose.add(hasGroupA);
		hasLicChoose.add(hasGroupB);
		hasLicChoose.add(hasGroupC);
		grid.add(searchForLabel);
		grid.add(pre);
		grid.add(pat);
		grid.add(doc);
		searchGrid.add(patNameLabel);
		searchGrid.add(patName);
		searchGrid.add(docNameLabel);
		searchGrid.add(docName);
		searchGrid.add(medNameLabel);
		searchGrid.add(medName);
		searchGrid.add(medCatLabel);
		searchGrid.add(medCat);
		searchGrid.add(medGroupLabel);
		searchGrid.add(licChoose);
		searchGrid.add(hasMedGroupLabel);
		searchGrid.add(hasLicChoose);
		searchGrid.add(searchButton);
		searchGrid.add(newDoc);
		searchGrid.add(statButton);
		bord.add(grid, BorderLayout.PAGE_START);
		bord.add(searchGrid, BorderLayout.LINE_START);
		flow.add(bord);
		showPrescriptionFlow.add(showPrescriptionButton);
		showPrescriptionFlow.add(save);
		showPrescriptionFlow.add(hidePrescriptionButton);
		showPrescription.add(showPrescriptionFlow, BorderLayout.LINE_END);
		showPrescription.add(showPrescriptionInfo, BorderLayout.CENTER);
		add(info, BorderLayout.PAGE_START);
		add(new JScrollPane(flow, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.LINE_START);
		//oppretter tabellen for visning og redigering
		tableModel = new TModel();
		table = new JTable(tableModel);
		//legger til elementer i hovedpanelet
		add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(new JScrollPane(showPrescription), BorderLayout.PAGE_END);
		//setter guien til å starte med resept valgt
		radioButtonListener.itemStateChanged(null);
		licenceListener.itemStateChanged(null);
	}
	public int getSearchFor()
	{
		return searchFor;
	}

	public void search()
	{
		switch(searchFor)
		{
			case SEARCH_PRESCRIPTION:
				searchPrescription();
			break;
			case SEARCH_PATIENT:
				searchPatient();
			break;
			case SEARCH_DOCTOR:
				searchDoctor();
			break;
		}
	}

	public PrescriptionReg searchPrescription()
	{
		PrescriptionReg res = register.getPrescriptions();
		String p = patName.getText();
		String d = docName.getText();
		String m = medName.getText();
		String c = medCat.getText();

		if(!p.equals("") )
			res = register.getPrescriptionsByPatient(res, p);
		if(!d.equals("") )
			res = register.getPrescriptionsByDoctor(res, d);
		if(!m.equals("") )
			res = register.getPrescriptionsByMedName(res, m);
		if(!c.equals("") )
			res = register.getPrescriptionsByCategory(res, c);
		res = register.getPrescriptionsByGroup(res, la, lb, lc);

		tableModel = new TModel(res);
		table.setModel(tableModel);
		return res;
	}
	public PatientReg searchPatient()
	{
		PatientReg res = register.getPatients();

		String p = patName.getText();
		String d = docName.getText();
		String m = medName.getText();
		String c = medCat.getText();

		if(!p.isEmpty() )
			res = register.getPatientsByName(res, p);
		if(!d.isEmpty() )
			res = register.getPatientsByDoctor(res, d);
		if(!m.isEmpty() )
			res = register.getPatientsByMed(res, m);
		if(!c.isEmpty() )
			res = register.getPatientsByCat(res, c);
		res = register.getPatientsByGroup(res, la, lb, lc);

		tableModel = new TModel(res);
		table.setModel(tableModel);
		tableModel.setTableCellEditor(table);
		return res;
	}
	public DoctorReg searchDoctor()
	{
		DoctorReg res = register.getDoctors();

		String p = patName.getText();
		String d = docName.getText();
		String m = medName.getText();
		String c = medCat.getText();;

		if(!d.equals("") )
			res = register.getDoctorsByName(res, d);
		if(!p.equals("") )
			res = register.getDoctorsByPatient(res, p);
		if(!m.equals("") )
			res = register.getDoctorsByMed(res, m);
		if(!c.equals("") )
			res = register.getDoctorsByCat(res, c);
		res = register.getDoctorsByGroup(res, la, lb, lc);
		res = register.getDoctorsByHasGroup(res, hla, hlb, hlc);

		tableModel = new TModel(res);
		table.setModel(tableModel);
		tableModel.setTableCellEditor(table);
		return res;
	}

	public void newDoctor()
	{
		win.changePanel(new AddDoctorGUI(win) );
	}
	public void showPrescription()
	{
		try
		{
			int row = table.getSelectedRow();
			if(row == -1)
			{
				JOptionPane.showMessageDialog(null, "Du må velge en resept først");
				return;
			}
			Prescription res = register.getPrescriptionByNr( (Integer)tableModel.getValueAt(row, TModel.PRESCRIPTION_NR) );
			showPrescriptionInfo.setText(res.toString() );
			//JOptionPane.showMessageDialog(null, tableModel);
		}
		catch(NullPointerException npe)
		{
			JOptionPane.showMessageDialog(null, "Feil", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		hidePrescriptionButton.setVisible(true);
		showPrescriptionInfo.setVisible(true);
		validate();
	}
	public void hidePrescription()
	{
		showPrescriptionInfo.setVisible(false);
		hidePrescriptionButton.setVisible(false);
		validate();
	}
	public void showStatistics()
	{
		StatisticTableModel model = null;
		PrescriptionReg pr;
		Person temp;
		int r;
		int LAST_DAY = 31, FIRST_DAY = 1;//programmet kan utides med mulighet for å velge dato selv
		Calendar start = Calendar.getInstance();//programmet ville isåfall hentet ut datoen fra brukeren her
		Calendar stop = Calendar.getInstance();
		start.set(Calendar.YEAR, stop.get(Calendar.YEAR) - 1);
		start.set(Calendar.MONTH, Calendar.DECEMBER);
		start.set(Calendar.DATE, LAST_DAY);
		stop.set(Calendar.YEAR, stop.get(Calendar.YEAR) + 1);
		stop.set(Calendar.MONTH, Calendar.JANUARY);
		stop.set(Calendar.DATE, FIRST_DAY);
		if(searchFor == SEARCH_PRESCRIPTION)
			pr = searchPrescription();
		else if(searchFor == SEARCH_DOCTOR)
		{
			r = table.getSelectedRow();
			if(r == -1)
			{
				JOptionPane.showMessageDialog(null, "Du må velge en doktor først");
				return;
			}
			temp = register.getDoctorByNumber( (String)tableModel.getValueAt(r, TModel.PERSON_NR) );
			pr = temp.getPrescriptions();
		}
		else if(searchFor == SEARCH_PATIENT)
		{
			r = table.getSelectedRow();
			if(r == -1)
			{
				JOptionPane.showMessageDialog(null, "Du må velge en pasient først");
				return;
			}
			temp = register.getPatientByNumber( (String)tableModel.getValueAt(r, TModel.PERSON_NR) );
			pr = temp.getPrescriptions();
		}
		else
			return;
		pr = register.getPrescriptionsBeforeDate(pr, stop);
		pr = register.getPrescriptionsAfterDate(pr, start);
		model = new StatisticTableModel(pr);
		table.setModel(model);
	}

	private class RadioButtonListener implements ItemListener//lytter for valg av søk type
	{
		public void itemStateChanged(ItemEvent e)
		{
			if(pre.isSelected() )
			{
				searchFor = SEARCH_PRESCRIPTION;
				hasLicChoose.setVisible(false);
				hasMedGroupLabel.setVisible(false);
				showPrescriptionButton.setVisible(true);
				save.setVisible(false);
				searchPrescription();
			}
			else if(pat.isSelected() )
			{
				searchFor = SEARCH_PATIENT;
				hasLicChoose.setVisible(false);
				hasMedGroupLabel.setVisible(false);
				showPrescriptionButton.setVisible(false);
				save.setVisible(true);
				hidePrescription();
				searchPatient();
			}
			else if(doc.isSelected() )
			{
				searchFor = SEARCH_DOCTOR;
				hasLicChoose.setVisible(true);
				hasMedGroupLabel.setVisible(true);
				showPrescriptionButton.setVisible(false);
				save.setVisible(true);
				hidePrescription();
				searchDoctor();
			}
		}
	}
	private class LicenceListener implements ItemListener//lytter for lisens og medikament gruppe valg
	{
		public void itemStateChanged(ItemEvent e)
		{
			if(groupA.isSelected() )
				la = true;
			else
				la = false;
			if(groupB.isSelected() )
				lb = true;
			else
				lb = false;
			if(groupC.isSelected() )
				lc = true;
			else
				lc = false;
			if(hasGroupA.isSelected() )
				hla = true;
			else
				hla = false;
			if(hasGroupB.isSelected() )
				hlb = true;
			else
				hlb = false;
			if(hasGroupC.isSelected() )
				hlc = true;
			else
				hlc = false;
		}
	}
	private class Listener implements ActionListener//knappelytter
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == searchButton || e.getSource() == patName || e.getSource() ==  docName ||
				e.getSource() == medName ||e.getSource() ==  medCat)
				search();
			else if(e.getSource() == newDoc)
				newDoctor();
			else if(e.getSource() == showPrescriptionButton)
				showPrescription();
			else if(e.getSource() == hidePrescriptionButton)
				hidePrescription();
			else if(e.getSource() == save)
				tableModel.saveChanges();
			else if(e.getSource() == statButton)
				showStatistics();
		}
	}
}