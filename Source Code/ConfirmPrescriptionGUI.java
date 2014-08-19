/*

Laget av Sondre Husevold (s198755)

Denne klassen gir en simpel bekreftelse og viser resepten slik at legen kan
bekrefte at alt er riktig skrevet.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.border.EmptyBorder;

public class ConfirmPrescriptionGUI extends JPanel{
	private JTextArea mainText;
	private JScrollPane mainScrollingText;
	private JButton sendButton;
	private Patient patient;
	private String medicamentName, group, categoryName, dose, manual;
	private Doctor you;
	private Register register;
	MainWindow mainframe;

	public ConfirmPrescriptionGUI(MainWindow mainWPara, Patient patientPara, String medicamentNamePara, String groupPara, String categoryNamePara,
									String dosePara, String manualPara, Doctor youPara){
		mainframe = mainWPara;
		patient = patientPara;
		you = youPara;
		register = mainframe.getRegister();
		medicamentName = medicamentNamePara;
		group = groupPara;
		categoryName = categoryNamePara;
		dose = dosePara;
		manual = manualPara;
		String prescription = "Pasient: \n\n" + patient.toString() + "\nDoktor: " + you.getfirstName() + " " + you.getlastName() +
								"\nMedisin navn: " + medicamentName + "\nGruppe: " + group +
								"\nKategori: " + categoryName + "\nDosering: " + dose + "\n\nAnvendelse: " + manual;

		mainText = new JTextArea(prescription, 20, 30);
		mainText.setEditable(false);
		mainText.setLineWrap(true);
		mainText.setWrapStyleWord(true);
		mainScrollingText = new JScrollPane(mainText);

		ButtonListener theListener = new ButtonListener();
		sendButton = new JButton("Send resept");;
		sendButton.addActionListener(theListener);

		JPanel lowerHalf = new JPanel();
		lowerHalf.setBorder(new EmptyBorder(5,5,5,5));
		lowerHalf.add(sendButton);

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5,5,5,5));
		add(mainScrollingText, BorderLayout.CENTER);
		add(lowerHalf, BorderLayout.SOUTH);
	}

	public void completePrescription(){ // Ferdiggjør prescriptions
		int groupInt=0;
		if(group.matches("A"))
			groupInt = Prescription.GROUP_A;
		else if(group.matches("B"))
			groupInt = Prescription.GROUP_B;
		else if(group.matches("C"))
			groupInt = Prescription.GROUP_C;
  	  Prescription newPrescription = new Prescription(patient, you, medicamentName, dose, groupInt, categoryName, manual);
  	  register.addPrescription(newPrescription);
	}

	private class ButtonListener implements ActionListener{
	    public void actionPerformed( ActionEvent e ){
			if(e.getSource() == sendButton){
	    	  completePrescription();
	    	  mainframe.changePanel(new PersonGUI(mainframe, you));
	      }
	    }
	  } // End of subclass
} // end of class
