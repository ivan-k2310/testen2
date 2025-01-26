describe('uitloggen', () => {
  // Grouping tests related to the logout functionality with `describe`.

  beforeEach(() => {
    // This block runs before each test to set up the initial state.

    // Visit the homepage of the application.
    cy.visit('http://localhost:3000/');

    // Simulate clicking on the first account element to log in.
    cy.get(".login__account").first().click();
  });

  it('overboeken', () => {
    // Test to verify the logout functionality.

    // Simulate clicking the logout button.
    cy.get(".app__logout").click();

    // Assert that the application redirects to the login page and displays the expected "Login" text.
    cy.get("h1").should("have.text", "Login");
  });
});