package b_Money_test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import b_Money.Currency;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(Double.valueOf(0.15), SEK.getRate());
		assertEquals(Double.valueOf(0.20), DKK.getRate());
		assertEquals(Double.valueOf(1.5), EUR.getRate());
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(0.1);
		DKK.setRate(0.2);
		EUR.setRate(0.3);
		assertEquals(Double.valueOf(0.1), SEK.getRate());
		assertEquals(Double.valueOf(0.2), DKK.getRate());
		assertEquals(Double.valueOf(0.3), EUR.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		Integer sekUniVal = SEK.universalValue(100);
		Integer dkkUniVal = DKK.universalValue(100);
		Integer eurUniVal = EUR.universalValue(100);
		assertEquals(Integer.valueOf(15), sekUniVal);
		assertEquals(Integer.valueOf(20), dkkUniVal);
		assertEquals(Integer.valueOf(150), eurUniVal);

	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(Integer.valueOf(13), EUR.valueInThisCurrency(100, DKK));
		assertEquals(Integer.valueOf(133), SEK.valueInThisCurrency(100, DKK));
		assertEquals(Integer.valueOf(100), DKK.valueInThisCurrency(13, EUR));
		assertEquals(Integer.valueOf(100), DKK.valueInThisCurrency(133, SEK));
	}

}
