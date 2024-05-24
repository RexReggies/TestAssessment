package com.assessment.tests;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.assessment.pages.Homepage;
import com.assessment.utils.DriverManager;
import com.assessment.utils.ExtentReport;
import com.aventstack.extentreports.ExtentTest;

public class SearchTest {
	WebDriver driver;
	Homepage homepage;

	@BeforeTest
	public void setUp() {
		driver = DriverManager.getDriver();
		driver.get("https://openweathermap.org/");
		homepage = new Homepage(driver);
		try {
			homepage.acceptCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSearchCity() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		homepage.searchAndVerifyCity("SearchCityHomePage", test);
		test.info("City Search Performed Successfully");
	}

	@Test
	public void testVerifyCityWeatherDetails() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		homepage.searchAndVerifyCity("VerifyWeatherDetails", test);
		test.info("City Search Performed Successfully");
		homepage.verifyWeatherDetails(test);
		test.info("Verified Weather Details Successfully");
	}

	@Test
	public void testSearchInvalidCity() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		homepage.searchAndVerifyInvalidCity("InvalidCity", test);
		test.info("Invalid City Pop Up Successfully");
	}

	@AfterTest
	public void tearDown() {
		DriverManager.quitDriver();
	}
}