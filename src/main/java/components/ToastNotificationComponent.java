package components;

import base.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ElementHelper;

import java.time.Duration;

public class ToastNotificationComponent extends BaseComponent {
    private static final Logger logger = LoggerFactory.getLogger(ToastNotificationComponent.class);
    
    @FindBy(xpath = "//div[@role='status']")
    private WebElement toastNotification;
    
    private static final By TOAST_LOCATOR = By.xpath("//div[@role='status']");
    
    // Lưu thông tin toast message gần nhất
    private String lastToastMessage = null;
    private boolean wasSuccessToast = false;
    private boolean wasErrorToast = false;
    
    // Sử dụng timeout ngắn để tối ưu hiệu suất
    private static final int TOAST_TIMEOUT = 1;

    public ToastNotificationComponent(WebDriver driver) {
        super(driver);
        this.elementHelper = new ElementHelper(driver, TOAST_TIMEOUT);
    }

    /**
     * Cố gắng bắt toast message hiện tại
     */
    private void tryCaptureCurrent() {
        try {
            if (elementHelper.isDisplayedWithoutWait(toastNotification)) {
                String message = elementHelper.getTextWithoutWait(toastNotification);
                String className = toastNotification.getAttribute("class");
                
                if (message != null && !message.isEmpty()) {
                    lastToastMessage = message;
                    logger.debug("Captured toast message: {}", lastToastMessage);
                    
                    // Phân loại toast message dựa trên nội dung và class
                    wasSuccessToast = isSuccessMessage(message, className);
                    wasErrorToast = isErrorMessage(message, className);
                    
                    logger.debug("Toast classification - Success: {}, Error: {}", wasSuccessToast, wasErrorToast);
                }
            }
        } catch (Exception e) {
            logger.debug("Could not capture toast: {}", e.getMessage());
        }
    }
    
    /**
     * Kiểm tra xem một thông báo có phải là thông báo thành công không
     */
    private boolean isSuccessMessage(String message, String className) {
        if (message == null) return false;
        
        boolean classIndicatesSuccess = (className != null && 
            (className.contains("success") || className.contains("green")));
        
        boolean messageIndicatesSuccess = message.toLowerCase().contains("success") || 
                                        message.toLowerCase().contains("successful") ||
                                        message.toLowerCase().contains("successfully");
                                        
        return classIndicatesSuccess || messageIndicatesSuccess;
    }
    
    /**
     * Kiểm tra xem một thông báo có phải là thông báo lỗi không
     */
    private boolean isErrorMessage(String message, String className) {
        if (message == null) return false;
        
        boolean classIndicatesError = (className != null && 
            (className.contains("error") || className.contains("red")));
            
        boolean messageIndicatesError = message.toLowerCase().contains("error") || 
                                      message.toLowerCase().contains("failed") ||
                                      message.toLowerCase().contains("incorrect") ||
                                      message.toLowerCase().contains("invalid") ||
                                      message.toLowerCase().contains("not found");
                                      
        return classIndicatesError || messageIndicatesError;
    }

    public boolean isToastDisplayed() {
        logger.debug("Checking if toast message is displayed");
        
        // Cố gắng bắt toast hiện tại
        tryCaptureCurrent();
        
        // Kiểm tra hiện tại hoặc lịch sử
        boolean currentlyDisplayed = elementHelper.isDisplayedWithoutWait(toastNotification);
        boolean hasToastHistory = lastToastMessage != null;
        
        logger.debug("Toast display check - Current: {}, History: {}", currentlyDisplayed, hasToastHistory);
        return currentlyDisplayed || hasToastHistory;
    }

    public String getToastMessage() {
        logger.debug("Getting toast message text");
        
        // Cố gắng bắt toast hiện tại
        tryCaptureCurrent();
        
        logger.debug("Returning toast message: {}", lastToastMessage);
        return lastToastMessage;
    }
    
    public boolean isSuccessToast() {
        logger.debug("Checking if toast is a success toast");
        
        // Cố gắng bắt toast hiện tại
        tryCaptureCurrent();
        
        // Kiểm tra trực tiếp nếu hiện đang hiển thị
        if (elementHelper.isDisplayedWithoutWait(toastNotification)) {
            String message = elementHelper.getTextWithoutWait(toastNotification);
            String className = toastNotification.getAttribute("class");
            
            // Cập nhật trạng thái nếu phát hiện toast thành công
            boolean isSuccess = isSuccessMessage(message, className);
            if (isSuccess) {
                wasSuccessToast = true;
                lastToastMessage = message;
            }
            
            logger.debug("Direct success toast check: {}", isSuccess);
            return isSuccess;
        }
        
        // Trả về trạng thái đã lưu
        logger.debug("Using cached success toast status: {}", wasSuccessToast);
        return wasSuccessToast;
    }
    
    public boolean isErrorToast() {
        logger.debug("Checking if toast is an error toast");
        
        // Cố gắng bắt toast hiện tại
        tryCaptureCurrent();
        
        // Kiểm tra trực tiếp nếu hiện đang hiển thị
        if (elementHelper.isDisplayedWithoutWait(toastNotification)) {
            String message = elementHelper.getTextWithoutWait(toastNotification);
            String className = toastNotification.getAttribute("class");
            
            // Cập nhật trạng thái nếu phát hiện toast lỗi
            boolean isError = isErrorMessage(message, className);
            if (isError) {
                wasErrorToast = true;
                lastToastMessage = message;
            }
            
            logger.debug("Direct error toast check: {}", isError);
            return isError;
        }
        
        // Trả về trạng thái đã lưu
        logger.debug("Using cached error toast status: {}", wasErrorToast);
        return wasErrorToast;
    }
    
    public boolean waitForToastToDisappear() {
        logger.debug("Waiting for toast to disappear");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(TOAST_LOCATOR));
        } catch (Exception e) {
            logger.warn("Toast did not disappear in time: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean waitForToastWithText(String expectedText) {
        logger.debug("Waiting for toast with text: {}", expectedText);
        
        // Kiểm tra nếu đã bắt được toast với text này
        if (lastToastMessage != null && lastToastMessage.contains(expectedText)) {
            logger.debug("Toast with text already captured: {}", expectedText);
            return true;
        }
        
        // Chờ trong thời gian ngắn cho toast xuất hiện
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TOAST_TIMEOUT));
            boolean appeared = wait.until(driver -> {
                tryCaptureCurrent();
                return lastToastMessage != null && lastToastMessage.contains(expectedText);
            });
            logger.debug("Toast with text '{}' found: {}", expectedText, appeared);
            return appeared;
        } catch (Exception e) {
            logger.debug("Toast with text '{}' did not appear: {}", expectedText, e.getMessage());
            return false;
        }
    }
    
    public boolean containsMessage(String expectedText) {
        // Cố gắng bắt toast hiện tại
        tryCaptureCurrent();
        
        String actualMessage = lastToastMessage;
        boolean contains = actualMessage != null && actualMessage.contains(expectedText);
        logger.debug("Toast contains '{}': {}", expectedText, contains);
        return contains;
    }
    
    /**
     * Reset trạng thái đã lưu về toast message
     */
    public void resetCapturedToast() {
        logger.debug("Resetting captured toast state");
        lastToastMessage = null;
        wasSuccessToast = false;
        wasErrorToast = false;
    }
}
