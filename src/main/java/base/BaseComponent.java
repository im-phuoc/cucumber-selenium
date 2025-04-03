package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ElementHelper;

import java.util.List;

public abstract class BaseComponent {
    protected static final Logger logger = LoggerFactory.getLogger(BaseComponent.class);
    protected static final int DEFAULT_TIMEOUT = 5;
    
    protected WebDriver driver;
    protected ElementHelper elementHelper;

    public BaseComponent(WebDriver driver) {
        this.driver = driver;
        this.elementHelper = new ElementHelper(driver, DEFAULT_TIMEOUT);
        PageFactory.initElements(driver, this);
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
}
