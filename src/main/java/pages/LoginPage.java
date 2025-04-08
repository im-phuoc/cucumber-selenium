package pages;

import base.BasePage;
import components.ToastNotificationComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(xpath = "//input[@name='username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and text()='Sign in']")
    private WebElement signInButton;


    @FindBy(css = "p.text-red-600")
    private List<WebElement> errors;



    private final ToastNotificationComponent toastNotificationComponent;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.pageUrl = "https://spring-auth.vercel.app/login";
        this.toastNotificationComponent = new ToastNotificationComponent(driver);
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        setText(usernameField, username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password: ******");
        setText(passwordField, password);
    }

    public void clickSignInButton() {
        logger.info("Clicking Sign In button");
        click(signInButton);
    }

    @Override
    public void enterText(String text, String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "username":
                enterUsername(text);
                break;
            case "password":
                enterPassword(text);
                break;
            default:
                logger.error("Unrecognized field name: {}", fieldName);
                throw new IllegalArgumentException("Unrecognized field name: " + fieldName);
        }
    }

    @Override
    public void clickButton(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "sign in":
            case "login":
            case "submit":
                clickSignInButton();
                break;
            default:
                logger.error("Unrecognized button name: {}", buttonName);
                throw new IllegalArgumentException("Unrecognized button name: " + buttonName);
        }
    }

    @Override
    public boolean isMessageDisplayed(String messageType) {
        logger.debug("Checking if message is displayed of type: {}", messageType);
        switch (messageType.toLowerCase()) {
            case "success":
                return toastNotificationComponent.isToastDisplayed() && 
                       toastNotificationComponent.isSuccessToast();
            case "error":
                // Ưu tiên kiểm tra form errors trước
                boolean hasFormErrors = !getErrorMessages().isEmpty();
                if (hasFormErrors) {
                    logger.debug("Found form errors, no need to check toast");
                    return true;
                }
                
                // Nếu không có form errors, mới kiểm tra toast error
                logger.debug("No form errors, checking toast notification");
                return toastNotificationComponent.isToastDisplayed() && 
                       toastNotificationComponent.isErrorToast();
            default:
                logger.warn("Unknown message type: {}", messageType);
                return false;
        }
    }

    @Override
    public String getMessage() {
        logger.debug("Getting message");
        // Ưu tiên kiểm tra form errors trước
        List<String> errorMessages = getErrorMessages();
        if (!errorMessages.isEmpty()) {
            return String.join(", ", errorMessages);
        }
        
        // Nếu không có form errors, mới kiểm tra toast
        if (toastNotificationComponent.isToastDisplayed()) {
            return toastNotificationComponent.getToastMessage();
        }
        
        return "";
    }

    @Override
    public List<String> getErrorMessages() {
        try {
            logger.debug("Getting form error messages");
            List<String> errorMessages = new ArrayList<>();
            for (WebElement error : errors) {
                if (isDisplayed(error)) {
                    errorMessages.add(getText(error));
                }
            }
            logger.debug("Found {} error messages", errorMessages.size());
            return errorMessages;
        } catch (Exception e) {
            logger.error("Error getting form error messages: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Additional methods
    public boolean isLoginFormDisplayed() {
        logger.debug("Checking if login form is displayed");
        return isDisplayed(usernameField) && isDisplayed(passwordField) && 
               isDisplayed(signInButton);
    }
    
    public ToastNotificationComponent getToastComponent() {
        return toastNotificationComponent;
    }
    
    // Business method
    public boolean login(String username, String password) {
        logger.info("Attempting login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickSignInButton();
        
        try {
            // Wait a bit for toast to appear
            Thread.sleep(500);
            return toastNotificationComponent.isToastDisplayed() && 
                   toastNotificationComponent.isSuccessToast() &&
                   toastNotificationComponent.containsMessage("Login successful");
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for toast: {}", e.getMessage());
            return false;
        }
    }
}
