package com.assessment.pages;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
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
		String celsiusTemp = rest.kelvinToCelsius(kelvinTemp);
		test.info("City-> "+cityParam);
		test.info("Weather Description-> " + desc +"\nTemperature-> " + celsiusTemp + " degree's");
		test.info("Temperature-> "+celsiusTemp);
	}

	public void getForeCast(String function, ExtentTest test) throws IOException, ParseException {
		String cords = excel.getCityNameFromExcel(function);
		TreeMap<String, String> forecastMap;
		forecastMap = rest.getfiveDayForeCast(cords);
		test.info("City-> "+function.split("Cords")[0]);
		for (Entry<String, String> entry : forecastMap.entrySet()) {
            test.info(entry.getKey()+"="+entry.getValue());
        }
	}

	public void invalidCityName(String function, ExtentTest test) throws IOException, ParseException {
		rest.invalidCityName(function, test);
	}

	public void invalidapiKey(ExtentTest test) throws IOException, ParseException {
		rest.invalidApiKey(test);
	}

}
