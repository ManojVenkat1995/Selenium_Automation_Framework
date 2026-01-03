package com.framework.web.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardPage extends BasePage {

    //Header
    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private By userProfileIcon = By.xpath("//span[@class='oxd-userdropdown-tab']/img[@alt='profile picture']");
    private By userName = By.xpath("//p[contains(@class,'oxd-userdropdown-name')]");
    //Body
    private By timeAtWork = By.xpath("//p[text()='Time at Work']");
    private By myActions = By.xpath("//p[text()='My Actions']");
    private By quickLuanch= By.xpath("//p[text()='Quick Launch']");
    private By buzzLatestPosts =By.xpath("//p[text()='Buzz Latest Posts']");
    private By employeeLeaves =By.xpath("//p[text()='Employees on Leave Today']");
    private By employeeSubUnit =By.xpath("//p[text()='Employee Distribution by Sub Unit']");
    private By employeeLocation =By.xpath("//p[text()='Employee Distribution by Location']");
    //SideNavBar
    List<String> expectedItems = Arrays.asList(
            "Admin", "PIM", "Leave", "Time", "Recruitment",
            "My Info", "Performance", "Dashboard",
            "Directory", "Maintenance", "Claim", "Buzz"
    );

    public DashboardPage() {
        super();
        // Verify dashboard loaded
        waitForCondition(driver -> !driver.findElements(dashboardHeader).isEmpty(), 10);
        log.info("DashboardPage loaded successfully");
    }

    //Header
    public boolean isDashboardLoaded() {return isDisplayed(dashboardHeader);}

    public String getUserName() {
        return getText(userName);
    }

    public boolean isPofileImgLoaded(){
        return isDisplayed(userProfileIcon);
    }

    //Body
    public boolean isTimeAtWorkVisible(){return isDisplayed(timeAtWork);}
    public boolean isMyActionsVisible(){return isDisplayed(myActions);}
    public boolean isQuickLaunchVisible(){return isDisplayed(quickLuanch);}
    public boolean isBuzzLatestPostsVisible(){return isDisplayed(buzzLatestPosts);}
    public boolean isEmployeeLeavesVisible(){return isDisplayed(employeeLeaves);}
    public boolean isEmployeeSubUnitVisible(){return isDisplayed(employeeSubUnit);}
    public boolean isEmployeeLocationVisible(){return isDisplayed(employeeLocation);}

    //Side Navigation Bar
    public List<String> getSidebarMenuTexts() {
        List<WebElement> menuElements = driver.findElements(
                By.xpath("//aside//a//span")
        );

        List<String> menuTexts = new ArrayList<>();

        for (WebElement element : menuElements) {
            String text = element.getText().trim();
            if (!text.isEmpty()) {
                menuTexts.add(text);
            }
        }
        return menuTexts;
    }

    public boolean areSidebarMenuItemsPresent(List<String> expectedMenuItems) {
        List<String> actualMenuItems = getSidebarMenuTexts();

        for (String expectedItem : expectedMenuItems) {
            if (!actualMenuItems.contains(expectedItem)) {
                return false;
            }
        }
        return true;
    }

}
