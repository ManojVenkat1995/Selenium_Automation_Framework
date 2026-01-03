package com.framework.base;

import com.framework.config.FrameworkConfig;
import com.framework.utils.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Listeners({
        com.framework.listeners.TestNGListener.class,
        io.qameta.allure.testng.AllureTestNg.class
})
public class BaseTest {

    protected WebDriver driver;
    protected Logger log;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"env"})
    public void beforeSuite(@Optional("dev") String env) {
        // Load environment config once per suite
        FrameworkConfig.load(env);
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        log = LoggerUtils.getLogger(this.getClass());

        String defaultBrowser = FrameworkConfig.getOrDefault("defaultBrowser", "chrome");
        String browserToUse = (browser == null || browser.isEmpty()) ? defaultBrowser : browser;

        log.info("Starting test. Browser: {}", browserToUse);

        DriverFactory.initDriver(browserToUse);
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        try {
            DriverFactory.quitDriver();
        } finally {
            DriverFactory.removeDriver();
        }
    }


}
