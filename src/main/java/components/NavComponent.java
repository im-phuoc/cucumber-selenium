package components;

import base.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavComponent extends BaseComponent {
    private static final Logger logger = LoggerFactory.getLogger(NavComponent.class);
    
    @FindBy(xpath = "//nav//a[text()='My App']")
    private WebElement homePageLink;

    @FindBy(xpath = "//nav//a[normalize-space()='Home']")
    private WebElement homeLink;

    @FindBy(xpath = "//nav//a[normalize-space()='Dashboard']")
    private WebElement dashboardLink;

    @FindBy(xpath = "//nav//a[normalize-space()='Profile']")
    private WebElement profileLink;

    @FindBy(xpath = "//nav//span[contains(.,'Welcome')]")
    private WebElement welcomeUsername;

    @FindBy(xpath = "//nav//button[text()='Logout']")
    private WebElement logoutButton;

    @FindBy(xpath = "//nav//a[text()='Login']")
    private WebElement loginLink;

    @FindBy(xpath = "//nav//a[text()='Register']")
    private WebElement registerLink;

    public NavComponent(WebDriver driver) {
        super(driver);
    }

    public void navigateHomePage() {
        logger.info("Navigating to Home page");
        click(homePageLink);
    }

    public void navigateDashboard() {
        logger.info("Navigating to Dashboard");
        click(dashboardLink);
    }

    public void navigateProfile() {
        logger.info("Navigating to Profile");
        click(profileLink);
    }

    public void navigateLoginPage() {
        logger.info("Navigating to Login page");
        click(loginLink);
    }

    public void navigateRegisterPage() {
        logger.info("Navigating to Register page");
        click(registerLink);
    }

    public void clickLogout() {
        logger.info("Clicking Logout button");
        click(logoutButton);
    }
    
    public String getWelcomeMessage() {
        try {
            logger.debug("Getting welcome message");
            return getText(welcomeUsername);
        } catch (Exception e) {
            logger.debug("Welcome message not found: {}", e.getMessage());
            return null;
        }
    }
    
    public boolean isLoggedIn() {
        try {
            logger.debug("Checking if user is logged in");
            return isDisplayed(logoutButton) && isDisplayed(welcomeUsername);
        } catch (Exception e) {
            logger.debug("User is not logged in: {}", e.getMessage());
            return false;
        }
    }
}
