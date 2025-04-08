package stepDefinitions;

import components.NavComponent;
import context.TestContext;
import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginSteps {
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    private final WebDriver driver;
    private final LoginPage loginPage;
    private final TestContext testContext;
    private final NavComponent navComponent;

    public LoginSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
        this.testContext = hooks.getTestContext();
        this.loginPage = new LoginPage(driver);
        this.navComponent = new NavComponent(driver);
    }

    @When("I login with valid credentials")
    public void loginWithValidCredentials() {
        logger.info("Logging in with valid credentials");
        loginPage.login("user", "123456");
    }
    
    @Then("I should see welcome message with my username")
    public void verifyWelcomeMessage() {
        logger.info("Verifying welcome message");
        String username = (String) testContext.getScenarioContext().getContext("username");
        if (username == null) {
            username = "user"; // Default if not set
        }
        
        String welcomeMessage = navComponent.getWelcomeMessage();
        assertTrue("Welcome message should contain username", 
                   welcomeMessage != null && welcomeMessage.contains(username));
    }
    
    @Then("I should be logged in")
    public void verifyLoggedIn() {
        logger.info("Verifying user is logged in");
        assertTrue("User should be logged in", navComponent.isLoggedIn());
    }
}
