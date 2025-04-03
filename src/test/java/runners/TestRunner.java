package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "hooks"},
        plugin = {"pretty", "json:target/cucumber-reports/Cucumber.json"},
        monochrome = true
//        tags = "@invalid-input or @invalid-credentials"
)
public class TestRunner {
} 