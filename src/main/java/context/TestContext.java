package context;

import org.openqa.selenium.WebDriver;
import pages.DashboardPage;
import pages.LoginPage;
import pages.RegisterPage;

public class TestContext {
    private WebDriver driver;
    private ScenarioContext scenarioContext;

    private LoginPage loginPage;
    private RegisterPage registerPage;
    private DashboardPage dashboardPage;

    public TestContext(WebDriver driver) {
        this.driver = driver;
        this.scenarioContext = new ScenarioContext();
        initializePageObjects();
    }

    public void initializePageObjects() {
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public RegisterPage getRegisterPage() {
        return registerPage;
    }
}
