package com.assessment.utils;

import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.HashMap;
import org.json.simple.parser.ParseException;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RestAssuredBuilder {
	final String credentials = System.getProperty("user.dir") + "/src/test/resources/testData/credentials.json";
	String currentWeatherURI = "https://api.openweathermap.org/data/2.5/weather?q={city}&APPID=482ae00bb50ebc6e2c1a8f00ec417175";
	String forecasteURI = "https://api.openweathermap.org/data/2.5/forecast?{cords}&APPID=482ae00bb50ebc6e2c1a8f00ec417175";
	String forecasteURIInvalidKey = "https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=12345";

	public Response getCurrentWeather(String city) throws IOException, ParseException {
		String updatedApiUri = currentWeatherURI.replace("{city}", city);
		Response response = RestAssured.get(updatedApiUri);
		System.out.println("Response Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 200;
		return response;
	}

	public Response invalidCityName(String city, ExtentTest test) throws IOException, ParseException {
		String updatedApiUri = currentWeatherURI.replace("{city}", city);
		Response response = RestAssured.get(updatedApiUri);
		test.info("Response Status Code: " + response.getStatusCode());
		test.info("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 404;
		String str = response.getBody().asString();
		assertTrue(str.contains("city not found"));
		return response;
	}

	public Response invalidApiKey(ExtentTest test) throws IOException, ParseException {
		Response response = RestAssured.get(forecasteURIInvalidKey);
		test.info("Response Status Code: " + response.getStatusCode());
		test.info("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 401;
		String str = response.getBody().asString();
		assertTrue(str.contains("Invalid API key"));
		return response;
	}

	public HashMap<String, String> getfiveDayForeCast(String cords) throws IOException, ParseException {
		String updatedApiUri = forecasteURI.replace("{cords}", cords);
		Response response = RestAssured.get(updatedApiUri);
		System.out.println("Response Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 200;
		HashMap<String, String> map = new HashMap<String, String>();
		int i = 0;
		int j = 0;
		while (j < 5) {
			String actualValue = JsonPath.from(response.body().asString()).get("list[" + i + "].dt_txt").toString();
			String day = actualValue.substring(8, 10);
			if (!map.containsKey(day)) {
				String weather = JsonPath.from(response.body().asString()).get("list[" + i + "].weather[0].main");
				map.put(day, weather);
				j++;
			}
			i++;
		}
		return map;
	}

	public String fetchJpathValue(Response response, String path) {
		JsonPath jsonPath = response.jsonPath();
		return jsonPath.getString(path);
	}

}
