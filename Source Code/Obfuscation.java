/*

Laget av Sondre Husevold (s198755)

Denne klassen tuller med passordet for å gjøre det vanskelig å lese rett av. 
Se på det som en hjemmesnekret amatør kryptering.

*/

import java.util.*;

public class Obfuscation {

	public static String readPassword(String passwordPara){
		StringBuilder passwordOutput = new StringBuilder(30);
		int iteration = 0;
		Scanner reader = new Scanner(passwordPara);
		reader.useDelimiter("");
		while(reader.hasNext()){
			iteration++;
			passwordOutput.append(convertLetter(reader.next(), (iteration*2)));
		}
		reader.close();
		String sesamsesam = reversePassword(passwordOutput.toString());
		return sesamsesam;
	}

	private static int convertLetter(String letter, int iteration){
		int converted = 0;
		boolean upper = false;
		char chara = letter.charAt(0);
		if(Character.isUpperCase(chara)){
			upper = true;
		}
		converted = chara * iteration;
		if(upper == true)
			converted = ((converted + iteration) / 3);
		if(iteration % 11 == 0)
			converted = ((converted + iteration) / 5) * 2;
		else if(iteration % 9 == 0)
			converted = ((converted + iteration) / 7) * 3;
		if(iteration % 8 == 0)
			converted  = ((converted + iteration)  / 8) * 3;
		else if(iteration % 6 == 0)
			converted = ((converted + iteration) / 4) * 2;
		if(iteration % 5 == 0)
			converted = (converted + iteration)  / 7;
		else if(iteration % 4 == 0)
			converted = ((converted + iteration) / 2) * 3;
	return converted;
	}

	/*

	Denne neste metoden splitter hele stringen hver gang den finner tallet 2 eller 5. Deretter bytter den det tallet ut med et tall beregnet
	utifra modulus til iterasjonen (som kommer ann på hvor mange 2 og 5 det er) og snur hele stringen feil vei.
	
	*/

	private static String reversePassword(String passwordIN){
		String[] splitted = passwordIN.split("[25]");
		String password = "";
		int iterator = splitted.length - 1;
		while(iterator != -1){
			password += splitted[iterator];
			if(iterator % 12 == 0)
				password += (iterator % 5);
			else if(iterator % 9 == 0)
				password += (iterator % 6);
			else if(iterator % 5 == 0)
				password += (iterator % 3);
			else if(iterator % 2 == 0)
				password += (iterator % 7);
			iterator--;
		}
		return password;
	}
}
