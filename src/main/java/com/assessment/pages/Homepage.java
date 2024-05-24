package com.assessment.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assessment.utils.BaseUtility;
import com.assessment.utils.DriverManager;
import com.assessment.utils.ExcelReader;
import com.aventstack.extentreports.ExtentTest;

public class Homepage {
	ExcelReader excelReader = new ExcelReader();
	WebDriver driver;
	WebDriverWait wait;
	BaseUtility base;

	@FindBy(xpath = "//input[@placeholder='Search city']")
	WebElement searchBar;
	@FindBy(xpath = "//button[@type = 'submit']")
	WebElement searchButton;
	@FindBy(id = "weather-info-id")
	WebElement weatherInfo;
	@FindBy(xpath = "//h2[@style]")
	WebElement cityHeading;
	@FindBy(xpath = "//button[contains(text(),'Accept')]")
	WebElement acceptBtn;
	@FindBy(xpath = "//span[@class='heading']")
	WebElement temperature;
	@FindBy(xpath = "//div[contains(text(),'Imperial: °F')]")
	WebElement fahrenheit;
	@FindBy(xpath = "//div[@class='widget-notification']")
	WebElement invalidCityPopUp;

	public Homepage(WebDriver driver) {
		this.driver = driver;
		this.wait = DriverManager.getWait();
		PageFactory.initElements(driver, this);
		base = new BaseUtility(driver, wait);
	}

	private void search(String city, ExtentTest test) {
		searchBar.clear();
		searchBar.sendKeys(city);
		base.click(searchButton);
		test.info("Search Button Clicked");
	}

	public void searchAndVerifyCity(String function, ExtentTest test) throws IOException {
		String city = excelReader.getCityNameFromExcel(function);
		search(city, test);
		By searchBox = By.xpath("//span[contains(text(),'" + city + "')]");
		base.click(searchBox);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'" + city + "')]")));
		String actualCity = base.getText(cityHeading);
		assertEquals(actualCity, city, "City is not Displayed");
		test.info(city + "Successfully verified on UI");
	}

	public void searchAndVerifyInvalidCity(String function, ExtentTest test) throws IOException {
		String city = excelReader.getCityNameFromExcel(function);
		search(city, test);
		wait.until(ExpectedConditions.visibilityOf(invalidCityPopUp));
		assertEquals(base.getText(invalidCityPopUp), "No results for " + city, "Error pop up not displayed");
		test.info(city + "PopUp verified on UI");
	}

	public void verifyWeatherDetails(ExtentTest test) throws IOException {
		HashMap<String, String> weatherMap = new HashMap<String, String>();
		weatherMap = excelReader.getWeatherDetailsFromExcel();
		isWithinRange(weatherMap, test);
	}

	public void isWithinRange(HashMap<String, String> weatherMap, ExtentTest test) {
		for (HashMap.Entry<String, String> entry : weatherMap.entrySet()) {
			String key = entry.getKey();
			String valueRange = entry.getValue();
			String actualText = "";
			switch (key) {
			case "CelsiusTemp":
				actualText = base.getText(temperature);
				break;
			case "FahrenheitTemp":
				base.click(fahrenheit);
				wait.until(ExpectedConditions.attributeContains(temperature, "innerText", "F"));
				actualText = base.getText(temperature);
				break;
			case "Visibility":
			case "Humidity":
			case "UV":
				actualText = driver.findElement(By.xpath("//span[contains(text(),'" + key + "')]/parent::li"))
						.getText();
				break;
			default:
				continue;
			}
			String[] rangeValues = valueRange.split(" to ");
			double minValue = Double.parseDouble(rangeValues[0].trim());
			double maxValue = Double.parseDouble(rangeValues[1].trim());
			double value = Double.parseDouble(actualText.replaceAll("[^\\d.]", ""));

			assertTrue(value >= minValue && value <= maxValue,
					key + "-> " + value + " is not within range -> " + minValue + " to " + maxValue);
			System.out.println(key + "-> " + value + " is within range -> " + minValue + " to " + maxValue);
			test.info(key + "-> " + value + " is within range -> " + minValue + " to " + maxValue);
		}
	}

	public void acceptCookies() {
		boolean blnAcceptBtn = base.checkIfElementIsLocated(acceptBtn);
		if (blnAcceptBtn) {
			acceptBtn.click();
		}
	}
}