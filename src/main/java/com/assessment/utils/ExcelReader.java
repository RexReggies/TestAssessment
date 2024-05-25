package com.assessment.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	public static final String directoryLoc = System.getProperty("user.dir")
			+ "/src/test/resources/testData/testData.xlsx";

	@SuppressWarnings("resource")
	public XSSFSheet readFromExcelSheet(String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(directoryLoc);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		return sheet;
	}

	public HashMap<String, String> filterRowsOnFunctions(XSSFSheet sheetObj, String function) {
		HashMap<String, String> dataMap = new LinkedHashMap<>();
		Iterator<Row> rows = sheetObj.rowIterator();
		Row headerRow = rows.next();
		while (rows.hasNext()) {
			Iterator<Cell> cells = rows.next().cellIterator();
			Cell identifier = cells.next();

			if (identifier.getStringCellValue().equals(function)) {
				Iterator<Cell> headerCell = headerRow.cellIterator();
				headerCell.next();
				while (cells.hasNext()) {
					dataMap.put(headerCell.next().getStringCellValue(), cells.next().getStringCellValue().trim());
				}
				break;
			}
		}
		return dataMap;
	}

	public HashMap<String, String> fetchTestData(String sheet, String function) throws IOException {
		XSSFSheet sheetObj = readFromExcelSheet(sheet);
		return filterRowsOnFunctions(sheetObj, function);
	}

	public String getCityNameFromExcel(String function) throws IOException {
		return fetchTestData("CityData", function).get("CityName").toString();
	}
	
	public String getApiKeyFromExcel(String function) throws IOException {
		return fetchTestData("APIData", function).get("AppId").toString();
	}
	
	public HashMap<String, String> getWeatherDetailsFromExcel() throws IOException {
		return fetchTestData("CityData", "VerifyWeatherDetails");
	}
}
