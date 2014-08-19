/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen definerer brukergrensesnittet for apoteker, klassen utvider JPanel med funskjoner for å finne, vise og hente ut resepter

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PharmacyGUI extends JPanel
{
	private final int DATA_FIELD_LENGTH = 20;
	private int selectedRow;
	private boolean la, lb, lc;
	private MainWindow win;
	private JLabel preNrLabel, patNameLabel, docNameLabel, medNameLabel, medCatLabel, medGroupLabel;
	private JTextArea info, showPrescriptionInfo;
	private JTextField preNr, patName, docName, medName, medCat;
	private JCheckBox groupA, groupB, groupC;
	private JButton searchButton, showPrescriptionButton, hidePrescriptionButton, recivePrescriptionButton;
	private Listener listener;
	private LicenceListener licenceListener;
	private JPanel showPrescription, showPrescriptionFlow, searchGrid, bord, licChoose, flow;
	private Register register;
	private TModel tableModel;
	private JTable table;

	public PharmacyGUI(MainWindow w)
	{
		win = w;
		register = win.getRegister();
		//oppretter lyttere
		listener = new Listener();
		licenceListener = new LicenceListener();
		//oppretter checkboxer
		groupA = new JCheckBox("Gruppe A", true);
		groupB = new JCheckBox("Gruppe B", true);
		groupC = new JCheckBox("Gruppe C", true);
		//oppretter paneler med ulike layoutmanagere
		setLayout(new BorderLayout() );
		showPrescription = new JPanel(new BorderLayout() );
		showPrescriptionFlow = new JPanel(new FlowLayout() );
		searchGrid = new JPanel(new GridLayout(15, 1) );
		bord = new JPanel(new BorderLayout() );
		licChoose = new JPanel(new GridLayout(1, 3) );
		flow = new JPanel(new FlowLayout() );
		//setter opp infofeltet
		info = new JTextArea("Velkommen! Du finner en resept ved å fylle ut en eller flere av feltene under og trykke på søk. " +
								"Marker resepten du vil hente i tabellen og trykk på åpne resept for å vise og hente ut resept");//skrive mer info her
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		showPrescriptionInfo = new JTextArea();
		showPrescriptionInfo.setEditable(false);
		showPrescriptionInfo.setVisible(false);
		info.setEditable(false);
		//oppretter søkefelter
		preNr = new JTextField(DATA_FIELD_LENGTH);
		patName = new JTextField(DATA_FIELD_LENGTH);
		docName = new JTextField(DATA_FIELD_LENGTH);
		medName = new JTextField(DATA_FIELD_LENGTH);
		medCat = new JTextField(DATA_FIELD_LENGTH);
		preNrLabel = new JLabel("Reseptnummer:");
		patNameLabel = new JLabel("Pasient navn/personnummer:");
		patNameLabel.setToolTipText("Søk på peronnummer for mest presise søk, evt. etternavn");
		docNameLabel = new JLabel("Doktor navn/pernnummer:");
		docNameLabel.setToolTipText("Søk på peronnummer for mest presise søk, evt. etternavn");
		medNameLabel = new JLabel("Medikament navn:");
		medGroupLabel = new JLabel("Forskrevet medikament i gruppe:");
		medCatLabel = new JLabel("Medikament kategori:");
		searchButton = new JButton("Søk");
		showPrescriptionButton = new JButton("Åpne resept");
		hidePrescriptionButton = new JButton("Lukk resept");
		hidePrescriptionButton.setVisible(false);
		recivePrescriptionButton = new JButton("Hent ut resept");
		recivePrescriptionButton.setEnabled(false);
		//legger til lyttere
		searchButton.addActionListener(listener);
		patName.addActionListener(listener);
		docName.addActionListener(listener);
		medName.addActionListener(listener);
		medCat.addActionListener(listener);
		showPrescriptionButton.addActionListener(listener);
		hidePrescriptionButton.addActionListener(listener);
		recivePrescriptionButton.addActionListener(listener);
		groupA.addItemListener(licenceListener);
		groupB.addItemListener(licenceListener);
		groupC.addItemListener(licenceListener);
		licenceListener.itemStateChanged(null);
		//legger elementer til i GUIen
		licChoose.add(groupA);
		licChoose.add(groupB);
		licChoose.add(groupC);
		searchGrid.add(preNrLabel);
		searchGrid.add(preNr);
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
		searchGrid.add(searchButton);
		showPrescriptionFlow.add(showPrescriptionButton);
		showPrescriptionFlow.add(hidePrescriptionButton);
		showPrescriptionFlow.add(recivePrescriptionButton);
		showPrescription.add(showPrescriptionFlow, BorderLayout.LINE_END);
		showPrescription.add(showPrescriptionInfo, BorderLayout.CENTER);
		bord.add(searchGrid, BorderLayout.LINE_START);
		flow.add(bord);
		add(info, BorderLayout.PAGE_START);
		add(new JScrollPane(flow, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.LINE_START);
		//oppretter tabellen for visning
		tableModel = new TModel(register.getPrescriptionsByRecived(null, false) );
		table = new JTable(tableModel);

		add(new JScrollPane(table), BorderLayout.CENTER);
		add(new JScrollPane(showPrescription), BorderLayout.PAGE_END);
	}

	public void searchPrescription()//søker ett resepter ut fra søkekriterier
	{
		PrescriptionReg res = register.getPrescriptions();
		String n = preNr.getText();
		String p = patName.getText();
		String d = docName.getText();
		String m = medName.getText();
		String c = medCat.getText();

		if(!n.equals("") )
		{
			try
			{
				res = register.getPrescriptionByNr(res, Integer.parseInt(n) );
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "Reseptnummeret må være et tall", "Feil", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		res = register.getPrescriptionsByRecived(res, false);
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
	}
	public void showPrescription()//viser showPrescription området
	{
		selectedRow = table.getSelectedRow();
		if(selectedRow == -1)
		{
			JOptionPane.showMessageDialog(null, "Du må velge en resept først");
			return;
		}
		Prescription res = register.getPrescriptionByNr( (Integer)tableModel.getValueAt(selectedRow, TModel.PRESCRIPTION_NR) );
		showPrescriptionInfo.setText(res.toString() );
		hidePrescriptionButton.setVisible(true);
		showPrescriptionButton.setVisible(false);
		showPrescriptionInfo.setVisible(true);
		if(res.isRecived() )
			recivePrescriptionButton.setEnabled(false);
		else
			recivePrescriptionButton.setEnabled(true);
		validate();
	}
	public void hidePrescription()//skjuler showPrescription området
	{
		showPrescriptionInfo.setVisible(false);
		hidePrescriptionButton.setVisible(false);
		recivePrescriptionButton.setEnabled(false);
		showPrescriptionButton.setVisible(true);
		validate();
	}
	public void recivePrescription()//registrerer valgt resept som henter
	{
		if(selectedRow == -1)
		{
			JOptionPane.showMessageDialog(null, "Du må velge en resept først");
			return;
		}
		Prescription res = register.getPrescriptionByNr( (Integer)tableModel.getValueAt(selectedRow, TModel.PRESCRIPTION_NR) );
		res.setRecived();
		showPrescriptionInfo.setText("Resept registrert som hentet" + res.toString() );
		recivePrescriptionButton.setEnabled(false);
		searchPrescription();
		validate();
		//showPrescriptionButton.setVisible(true);
		//JOptionPane.showMessageDialog(null, tableModel);
	}

	private class LicenceListener implements ItemListener//lytter for medikament gruppe checkboxer
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
		}
	}
	private class Listener implements ActionListener//lytter for knapper
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == searchButton || e.getSource() == patName || e.getSource() ==  docName ||
				e.getSource() == medName ||e.getSource() ==  medCat)
				searchPrescription();
			else if(e.getSource() == showPrescriptionButton)
				showPrescription();
			else if(e.getSource() == hidePrescriptionButton)
				hidePrescription();
			else if(e.getSource() == recivePrescriptionButton)
				recivePrescription();
		}
	}
}