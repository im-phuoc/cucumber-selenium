package stepDefinitions;

import components.NavComponent;
import context.TestContext;
import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.RegisterPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegisterSteps {
    private static final Logger logger = LoggerFactory.getLogger(RegisterSteps.class);
    private final WebDriver driver;
    private final RegisterPage registerPage;
    private final TestContext testContext;
    private final NavComponent navComponent;

    public RegisterSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
        this.testContext = hooks.getTestContext();
        this.registerPage = new RegisterPage(driver);
        this.navComponent = new NavComponent(driver);
    }

    @When("I register with random credentials")
    public void registerWithRandomCredentials() {
        logger.info("Registering with random credentials");
        String username = "user_" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "password_" + System.currentTimeMillis();
        
        testContext.getScenarioContext().setContext("username", username);
        testContext.getScenarioContext().setContext("email", email);
        testContext.getScenarioContext().setContext("password", password);
        
        registerPage.register(username, email, password);
    }
    
    @When("I register with existing username")
    public void registerWithExistingUsername() {
        logger.info("Registering with existing username");
        // Assume "user" already exists
        String username = "user";
        String email = "another_" + System.currentTimeMillis() + "@example.com";
        String password = "password_" + System.currentTimeMillis();
        
        registerPage.register(username, email, password);
    }
    
    @Then("I should see a registration successful message")
    public void verifyRegistrationSuccessful() {
        logger.info("Verifying registration successful message");
        assertTrue("Expected registration success message", 
                   registerPage.isMessageDisplayed("success"));
        String message = registerPage.getMessage();
        assertTrue("Expected message to contain registration successful text", 
                   message.contains("Registration successful"));
    }
} 