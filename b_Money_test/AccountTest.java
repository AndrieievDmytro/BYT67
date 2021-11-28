package b_Money_test;

import org.junit.Before;
import org.junit.Test;
import b_Money.Account;
import b_Money.AccountDoesNotExistException;
import b_Money.Bank;
import b_Money.Currency;
import b_Money.Money;

import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		String id = "New shoes";

		// Timed payment does not exist check
		assertFalse(testAccount.timedPaymentExists(id));

		//Add timed payment 
		testAccount.addTimedPayment(id, 20, 15, new Money(100, SEK), SweBank, "Alice");

		// Timed payment exists check
		assertTrue(testAccount.timedPaymentExists(id));
		
		//Remove timed payment
		testAccount.removeTimedPayment(id);

		// Remove timed payment check
		assertFalse(testAccount.timedPaymentExists(id));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// BUG! tick() should be called once
		testAccount.addTimedPayment("New shoes", 10, 0, new Money(2000000, SEK), SweBank, "Alice");
		for (int i = 0; i < 50; i++) {
			testAccount.tick();
		}

		assertEquals(0, (int)testAccount.getBalance().getAmount());
	}

	@Test
	public void testAddWithdraw() {
		// Deposite same currency 
		testAccount.deposit(new Money(100, SEK));
		assertEquals(10000100, (int)testAccount.getBalance().getAmount());
		
		// Withdraw same currency
		testAccount.withdraw(new Money(100, SEK));
		assertEquals(10000000, (int)testAccount.getBalance().getAmount());

		// Deposit different currency
		testAccount.deposit(new Money(10000000, new Currency("EUR", 0.20)));
		assertEquals(23333333, (int)testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		assertTrue(new Money(10000000, SEK).equals(testAccount.getBalance()));
	}
}
