package no.hvl.dat100.prosjekt.kontroll;

//import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Spillere;
import no.hvl.dat100.prosjekt.modell.Kort;
import no.hvl.dat100.prosjekt.modell.KortSamling;
import no.hvl.dat100.prosjekt.modell.KortUtils;
import no.hvl.dat100.prosjekt.modell.Kortfarge;

/**
 * Klasse som for √• representere en vri√•tter syd-spiller. Strategien er √•
 * lete gjennom kortene man har p√• hand og spille det f√∏rste som er lovlig.
 *
 */
public class SydSpiller extends Spiller {

	/**
	 * Konstrukt√∏r.
	 * 
	 * @param spiller posisjon for spilleren (nord eller syd).
	 */
	public SydSpiller(Spillere spiller) {
		super(spiller);
	}

	/**
	 * Metode for √• implementere strategi. Strategien er √• spille det f√∏rste
	 * kortet som er lovlig (ogs√• en √•tter selv om man har andre kort som ogs√•
	 * kan spilles). Dersom man ikke har lovlige kort √• spille, trekker man om man
	 * ikke allerede har trukket maks antall ganger. I s√• fall sier man forbi.
	 * 
	 * @param topp kort som ligg √∏verst p√• til-bunken.
	 */
	@Override
	public Handling nesteHandling(Kort topp) {

		// TODO - START

		// Kort pÂ hÂnd settes inn i kort-tabell.
		Kort[] hand = getHand().getAllekort();

		// Kort pÂ hÂnd som kan legges ned settes inn i nye kortsamlinger.
		KortSamling lovlige = new KortSamling();
		KortSamling attere = new KortSamling();

		for (Kort k : hand) {
			if (Regler.kanLeggeNed(k, topp)) {
				if (Regler.atter(k)) {
					attere.leggTil(k);
				} else {
					lovlige.leggTil(k);
				}
			}
		}

		// De lovlige kortene (bortsett fra Âttere) kopieres sÂ over i en kort-tabell
		// igjen.
		Kort[] handLovlige = lovlige.getAllekort();

		// Variabler og kortsamlinger for Â holde oversikt over hvilke kort syd har mest
		// av.
		int antallHjerter = 0;
		int antallRuter = 0;
		int antallKlover = 0;
		int antallSpar = 0;

		int[] antall = new int[13];

		KortSamling hjerterLovlige = new KortSamling();
		KortSamling ruterLovlige = new KortSamling();
		KortSamling kloverLovlige = new KortSamling();
		KortSamling sparLovlige = new KortSamling();

		// Variabler oppdateres, og kort legges eventuelt inn i kortsamlinger.
		// Teller ned hvis verdi er 8, ettersom denne fargen vil kunne spilles senere.
		for (int i = 0; i < hand.length; i++) {

			Kortfarge farge = hand[i].getFarge();

			switch (farge) {
			case Hjerter:
				if (hand[i].getVerdi() == 8) {
					antallHjerter--;
				} else {
					antallHjerter++;
				}
				break;
			case Ruter:
				if (hand[i].getVerdi() == 8) {
					antallRuter--;
				} else {
					antallRuter++;
				}
				break;
			case Klover:
				if (hand[i].getVerdi() == 8) {
					antallKlover--;
				} else {
					antallKlover++;
				}
				break;
			case Spar:
				if (hand[i].getVerdi() == 8) {
					antallSpar--;
				} else {
					antallSpar++;
				}
				break;
			}
			
			// Kortverdi ¯ker samsvarende posisjon i tabellen med en.
			int verdi = hand[i].getVerdi();
			antall[verdi - 1]++;
		}

		boolean lovligHjerter = false;
		boolean lovligRuter = false;
		boolean lovligKlover = false;
		boolean lovligSpar = false;

		for (int i = 0; i < handLovlige.length; i++) {

			Kortfarge farge = handLovlige[i].getFarge();

			switch (farge) {
			case Hjerter:
				hjerterLovlige.leggTil((handLovlige[i]));
				lovligHjerter = true;
				break;
			case Ruter:
				ruterLovlige.leggTil((handLovlige[i]));
				lovligRuter = true;
				break;
			case Klover:
				kloverLovlige.leggTil((handLovlige[i]));
				lovligKlover = true;
				break;
			case Spar:
				sparLovlige.leggTil((handLovlige[i]));
				lovligSpar = true;
				break;
			}

		}

		// Nulstiller antall kort hvis kortfarge ikke har et lovlig kort.
		if (lovligHjerter == false)
			antallHjerter = -1;
		if (lovligRuter == false)
			antallRuter = -1;
		if (lovligKlover == false)
			antallKlover = -1;
		if (lovligSpar == false)
			antallSpar = -1;

		// Kortet "spill" velges. F¯rst sjekkes det hvilken farge pÂ hÂnd som har flest
		// kort, sÂ sjekkes det
		// om det finnes et lovlig kort av denne fargen med en kortverdi som gÂr igjen
		// mer enn en gang pÂ hÂnden.
		Kort spill = null;

		if (lovlige.getAntalKort() > 0) {

			if (antallHjerter >= antallRuter && antallHjerter >= antallKlover && antallHjerter >= antallSpar) {
				spill = getOptimaltKort(hjerterLovlige.getAllekort(), antall);
			} else if (antallRuter >= antallHjerter && antallRuter >= antallKlover && antallRuter >= antallSpar) {
				spill = getOptimaltKort(ruterLovlige.getAllekort(), antall);
			} else if (antallKlover >= antallRuter && antallKlover >= antallHjerter && antallKlover >= antallSpar) {
				spill = getOptimaltKort(kloverLovlige.getAllekort(), antall);
			} else if (antallSpar >= antallRuter && antallSpar >= antallKlover && antallSpar >= antallHjerter) {
				spill = getOptimaltKort(sparLovlige.getAllekort(), antall);
			}

		}

		// Hvis spiller har et lovlig kort vil kortet "spill" legges ned som handling.
		// Ellers vil spiller trekke inn og neste spiller fÂr sin tur.
		// ≈ttere legges ned sist, og bare hvis mindre enn fire vanlige kort pÂ hÂnd.
		Handling handling = null;

		if (!lovlige.erTom()) {
			handling = new Handling(HandlingsType.LEGGNED, spill);
		} else if (!attere.erTom() && getHand().getAntalKort() < 4 + attere.getAntalKort()) {
			spill = attere.seSiste();
			handling = new Handling(HandlingsType.LEGGNED, spill);
		} else if (getAntallTrekk() < Regler.maksTrekk()) {
			handling = new Handling(HandlingsType.TREKK, null);
		} else {
			handling = new Handling(HandlingsType.FORBI, null);
		}

		return handling;

		// TODO - END
	}
	
	// Hjelpemetode for Â velge kort.
	private Kort getOptimaltKort(Kort[] kort, int[] antall) {
		
		// GÂr gjennom kortsamling av gitt type lovlige kort.
		// Hvis kortverdi har mindre enn ett tilfelle pÂ hÂnd velges det.
		// Siste kort velges hvis ingen andre verdier har mindre enn ett tilfelle.
			for (int i = 0; i < kort.length-1; i++)
				for (int j = 1; j <= 14; j++)
					if (kort[i].getVerdi() == j && antall[j-1] < 2)
						return kort[i];
		
		return kort[kort.length-1];
	}
}