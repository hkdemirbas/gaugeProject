package com.hepsiburada;


import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class BaseTest {

    protected static
    WebDriver driver;

    protected static WebDriverWait webDriverWait;

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private String baseUrl = "https://www.hepsiburada.com";
    private static String browserName = "chrome";
    private static boolean isFullScreen = true;

    DesiredCapabilities capabilities = new DesiredCapabilities();


    @BeforeScenario
    public void setUp() throws Exception {
        String selectPlatform = "win";
        String selectBrowser = "chrome";

        if (StringUtils.isEmpty(System.getenv("key"))) {
            if ("win".equalsIgnoreCase(selectPlatform)) {
                if ("chrome".equalsIgnoreCase(selectBrowser)) {
                    ChromeOptions options = new ChromeOptions();
                    capabilities = DesiredCapabilities.chrome();
                    Map<String, Object> prefs = new HashMap<String, Object>();
                    prefs.put("profile.default_content_setting_values.notifications", 2);
                    options.setExperimentalOption("prefs", prefs);
                    options.addArguments("--kiosk");
                    options.addArguments("--disable-notifications");
                    options.addArguments("--start-fullscreen");
                    System.setProperty("webdriver.chrome.driver", "chromeDriver/chromedriver.exe");
                    driver = new ChromeDriver(options);
                    driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
                    driver.get(baseUrl);
                }

            }

        } else {
            ChromeOptions options = new ChromeOptions();
            capabilities = DesiredCapabilities.chrome();

            options.setExperimentalOption("w3c", false);

            options.addArguments("disable-translate");

            options.addArguments("--disable-notifications");

            options.addArguments("--start-fullscreen");

            Map<String, Object> prefs = new HashMap<>();

            options.setExperimentalOption("prefs", prefs);

            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            capabilities.setCapability("key", System.getenv("key"));

            browserName = System.getenv("browser");
        }

    }

    @AfterScenario

    public void tearDown() {
        driver.quit();

    }

}