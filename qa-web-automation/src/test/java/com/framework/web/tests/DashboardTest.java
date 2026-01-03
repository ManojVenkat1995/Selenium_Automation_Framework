package com.framework.web.tests;

import com.framework.base.BaseTest;
import com.framework.web.pages.DashboardPage;
import com.framework.web.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;


@Epic("OrangeHRM Automation")
@Feature("Dashboard Functionality")
public class DashboardTest extends BaseTest {

    @BeforeMethod
    public void Login(){
        LoginPage loginPage =new LoginPage();
        loginPage.login("Admin","admin123");
        Assert.assertTrue(loginPage.successfulLogin().isDashboardLoaded(),
                "Dashboard should be visible after valid login");
    }

    @Test(description = "Verify Dashboard Elements Visibility")
    @Severity(SeverityLevel.NORMAL)
    @Story("Valid Dashboard Elements")
    public void testDashboardElementsLoaded() {
        DashboardPage dashboardPage =new DashboardPage();

        log.info("Verifying All Dashboard Elements Visibility");

        Assert.assertTrue(dashboardPage.isMyActionsVisible());
        Assert.assertTrue(dashboardPage.isTimeAtWorkVisible());
        Assert.assertTrue(dashboardPage.isBuzzLatestPostsVisible());
        Assert.assertTrue(dashboardPage.isEmployeeLocationVisible());
        Assert.assertTrue(dashboardPage.isEmployeeLeavesVisible());
        Assert.assertTrue(dashboardPage.isEmployeeSubUnitVisible());
        Assert.assertTrue(dashboardPage.isQuickLaunchVisible());

    }

    @Test(description = "Verify Side Navigation Bar items Visibility")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Valid Side Navigation Bar menu")
    public void verifySidebarMenuItems() {
        DashboardPage dashboardPage =new DashboardPage();
        List<String> expectedMenuItems = Arrays.asList(
                "Admin", "PIM", "Leave", "Time", "Recruitment",
                "My Info", "Performance", "Dashboard",
                "Directory", "Maintenance", "Claim", "Buzz"
        );

        Assert.assertTrue(
                dashboardPage.areSidebarMenuItemsPresent(expectedMenuItems),
                "One or more sidebar menu items are missing"
        );
    }

}
