package com.assessment.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseUtility {
	WebDriver driver;
	WebDriverWait wait;
	public static final String directoryLoc = System.getProperty("user.dir");

	public BaseUtility(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void click(WebElement el) {
		wait.until(ExpectedConditions.elementToBeClickable(el));
		try {
			el.click();
		} catch (Exception ElementClickInterceptedException) {
			ElementClickInterceptedException.printStackTrace();
		}
	}

	public void click(By by) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.findElement(by).click();
	}

	public String getText(WebElement el) {
		wait.until(ExpectedConditions.visibilityOf(el));
		return el.getText();
	}

	public boolean checkIfElementIsLocated(WebElement ele) {
		try {
			return ele.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

}
