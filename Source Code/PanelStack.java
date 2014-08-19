/*Skrevet av Magnus Tønsager, s198761. Sist endret 08.05.14
Klassen inneholder funskjoner for å navigere frem og tilbake mellom paneler

*/

import javax.swing.*;
import java.util.*;

public class PanelStack
{
	private LinkedList<JPanel> backList;//liste for tidligere paneler
	private LinkedList<JPanel> forList;//liste for paneler etter back() har blitt kaldt opp
	private JPanel cur;
	private boolean clear = true;;
	private final int MAX_SIZE = 30;//maks antall paneler som skal lagres
	private ListIterator<JPanel> backIter;//iteratorer
	private ListIterator<JPanel> forIter;

	public PanelStack(JPanel start)
	{
		backList = new LinkedList<JPanel>();
		forList = new LinkedList<JPanel>();
		backList.addFirst(start);
	}
	public void add(JPanel in)//metode legger til et panel i listen
	{
		cur = in;
		backList.addFirst(in);
		backIter = backList.listIterator();
		backIter.next();
		if(clear)
			forList.clear();
		clear = true;
		if(backList.size() > MAX_SIZE)
			backList.removeLast();
	}

	public void clear()//metode nullstiller listene og iteratorene
	{
		backList.clear();
		forList.clear();
		backIter = backList.listIterator();
		forIter = forList.listIterator();
	}

	public boolean hasBack()//returnerer true om det er mulig å navigere bakover
	{
		if(backIter.hasNext() )
			return true;
		return false;
	}

	public boolean hasForward()//returnerer true om det er mulig å navigere framover
	{
		if(forIter == null)
			return false;
		return forIter.hasNext();
	}

	public JPanel back()//navigerer bakover i listen
	{
		backIter.remove();
		JPanel res = backIter.next();
		backIter.remove();
		if(!forList.contains(cur) )
			forList.addFirst(cur);
		forIter = forList.listIterator();
		clear = false;
		return res;
	}

	public JPanel forward()//navigerer framover i listen
	{
		clear = false;
		JPanel res = forIter.next();
		return res;
	}
}