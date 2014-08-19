/*

Laget av Sondre Husevold (s198755)

Brukergrensesnittet til legene. Her er det metoder for å søke 

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class PersonGUI extends JPanel{
	private JButton searchButton, createPatient, editPatient, showPreButton, hidePreButton, writePreButton;
	private JTextField firstNameField, lastNameField, pnField, addressField, telephoneField;
	private JTable mainArea;
	private JScrollPane scrollingMain;
	private MainWindow main;
	private Doctor you;
	private Register register;
	private TModel model;
	private JPanel nameSearch, searchCriteria, searchButtonPlacements, leftPanel, lowButton, mainWindow;
	private ButtonListener theListener;

	public PersonGUI(MainWindow mainPara, Doctor youPara){
		main = mainPara;
		register = main.getRegister();
		you = youPara;

		theListener = new ButtonListener();
		showPreButton = new JButton("Vis tidligere resepter");
		writePreButton = new JButton("Skriv resept");
		writePreButton.addActionListener(theListener);
		hidePreButton = new JButton("Tilbake til søkeresultat");
		hidePreButton.setVisible(false);
		hidePreButton.addActionListener(theListener);
		showPreButton.setToolTipText("Viser tidligere resepter");
		showPreButton.addActionListener(theListener);
		writePreButton.setToolTipText("Skriv en resept til valgt person");
		searchButton = new JButton("Søk");
		searchButton.addActionListener(theListener);
		createPatient = new JButton("Lag ny pasient");
		createPatient.addActionListener(theListener);
		editPatient = new JButton("Gjør endring på pasient");
		editPatient.addActionListener(theListener);

		firstNameField = new JTextField(20);
		lastNameField = new JTextField(20);
		pnField = new JTextField(20);
		addressField = new JTextField(20);
		telephoneField = new JTextField(8);

		setUpTable();
		mainArea = new JTable(model);
		mainArea.setRowSelectionAllowed(true);
		mainArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollingMain = new JScrollPane(mainArea);

		nameSearch = new JPanel(new GridLayout(0,1, 0, 5));
		nameSearch.add(new JLabel("Fornavn: "));
		nameSearch.add(firstNameField);
		nameSearch.add(new JLabel("Etternavn: "));
		nameSearch.add(lastNameField);
		nameSearch.add(new JLabel("Personnummer: "));
		nameSearch.add(pnField);
		nameSearch.add(new JLabel("Adresse: "));
		nameSearch.add(addressField);
		nameSearch.add(new JLabel("Telefon: "));
		nameSearch.add(telephoneField);

		searchCriteria = new JPanel(new GridLayout(0,1,0,5));
		searchCriteria.add(nameSearch);
		searchCriteria.setSize(609, 400);
		searchCriteria.setBorder(new EmptyBorder(20,5,10,5));

		searchButtonPlacements = new JPanel(new GridLayout(3,1));
		searchButtonPlacements.add(searchButton);
		searchButtonPlacements.add(createPatient);
		searchButtonPlacements.add(editPatient);
		searchButtonPlacements.setSize(270, 100);

		JPanel setButtoninStone = new JPanel();
		setButtoninStone.add(searchButtonPlacements);
		setButtoninStone.setBorder(new EmptyBorder(10,0,0,0));

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(searchCriteria, BorderLayout.NORTH);
		leftPanel.add(setButtoninStone, BorderLayout.CENTER);
		leftPanel.setBorder(new EmptyBorder(0,10,0,15));

		lowButton = new JPanel(new FlowLayout() );
		lowButton.add(showPreButton);
		lowButton.add(writePreButton);
		lowButton.add(hidePreButton);

		mainWindow = new JPanel(new BorderLayout());
		mainWindow.add(scrollingMain, BorderLayout.CENTER);
		mainWindow.add(lowButton, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
			BorderLayout.LINE_START);
		add(mainWindow, BorderLayout.CENTER);
	}

	public void setUpTable(){ // Setter opp tabell med alle registrerte pasienter.
		model = new TModel(register.getPatients() );
		model.setCellEditable(false);
	}

	public void findAndSetupTable(){ // Finner pasienter basert på søkekriterier og setter opp tabell.
		if(firstNameField.getText().isEmpty() && lastNameField.getText().isEmpty() &&
				pnField.getText().isEmpty() && addressField.getText().isEmpty() && telephoneField.getText().isEmpty()){
			
			setUpTable();
		}
		else{
			PatientReg searchedPatReg = search();
			if(searchedPatReg == null || searchedPatReg.isEmpty())
				model = new TModel();
			else
				model = new TModel(searchedPatReg);
		}
		mainArea.setModel(model);
		validate();
	}

	public PatientReg search(){
		PatientReg searchedPatientReg = register.getPatients();
		searchedPatientReg = register.getPatients().findPatientByPersonNr(pnField.getText());
		if((searchedPatientReg == null || searchedPatientReg.isEmpty())){
			searchedPatientReg = register.getPatients().findPatientByName(firstNameField.getText(), lastNameField.getText());
			if((searchedPatientReg == null || searchedPatientReg.isEmpty())){
				searchedPatientReg = register.getPatients().findPatientByTelephone(telephoneField.getText());
				if(searchedPatientReg == null || searchedPatientReg.isEmpty()){
					searchedPatientReg = register.getPatients().findPatientByAddress(addressField.getText());
				}
			}
		}
		return searchedPatientReg;
	}

	public Patient selectPatient(){ 
		if(mainArea.getSelectedRow() == -1){
			return null;
		}
		PatientReg patient = register.getPatients().findPatientByPersonNr((String)mainArea.getValueAt(mainArea.getSelectedRow(), 0) );
		if(patient.iterator().hasNext()){
			return patient.iterator().next();
		}
		return null;
	}
	
	public void showPreviousPrescriptions(){
		Patient pat = selectPatient();
		if(pat == null){
			JOptionPane.showMessageDialog(null, "Du må velge en pasient først", "FEIL", JOptionPane.ERROR_MESSAGE);
			return;
		}
		model = new TModel(pat.getPrescriptions());
		mainArea.setModel(model);
		hidePreButton.setVisible(true);
		showPreButton.setVisible(false);
		writePreButton.setVisible(false);
	}
	
	public void hidePreviousPrescriptions(){
		findAndSetupTable();
		hidePreButton.setVisible(false);
		showPreButton.setVisible(true);
		writePreButton.setVisible(true);
	}

	private class ButtonListener implements ActionListener{
	    public void actionPerformed( ActionEvent e ){
	     if ( e.getSource() == writePreButton){
	    	  try{
				  Patient pat = selectPatient();
				  if(pat == null)
				  	throw new NullPointerException(null);
	    		  main.changePanel(new WritePrescriptionGUI(main, selectPatient(), you));
	    	  }
	    	  catch(NullPointerException npe){
	    		  JOptionPane.showMessageDialog(null, "Du må velge en pasient først", "FEIL!", JOptionPane.ERROR_MESSAGE);
	    		  return;
	    	  }
	      }
	      else if(e.getSource() == searchButton){
	    	  findAndSetupTable();
	      }
	      else if(e.getSource() == createPatient){
	    	  main.changePanel(new PatientCreationKit(main, null, false, you));
	      }
	      else if(e.getSource() == editPatient){
	    	  try{
	    		  main.changePanel(new PatientCreationKit(main, selectPatient(), true, you));
	    	  }
	    	  catch(NullPointerException npe){
	    		  JOptionPane.showMessageDialog(null, "Du må velge en pasient først!", "FEIL!", JOptionPane.ERROR_MESSAGE);
	    		  return;
	    	  }
	      }
	      else if(e.getSource() == showPreButton)
	      	showPreviousPrescriptions();
	      else if(e.getSource() == hidePreButton)
	      	hidePreviousPrescriptions();
	    }
	  }// End of ButtonListener subclass
} // End of class
