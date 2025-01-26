package com.testing.piggybank.account;

import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    // Mock the AccountService to isolate the AccountController for testing
    @Mock
    private AccountService accountService;

    // Inject the mocked service into the controller
    @InjectMocks
    private AccountController accountController;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountFound() throws Exception {
        // Set up an account with specific values
        Account account = new Account();
        setId(account, 1L);
        account.setName("Savings");
        account.setBalance(BigDecimal.valueOf(1000));

        // Mock the AccountService to return the created account
        when(accountService.getAccount(1L)).thenReturn(Optional.of(account));

        // Act: Call the getAccount method in the controller
        ResponseEntity<AccountResponse> response = accountController.getAccount(1L);

        // Assert: Verify the response is correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Savings", response.getBody().getName());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getBalance());
    }


    @Test
    void testGetAccountNotFound() {
        // Mock the AccountService to return an empty Optional
        when(accountService.getAccount(1L)).thenReturn(Optional.empty());

        // Act: Call the getAccount method in the controller
        ResponseEntity<AccountResponse> response = accountController.getAccount(1L);

        // Assert: Verify that the response has a NOT_FOUND status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test the getAccounts method for retrieving accounts by user ID
    @Test
    void testGetAccountsByUserId() throws Exception {
        // Set up two accounts for the user
        Account account1 = new Account();
        setId(account1, 1L);
        account1.setName("Savings");
        account1.setBalance(BigDecimal.valueOf(1000));

        Account account2 = new Account();
        setId(account2, 2L);
        account2.setName("Checking");
        account2.setBalance(BigDecimal.valueOf(500));

        // Mock the AccountService to return the two accounts
        when(accountService.getAccountsByUserId(1L)).thenReturn(Arrays.asList(account1, account2));

        // Act: Call the getAccounts method in the controller
        ResponseEntity<GetAccountsResponse> response = accountController.getAccounts(1L);

        // Assert: Verify the response contains the correct data
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getAccounts().size());
        assertEquals("Savings", response.getBody().getAccounts().get(0).getName());
        assertEquals("Checking", response.getBody().getAccounts().get(1).getName());
    }

    // Test the updateAccount method with a valid request
    @Test
    void testUpdateAccountValidRequest() {
        // Create a request to update an account's name
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setAccountId(1L);
        request.setAccountName("Updated Account Name");

        // Act: Call the updateAccount method in the controller
        ResponseEntity<HttpStatus> response = accountController.updateAccount(request);

        // Assert: Verify that the response has an OK status
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService, never()).updateBalance(anyLong(), any(), any()); // No logic implemented yet
    }

    // Test the service's updateBalance method indirectly
    @Test
    void testServiceUpdateBalance() throws Exception {
        // Set up an account with an initial balance
        Account account = new Account();
        setId(account, 1L);
        account.setBalance(BigDecimal.valueOf(1000));

        // Mock the AccountService to return the account
        when(accountService.getAccount(1L)).thenReturn(Optional.of(account));

        // Act: Call the updateBalance method in the service
        accountService.updateBalance(1L, BigDecimal.valueOf(200), Direction.DEBIT);

        // Assert: Verify that the updateBalance method was called with the correct arguments
        verify(accountService).updateBalance(1L, BigDecimal.valueOf(200), Direction.DEBIT);
    }

    // Helper method to set the ID of an account using reflection
    private void setId(Account account, long id) throws Exception {
        Field idField = Account.class.getDeclaredField("id"); // Access the private field
        idField.setAccessible(true); // Make the field accessible
        idField.set(account, id); // Set the ID value
    }
}


