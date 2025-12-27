package com.framework.base;

import com.framework.config.FrameworkConfig;
import com.framework.utils.LoggerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger log = LoggerUtils.getLogger(DriverFactory.class);

    public static void initDriver(String browser) {
        if (driver.get() != null) {
            return;
        }

        // load env config once (env = system property or default "dev")
        String env = System.getProperty("env", "dev");
        FrameworkConfig.load(env);

        String runMode = System.getProperty("runMode",
                FrameworkConfig.getOrDefault("runMode", "local")); // local or grid

        log.info("Initializing driver. Browser: {}, RunMode: {}, Env: {}", browser, runMode, env);

        WebDriver webDriver;
        switch (runMode.toLowerCase()) {
            case "grid":
                webDriver = createRemoteDriver(browser);
                break;
            default:
                webDriver = createLocalDriver(browser);
        }

        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    private static WebDriver createLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                return new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                return new org.openqa.selenium.firefox.FirefoxDriver(ffOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                return new org.openqa.selenium.edge.EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(String browser) {
        String gridUrl = System.getProperty("gridUrl",
                FrameworkConfig.getOrDefault("gridUrl", "http://localhost:4444/wd/hub"));

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setPlatform(Platform.ANY);

        switch (browser.toLowerCase()) {
            case "chrome":
                caps.setBrowserName("chrome");
                break;
            case "firefox":
                caps.setBrowserName("firefox");
                break;
            case "edge":
                caps.setBrowserName("MicrosoftEdge");
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser for grid: " + browser);
        }

        try {
            return new RemoteWebDriver(new URL(gridUrl), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized for this thread.");
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver", e);
            } finally {
                driver.remove();
            }
        }
    }
}
