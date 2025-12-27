package com.framework.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    public static String captureScreenshot(WebDriver driver, String testName) {
        Logger log = LoggerUtils.getLogger(ScreenshotUtils.class);

        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Driver does not support screenshots");
            return null;
        }

        byte[] srcBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";

        try {
            Path dirPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Path destPath = dirPath.resolve(fileName);
            Files.write(destPath, srcBytes);
            log.info("Screenshot saved: {}", destPath.toAbsolutePath());
            return destPath.toString();
        } catch (IOException e) {
            log.error("Failed to save screenshot for test: {}", testName, e);
            return null;
        }
    }
}
