package com.framework.base;

import com.framework.config.FrameworkConfig;
import com.framework.utils.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

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
    public void tearDown(ITestResult result) {
        try {
            String methodName = result.getMethod().getMethodName();
            if (result.getStatus() == ITestResult.FAILURE) {
                log.error("TEST FAILED: {}", methodName, result.getThrowable());
                // Screenshot capture will be added via ScreenshotUtils + listener later
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                log.info("TEST PASSED: {}", methodName);
            } else if (result.getStatus() == ITestResult.SKIP) {
                log.warn("TEST SKIPPED: {}", methodName);
            }
        } finally {
            DriverFactory.quitDriver();
        }
    }
}
