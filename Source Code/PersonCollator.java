/*
 Laget av Sondre Husevold (s198755).

 Person collator tar hånd om personer, sorterer dem etter navn, og dobbeltsjekker eventuell dobbellagring.

 */

import java.util.*;
import java.io.Serializable;
import java.text.*;

import javax.swing.JOptionPane;

class PersonCollator implements Comparator<Person>, Serializable{
	private static final long serialVersionUID = 42L;

private String regex = "<\0<0<1<2<3<4<5<6<7<8<9" +
                  "<A,a<B,b<C,c<D,d<E,e<F,f<G,g<H,h<I,i<J,j" +
                 "<K,k<L,l<M,m<N,n<O,o<P,p<Q,q<R,r<S,s<T,t" +
                 "<U,u<V,v<W,w<X,x<Y,y<Z,z<Æ,æ<Ø,ø<Å=AA,å=aa;AA,aa";

private transient RuleBasedCollator collator;

  public PersonCollator(){
    try{
      collator = new RuleBasedCollator(regex);
    }
    catch (ParseException pe){
    	JOptionPane.showMessageDialog(null, "En feil ved oppretting av kollator har oppstått.", "FEIL", JOptionPane.ERROR_MESSAGE);
    }
  }

  public int compare(Person personOne, Person personTwo){

	  	if(collator == null){ // Oppretter ny kollator dersom mangler etter lesing av fil.
			try {
				collator = new RuleBasedCollator(regex);
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(null, "En feil har oppstått ved oppretting av kollator. Dette er en kritisk feil.\n" +
									"Lukk programmet og slett data mappe for ditt eget beste.", "FEIL", JOptionPane.ERROR_MESSAGE);
			}
	  	}

	    String first = personOne.getfirstName() + " " + personOne.getlastName();
	    String second = personTwo.getfirstName() + " " + personTwo.getlastName();
	    int comparisonName = collator.compare( first, second );
	    first = personOne.getPersonNr();
	    second = personTwo.getPersonNr();
	    int comparisonSocialSecurity = collator.compare(first, second);
	    if(comparisonSocialSecurity == 0){
	    	return comparisonSocialSecurity; // Returnerer 0 for å ikke legge inn person dersom personnummer er likt.
	    }
	    if(comparisonName == 0)
	    	comparisonName++;
	    return comparisonName;
	 }

} // End of class