package com.framework.web.pages;

import com.framework.base.BasePage;
import com.framework.config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    // OrangeHRM Live Demo locators (verified)
    private By loginHeading= By.xpath("//*[text()='Login']");
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By companyLogo_1= By.xpath("//img[@alt='company-branding']");
    private By companyLogo_2 = By.xpath("//div[@class='orangehrm-login-logo']");
    private By invalidCredentialsMsg = By.xpath("//p[normalize-space()='Invalid credentials']");

    public LoginPage() {
        super();
        driver.get(FrameworkConfig.get("baseUrl"));
        log.info("Navigated to: {}", FrameworkConfig.get("baseUrl"));
        // Verify login page loaded
        waitForCondition(ExpectedConditions.visibilityOfElementLocated(loginHeading), 10);
        waitForCondition(driver -> !driver.findElements(companyLogo_1).isEmpty(), 10);
        waitForCondition(driver -> !driver.findElements(companyLogo_2).isEmpty(), 10);
        log.info("LoginPage loaded successfully");
    }

    public void login(String username, String password) {
        log.info("Attempting login - Username: {}", username);


        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);

        log.info("Login attempt completed");
    }

    public boolean isInvalidCredentialsErrorDisplayed() {
        waitForCondition(ExpectedConditions.visibilityOfElementLocated(invalidCredentialsMsg),10);
        return isDisplayed(invalidCredentialsMsg);
    }

    public DashboardPage successfulLogin() {
        log.info("Login successful - redirecting to DashboardPage");
        return new DashboardPage();
    }
}
