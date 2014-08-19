/*

Laget av Sondre Husevold (s198755)

Skriving av resept skjer her. All informasjon som navn på legemidler osv blir satt inn her av legene.

*/

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class WritePrescriptionGUI extends JPanel{
	private JButton confirmButton;
	private JTextField medicamentName, categoryName, doseArea;
	private JComboBox<String> groupBox;
	private JTextArea manualArea;
	private JScrollPane manualScroll;
	private MainWindow mainframe;
	private Patient patient;
	private Doctor you;

	public WritePrescriptionGUI(MainWindow m, Patient patientPara, Doctor youPara){
		patient = patientPara;
		you = youPara;
		mainframe = m;

		medicamentName = new JTextField(20);
		categoryName = new JTextField(20);
		doseArea = new JTextField(20);
		medicamentName.setToolTipText("Navn på legemiddelet.");
		categoryName.setToolTipText("Legemiddelets kategori.");
		
		doseArea.setToolTipText("Dosen til legemiddelet som blir skrevet ut.");
		manualArea = new JTextArea(20, 30);
		manualArea.setLineWrap(true);
		manualArea.setWrapStyleWord(true);
		manualScroll = new JScrollPane(manualArea);
		manualArea.setToolTipText("Skriv inn en anvisning til hvordan legemiddelet skal brukes.");


		setupLicenses();
		confirmButton = new JButton("Skriv resept");
		ButtonListener theListener = new ButtonListener();
		confirmButton.addActionListener(theListener);

		JPanel setMedicamentPropertiesPanel = new JPanel(new GridLayout(0,1));
		setMedicamentPropertiesPanel.add(new JLabel("Medikament navn:"));
		setMedicamentPropertiesPanel.add(medicamentName);
		setMedicamentPropertiesPanel.add(new JLabel("Gruppe:"));
		setMedicamentPropertiesPanel.add(groupBox);
		setMedicamentPropertiesPanel.add(new JLabel("Kategori:"));
		setMedicamentPropertiesPanel.add(categoryName);
		setMedicamentPropertiesPanel.setBorder(new EmptyBorder(0,0,5,0));

		JLabel dose = new JLabel("Dosering:");
		dose.setBorder(new EmptyBorder(5,0,5,0));
		setMedicamentPropertiesPanel.add(dose);
		setMedicamentPropertiesPanel.add(doseArea);

		JPanel setLeftPanel = new JPanel(new BorderLayout());
		setLeftPanel.add(setMedicamentPropertiesPanel, BorderLayout.NORTH);
		setLeftPanel.setBorder(new EmptyBorder(5, 5, 5, 5) );

		JPanel setButtons = new JPanel();
		setButtons.add(confirmButton);

		JPanel setManualAreaPanel = new JPanel(new BorderLayout());
		JLabel anvisning = new JLabel("Anvisning:");
		anvisning.setBorder(new EmptyBorder(5,0,5,0));
		setManualAreaPanel.add(anvisning, BorderLayout.NORTH);
		setManualAreaPanel.add(manualScroll, BorderLayout.CENTER);
		setManualAreaPanel.setBorder(new EmptyBorder(5, 5, 5, 5) );

		setLayout(new BorderLayout());
		add(setLeftPanel, BorderLayout.LINE_START);
		add(setManualAreaPanel, BorderLayout.CENTER);
		add(setButtons, BorderLayout.SOUTH);
	}

	public void setupLicenses(){ // Setter opp lisenser for JComboBox basert på rettigheter til legen.
		int licenses = 0;
		if(you.hasLicense(Prescription.GROUP_A))
			licenses++;
		if(you.hasLicense(Prescription.GROUP_B))
			licenses++;
		if(you.hasLicense(Prescription.GROUP_C))
			licenses++;
		String[] groups = new String[licenses--];
		if(you.hasLicense(Prescription.GROUP_C))
			groups[licenses--] = "C";
		if(you.hasLicense(Prescription.GROUP_B))
			groups[licenses--] = "B";
		if(you.hasLicense(Prescription.GROUP_A))
			groups[licenses--] = "A";

		groupBox = new JComboBox<String>(groups);
		groupBox.setToolTipText("Legemiddelets reseptgruppe.");
	}

	public boolean checkFields(){ // Sjekker felter for feil.
		if(doseArea.getText().isEmpty() || manualArea.getText().isEmpty() ||
				medicamentName.getText().isEmpty() || categoryName.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Det må skrives noe inn i alle felt!", "FEIL!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private class ButtonListener implements ActionListener{
	    public void actionPerformed( ActionEvent e ){
	      if(e.getSource() == confirmButton){
	    	  if(!checkFields())
	    		  return;
	    	  mainframe.changePanel(new ConfirmPrescriptionGUI(mainframe, patient, medicamentName.getText(),
	    			  (String)groupBox.getSelectedItem(), categoryName.getText(), doseArea.getText(), manualArea.getText(), you));
	      }
	    }
	} // End of subclass
} // End of class
