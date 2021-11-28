package b_Money_test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import b_Money.AccountDoesNotExistException;
import b_Money.AccountExistsException;
import b_Money.Bank;
import b_Money.Currency;
import b_Money.Money;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals("SEK", SweBank.getCurrency().getName());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		//BUG! Should be put instead of get
		assertTrue(SweBank.getAccountsList().containsKey("Ulrika"));
		assertFalse(SweBank.getAccountsList().containsKey("Unknown")); 
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		// BUG! Should throw exception if an account does not exist
		DanskeBank.deposit("Gertrud", new Money(Integer.valueOf(100), DKK));
		assertEquals(Integer.valueOf(100), DanskeBank.getBalance("Gertrud"));
		assertFalse(DanskeBank.getAccountsList().containsKey("Unknown"));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		// BUG! should withdraw instead of deposit
		DanskeBank.deposit("Gertrud", new Money(Integer.valueOf(100), DKK));
		DanskeBank.withdraw("Gertrud", new Money(Integer.valueOf(100), DKK));
		assertEquals(Integer.valueOf(0), DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		DanskeBank.deposit("Gertrud", new Money(Integer.valueOf(100), DKK));
		assertEquals(Integer.valueOf(100), DanskeBank.getBalance("Gertrud"));
		assertTrue(DanskeBank.getAccountsList().get("Gertrud").getBalance().equals(new Money(Integer.valueOf(100), DKK)));
		assertFalse(SweBank.getAccountsList().get("Bob").getBalance().equals(new Money(Integer.valueOf(100), DKK)));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		//Deposit money 
		SweBank.deposit("Ulrika", new Money(Integer.valueOf(100), SEK));
		SweBank.deposit("Bob", new Money(Integer.valueOf(100), SEK));
		DanskeBank.deposit("Gertrud", new Money(100, DKK));

		//Account from which money is being transfered does not exists 
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Does not exist", DanskeBank, "Gertrud", new Money(100, DKK)));
		
		//Account to which money is being transfered does not exists
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Gertrud", DanskeBank, "Does not exist", new Money(100, DKK)));
		
		//Transfer money between two accounts
		SweBank.transfer("Bob", Nordea, "Bob", new Money(100, SEK));
		assertEquals(Integer.valueOf(0), SweBank.getBalance("Bob"));
		assertEquals(Integer.valueOf(100), Nordea.getBalance("Bob"));

		//Transfer money between two accounts on the same bank
		// BUG!  Shoud be toaccount instead of fromaccount
		SweBank.transfer("Ulrika", "Bob", new Money(100, SEK));
		assertEquals(Integer.valueOf(0), SweBank.getBalance("Ulrika"));
		assertEquals(Integer.valueOf(100), SweBank.getBalance("Bob"));

		//Transfer money to the same accounts on the same bank
		DanskeBank.transfer("Gertrud", DanskeBank, "Gertrud", new Money(100, DKK));
		assertEquals(Integer.valueOf(100), DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// BUG! Should throw exception in add and remove- TimedPayment

		//Account from which money is being transfered does not exists 
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Does not exist", DanskeBank, "Gertrud", new Money(100, DKK)));
		
		//Account to which money is being transfered does not exists
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Gertrud", DanskeBank, "Does not exist", new Money(100, DKK)));

		//Remove Timed Payment test
		assertThrows(AccountDoesNotExistException.class, () -> DanskeBank.removeTimedPayment("Does not exist", "For cappuccino"));

	}
}
