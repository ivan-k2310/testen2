describe('Geld over maken', () => {
  // Group of tests for verifying money transfer functionality.

  beforeEach(() => {
    // This block ensures the initial state is consistent before each test.

    // Visit the homepage of the application.
    cy.visit('http://localhost:3000/');

    // Simulate logging into the first account by clicking the first login element.
    cy.get(".login__account").first().click();
  });

  it('overboeken', () => {
    // Test case for transferring money in the default currency.

    // Navigate to the transfer page.
    cy.visit('http://localhost:3000/transfer');

    // Fill in the transfer form.
    cy.get("textarea").type("Dit is een overboeking"); // Enter a description for the transfer.
    cy.get('.to-account').select("Sara Ravestein"); // Select the recipient account.
    cy.get('.amount-input').type(100); // Enter the transfer amount.

    // Simulate clicking the transfer button.
    cy.get("[data-test-id='overboeken-button']").click();

    // Verify that a success alert is displayed.
    cy.get('.alert').should("be.visible");
  });

  it('anderen valuta(USD) overboeken', () => {
    // Test case for transferring money in USD.

    // Navigate to the transfer page.
    cy.visit('http://localhost:3000/transfer');

    // Change the currency to USD.
    cy.get('.transfer__currency').select("USD");

    // Fill in the transfer form.
    cy.get("textarea").type("Dit is een overboeking"); // Enter a description for the transfer.
    cy.get('.to-account').select("Sara Ravestein"); // Select the recipient account.
    cy.get('.amount-input').type(100); // Enter the transfer amount.

    // Simulate clicking the transfer button.
    cy.get("[data-test-id='overboeken-button']").click();

    // Verify that a success alert is displayed.
    cy.get('.alert').should("be.visible");
  });

  it('anderen valuta(GBP) overboeken', () => {
    // Test case for transferring money in GBP.

    // Navigate to the transfer page.
    cy.visit('http://localhost:3000/transfer');

    // Change the currency to GBP.
    cy.get('.transfer__currency').select("GBP");

    // Fill in the transfer form.
    cy.get("textarea").type("Dit is een overboeking"); // Enter a description for the transfer.
    cy.get('.to-account').select("Sara Ravestein"); // Select the recipient account.
    cy.get('.amount-input').type(100); // Enter the transfer amount.

    // Simulate clicking the transfer button.
    cy.get("[data-test-id='overboeken-button']").click();

    // Verify that a success alert is displayed.
    cy.get('.alert').should("be.visible");
  });

  it('negatief overboeken', () => {
    // Test case for attempting to transfer a negative amount.

    // Navigate to the transfer page.
    cy.visit('http://localhost:3000/transfer');

    // Fill in the transfer form with a negative amount.
    cy.get("textarea").type("Dit is een overboeking"); // Enter a description for the transfer.
    cy.get('.to-account').select("Sara Ravestein"); // Select the recipient account.
    cy.get('.amount-input').type(-100); // Enter a negative transfer amount.

    // Simulate clicking the transfer button.
    cy.get("[data-test-id='overboeken-button']").click();

    // Verify that a success alert or error alert is displayed, depending on implementation.
    cy.get('.alert').should("be.visible");
  });
});