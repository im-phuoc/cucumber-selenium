package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ElementHelper {
    private static final Logger logger = LoggerFactory.getLogger(ElementHelper.class);
    private final WebDriver driver;
    public final WebDriverWait wait;
    
    public ElementHelper(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
    
    public WebElement waitForVisibility(WebElement element) {
        logger.debug("Waiting for element to be visible: {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public List<WebElement> waitForVisibilityOfAllElements(List<WebElement> elements) {
        logger.debug("Waiting for all elements to be visible");
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }
    
    public WebElement waitForClickable(WebElement element) {
        logger.debug("Waiting for element to be clickable: {}", element);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    public void click(WebElement element) {
        logger.debug("Clicking on element: {}", element);
        waitForClickable(element).click();
    }
    
    public void javascriptClick(WebElement element) {
        logger.debug("JavaScript clicking on element: {}", element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", waitForVisibility(element));
    }
    
    public void setText(WebElement element, String text) {
        logger.debug("Setting text '{}' on element: {}", text, element);
        WebElement visibleElement = waitForVisibility(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }
    
    public String getText(WebElement element) {
        logger.debug("Getting text from element: {}", element);
        return waitForVisibility(element).getText();
    }
    
    /**
     * Lấy text của element mà không đợi nó hiển thị
     * Hữu ích cho các trường hợp cần xử lý nhanh như toast message
     */
    public String getTextWithoutWait(WebElement element) {
        try {
            return element.getText();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Kiểm tra nhanh nếu element được hiển thị mà không đợi/timeout
     * Hữu ích khi kiểm tra các element xuất hiện và biến mất nhanh như toast
     */
    public boolean isDisplayedWithoutWait(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isDisplayed(WebElement element) {
        try {
            logger.debug("Checking if element is displayed: {}", element);
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element is not displayed: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isEnabled(WebElement element) {
        try {
            logger.debug("Checking if element is enabled: {}", element);
            return waitForVisibility(element).isEnabled();
        } catch (Exception e) {
            logger.debug("Element is not enabled: {}", e.getMessage());
            return false;
        }
    }
} 