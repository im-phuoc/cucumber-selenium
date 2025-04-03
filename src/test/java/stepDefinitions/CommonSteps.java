package stepDefinitions;

import base.BasePage;
import components.NavComponent;
import components.ToastNotificationComponent;
import config.ConfigManager;
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
import pages.RegisterPage;

import java.util.List;

import static org.junit.Assert.*;

public class CommonSteps {
    private static final Logger logger = LoggerFactory.getLogger(CommonSteps.class);
    private final WebDriver driver;
    private BasePage currentPage;
    private final TestContext testContext;
    private final NavComponent navComponent;

    public CommonSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
        this.testContext = hooks.getTestContext();
        this.navComponent = new NavComponent(driver);
    }

    @Given("I navigate to the login page")
    public void navigateToLoginPage() {
        logger.info("Navigating to login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        currentPage = loginPage;
        testContext.getScenarioContext().setContext("currentPage", currentPage);
    }
    
    @Given("I navigate to the register page")
    public void navigateToRegisterPage() {
        logger.info("Navigating to register page");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.navigateTo();
        currentPage = registerPage;
        testContext.getScenarioContext().setContext("currentPage", currentPage);
    }

    @When("I enter {string} in the {string} field")
    public void enterTextField(String text, String fieldName) {
        logger.info("Entering text '{}' in the {} field", text, fieldName);
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        currentPage.enterText(text, fieldName);
        
        // Store value for later verification
        testContext.getScenarioContext().setContext(fieldName.toLowerCase(), text);
    }

    @When("I enter random user in the username field")
    public void enterRandomUsername() {
        String randomUsername = "user_" + System.currentTimeMillis();
        logger.info("Entering random username: {}", randomUsername);
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        currentPage.enterText(randomUsername, "username");
        testContext.getScenarioContext().setContext("username", randomUsername);
    }
    
    @When("I enter random password in the password field")
    public void enterRandomPassword() {
        String randomPassword = "password_" + System.currentTimeMillis();
        logger.info("Entering random password: ******");
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        currentPage.enterText(randomPassword, "password");
        testContext.getScenarioContext().setContext("password", randomPassword);
    }

    @And("I click the login button")
    public void clickLoginButton() {
        logger.info("Clicking login button");
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        currentPage.clickButton("sign in");
    }
    
    @And("I click the register button")
    public void clickRegisterButton() {
        logger.info("Clicking register button");
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        currentPage.clickButton("register");
    }

    @Then("I should be redirected to the dashboard")
    public void verifyDashboardRedirection() {
        logger.info("Verifying redirection to dashboard");
        String expectedUrl = "https://spring-auth.vercel.app/";
        String actualUrl = driver.getCurrentUrl();
        assertEquals("Expected to be redirected to dashboard", expectedUrl, actualUrl);
        assertTrue("Expected to be logged in", navComponent.isLoggedIn());
    }
    
    @Then("I should be redirected to the login page")
    public void verifyLoginPageRedirection() {
        logger.info("Verifying redirection to login page");
        String expectedUrl = "https://spring-auth.vercel.app/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals("Expected to be redirected to login page", expectedUrl, actualUrl);
    }

    @Then("I should see a message {string}")
    public void verifyMessage(String expectedMessage) {
        logger.info("Verifying message: {}", expectedMessage);
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        
        // Cho phép toast message có thời gian xuất hiện (tối đa 1 giây)
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // Bỏ qua
        }
        
        // Xác định loại thông báo dựa trên nội dung
        boolean isSuccessMessage = expectedMessage.toLowerCase().contains("success") || 
                                 expectedMessage.toLowerCase().contains("successful");
                                 
        String messageType = isSuccessMessage ? "success" : "error";
        logger.debug("Expected message type: {}", messageType);
        
        // Kiểm tra phiên bản trực tiếp từ trang
        if (!currentPage.isMessageDisplayed(messageType)) {
            // Log thông tin debug để trợ giúp chẩn đoán
            logger.warn("Message of type '{}' not immediately displayed, checking message content", messageType);
            
            // Kiểm tra nội dung thông báo trực tiếp
            String actualMessage = currentPage.getMessage();
            if (actualMessage != null && !actualMessage.isEmpty()) {
                logger.debug("Found message: {}", actualMessage);
                assertTrue("Expected message to contain: " + expectedMessage + ", but was: " + actualMessage, 
                          actualMessage.contains(expectedMessage));
                return;
            }
            
            // Nếu không tìm thấy thông báo, kiểm tra lại loại thông báo
            boolean isDisplayed = isSuccessMessage ? 
                                 currentPage.isMessageDisplayed("success") : 
                                 currentPage.isMessageDisplayed("error");
                                 
            assertTrue("Expected a " + messageType + " message to be displayed", isDisplayed);
        }
        
        // Kiểm tra nội dung thông báo
        String actualMessage = currentPage.getMessage();
        assertTrue("Expected message to contain: " + expectedMessage + ", but was: " + actualMessage, 
                   actualMessage != null && actualMessage.contains(expectedMessage));
    }

    @Then("I should see error messages containing {string}")
    public void verifyErrorMessages(String expectedErrorsStr) {
        logger.info("Verifying error messages containing: {}", expectedErrorsStr);
        currentPage = (BasePage) testContext.getScenarioContext().getContext("currentPage");
        
        // Lấy danh sách lỗi từ form trước (không đợi toast)
        List<String> actualErrors = currentPage.getErrorMessages();
        
        // Kiểm tra xem có lỗi form không
        if (actualErrors.isEmpty()) {
            // Cho phép toast có thời gian xuất hiện
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // Bỏ qua
            }
            
            // Nếu không có lỗi form, mới kiểm tra toast
            assertTrue("Expected error messages to be displayed", currentPage.isMessageDisplayed("error"));
            
            // Nếu không có lỗi form nhưng có toast error, lấy message từ toast
            String toastMessage = currentPage.getMessage();
            actualErrors = List.of(toastMessage);
        }
        
        // Split expected errors if multiple
        String[] expectedErrors = expectedErrorsStr.split(",\\s*");
        
        logger.debug("Expected errors: {}", (Object)expectedErrors);
        logger.debug("Actual errors: {}", actualErrors);
        
        // Basic count check if we can determine
        if (expectedErrors.length > 0 && !expectedErrorsStr.contains("and")) {
            assertEquals("Number of error messages doesn't match", 
                         expectedErrors.length, actualErrors.size());
        }
        
        // Check each expected error is found in at least one actual error
        for (String expectedError : expectedErrors) {
            boolean found = false;
            expectedError = expectedError.trim();
            
            for (String actualError : actualErrors) {
                if (actualError.contains(expectedError)) {
                    found = true;
                    break;
                }
            }
            
            assertTrue("Expected to find error containing: " + expectedError + 
                      " in actual errors: " + actualErrors, found);
        }
    }
}
