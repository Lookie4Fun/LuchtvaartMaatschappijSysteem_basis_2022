package test;

import main.domeinLaag.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class VluchtTest {

	static LuchtvaartMaatschappij lvm;
	static Fabrikant f1;
	static VliegtuigType vtt1;
	static Vliegtuig vt1;
	static Luchthaven lh1, lh2, lh3, lh4;
	static Vlucht vl1, vl2;

	LocalDate currentdate = LocalDate.now();
	int jaar = Year.now().getValue();
	int maand = currentdate.getMonthValue();
	int dag = currentdate.getMonthValue();
	int uur = currentdate.atStartOfDay().getHour();
	int min = currentdate.atStartOfDay().getMinute();
	int sec = currentdate.atStartOfDay().getSecond();


	@BeforeEach
	public void initialize() {

		try {
			lvm = new LuchtvaartMaatschappij("NLM");
			f1 = new Fabrikant("Airbus", "G. Dejenelle");
			vtt1 = f1.creeervliegtuigtype("A-200", 140);
			Calendar datum = Calendar.getInstance();
			datum.set(2000, 01, 01);
			vt1 = new Vliegtuig(lvm, vtt1, "Luchtbus 100", datum);
			Land l1 = new Land("Nederland", 31);
			Land l2 = new Land("België", 32);
			Land l3 = new Land("Duitsland", 33);
			Land l4 = new Land("Frankrijk", 34);
			lh1 = new Luchthaven("Schiphol", "ASD", true, l1);
			lh2 = new Luchthaven("Tegel", "TEG", true, l2);
			lh3 = new Luchthaven("Bonn ", "Dop", true, l3);
			lh4 = new Luchthaven("Charles de Gaule","CHGA",true,l4);
			Calendar vertr = Calendar.getInstance();
			vertr.set(2020, 03, 30, 14, 15, 0);
			Calendar aank = Calendar.getInstance();
			aank.set(2020, 03, 30, 15, 15, 0);
			vl1 = new Vlucht(vt1, lh1, lh2, vertr, aank);
			vertr.set(2020, 4, 1, 8, 15, 0);
			aank.set(2020, 4, 1, 9, 15, 0);
			vl2 = new Vlucht(vt1, lh1, lh2, vertr, aank);
		} catch (Exception e) {
			String errorMessage = "Exception: " + e.getMessage();
			System.out.println(errorMessage);
		}
	}

	/**
	 * Business rule:
	 * De bestemming moet verschillen van het vertrekpunt van de vlucht.
	 */

	@Test
	public void test1BestemmingMagNietGelijkZijnAanVertrek_False() {
		Vlucht vlucht = new Vlucht();
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh1);
			vlucht.zetBestemming(lh1);
			vlucht.bewaar();
		} catch (VluchtException e) {
			System.out.println(e);
			Luchthaven bestemming = vlucht.getBestemming();
			assertEquals(vlucht.getBestemming(), lh1);
		}
	}

	@Test
	public void test2BestemmingMagNietGelijkZijnAanVertrek_True() {
		Vlucht vlucht = new Vlucht();
		Luchthaven bestemming;
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh2);
			bestemming = vlucht.getBestemming();
			assertNull(bestemming);
			vlucht.zetBestemming(lh1);
			bestemming = vlucht.getBestemming();
			assertEquals(bestemming, lh1);
		} catch (IllegalArgumentException e) {
			bestemming = vlucht.getBestemming();
			assertEquals(bestemming, lh1);
		}
	}

	@Test
	public void test3VertrekTijdMoetGeldigZijn_False() {
			Calendar vertr = Calendar.getInstance();
			Vlucht vlucht = new Vlucht();
			vertr.set(2025, 9, 31, 24, 0, 0);
			try {
				vlucht.zetVliegtuig(vt1);
				vlucht.zetVertrekpunt(lh3);
				vlucht.zetBestemming(lh1);
				vlucht.zetVertrekTijd(vertr);
				assertEquals(vlucht.getVertrekTijd(), vertr);
				vlucht.bewaar();
				System.out.println("succesvol ingevoerd");
			} catch (VluchtException e) {
				System.out.println(e);
				assertNull(vlucht.getVertrekTijd());
			}
		}

	@Test
	public void test4AankomstTijdMoetGeldigZijn_False() {
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 9, 30, 24, 0, 0);
		aank.set(2025, 9, 30, 24, 1, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
			assertNull(vlucht.getVertrekTijd());
		}
	}

	@Test
	public void test5VertrekEnAankomstTijdenMoetenGeldigZijn_True(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 9, 30, 12, 0, 0);
		aank.set(2025, 9, 30, 12, 1, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
			assertNull(vlucht.getVertrekTijd());
		}
	}

	@Test
	public void test6VertrekTijdMagNietInHetVerldenLiggen_False(){
		Calendar vertr = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min-1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Tijd in het verleden");
			assertNull(vlucht.getVertrekTijd());
		}
	}

	@Test
	public void test7VertrekTijdEnAankomstTijdMagNietInHetVerledenLiggen_False() {
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min -2 , sec);
		aank.set(jaar,maand,dag,uur,min-1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			assertNull(vlucht.getVertrekTijd());
			if(vlucht.getVertrekTijd() == null){
				System.out.println(e);
				System.out.println("tijd in het verleden");
			}
			if(vlucht.getAankomstTijd() == null){
				System.out.println(e);
				System.out.println("tijd in het verleden");
			}
		}
	}

	@Test
	public void test8VertrekTijdEnAankomstTijdMagNietInHetVerledenLiggenEnAankomstMoetInDeToekomstLiggen_True(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		aank.set(jaar,maand,dag,uur,min+1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Tijd in het verleden");
			assertNull(vlucht.getVertrekTijd());
		}
	}

	@Test
	public void test9VertrekTijdMagNietGroterZijnDanAankomstTijd_False(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min+1, sec);
		aank.set(jaar,maand,dag,uur,min, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(vertr.after(aank));
		}
	}

	@Test
	public void test10VertrekTijdMagNietGroterZijnDanAankomstTijd_True(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		aank.set(jaar,maand,dag,uur,min+1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(vertr.after(aank));
		}
	}

	@Test
	public void test11VluchtenMogenNietOverlappen_False(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		vertr.set(2025, 7, 1, 12, 43, 0);
		aank.set(2025,7,1,15,36, 0);
		Vlucht vlucht1 = new Vlucht(vt1,lh1,lh4,vertr,aank);
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 7, 1, 15, 35, 0);
		aank.set(2025,7,1,16,36, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void test12VluchtenMogenNietOverlappen_False() {
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		vertr.set(2025, 7, 1, 12, 43, 0);
		aank.set(2025,7,1,15,36, 0);
		Vlucht vlucht1 = new Vlucht(vt1,lh1,lh4,vertr,aank);
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 7, 1, 11, 36, 0);
		aank.set(2025,7,1,12,44, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd (dit zou niet mogen)");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void test13VluchtenMogenNietOverlappen_False() {
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		vertr.set(2025, 7, 1, 12, 43, 0);
		aank.set(2025,7,1,15,36, 0);
		Vlucht vlucht1 = new Vlucht(vt1,lh1,lh4,vertr,aank);
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 7, 1, 12, 42, 0);
		aank.set(2025,7,1,15,37, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd (dit zou niet mogen)");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void test14VluchtenMogenNietOverlappen_True() {
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		vertr.set(2025, 7, 1, 12, 43, 0);
		aank.set(2025,7,1,15,36, 0);
		Vlucht vlucht1 = new Vlucht(vt1,lh1,lh4,vertr,aank);
		Vlucht vlucht = new Vlucht();
		vertr.set(2025, 7, 1, 15, 37, 0);
		aank.set(2025,7,1,16,37, 0);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void test15EenVliegtuigMoetToegewezenWorden_False() {
		Vliegtuig vliegtuig;
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		aank.set(jaar,maand,dag,uur,min+1, sec);
		try {
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vliegtuig = vlucht.getVliegtuig();
			assertNull(vliegtuig);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
		}
	}

	@Test
	public void test16EenVertrekpuntMoetToegewezenWorden_False() {
		Luchthaven vertrekPunt;
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		aank.set(jaar, maand, dag, uur, min + 1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
		}
	}

		@Test
		public void test17EenBestemmingMoetToegewezenWorden_False(){
			Calendar vertr = Calendar.getInstance();
			Calendar aank = Calendar.getInstance();
			Vlucht vlucht = new Vlucht();
			vertr.set(jaar, maand, dag, uur, min, sec);
			aank.set(jaar,maand,dag,uur,min+1, sec);
			try {
				vlucht.zetVliegtuig(vt1);
				vlucht.zetVertrekpunt(lh3);
				vlucht.zetVertrekTijd(vertr);
				assertEquals(vlucht.getVertrekTijd(), vertr);
				vlucht.zetAankomstTijd(aank);
				assertEquals(vlucht.getAankomstTijd(), aank);
				vlucht.bewaar();
				System.out.println("succesvol ingevoerd");
			} catch (VluchtException e) {
				System.out.println(e);
			}
		}

	@Test
	public void test18VertrektijdMoetToegewezenZijn_False(){
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		aank.set(jaar,maand,dag,uur,min+1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println("Vertrektijd ongeldig");
			System.out.println(e);
		}
	}

	@Test
	public void test19VertrektijdMoetToegewezenZijn_False(){
		Calendar vertr = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
		}
	}

	@Test
	public void test20AlleGegevensMoetenIngevoerdZijn_True(){
		Calendar vertr = Calendar.getInstance();
		Calendar aank = Calendar.getInstance();
		Vlucht vlucht = new Vlucht();
		vertr.set(jaar, maand, dag, uur, min, sec);
		aank.set(jaar,maand,dag,uur,min+1, sec);
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			assertEquals(vlucht.getVertrekTijd(), vertr);
			vlucht.zetAankomstTijd(aank);
			assertEquals(vlucht.getAankomstTijd(), aank);
			vlucht.bewaar();
			System.out.println("succesvol ingevoerd");
		} catch (VluchtException e) {
			System.out.println(e);
		}
	}
}
