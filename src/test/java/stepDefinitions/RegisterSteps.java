package stepDefinitions;

import com.github.javafaker.Faker;
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
    private final Faker faker = new Faker();

    public RegisterSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
        this.testContext = hooks.getTestContext();
        this.registerPage = new RegisterPage(driver);
        this.navComponent = new NavComponent(driver);
    }

    @When("I register with random credentials")
    public void registerWithRandomCredentials() {
        logger.info("Registering with random credentials");
        String username = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 20, true, true);
        
        logger.info("Generated random username: {}", username);
        logger.info("Generated random email: {}", email);
        logger.info("Generated random password: ******");
        
        // Lưu thông tin để xác thực sau
        testContext.getScenarioContext().setContext("username", username);
        testContext.getScenarioContext().setContext("email", email);
        testContext.getScenarioContext().setContext("password", password);
        
        // Thực hiện đăng ký
        registerPage.register(username, email, password);
    }
    
    @When("I register with existing username")
    public void registerWithExistingUsername() {
        logger.info("Registering with existing username");
        // Sử dụng username cố định đã tồn tại
        String username = "user";
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 20, true, true);
        
        logger.info("Using existing username: {}", username);
        logger.info("Generated random email: {}", email);
        logger.info("Generated random password: ******");
        
        // Thực hiện đăng ký
        registerPage.register(username, email, password);
    }
    
    @When("I register with existing email")
    public void registerWithExistingEmail() {
        logger.info("Registering with existing email");
        // Sử dụng email cố định đã tồn tại
        String username = faker.name().username();
        String email = "user@example.com";
        String password = faker.internet().password(8, 20, true, true);
        
        logger.info("Generated random username: {}", username);
        logger.info("Using existing email: {}", email);
        logger.info("Generated random password: ******");
        
        // Thực hiện đăng ký
        registerPage.register(username, email, password);
    }
    
    @When("I register with specific details")
    public void registerWithSpecificDetails() {
        // Lấy thông tin từ context - được đặt bởi các step trước đó
        String username = (String) testContext.getScenarioContext().getContext("username");
        String email = (String) testContext.getScenarioContext().getContext("email");
        String password = (String) testContext.getScenarioContext().getContext("password");
        
        logger.info("Registering with specific details - Username: {}, Email: {}", username, email);
        
        // Đảm bảo rằng tất cả thông tin cần thiết đều có sẵn
        if (username == null || email == null || password == null) {
            throw new IllegalStateException("Missing required registration details. Make sure to enter all fields first.");
        }
        
        // Thực hiện đăng ký
        registerPage.register(username, email, password);
    }
    
    @When("I enter a random username")
    public void enterRandomUsername() {
        String username = faker.name().username();
        logger.info("Entering random username: {}", username);
        registerPage.enterUsername(username);
        testContext.getScenarioContext().setContext("username", username);
    }
    
    @When("I enter a random email")
    public void enterRandomEmail() {
        String email = faker.internet().emailAddress();
        logger.info("Entering random email: {}", email);
        registerPage.enterEmail(email);
        testContext.getScenarioContext().setContext("email", email);
    }
    
    @When("I enter a random password")
    public void enterRandomPassword() {
        String password = faker.internet().password(8, 20, true, true);
        logger.info("Entering random password: ******");
        registerPage.enterPassword(password);
        testContext.getScenarioContext().setContext("password", password);
    }
    
    @Then("I should see a registration successful message")
    public void verifyRegistrationSuccessful() {
        logger.info("Verifying registration successful message");
        
        // Cho phép toast có thời gian xuất hiện
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // Bỏ qua
        }
        
        assertTrue("Expected registration success message", 
                   registerPage.isMessageDisplayed("success"));
        String message = registerPage.getMessage();
        assertTrue("Expected message to contain registration successful text", 
                   message != null && message.contains("Registration successful! Please login."));
    }
} 