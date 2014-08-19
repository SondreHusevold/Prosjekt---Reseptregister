/*Skrevet av Magnus Tønsager, s198761. Sist endret 11.05.14
Klassen definerer brukergrensesnittet for login. Klassen utvider JPanel med felter for brukernavn og passord, har metoder for å logge inn

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartGUI extends JPanel
{
	private final int FIELD_LENGTH = 10;
	private final String ADMIN_PASS = "69830610", PHARMACY_PASS = "69830610";//bør være kryptert
	private final String ADMIN = "admin", PHARMACY = "apotek";
	private String adminPass, pharmacyPass;
	private MainWindow win;
	private JLabel userLabel, passLabel;
	private JTextField user, pass;
	private JButton login;
	private ButtonListener listener;
	private Register register;
	private JTextArea info;

	public StartGUI(MainWindow w)
	{
		listener = new ButtonListener();
		win = w;
		register = w.getRegister();
		adminPass = ADMIN_PASS;//bør legges til en funskjon for kryptering her
		pharmacyPass = PHARMACY_PASS;
		info = new JTextArea("Velkommen!\nSkriv inn ditt brukernavn og passord. For admin er brukernavnet admin, " +
								"for apotek er brukernavnet apotek");
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		userLabel = new JLabel("Brukernavn");
		userLabel.setToolTipText("Ditt brukernavn");
		passLabel = new JLabel("Passord");
		passLabel.setToolTipText("Ditt passord");
		user = new JTextField(FIELD_LENGTH);
		pass = new JPasswordField(FIELD_LENGTH);
		login = new JButton("Logg inn");
		login.addActionListener(listener);
		user.addActionListener(listener);
		pass.addActionListener(listener);

		JPanel inputFields = new JPanel(new GridLayout(2,2));
		JPanel bord = new JPanel(new BorderLayout() );
		JPanel flow = new JPanel(new FlowLayout(FlowLayout.CENTER) );
		inputFields.add(userLabel);
		inputFields.add(user);
		inputFields.add(passLabel);
		inputFields.add(pass);

		setLayout(new BorderLayout() );
		add(info, BorderLayout.PAGE_START);
		bord.add(inputFields, BorderLayout.CENTER);
		bord.add(login, BorderLayout.PAGE_END);
		flow.add(bord);
		add(new JScrollPane(flow), BorderLayout.CENTER);
		bord.setBorder(BorderFactory.createTitledBorder("Login") );
	}

	public JFrame getMainWindow()
	{
		return win;
	}

	public void login()//metode leser av feltene og logger inn riktig person
	{
		String p = pass.getText();
		switch(user.getText().toLowerCase() )
		{
			case PHARMACY:
				loginAsPharmacy(p);
			break;
			case ADMIN:
				loginAsAdmin(p);
			break;
			default:
				loginAsDoctor(p);
		}
	}
	public void loginAsAdmin(String p)
	{
		if(testPassword(p, adminPass) )
		{
			JPanel res = new AdminGUI(win);
			win.changePanel(res);
			return;
		}
		JOptionPane.showMessageDialog(null, "Feil passord");
	}
	public void loginAsPharmacy(String p)
	{
		if(testPassword(p, pharmacyPass) )
		{
			JPanel res = new PharmacyGUI(win);
			win.changePanel(res);
			return;
		}
		JOptionPane.showMessageDialog(null, "Feil passord");
	}
	public void loginAsDoctor(String p)
	{
		Doctor doc = register.getDoctorByNumber(user.getText() );
		if(doc == null)
		{
			JOptionPane.showMessageDialog(null, "Ugyldig brukernavn");
			return;
		}
		if(testPassword(p, doc.getHashedPass() ) )
		{
			JPanel res = new PersonGUI(win, doc);
			win.changePanel(res);
			return;
		}
		JOptionPane.showMessageDialog(null, "Feil passord");
	}
	public boolean testPassword(String passwordOne, String hashedPass)//bør utvides med kryptering av passord
	{
		if(Obfuscation.readPassword(passwordOne).equals(hashedPass) )
			return true;
		return false;
	}
	private class ButtonListener implements ActionListener//lytter for
	{
		public void actionPerformed( ActionEvent e )
		{
			if(e.getSource() == login || e.getSource() == pass || e.getSource() == user)
				login();
		}
	}
}