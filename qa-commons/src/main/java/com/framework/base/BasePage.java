package com.framework.base;

import com.framework.utils.LoggerUtils;
import com.framework.utils.WaitUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

public abstract class BasePage {

    protected WebDriver driver;
    protected Logger log;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.log = LoggerUtils.getLogger(this.getClass());
    }

    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator);
        log.info("Clicking on element: {}", locator);
        find(locator).click();
    }

    protected void type(By locator, String text) {
        WaitUtils.waitForVisible(driver, locator);
        log.info("Typing '{}' into element: {}", text, locator);
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        WaitUtils.waitForVisible(driver, locator);
        String text = find(locator).getText();
        log.info("Text from {} is '{}'", locator, text);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void waitForCondition(ExpectedCondition<?> condition, long timeoutInSeconds) {
        WaitUtils.waitForCondition(driver, condition, timeoutInSeconds);
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        log.info("Current URL: {}", url);
        return url;
    }
}
