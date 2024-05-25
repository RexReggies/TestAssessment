package com.assessment.tests;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import com.assessment.pages.ApiPage;
import com.assessment.utils.ExtentReport;
import com.aventstack.extentreports.ExtentTest;

public class TestApi {
	ApiPage apipage = new ApiPage();

	@Test
	public void getCurrentWeather() throws IOException, ParseException {
		ExtentTest test = ExtentReport.getTest();
		apipage.getCityCurrentTemp("CurrentWeatherApi", test);
	}

	@Test
	public void getFiveDayForecast() throws IOException, ParseException {
		ExtentTest test = ExtentReport.getTest();
		apipage.getForeCast("MarzoliCords", test);
	}

	@Test
	public void invalidCityName() throws IOException, ParseException {
		ExtentTest test = ExtentReport.getTest();
		apipage.invalidCityName("InvalidCity", test);
	}

	@Test
	public void invalidapiKey() throws IOException, ParseException {
		ExtentTest test = ExtentReport.getTest();
		apipage.invalidapiKey(test);
	}
}
