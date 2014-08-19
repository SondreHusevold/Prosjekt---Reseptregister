/*Skrevet av Magnus Tønsager, s198761. Sist endret 08.05.14
Klassen inneholder programmets main metode og starter programmet, bruker et Datamanager objekt til å opprette programmets Register

*/
import java.awt.*;

public class Driver
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				Datamanager data = new Datamanager();
				Register reg = data.readRegisterFile();
				MainWindow window = new MainWindow(reg);
			}
		});
	}
}