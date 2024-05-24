package com.assessment.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverManager {
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			setDriver("chrome");
		}
		return driver.get();
	}

	public static WebDriverWait getWait() {
		return wait.get();
	}

	public static void setDriver(String browser) {
		switch (browser.toLowerCase()) {
		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--start-maximized");
			driver.set(new ChromeDriver(chromeOptions));
			break;
		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addArguments("--start-maximized");
			driver.set(new FirefoxDriver(firefoxOptions));
			break;
		default:
			throw new IllegalArgumentException("Browser type not supported: " + browser);
		}
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(45)));
	}

	public static void quitDriver() {
		if (driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}
}