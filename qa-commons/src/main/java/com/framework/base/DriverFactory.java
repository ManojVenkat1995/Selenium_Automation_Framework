package com.framework.base;

import com.framework.config.FrameworkConfig;
import com.framework.utils.LoggerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
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

                // CI / Headless optimizations
                if (isCIEnvironment()) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-extensions");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }

                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();

                if (isCIEnvironment()) {
                    ffOptions.addArguments("--headless");
                    ffOptions.addArguments("--width=1920");
                    ffOptions.addArguments("--height=1080");
                    ffOptions.addArguments("--no-sandbox");
                } else {
                    ffOptions.addArguments("--width=1920");
                    ffOptions.addArguments("--height=1080");
                }

                return new FirefoxDriver(ffOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (isCIEnvironment()) {
                    edgeOptions.addArguments("--headless");
                    edgeOptions.addArguments("--no-sandbox");
                }

                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static boolean isCIEnvironment() {
        String[] ciVars = {
                "CI", "CONTINUOUS_INTEGRATION", "GITHUB_ACTIONS", "JENKINS_URL", "TRAVIS", "CIRCLECI"
        };
        for (String var : ciVars) {
            if (System.getenv(var) != null) {
                log.info("CI environment detected: {}", var);
                return true;
            }
        }
        return false;
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
//                driver.remove();//Written seperately for screenshot issue
            }
        }
    }

    public static void removeDriver() {
        driver.remove();
    }
}
