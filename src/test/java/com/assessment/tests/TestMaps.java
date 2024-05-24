package com.assessment.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.assessment.pages.Maps;
import com.assessment.utils.DriverManager;
import com.assessment.utils.ExtentReport;
import com.aventstack.extentreports.ExtentTest;

public class TestMaps {
	WebDriver driver;
	Maps maps;

	@BeforeTest
	public void setUp() {
		driver = DriverManager.getDriver();
		driver.get("https://openweathermap.org/");
		maps = new Maps(driver);
		try {
			maps.acceptCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 1)
	public void navigateToMaps() {
		ExtentTest test = ExtentReport.getTest();
		maps.navigateToMaps();
		test.info("Navigated successfully to maps section");
	}

	@Test(priority = 2)
	public void verifyMapLayers() {
		ExtentTest test = ExtentReport.getTest();
		maps.searchInMaps();
		test.info("Search action performed");
		maps.verifyLayers(test);

	}

	@AfterTest
	public void tearDown() {
		DriverManager.quitDriver();
	}
}