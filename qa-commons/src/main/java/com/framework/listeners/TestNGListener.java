package com.framework.listeners;

import com.framework.base.DriverFactory;
import com.framework.utils.LoggerUtils;
import com.framework.utils.ScreenshotUtils;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                attachScreenshot(driver, result.getMethod().getMethodName() + "_failure");
                String path = ScreenshotUtils.captureScreenshot(
                        driver,
                        result.getMethod().getMethodName()
                );
                log.info("Screenshot captured: {}", path);
            } catch (Exception e) {
                log.error("Failed to capture screenshot", e);
            }
        } else {
            log.warn("Driver was null in onTestFailure, no screenshot captured");
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
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
