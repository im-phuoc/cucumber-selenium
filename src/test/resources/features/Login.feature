@login
Feature: User Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account and use the system

  Background:
    Given I navigate to the login page

  @successful-login
  Scenario: Successful login with valid credentials
    When I enter "user" in the "username" field
    And I enter "123456" in the "password" field
    And I click the login button
    Then I should see a message "Login successful"
    And I should be redirected to the dashboard
    And I should be logged in
    And I should see welcome message with my username

  @quick-login
  Scenario: Quick login with valid credentials
    When I login with valid credentials
    Then I should see a message "Login successful"
    And I should be logged in

  @invalid-input
  Scenario Outline: Login failed with invalid input
    When I enter "<username>" in the "username" field
    And I enter "<password>" in the "password" field
    And I click the login button
    Then I should see error messages containing "<message>"

    Examples:
      | username | password | message                                        |
      | a        | 1        | Username must be between 2 and 20 characters, Password must be at least 6 characters |
      | a        | 123456   | Username must be between 2 and 20 characters                       |
      | abc      | 1        | Password must be at least 6 characters         |

  @invalid-credentials
  Scenario Outline: Login failed with invalid credentials
    When I enter "<username>" in the "username" field
    And I enter "<password>" in the "password" field
    And I click the login button
    Then I should see a message "<message>"

    Examples:
      | username  | password | message                            |
      | user      | 1234567  | Incorrect username or password     |
      | not-found | 12345678 | User not found                     |
