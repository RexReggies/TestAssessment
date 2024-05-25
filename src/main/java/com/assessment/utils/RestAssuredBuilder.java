package com.assessment.utils;

import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RestAssuredBuilder {
	ExcelReader excel = new ExcelReader();
	String appId = "";
	String baseURI = "https://api.openweathermap.org/data/2.5/";
	String currentWeatherURI = baseURI + "weather?q={city}";
	String forecasteURI = baseURI + "forecast?{cords}";
	String forecasteURIInvalidKey = baseURI + "weather?q=London,uk";

	public RestAssuredBuilder() {
		try {
			appId = excel.getApiKeyFromExcel("key");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Response getCurrentWeather(String city) throws IOException, ParseException {
		String updatedApiUri = currentWeatherURI.replace("{city}", city);
		Response response = RestAssured.given().queryParam("APPID", appId).get(updatedApiUri);
		assert response.getStatusCode() == 200;
		return response;
	}

	public Response invalidCityName(String city, ExtentTest test) throws IOException, ParseException {
		String updatedApiUri = currentWeatherURI.replace("{city}", city);
		Response response = RestAssured.given().queryParam("APPID", appId).get(updatedApiUri);
		test.info("Response Status Code: " + response.getStatusCode());
		test.info("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 404;
		String str = response.getBody().asString();
		assertTrue(str.contains("city not found"));
		return response;
	}

	public Response invalidApiKey(ExtentTest test) throws IOException, ParseException {
		Response response = RestAssured.given().queryParam("APPID", appId+"12345").get(forecasteURIInvalidKey);
		test.info("Response Status Code: " + response.getStatusCode());
		test.info("Response Body: " + response.getBody().asString());
		assert response.getStatusCode() == 401;
		String str = response.getBody().asString();
		assertTrue(str.contains("Invalid API key"));
		return response;
	}

	public TreeMap<String, String> getfiveDayForeCast(String cords) throws IOException, ParseException {
		String updatedApiUri = forecasteURI.replace("{cords}", cords);
		Response response = RestAssured.given().queryParam("APPID", appId).get(updatedApiUri);
		assert response.getStatusCode() == 200;
		TreeMap<String, String> map = new TreeMap<>();
		int i = 0;
		int j = 0;
		while (j < 5) {
			String actualValue = JsonPath.from(response.body().asString()).get("list[" + i + "].dt_txt").toString();
			String day = actualValue.substring(8, 10);
			if (!map.containsKey("Day " + day)) {
				String weather = JsonPath.from(response.body().asString()).get("list[" + i + "].weather[0].main");
				System.out.println("Response Body: " + response.getBody().asString());
				String strTemp = JsonPath.from(response.body().asString()).get("list[" + i + "].main.temp").toString();
				double temp = Double.parseDouble(strTemp);

				String humidity = JsonPath.from(response.body().asString()).get("list[" + i + "].main.humidity")
						.toString();
				map.put("Day " + day, " Weather-> " + weather + ", Temperature-> " + kelvinToCelsius(temp)
						+ ", Humidity-> " + humidity);
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

	public String kelvinToCelsius(double kelvin) {
		DecimalFormat df = new DecimalFormat("#.00");
		double celsius = kelvin - 273.15;
		return df.format(celsius);
	}
}