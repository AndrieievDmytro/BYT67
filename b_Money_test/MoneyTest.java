package b_Money_test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import b_Money.Currency;
import b_Money.Money;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(Integer.valueOf(10000), SEK100.getAmount());
		assertEquals(Integer.valueOf(1000), EUR10.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK200.getCurrency());
		assertEquals(SEK, SEK0.getCurrency());
		assertEquals(EUR, EUR0.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("10.0 EUR", EUR10.toString());
		assertEquals("-100.0 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(Integer.valueOf(0), SEK0.universalValue());
		assertEquals(Integer.valueOf(1500), SEK100.universalValue());
		assertEquals(Integer.valueOf(1500), EUR10.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertTrue(SEK0.equals(EUR0));
		assertTrue(SEK100.equals(EUR10));
		assertTrue(SEK100.equals(SEK100));
		assertFalse(SEK100.equals(SEK200));
	}

	@Test
	public void testAdd() {
		//Addition result check
		assertEquals(SEK200.getAmount(), SEK100.add(EUR10).getAmount());
		assertEquals(SEK0.getAmount(), SEK100.add(SEKn100).getAmount());
		assertEquals(Integer.valueOf(3000), EUR20.add(EUR10).getAmount());
		//Additon of 0 amount check
		assertTrue(EUR10.equals(EUR10.add(EUR0)));
		assertFalse(EUR10.equals(EUR10.add(EUR10)));
		//Currency after addition operation check
		assertEquals(SEK, SEK100.add(EUR10).getCurrency());
	}

	@Test
	public void testSub() {
		//Subtraction result check
		assertEquals(SEK0.getAmount(), SEK100.sub(EUR10).getAmount());
		assertEquals(Integer.valueOf(20000), SEK100.sub(SEKn100).getAmount());
		assertEquals(Integer.valueOf(1000), EUR20.sub(EUR10).getAmount());
		//Subtraction of 0 amount check
		assertTrue(EUR10.equals(EUR10.sub(EUR0)));
		assertFalse(EUR10.equals(EUR10.sub(EUR10)));
		//Currency after addition operation check
		assertEquals(SEK, SEK100.sub(EUR10).getCurrency());
	}

	@Test
	public void testIsZero() {
		assertTrue(EUR0.isZero());
		assertTrue(SEK0.isZero());
		assertFalse(EUR10.isZero());
		assertFalse(SEK100.isZero());
	}

	@Test
	public void testNegate() {
		assertTrue(SEKn100.equals(SEK100.negate()));
		assertFalse(EUR10.equals(EUR10.negate()));
	}

	@Test
	public void testCompareTo() {
		//This amount ==  other amount 
		assertEquals(0, EUR10.compareTo(EUR10)); 
		assertEquals(0, EUR10.compareTo(SEK100));
		assertEquals(0, SEK0.compareTo(EUR0));
		//This amount <  other amount
		assertEquals(-1, SEK100.compareTo(EUR20));
		assertEquals(-1, EUR10.compareTo(EUR20));
		assertEquals(-1, EUR0.compareTo(SEK100));
		assertEquals(-1, SEKn100.compareTo(EUR20));
		//This amount > other amount
		assertEquals(1, EUR20.compareTo(SEK100));
		assertEquals(1, EUR20.compareTo(EUR10));
		assertEquals(1, SEK100.compareTo(EUR0));
		assertEquals(1, SEK100.compareTo(SEKn100));
	}
}
