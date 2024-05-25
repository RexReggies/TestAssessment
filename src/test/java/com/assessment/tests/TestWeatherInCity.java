package com.assessment.tests;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.assessment.pages.WeatherInCity;
import com.assessment.utils.DriverManager;
import com.assessment.utils.ExtentReport;
import com.aventstack.extentreports.ExtentTest;

public class TestWeatherInCity {
	WebDriver driver;
	WeatherInCity weath;

	@BeforeTest
	public void setUp() {
		driver = DriverManager.getDriver();
		driver.get("https://openweathermap.org/");
		weath = new WeatherInCity(driver);
		try {
			weath.acceptCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 1)
	public void testSearchWeatherInYourCity() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.searchWeatherInYourCity("WeatherInYourCity");
		test.info("Weather in you city search successfull");
	}

	@Test(priority = 2)
	public void testVerfiyWeatherInYourCityHeadline() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.verifyHeadline(test);
	}

	@Test(priority = 3)
	public void testVerfiyWeatherInYourCitySearchBoxValue() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.verifySearchBoxValue("WeatherInYourCity");
		test.info("Verified Existing Search box value successfully");
	}

	@Test(priority = 4)
	public void testVerfiyWeatherInYourCityDetails() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.verifyCityWeatherDetails("WeatherInYourCity", test);
		test.info("Verified weather in your city line successfully");
	}

	@Test(priority = 5)
	public void testVerfiyInvalidCityWeatherDetails() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.searchAndVerifyInvalidCity("InvalidCity", test);
		test.info("Verified Invalid city alert successfully");
	}

	@Test(priority = 6)
	public void testVerfiyRedirectionToCityPage() throws IOException {
		ExtentTest test = ExtentReport.getTest();
		weath.verifyRedirectionToCityPage("WeatherInYourCity", test);
		test.info("verified Redirection successfully");
	}

	@AfterTest
	public void tearDown() {
		DriverManager.quitDriver();
	}
}