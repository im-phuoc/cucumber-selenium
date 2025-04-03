package stepDefinitions;

import config.ConfigManager;
import hooks.Hooks;
import org.openqa.selenium.WebDriver;

public class ProfileSteps {
    private WebDriver driver;
    private ConfigManager configManager;

    public ProfileSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
        this.configManager = new ConfigManager();
    }

}
