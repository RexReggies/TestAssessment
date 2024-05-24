# TestAssessment
- Tech Stack used in Project :- Maven, Selenium, Java, Xlsx(using Apache),RestAssured, TestNg and ExtentReport
- With the help of Maven the project will download dependencies on its own but make sure to install TestNg  for more control.
- src/main/java has utils and page classes.
- src/test/java has all Testng classes.
- Extent Reports are configured at listener levels which will be available after run in "Test/extent-reports.html"
- "weatherTestng.xml" can be right-clicked and run as testng suite, this will execute 3 test classes parallely in 3 threads or you can execute from terminal using "mvn -DsuiteXmlFile=weatherTestng.xml test".
- "weatherTestngApi.xml" can be right-clicked and run as testng suite, this will execute all the API test classes or you can execute from terminal using "mvn -DsuiteXmlFile=weatherTestngApi.xml test".
- City-Data and a few of the attributes are fetched from the XLSX file which is stored in "testData.xlsx"("src/test/resources/testData/testData.xlsx")
