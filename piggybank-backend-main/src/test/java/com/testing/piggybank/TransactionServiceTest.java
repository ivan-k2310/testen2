package com.testing.piggybank.transaction;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Direction;
import com.testing.piggybank.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;

import java.util.List;
import java.util.Optional;

import static com.testing.piggybank.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

	// Mocked dependencies for the TransactionService
	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private CurrencyConverterService converterService;

	@Mock
	private AccountService accountService;

	// Class under test with mocked dependencies injected
	@InjectMocks
	private TransactionService transactionService;

	// Initialize mocks before each test
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize the @Mock and @InjectMocks annotations
	}

	@Test
	void testGetTransactions() throws Exception {
		// Set up the test data
		Account account1 = new Account();
		Account account2 = new Account();
		setId(account1, 1L);
		setId(account2, 2L);

		// Create two transactions with sender and receiver
		Transaction transaction1 = new Transaction();
		transaction1.setSenderAccount(account1);
		transaction1.setReceiverAccount(account2);
		transaction1.setDateTime(Instant.now()); // Current timestamp

		Transaction transaction2 = new Transaction();
		transaction2.setSenderAccount(account2);
		transaction2.setReceiverAccount(account1);
		transaction2.setDateTime(Instant.now().minusSeconds(60)); // Earlier timestamp

		// Mock the repository to return the list of transactions
		List<Transaction> transactions = List.of(transaction1, transaction2);
		when(transactionRepository.findAll()).thenReturn(transactions);

		// Act: Call the method under test
		List<Transaction> result = transactionService.getTransactions(1, 1L);

		// Assert: Verify the results
		assertEquals(1, result.size()); // Ensure only one transaction is returned
		assertEquals(transaction2, result.get(0)); // Ensure transactions are sorted by descending datetime
	}

	@Test
	void testFilterAndLimitTransactions() throws Exception {
		// Set up the test data
		Account account1 = new Account();
		Account account2 = new Account();
		setId(account1, 1L);
		setId(account2, 2L);

		// Create two transactions between the accounts
		Transaction transaction1 = new Transaction();
		transaction1.setSenderAccount(account1);
		transaction1.setReceiverAccount(account2);

		Transaction transaction2 = new Transaction();
		transaction2.setSenderAccount(account2);
		transaction2.setReceiverAccount(account1);

		// List of all transactions
		List<Transaction> transactions = List.of(transaction1, transaction2);

		// Act: Call the method under test with filtering and limiting
		List<Transaction> result = transactionService.filterAndLimitTransactions(transactions, 1L, 1);

		// Assert: Verify the filtered and limited results
		assertEquals(1, result.size());
		assertTrue(result.contains(transaction2)); // Verify the filtered transaction is correct
	}

	@Test
	void testCreateTransaction() throws Exception {
		// Set up the test request and mock dependencies
		CreateTransactionRequest request = new CreateTransactionRequest();
		request.setSenderAccountId(1L);
		request.setReceiverAccountId(2L);
		request.setAmount(BigDecimal.valueOf(90));
		request.setCurrency(USD); // Example currency
		request.setDescription("Test Transaction");

		// Mock sender and receiver accounts
		Account senderAccount = new Account();
		Account receiverAccount = new Account();
		setId(senderAccount, 1L);
		setId(receiverAccount, 2L);

		// Mock the account and currency conversion services
		when(accountService.getAccount(1L)).thenReturn(Optional.of(senderAccount));
		when(accountService.getAccount(2L)).thenReturn(Optional.of(receiverAccount));
		when(converterService.toEuro(USD, BigDecimal.valueOf(90))).thenReturn(BigDecimal.valueOf(90));

		// Act: Call the method under test
		transactionService.createTransaction(request);

		// Assert: Verify interactions with the mocked dependencies
		verify(accountService).updateBalance(1L, BigDecimal.valueOf(90), Direction.CREDIT);
		verify(accountService).updateBalance(2L, BigDecimal.valueOf(90), Direction.DEBIT);
		verify(transactionRepository).save(any(Transaction.class));
	}

	// Helper method to set the ID of an account using reflection
	private void setId(Account account, long id) throws Exception {
		Field idField = Account.class.getDeclaredField("id");
		idField.setAccessible(true); // Make the field accessible
		idField.set(account, id); // Set the ID value
	}

	@Test
	void testSettersAndGetters() {
		// Test the setter and getter methods of TransactionResponse
		TransactionResponse response = new TransactionResponse();

		// Set sample data
		long id = 1L;
		String description = "Test transaction";
		BigDecimal amount = BigDecimal.valueOf(100.50);
		Instant dateTime = Instant.now();

		// Set values using setters
		response.setId(id);
		response.setDescription(description);
		response.setAmount(amount);
		response.setDateTime(dateTime);

		// Assert: Verify values using getters
		assertEquals(id, response.getId());
		assertEquals(description, response.getDescription());
		assertEquals(amount, response.getAmount());
		assertEquals(dateTime, response.getDateTime());
	}

	@Test
	void testGetSettersAndGetters() {
		// Test the setter and getter methods of GetTransactionsResponse
		GetTransactionsResponse response = new GetTransactionsResponse();

		// Arrange: Create transaction responses
		TransactionResponse transaction1 = new TransactionResponse();
		transaction1.setId(1L);

		TransactionResponse transaction2 = new TransactionResponse();
		transaction2.setId(2L);

		List<TransactionResponse> transactions = List.of(transaction1, transaction2);

		// Set the list of transactions in the response
		response.setTransactions(transactions);

		// Assert: Verify the data
		assertEquals(transactions, response.getTransactions());
		assertEquals(2, response.getTransactions().size());
		assertEquals(1L, response.getTransactions().get(0).getId());
		assertEquals(2L, response.getTransactions().get(1).getId());
	}
}

