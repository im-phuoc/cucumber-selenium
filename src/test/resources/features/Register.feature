@register
Feature: User Registration Functionality
  As a user
  I want to register a new account
  So that I can access the application features

  Background:
    Given I navigate to the register page

  @successful-register
  Scenario: Successful register with valid credentials
    When I enter random user in the username field
    And I enter "test@example.com" in the "email" field
    And I enter random password in the password field
    And I click the register button
    Then I should see a message "Registration successful"
    And I should be redirected to the login page

  @quick-register
  Scenario: Quick registration with random credentials
    When I register with random credentials
    Then I should see a registration successful message
    And I should be redirected to the login page

  @existing-username
  Scenario: Registration with existing username
    When I register with existing username
    Then I should see a message "Username already exists"

  @invalid-register-input
  Scenario Outline: Registration failed with invalid input
    When I enter "<username>" in the "username" field
    And I enter "<email>" in the "email" field
    And I enter "<password>" in the "password" field
    And I click the register button
    Then I should see error messages containing "<message>"

    Examples:
      | username | email            | password | message                                        |
      | a        | test@example.com | 1        | Username must be between 2 and 20 characters, Password must be at least 6 characters |
      | a        | test@example.com | 123456   | Username must be between 2 and 20 characters   |
      | user123  | test@example.com | 1        | Password must be at least 6 characters         |
      | user123  | invalid-email    | 123456   | Please enter a valid email                     |