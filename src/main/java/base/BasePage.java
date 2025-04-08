package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ElementHelper;

import java.util.List;
import java.util.Objects;

public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected static final int DEFAULT_TIMEOUT = 5;
    
    protected WebDriver driver;
    protected String pageUrl;
    protected ElementHelper elementHelper;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementHelper = new ElementHelper(driver, DEFAULT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        logger.info("Navigating to page: {}", pageUrl);
        driver.get(pageUrl);
        waitForPageToLoad();
    }

    public void waitForPageToLoad() {
        logger.debug("Waiting for page to load");
        try {
            new ElementHelper(driver, 3).wait.until(webDriver -> 
                Objects.equals(((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState"), "complete"));
        } catch (Exception e) {
            logger.warn("Timeout waiting for page to load: {}", e.getMessage());
        }
    }

    // Delegate methods to ElementHelper
    protected WebElement waitForVisibility(WebElement element) {
        return elementHelper.waitForVisibility(element);
    }

    protected List<WebElement> waitForVisibilityOfAllElements(List<WebElement> elements) {
        return elementHelper.waitForVisibilityOfAllElements(elements);
    }

    protected WebElement waitForClickable(WebElement element) {
        return elementHelper.waitForClickable(element);
    }

    protected void click(WebElement element) {
        elementHelper.click(element);
    }

    protected void javascriptClick(WebElement element) {
        elementHelper.javascriptClick(element);
    }

    protected void setText(WebElement element, String text) {
        elementHelper.setText(element, text);
    }

    protected String getText(WebElement element) {
        return elementHelper.getText(element);
    }

    protected boolean isDisplayed(WebElement element) {
        return elementHelper.isDisplayed(element);
    }

    protected boolean isEnabled(WebElement element) {
        return elementHelper.isEnabled(element);
    }

    // Abstract methods that all pages must implement
    public abstract void enterText(String text, String fieldName);
    public abstract void clickButton(String buttonName);
    public abstract boolean isMessageDisplayed(String messageType);
    public abstract String getMessage();
    public abstract List<String> getErrorMessages();
}