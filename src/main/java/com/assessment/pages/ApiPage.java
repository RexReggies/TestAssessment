package com.assessment.pages;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

import com.assessment.utils.ExcelReader;
import com.assessment.utils.RestAssuredBuilder;
import com.aventstack.extentreports.ExtentTest;

import io.restassured.response.Response;

public class ApiPage {

	RestAssuredBuilder rest = new RestAssuredBuilder();
	ExcelReader excel = new ExcelReader();

	public void getCityCurrentTemp(String function, ExtentTest test) throws IOException, ParseException {
		String cityParam = excel.getCityNameFromExcel(function);
		Response response = rest.getCurrentWeather(cityParam);
		String desc = rest.fetchJpathValue(response, "weather[0].description");
		String temp = rest.fetchJpathValue(response, "main.temp");
		Double kelvinTemp = Double.parseDouble(temp);
		Double celsiusTemp = kelvinToCelsius(kelvinTemp);
		DecimalFormat df = new DecimalFormat("#.00");
		test.info("Weather Description-> " + desc);
		test.info("Temperature-> " + df.format(celsiusTemp) + " degree's");
	}

	private double kelvinToCelsius(double kelvin) {
		return kelvin - 273.15;
	}

	public void getForeCast(String function, ExtentTest test) throws IOException, ParseException {
		String cords = excel.getCityNameFromExcel(function);
		HashMap<String, String> forecastMap;
		forecastMap = rest.getfiveDayForeCast(cords);
		test.info(forecastMap.toString());
	}

	public void invalidCityName(String function, ExtentTest test) throws IOException, ParseException {
		rest.invalidCityName(function, test);
	}

	public void invalidapiKey(ExtentTest test) throws IOException, ParseException {
		rest.invalidApiKey(test);
	}

}
