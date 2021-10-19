package no.hvl.dat100.prosjekt.modell;

import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;

public class KortUtils {

	/**
	 * Sorterer en samling. RekkefÃ¸lgen er bestemt av compareTo() i Kort-klassen.
	 * 
	 * @see Kort
	 * 
	 * @param samling
	 * 			samling av kort som skal sorteres. 
	 */
	
	public static void sorter(KortSamling samling) {
		
		// TODO - START
		
		// Oppretter midlertidig tabell.
		Kort[] kort = samling.getAllekort();
		
		// Sorterer midlertidig tabell, metoden compareTo() avgjør verdien på kort.
		for (int i = 0; i < kort.length; i++) {
			for (int j = 1; j < kort.length-i; j++) {
			
				if (kort[j-1].compareTo(kort[j]) > 0) {
					Kort tmp = kort[j-1];
					kort[j-1] = kort[j];
					kort[j] = tmp;
				}
			
			}
		}
		
		// Usortert samling tømmes, og kort settes på nytt inn i samling.
		samling.fjernAlle();
		
		for (int i = 0; i < kort.length; i++) {
			samling.leggTil(kort[i]);
		}
		
		// TODO - END
	}
	
	/**
	 * Stokkar en kortsamling. 
	 * 
	 * @param samling
	 * 			samling av kort som skal stokkes. 
	 */
	public static void stokk(KortSamling samling) {
		
		// TODO - START
		
		// Kortsamling legges over i en midlertidig tabell.
		Kort[] kort = samling.getAllekort();
		int antall = samling.getAntalKort();
		samling.fjernAlle();

		// For-løkke går gjennom hvert kort i den nye tabellen.
		for (int i = 0; i < kort.length; i++) {	
			
			// Variabelen "random" settes til et vilkårlig tall fra antall kort.
			int random = (int) (Math.random() * (antall));
			
			// Hvis kort[random] ikke finnes i samling settes det inn.  
			if (!samling.har(kort[random])) {
				samling.leggTil(kort[random]);
			}
			// Hvis det allerede finnes, settes random på nytt helt til kort[random] ikke finnes i samling.
			else {
				while (samling.har(kort[random])) {
					random = (int) (Math.random() * (antall));
				}
				// kort[random] legges så inn i tabell.
				samling.leggTil(kort[random]);
			}
			
		}
		
		// TODO - END
	}
	
}
