package com.automator.setup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.automator.utils.Application;
import com.automator.utils.DataLoader;
import com.automator.utils.PropManager;
import com.automator.utils.VerificationListener;
import com.automator.web.resources.ReportController;

@Listeners(VerificationListener.class)
public class TestSetup {

	private HashMap<String, String> TestDataCollection = null;
	public static ReportController repControl;
	
	/**
	 * Run before every TestNG test
	 * 
	 * @param testName
	 *            - ITestContext Object
	 */
	@BeforeTest
	public void setup(ITestContext testName) {

		try {			
			repControl = new ReportController();			
			// switch selenium logging off
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
			// Start Report Engine
			repControl.getReporter().startSuite(PropManager.getProperty("ReportName"), testName.getCurrentXmlTest().getThreadCount());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Runs before @Test execution
	 * 
	 * @param method
	 *            - Reference to the method to be executed
	 * @param testArgs
	 *            - Arguments to the test method
	 * @throws IOException
	 */
	@BeforeMethod
	public void startIteration(Method method, Object[] testArgs) throws IOException {

		TestSetup.repControl.getReporter().initializeSoftAssert();
		
		// load test data in to testdata collection hashmap
		loadTestData(testArgs[1]);

		// Start Test Report
		repControl.getReporter().startNewTest(method.getName());

		// start the driver as per the browser specified
		DriverFactory.initializeDriver(PropManager.getProperty("BrowserName"));
	}

	/**
	 * Runs after @Test execution
	 * 
	 * @param currentResult
	 *            - Results if current execution
	 */
	@AfterMethod(alwaysRun = true)
	public void endIteration(ITestResult currentResult) {

		// collect all asser results
		// End Test
		repControl.getReporter().endTest(currentResult.getMethod().getMethodName());
		
		// Close the browser and server
		DriverFactory.closeWebDriver();
	}

	/**
	 * Data provider for TestNg TESTS
	 * 
	 * @return - 2D Array
	 */
	@DataProvider(name = "iterator")
	public Object[][] createData(Method m) {
		// read excel data for the current test
		return new DataLoader(m.getName(), getClassName(m), getApplication(m)).readExcel();
	}

	private String getClassName(Method m) {
		String className = m.getDeclaringClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		return className;
	}

	private String getApplication(Method m) {
		String application = "";

		try {
			Application app = m.getAnnotation(Application.class);
			application = app.value();
		} catch (Exception me) {
			try {
				Application app = m.getDeclaringClass().getAnnotation(Application.class);
				application = app.value();
			} catch (Exception ce) {
				application = "";
			}
		}

		if (application.isEmpty()) {
			application = m.getDeclaringClass().getPackage().getName();
			if (application.contains("apps.")) {
				application = application.substring(application.indexOf("apps.") + 5);
				application = application.substring(0, application.indexOf("."));
			}
		}

		return application;
	}

	/**
	 * Loads the test argument array in to TestDataCollection map
	 * 
	 * @param testData
	 *            - Test Data Array
	 */
	public void loadTestData(Object testData) {

		//initilize the test data collection hashmap
		TestDataCollection = new HashMap<String, String>();
		
		Object[][] testDataArray = (Object[][]) testData;
		for (Object[] dataRow : testDataArray) {
			if (dataRow != null) {
				// if the specified data column already exists
				if (TestDataCollection.containsKey(dataRow[0].toString())) {
					// replace the data if the input is not blank
					if (!dataRow[1].toString().trim().equals("")) {
						boolean append = false;
						String finalData = TestDataCollection.get(dataRow[0].toString()).toString();
						String addData = dataRow[1].toString();
						// check if a duplicate ref data call is already made.
						// If yes then the current data should be appended
						if (finalData.contains("%callkey%")) {
							append = true;
						} else {
							// check if the current call is a duplicate ref
							// data.
							// If yes then the current data should be appended
							if (addData.contains("%callkey%")) {
								append = true;
							}
						}

						if (append) {
							// append data to previous data
							finalData += "%sep%" + addData;
						} else {
							// overwrite previous data
							finalData = addData;
						}
						TestDataCollection.put(dataRow[0].toString(), finalData);
					}
				} else {
					// add the new key
					TestDataCollection.put(dataRow[0].toString(), dataRow[1].toString());
				}
			}
		}
	}

	/**
	 * Retrieve the particular test data from TestDataCollection hashmap
	 * 
	 * @param identifier
	 * @return
	 */
	public String use(String identifier) {
		return use(identifier, null);
	}

	/**
	 * Retrieve the particular test data from TestDataCollection hashmap
	 * 
	 * @param identifier
	 * @param callKey
	 *            - Duplicate call key for Ref data
	 * @return
	 */
	public String use(String identifier, String callKey) {
		String retVal = "";
		if (TestDataCollection.containsKey(identifier)) {
			// check if the data fetched contains multiple items
			if (TestDataCollection.get(identifier).toString().contains("%sep%")) {
				// split the multiple items
				String[] testDataArray = TestDataCollection.get(identifier).toString().split("%sep%");
				for (String tesData : testDataArray) {
					// find the data that matches the specified call key
					if (callKey == null) {
						if (!tesData.contains("%callkey%")) {
							retVal = tesData;
							break;
						}
					} else {
						if (tesData.contains("%callkey%")) {
							String[] keyArray = tesData.split("%callkey%");
							if (keyArray[0].equalsIgnoreCase(callKey)) {
								if (keyArray.length > 1) {
									retVal = keyArray[1];
								} else {
									retVal = "";
								}
								break;
							}

						}
					}
				}
				if (retVal.equals("")) {
					retVal = "<Data Not Found>";
				}
			} else {
				// if multiple data doesn't exist then return the data with
				// specified key
				retVal = TestDataCollection.get(identifier).toString();
			}
		} else {
			retVal = "<Data Not Found>";
		}

		if (retVal.contains("%callkey%")) {
			retVal = "";
		}
		return retVal;
	}

	/**
	 * Executes after @Test
	 * 
	 * @throws IOException
	 */
	@AfterTest(alwaysRun = true)
	public void destroy() throws IOException {
		repControl.getReporter().endSuite();
		DriverFactory.disposeAllWebDrivers();
	}

	
	
}
