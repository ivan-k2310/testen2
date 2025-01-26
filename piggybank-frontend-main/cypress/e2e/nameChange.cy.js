describe('Geld over maken', () => {
  // Grouping tests related to transferring money to an account.

  beforeEach(() => {
    // This block runs before each test to ensure the initial state is consistent.

    // Visit the homepage of the application.
    cy.visit('http://localhost:3000/');

    // Simulate clicking on the first account element to log in.
    cy.get(".login__account").first().click();
  });

  it('passes', () => {
    // Test to verify that the account name can be updated in the settings page
    // and reflects on the main page.

    // Navigate to the settings page.
    cy.visit('http://localhost:3000/settings');

    // Clear the input field and type a new account name.
    cy.get("input").clear().type("Dit is een naam");

    // Simulate clicking the save button for the account name.
    cy.get("[data-test-id='Naam-button']").click();

    // Assert that a success alert is visible after saving.
    cy.get('.alert').should("be.visible");

    // Navigate back to the homepage.
    cy.visit('http://localhost:3000');

    // Assert that the account name has been updated to the new name.
    cy.get(".accounts__account-name").should("have.text", "Dit is een naam");
  });
});
