package com.assessment.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

	public class Maps {
		ExcelReader excelReader = new ExcelReader();
	    WebDriver driver;
	    WebDriverWait wait;
	    BaseUtility base;

	    @FindBy(xpath = "//a[text()='Maps']") WebElement searchBar;
	    @FindBy(xpath = "//button[contains(text(),'Accept')]") WebElement acceptBtn;
	    @FindBy(xpath = "//img[@class='leaflet-tile leaflet-tile-loaded']") List<WebElement> layerImg;
	    @FindBy(xpath = "//a[@title='Nominatim Search']") WebElement searchBtn;
	    @FindBy(xpath = "//input[@style and not(@type)]") WebElement inputBox;
	  
	   

	    public Maps(WebDriver driver) {
	        this.driver = driver;
	        this.wait = DriverManager.getWait();
	        PageFactory.initElements(driver, this);
	        base = new BaseUtility(driver,wait);
	    }

	    public void navigateToMaps() {
	        base.click(searchBar);
	    }
	    
	    public void searchInMaps() {
	    	base.click(searchBtn);
	    	inputBox.sendKeys("Bengaluru"+Keys.ENTER);
	    }
	    
		public void verifyLayers(ExtentTest test) {
			List<String> stringList = new ArrayList<>();
			stringList.add("Temperature");
			stringList.add("Pressure");
			stringList.add("Wind speed");
			stringList.add("Clouds");
			stringList.add("Global Precipitation");
			String src = "";
			boolean flag = false;
			for (String str : stringList) {
				base.click(By.xpath("//span[contains(text(),'" + str + "')]"));
				if (str.equals("Temperature")) {
					src = "sat.owm.io/vane/2.0/weather/TA2";
				} else if (str.equals("Pressure")) {
					src = "sat.owm.io/vane/2.0/weather/APM";
				} else if (str.equals("Wind speed")) {
					src = "sat.owm.io/vane/2.0/weather/WS";
				} else if (str.equals("Clouds")) {
					src = "sat.owm.io/vane/2.0/weather/CL";
				} else if (str.equals("Global Precipitation")) {
					src = "sat.owm.io/maps/2.0/radar";
				}
				wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[contains(@src,'" + src + "')]")));
				flag = base
						.checkIfElementIsLocated(driver.findElement(By.xpath("//img[contains(@src,'" + src + "')]")));
				assertTrue(flag, str + " layer not verified");
				test.info("verified Layer->"+str);
			}
		}
	    
	    public void acceptCookies() {
	    	boolean blnAcceptBtn = base.checkIfElementIsLocated(acceptBtn);
	    	if(blnAcceptBtn) {
	    		acceptBtn.click();
	    	}
	    }
	}
