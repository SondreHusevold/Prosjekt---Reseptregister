/*

Laget av Sondre Husevold (s198755)

Lager og endrer pasienter. Til lege bruk.

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class PatientCreationKit extends JPanel{
	private Register register;
	private JButton addPatient, backButton;
	private JTextField firstNameField, lastNameField, pnField, telephoneField, addressField;
	private Patient patient;
	private Doctor you;
	private JLabel pNRLabel, firstNameLabel, lastNameLabel, telephoneLabel;
	private Boolean edit;
	private ButtonListener buttonListener;
	private MainWindow mainframe;

	public PatientCreationKit(MainWindow mainPara, Patient patientPara, Boolean editPara, Doctor youPara){
		mainframe = mainPara;
		register = mainframe.getRegister();
		patient = patientPara;
		you = youPara;
		edit = editPara;

		setLayout(new BorderLayout() );
		firstNameField = new JTextField(20);
		lastNameField = new JTextField(20);
		pnField = new JTextField(8);
		telephoneField = new JTextField(20);
		addressField = new JTextField(20);

		firstNameField.setToolTipText("Fornavn kan ikke inneholde tall og obskure symboler.");
		lastNameField.setToolTipText("Etternavn kan ikke inneholde tall og obskure symboler.");
		pnField.setToolTipText("Personnummer må inneholde: Seks siffer som tilsvarer en ekte dato og fem vilkårlige tall.");
		telephoneField.setToolTipText("Tar kun imot norske telefonnummer på åtte siffer.");
		addressField.setToolTipText("Adresser kan inneholde alle bokstaver, symboler og tall.");

		JPanel mainPanel = new JPanel(new GridLayout(12,1));
		JPanel flow = new JPanel(new FlowLayout());
		mainPanel.add(firstNameLabel = new JLabel("Fornavn: "));
		mainPanel.add(firstNameField);
		mainPanel.add(lastNameLabel = new JLabel("Etternavn: "));
		mainPanel.add(lastNameField);
		mainPanel.add(pNRLabel = new JLabel("Personnummer: "));
		mainPanel.add(pnField);
		mainPanel.add(telephoneLabel = new JLabel("Telefonnummer: "));
		mainPanel.add(telephoneField);
		mainPanel.add(new JLabel("Adresse: "));
		mainPanel.add(addressField);

		if(edit)
			setupEdit();
		else
			addPatient = new JButton("Legg pasient");

		buttonListener = new ButtonListener();
		addPatient.addActionListener(buttonListener);

		mainPanel.add(addPatient);
		flow.add(mainPanel);
		add(new JScrollPane(flow, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
			BorderLayout.LINE_START);
	}

	public void setupEdit(){
		pNRLabel.setForeground(Color.gray);
		pnField.setForeground(Color.gray);
		pnField.setToolTipText("Det er ikke mulig å endre et personnummer.");
		firstNameField.setText(patient.getfirstName());
		lastNameField.setText(patient.getlastName());
		pnField.setText(patient.getPersonNr());
		pnField.setEditable(false);
		telephoneField.setText(patient.getTelephoneNr());
		addressField.setText(patient.getAddress());
		addPatient = new JButton("Endre pasient");
	}


	public boolean checkFields(){
		String regexPattern = "";

		if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || pnField.getText().isEmpty() ||
				telephoneField.getText().isEmpty() || addressField.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Det må skrives noe inn i alle felt.", "FEIL!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(!edit){
			if(!(register.getPatients().findPatientByPersonNr(pnField.getText() ) ).isEmpty() ){
				pNRLabel.setForeground(Color.red);
				JOptionPane.showMessageDialog(null, "Det finnes en person med dette personnummeret.", "FEIL!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			regexPattern = "(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[012])(\\d{7})";
			if(!pnField.getText().matches(regexPattern)){
				pNRLabel.setForeground(Color.red);
				JOptionPane.showMessageDialog(null, "Personnummeret er ikke gyldig.", "FEIL!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		regexPattern = "\\d{8}";
		if(!telephoneField.getText().matches(regexPattern)){
			telephoneLabel.setForeground(Color.red);
			JOptionPane.showMessageDialog(null, "Telefonnummeret er ikke gyldig", "FEIL!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private class ButtonListener implements ActionListener
	  {
	    public void actionPerformed( ActionEvent e )
	    {
	     if(e.getSource() == addPatient){
	    	 if(!checkFields())
	    		 return;
	    	 if(edit){
	    		 patient.setfirstName(firstNameField.getText());
	    		 patient.setlastName(lastNameField.getText());
	    		 patient.setTelephoneNr(telephoneField.getText());
	    		 patient.setAddress(addressField.getText());
	    	 }
	    	 else{
	    		 Patient newPatient = new Patient(firstNameField.getText(), lastNameField.getText(),
	    				 pnField.getText(), telephoneField.getText(), addressField.getText());
	    		 register.addPatient(newPatient);
	    	 }
	    	 mainframe.changePanel(new PersonGUI(mainframe, you));
	     	}
	     else if(e.getSource() == backButton){
	    	 mainframe.changePanel(new PersonGUI(mainframe, you));
	     }
	  }
	} // End of Class
} // End of Class
