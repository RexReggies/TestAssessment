package com.assessment.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.IOException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assessment.utils.BaseUtility;
import com.assessment.utils.DriverManager;
import com.assessment.utils.ExcelReader;
import com.aventstack.extentreports.ExtentTest;


	public class WeatherInCity{
		ExcelReader excelReader = new ExcelReader();
	    WebDriver driver;
	    WebDriverWait wait;
	    BaseUtility base;

	    @FindBy(xpath = "//input[@placeholder='Weather in your city']") WebElement weatherCitySearchBar;
	    @FindBy(id = "search_str") WebElement searchBox;
	    @FindBy(xpath="//h2[contains(@class,'headline')]") WebElement headline;
	    @FindBy(xpath = "//button[@type = 'submit']") WebElement searchButton;
	    @FindBy(xpath = "//a[contains(@href,'city')]") WebElement cityName;
	    @FindBy(xpath = "//p[contains(text(),'temperature from')]") WebElement tempParagraph;
	    @FindBy(xpath = "//p[contains(text(),'Geo coords')]") WebElement geoCords;
	    @FindBy(xpath="//h2[@style]") WebElement cityHeading;
	    @FindBy(xpath = "//button[contains(text(),'Accept')]") WebElement acceptBtn;
	    @FindBy(xpath = "//div[contains(@class,'alert')]") WebElement alert;

	    public WeatherInCity(WebDriver driver) {
	        this.driver = driver;
	        this.wait = DriverManager.getWait();
	        PageFactory.initElements(driver, this);
	        base = new BaseUtility(driver,wait);
	    }

	    public void searchWeatherInYourCity(String function) throws IOException {
	    	String city = excelReader.getCityNameFromExcel(function);
	    	weatherCitySearchBar.sendKeys(city+Keys.ENTER);
	    	search(city);
	    }
	    
	    private void search(String city) {
	    	searchBox.clear();
	        searchBox.sendKeys(city);
	    	base.click(searchButton);
	    }
	    
	    public void verifyHeadline(ExtentTest test) {
	    	assertEquals("Weather in your city",base.getText(headline),"Headline not matched");
	    	test.info("Verified HeadLine-> "+base.getText(headline) );
	    }
	    
	    public void verifySearchBoxValue(String function) throws IOException {
	    	assertEquals(searchBox.getAttribute("value"),excelReader.getCityNameFromExcel(function),"Searchbox has different city");
	    }
	    
	    public void searchAndVerifyInvalidCity(String function, ExtentTest test) throws IOException {
	    	String city = excelReader.getCityNameFromExcel(function);
	    	search(city);
	    	System.out.println(base.getText(alert));
	    	assertTrue((base.getText(alert).contains("Not found")),"Alert box not found");
	    	test.info("alertBoxVerified");
	    }
	    
	    public void verifyCityWeatherDetails(String function,ExtentTest test) throws IOException {
	    	assertEquals(base.getText(cityName),excelReader.getCityNameFromExcel(function),"City Name doesn't matched with UI");
	    	test.info("City name machex -> "+base.getText(cityName));
	    	assertTrue(base.checkIfElementIsLocated(tempParagraph),"Temperature line not found on UI");
	    	test.info("Temperature section verified on UI");
	    	assertTrue(base.checkIfElementIsLocated(geoCords),"Geo Location not found on UI");
	    	test.info("Geo Cords Verified on UI");
	    }
	    
	    public void verifyRedirectionToCityPage(String function,ExtentTest test) throws IOException {
	    	String city = excelReader.getCityNameFromExcel(function);
	    	search(city);
	    	base.click(cityName);
	    	assertEquals(base.getText(cityHeading),excelReader.getCityNameFromExcel(function),"City is not Displayed");
	    	test.info("Redirection verfied -> "+base.getText(cityHeading));
	    }
	    
	    public void acceptCookies() {
	    	boolean blnAcceptBtn = base.checkIfElementIsLocated(acceptBtn);
	    	if(blnAcceptBtn) {
	    		acceptBtn.click();
	    	}
	    }
	    
	}
