/*Skrevet av Sondre Husevold s198755 og Magnus Tønsager s198761. Sist endret 08.05.14
Klassen definerer programmets hovedvindu, dette er rammen for programmet og det vil være oppe under hele kjøringen.
Klassen har funksjoner for å endre det midterste panelet til et ønsket panel, og for å navigere frem og tilbake mellom tdligere paneler.
Klassen sørger for å kalle opp Register.exit() når vinduet lukkes

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame
{
	private JPanel middle, backForBord, backFlow, forFlow, logoutFlow;
	private Register register;
	private WinListener winListener;
	private MainListener listener;
	private PanelStack panelStack;//inneholder funskjoner for frem og tilbake knapp
	private JButton back, forward, logout;
	private Container c;
	private BorderLayout layout;

	public MainWindow(Register reg)
	{
		super("Reseptregister");
		register = reg;
		//oppretter lyttere
		listener = new MainListener();
		winListener = new WinListener();
		//oppretter paneler
		backForBord = new JPanel(new BorderLayout() );
		backFlow = new JPanel(new FlowLayout(FlowLayout.LEFT) );
		forFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT) );
		logoutFlow = new JPanel(new FlowLayout(FlowLayout.LEFT) );
		//oppretter knapper
		back = new JButton("Tilbake");
		back.setToolTipText("Tilbake til forrige panel");
		forward = new JButton("Fram");
		logout = new JButton("Logg ut");
		back.addActionListener(listener);
		forward.addActionListener(listener);
		logout.addActionListener(listener);
		back.setEnabled(false);
		forward.setEnabled(false);
		logout.setVisible(false);
		backFlow.add(back);
		forFlow.add(forward);
		logoutFlow.add(logout);
		backForBord.add(backFlow, BorderLayout.LINE_START);
		backForBord.add(logoutFlow, BorderLayout.CENTER);
		backForBord.add(forFlow, BorderLayout.LINE_END);

		c = getContentPane();
		layout = new BorderLayout();
		c.setLayout(layout);

		middle = new StartGUI(this);
		panelStack = new PanelStack(middle);
		//legger til komponenter
		c.add(middle, BorderLayout.CENTER);
		c.add(backForBord, BorderLayout.PAGE_END);

		addWindowListener(winListener);
		setDim();//bruker setDim() metoden til å sette vinduets start størrelse
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void setSizeByPanel()//metode brukes ikke, men er her hvis det er ønskelig å endre størrelsen basert på gjeldene hoved panel
	{
		setSize(middle.getPreferredSize() );//krever redefinisjon av getPrefferedSize() metoden
	}

	private void setDim()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int hight = screenSize.height, width = screenSize.width;
		double hReduction = 0.85, vReduction = 0.85;

		setSize((int)(width*vReduction), (int)(hight*hReduction) );
	}

	public void logout()//metode setter hoved panelet, middle, tilbake til StartGUI, og kaller opp PanelStack.clear()
	{
		panelStack.clear();
		changePanel(new StartGUI(this) );
		logout.setVisible(false);
	}

	public Register getRegister()
	{
		return register;
	}

	public PanelStack getPanelStack()
	{
		return panelStack;
	}

	public void changePanel(JPanel in)//metode endrer hovedpanelet middle til in
	{
		panelStack.add(in);
		back.setEnabled(panelStack.hasBack() );
		forward.setEnabled(panelStack.hasForward() );
		c.remove(layout.getLayoutComponent(BorderLayout.CENTER) );
		middle = in;
		c.add(middle, BorderLayout.CENTER);
		logout.setVisible(true);
		validate();
		repaint();
	}

	public void back()
	{
		changePanel(panelStack.back() );
	}

	public void forward()
	{
		changePanel(panelStack.forward() );
	}

	private class MainListener implements ActionListener//frem, tilbake og logg ut lytter
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == back)
				back();
			else if(e.getSource() == forward)
				forward();
			else if(e.getSource() == logout)
				logout();
		}
	}
	private class WinListener extends WindowAdapter//vindu lytter
	{
		public void windowClosing(WindowEvent e)
		{
			register.exit();
		}
	}
}