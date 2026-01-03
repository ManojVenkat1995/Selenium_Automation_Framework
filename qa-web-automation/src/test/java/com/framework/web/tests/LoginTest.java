package com.framework.web.tests;


import com.framework.base.BaseTest;
import com.framework.web.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;


@Epic("OrangeHRM Automation")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {

    @Test(description = "Verify valid user can login successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Valid Login Flow")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage();

        log.info("Executing valid login test");
        loginPage.login("Admin", "admin123");

        // Verify successful login
        Assert.assertTrue(loginPage.successfulLogin().isDashboardLoaded(),
                "Dashboard should be visible after valid login");

        log.info("Valid login test PASSED");
    }

    @Test(description = "Verify invalid credentials show error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login Flow")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();

        log.info("Executing invalid login test");
        loginPage.login("wronguser", "wrongpass");

        // Verify error message
        Assert.assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(),
                "Invalid credentials error should be displayed");

        log.info("Invalid login test PASSED");
    }

}
