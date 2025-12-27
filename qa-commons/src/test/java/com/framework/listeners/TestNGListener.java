package com.framework.listeners;

import com.framework.base.DriverFactory;
import com.framework.utils.LoggerUtils;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {

    private static final Logger log = LoggerUtils.getLogger(TestNGListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("TEST START: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("TEST FAILED: {}", result.getMethod().getMethodName());
        log.error("Failure cause:", result.getThrowable());

        // Auto-capture screenshot on failure
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver,
                    result.getMethod().getMethodName());
            if (screenshotPath != null) {
                log.info("Screenshot captured: {}", screenshotPath);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST SKIPPED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Optional: handle unstable tests
        log.warn("Test failed but within success %: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Starting test suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Finished test suite: {} - Passed: {}, Failed: {}, Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }
}
