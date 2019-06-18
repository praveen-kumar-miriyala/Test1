package com.automator.web.resources;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import atu.webdriver.utils.table.WebTable;
import com.automator.setup.DriverFactory;
import com.automator.setup.TestSetup;
import com.automator.utils.PropManager;

import org.openqa.selenium.support.ui.Select;

public class SeleniumActions {

	WebDriver driver;
	WebElement element;

	// Constructor
	public SeleniumActions() {
		// set the specified driver from driver factory as the action driver
		this.driver = DriverFactory.getWebDriver();
		if (driver != null) {

			driver.manage().timeouts().implicitlyWait(PropManager.getInteger("Implicit_Wait", 5), TimeUnit.SECONDS);
		}
	}

	/**
	 * Opens a Page URL
	 * 
	 * @param URL
	 *            - URL of the page to open
	 * @return
	 */
	public SeleniumActions OpenURl(String URL) {
		driver.navigate().to(URL);
		TestSetup.repControl.getReporter().ActionLog("Navigated to URL [" + URL + "]");
		return this;
	}

	/**
	 * Set wait time on the WebDriver
	 * 
	 * @param time
	 *            - Time in seconds
	 * @return
	 */
	public SeleniumActions driverwait(long time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		return this;
	}

	/**
	 * Wait for the specified tim equating to page laod wait time specified in
	 * config
	 * 
	 * @return
	 */
	public SeleniumActions Waitforpageload() {
		driverwait(PropManager.getInteger("Pageload_Wait", 5));
		return this;

	}

	/**
	 * Select value from dropdown
	 * 
	 * @param locator
	 *            - Locator of the dropdown
	 * @param selecttext
	 *            - Text to be selected in the dropdown
	 * @return
	 */
	public SeleniumActions Select(ObjectLocator locator, String selecttext) {

		Select cmbSelect = new Select(FindElement(locator));
		cmbSelect.selectByValue(selecttext);
		TestSetup.repControl.getReporter().ActionLog("Selected text [" + selecttext + "] in combo box [" + locator.locatorDescription + "]");
		return this;
	}

	/**
	 * Click on any locator
	 * 
	 * @param locator
	 *            - Locator of the element to be clicked
	 * @return
	 */
	public SeleniumActions Click(ObjectLocator locator) {

		FindElement(locator).click();
		TestSetup.repControl.getReporter().ActionLog("Clicked on [" + locator.locatorDescription + "]");
		System.out.println("Clicked On " + locator.locatorDescription);
		return this;
	}

	/**
	 * Click on any locator and scroll
	 * 
	 * @param locator
	 *            - Locator of the element to be clicked
	 * @return
	 */
	public SeleniumActions Click(ObjectLocator locator, boolean scroll) {
		for (int i = 0; i < PropManager.getInteger("autoScrolls", 1); i++) {
			try {
				Click(locator);
			} catch (org.openqa.selenium.ElementNotVisibleException ex) {
				Scroll(0, PropManager.getInteger("autoScrollPixel", 1));
			}
		}
		return this;
	}

	public SeleniumActions Scroll(long x, long y) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + x + "," + y + ")", "");
		return this;
	}

	/**
	 * Verify if an element is present on the page currently displayed
	 * 
	 * @param locator
	 *            - Locator of the element to be verified
	 * @return
	 */
	public SeleniumActions verifyElementPresent(ObjectLocator locator) {

		// wait as per the specified time in seconds for control load on the
		// page
		driverwait(Integer.parseInt(PropManager.getProperty("Control_Wait")));
		if (IsElementPresent(locator)) {
			TestSetup.repControl.getReporter().ActionLog("Element [" + locator.locatorDescription + "] found successfully");

		} else {
			TestSetup.repControl.getReporter().AssertFail("Element [" + locator.locatorDescription + "] not present on the page");
		}
		return this;
	}

	/**
	 * Verify id element is displayed on the page
	 * 
	 * @param locator
	 *            - Locator of the element to be verified
	 * @return
	 */
	public boolean IsElementPresent(ObjectLocator locator) {

		WebElement checkElement = FindElement(locator);
		if (checkElement != null) {
			if (checkElement.isDisplayed()) {
				TestSetup.repControl.getReporter().ActionLog("Element [" + locator.locatorDescription + "] is displayed");
				return true;
			} else {
				// ActionLog("Element [" + locator.locatorDescription + "] is
				// not displayed");
				return false;

			}
		} else {
			return false;
		}

	}

	/**
	 * Check if the element is enabled
	 * 
	 * @param locator
	 *            - Locator of the element to be verified
	 * @return
	 */
	public boolean IsElementEnabled(ObjectLocator locator) {

		WebElement checkElement = FindElement(locator);

		if (checkElement != null) {
			if ((FindElement(locator).isEnabled())) {
				TestSetup.repControl.getReporter().ActionLog("Element [" + element.getText() + "] is enabled");
				return true;
			} else {
				TestSetup.repControl.getReporter().ActionLog("Element [" + element.getText() + "] is not enabled");
				return false;

			}
		} else {
			return false;
		}

	}

	/**
	 * Forms the webelement using the description provided
	 * 
	 * @param locator
	 *            - Descriptor of the web element to be formed
	 * @return
	 */
	public WebElement FindElement(ObjectLocator locator) {

		WebElement retElement = null;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			retElement = driver.findElement(locator.Locator);

			if (driver.findElement(locator.Locator) != null) {
				TestSetup.repControl.getReporter().ActionLog(locator.locatorDescription + " Found Successfully");
			}
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			// Handle exception if the element is not found
			TestSetup.repControl.getReporter().AssertFail("NoSuchElementException: The Object " + locator.locatorDescription + " not found! "
					+ ex.getMessage());
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			// exception occurs when page is still loading. Wait for page load
			// and check again
			Waitforpageload();
			retElement = driver.findElement(locator.Locator);
		} catch (ElementNotVisibleException ex) {
			// Handle exception if the element is not found
			TestSetup.repControl.getReporter().AssertFail("ElementNotVisibleException: The Object " + locator.locatorDescription + " not found! ");
		} catch (WebDriverException ex) {
			// Handle exception if the element is not found
			TestSetup.repControl.getReporter().AssertFail("NoSuchElementException: The Object " + locator.locatorDescription + " not found! "
					+ ex.getMessage());
		}
		return retElement;
	}

	/**
	 * Enter text in to an element
	 * 
	 * @param locator
	 *            - Locator of the element to enter text on
	 * @param text
	 *            = Text to be entered
	 * @return
	 */
	public SeleniumActions EnterText(ObjectLocator locator, String text) {
		driverwait(Integer.parseInt(PropManager.getProperty("Control_Wait")));
		WebElement txtElement = FindElement(locator);
		txtElement.clear();
		txtElement.sendKeys(text);
		TestSetup.repControl.getReporter().ActionLog("Entered text [" + text + "] in text box [" + locator.locatorDescription + "]");
		return this;
	}

	/**
	 * gets all the child elements under the specified object
	 * 
	 * @param locator
	 *            - Locator of the parent
	 * @return
	 */
	public List<WebElement> GetAllElements(ObjectLocator locator) {

		List<WebElement> elements = driver.findElements(locator.Locator);
		return elements;

	}

	/**
	 * Get displayed text from the element
	 * 
	 * @param locator
	 *            - Locator of the element
	 * @return
	 */
	public String GetText(ObjectLocator locator) {

		String value = null;
		value = FindElement(locator).getText();
		return value;
	}

	/**
	 * waits for webelement to be present and clickable on the screen
	 * 
	 * @param locator
	 *            - Locator of the webelement
	 * @return
	 */
	public SeleniumActions WaitForWebElement(ObjectLocator locator) {

		element = (new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOfElementLocated(locator.Locator)));
		return this;
	}

	/**
	 * Verifies that a particular text is present in the object
	 * 
	 * @param locator
	 *            - Locator of the object to be verified
	 * @param expectedText
	 *            - Text to be verified
	 */
	public SeleniumActions verifyTextInObject(ObjectLocator locator, String expectedText) {
		String locDesc = locator.locatorDescription;
		String tagName = FindElement(locator).getTagName();
		String actualText = "";
		// if control is a combo box
		if (tagName.toLowerCase().equals("select")) {
			Select cmbSelect = new Select(FindElement(locator));
			actualText = cmbSelect.getFirstSelectedOption().getText();

			if (cmbSelect.getFirstSelectedOption().getText().equals(expectedText)) {
				System.out.println(expectedText + "is same as the text displayed");
			}
		}
		// if control is a text box
		else if (tagName.toLowerCase().equals("input")) {
			actualText = GetAttribute(locator);
		} else {
			actualText = GetText(locator);
		}
		if (actualText.equals(expectedText)) {
			TestSetup.repControl.getReporter().ActionLog("Expected text [" + expectedText + "] is matching [" + actualText + "] for control ["
					+ locDesc + "]");
		} else {
			TestSetup.repControl.getReporter().AssertFail("Expected text [" + expectedText + "] is not matching [" + actualText + "] for control ["
					+ locDesc + "]");
		}
		return this;
	}

	/**
	 * Verifies the header cells of the table for its value
	 * 
	 * @param locator
	 *            - Object to be verified
	 * @param expectedText
	 *            - Expected HeaderText Array
	 * @param colIndex
	 *            - Index of the expected header texts in the table
	 */
	public void verifyTableHeaderCell(ObjectLocator locator, String[] expectedText, int[] colIndex) {
		WebElement verifyElement = FindElement(locator);
		WebTable table = WebTable.getTable(verifyElement);
		String controlName = locator.locatorDescription;
		String errMessage = "";
		int indexCounter = 0;
		for (int hdrIndexes : colIndex) {
			// String
			// actualText=table.getRow(0).getHeaderCellElement(hdrIndexes).getText();
			// System.out.println(actualText);
			String actualText = table.getRow(0).getHeaderCell(hdrIndexes).getText();
			// System.out.println(actualText);

			if (!actualText.equals(expectedText[indexCounter])) {
				errMessage += "Expected text [" + expectedText + "] is not matching [" + actualText
						+ "] in header col index [" + Integer.toString(hdrIndexes) + "]\n";
			}
			indexCounter++;
		}
		if (errMessage != "") {
			errMessage = "Failure in verifying [" + controlName + "]\n" + errMessage;
			TestSetup.repControl.getReporter().AssertFail(errMessage);
		}
	}

	/**
	 * Verfies the table row count
	 * 
	 * @param locator
	 *            - Object to be verified
	 * @param verifyType
	 *            - Exact > Exact row count, Exits > If row exists
	 * @param rowCount
	 *            - Row Count to be verified in case of exact
	 */
	public void verifyTableRowCount(ObjectLocator locator, String verifyType, int rowCount) {
		WebElement verifyElement = FindElement(locator);
		WebTable table = WebTable.getTable(verifyElement);
		String controlName = locator.locatorDescription;
		// if an exact match is required
		if (verifyType.equals("Exact")) {
			if (rowCount != table.getRowCount()) {
				TestSetup.repControl.getReporter().AssertFail(
						"Expected row count [" + Integer.toString(rowCount) + "] does not match actual row count ["
								+ Integer.toString(table.getRowCount()) + "] for table [" + controlName + "]");
			} else {
				TestSetup.repControl.getReporter().ActionLog("Expected row count [" + Integer.toString(rowCount) + "] matches actual row count ["
						+ Integer.toString(table.getRowCount()) + "] for table [" + controlName + "]");
			}
		}
		// check if rows exists
		else if (verifyType.equals("Exists")) {
			if (table.getRowCount() < 0) {
				TestSetup.repControl.getReporter().AssertFail("No rows exist for table [" + controlName + "]");
			} else {
				TestSetup.repControl.getReporter().ActionLog("Rows exist for table [" + controlName + "]");
			}
		}
	}

	/**
	 * Get attribute value for attribute 'value'
	 * 
	 * @param locator
	 *            - - Object from which the attribute should be parsed
	 * @return
	 */
	public String GetAttribute(ObjectLocator locator) {
		String value = null;
		value = FindElement(locator).getAttribute("value");
		return value;
	}

	/**
	 * Verify an attribute is not empty for given object
	 * 
	 * @param locator
	 *            - Objectlocator
	 * @return - true if not empty
	 */
	public boolean verifyNotEmpty(ObjectLocator locator) {

		String actualText = "";
		String tagName = FindElement(locator).getTagName();
		actualText = GetAttribute(locator);
		if (tagName.toLowerCase().equals("input")) {

			actualText = GetAttribute(locator);
		} else {
			actualText = GetText(locator);
		}

		if (!actualText.isEmpty()) {
			TestSetup.repControl.getReporter().ActionLog("Element [" + locator + "] is having [" + actualText + "]");
			return true;
		} else {
			TestSetup.repControl.getReporter().AssertFail("Element [" + locator + "] is empty");
			return false;
		}

	}

	/**
	 * select text from drop down list by visible text
	 * 
	 * @param locator
	 *            - Object locator of the dropdown
	 * @param string
	 *            - text to select
	 * @return -
	 */
	public SeleniumActions SelectByVisibleText(ObjectLocator locator, String selecttext) {

		Select cmbSelect = new Select(FindElement(locator));
		cmbSelect.selectByVisibleText(selecttext);
		TestSetup.repControl.getReporter().ActionLog(
				"Selected visible text [" + selecttext + "] in combo box [" + locator.locatorDescription + "]");
		return this;
	}

	/**
	 * Removing existing data in a textfiled
	 * 
	 * @param locator
	 *            - Object locator if the text bix
	 * @return -
	 */
	public SeleniumActions Clear(ObjectLocator locator) {
		FindElement(locator).clear();
		return this;
	}

	/**
	 * Verify if the text in object is matching a pattern
	 * 
	 * @param locator
	 *            - Locator of the object to verify
	 * @param pattern
	 *            - RegEx pattern
	 * @return - true if matching
	 */
	public SeleniumActions verifyTextPatternInObject(ObjectLocator locator, String pattern) {
		String locDesc = locator.locatorDescription;
		String tagName = FindElement(locator).getTagName();
		String actualText = "";
		Pattern r = Pattern.compile(pattern);

		if (tagName.toLowerCase().equals("input")) {

			actualText = GetAttribute(locator);
		} else {
			actualText = GetText(locator);
		}
		if ((r.matcher(actualText)).find()) {
			TestSetup.repControl.getReporter().ActionLog("Expected text pattern [" + pattern + "] is matching [" + actualText + "] for control ["
					+ locDesc + "]");
		} else {
			TestSetup.repControl.getReporter().AssertFail("Expected text pattern [" + pattern + "] is not matching [" + actualText
					+ "] for control [" + locDesc + "]");
		}
		return this;
	}

	/**
	 * Switch to frame using framename
	 * 
	 * @param frameString
	 *            framename
	 * @return
	 */
	public SeleniumActions SwitchtoFrame(String frameString) {

		driver.switchTo().frame(frameString);
		TestSetup.repControl.getReporter().ActionLog("Switched TO Frame");
		return this;
	}

	public SeleniumActions SwitchtoWindow(String Windowhandle) {

		driver.switchTo().window(Windowhandle);
		TestSetup.repControl.getReporter().ActionLog("Switched TO Window" + driver.getTitle());
		return this;
	}

	/**
	 * Switch to frame using frmae element
	 * 
	 * @param frameElement
	 *            - The frame element
	 * @return -null
	 */

	public SeleniumActions SwitchtoFrame(WebElement frameElement) {
		driver.switchTo().frame(frameElement);
		TestSetup.repControl.getReporter().ActionLog("Switched to iframe using element");
		return this;
	}

	/**
	 * Get the string window handle
	 * 
	 * @return - String
	 */
	public String GetWindowHandle() {

		return this.driver.getWindowHandle();

	}

	/**
	 * Get the string page title
	 * 
	 * @return - String
	 */
	public String GetPageTitle() {

		return driver.getTitle();

	}

	/**
	 * Close current browser session
	 * 
	 * @return - String
	 */
	public SeleniumActions CloseCurrentBrowser() {

		driver.close();
		return this;

	}

	/**
	 * Double click on a webelement
	 * 
	 * @param locator
	 *            - Descriptor of the webelement
	 * @return
	 */
	public SeleniumActions DoubleClickOnElement(ObjectLocator locator) {

		element = FindElement(locator);
		org.openqa.selenium.interactions.Actions act = new org.openqa.selenium.interactions.Actions(driver);
		act.doubleClick(element).build().perform();
		TestSetup.repControl.getReporter().ActionLog("Double clicked on " + locator.locatorDescription);
		return this;

	}

	/**
	 * Switch to default content
	 * 
	 * @return
	 */
	public SeleniumActions switchtoDefaultcontent() {

		driver.switchTo().defaultContent();
		return this;

	}

	/**
	 * Switch to active element
	 * 
	 * @return
	 */
	public WebElement SwitchToActiveElement() {

		return driver.switchTo().activeElement();

	}

	/**
	 * Get all window Handles as a list
	 * 
	 * @return
	 */
	public List<String> GetWindowHandles() {
		List<String> elements = new ArrayList<>(driver.getWindowHandles());
		return elements;
	}

	/**
	 * Use browser back to navigate back
	 * 
	 * @return
	 */
	public SeleniumActions GotoBack() {
		driver.navigate().back();
		return this;
	}

	public String GetText(WebElement element) {

		String Text = element.getText();
		return Text;
	}

	public File driverScreenShot(File screenShot) {

		try {
			Waitforpageload();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, screenShot);
			return screenShot;

		} catch (IOException Io) {
			TestSetup.repControl.getReporter().AssertFail("File Not Saved ");
			return null;
		}
	}
	
	public File robotScreenShot(File screenShot) {

		try {
			
			Waitforpageload();
			Robot robot = new Robot();
			BufferedImage screenShotImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShotImage, "JPG",screenShot);
			return screenShot;

		} catch (IOException | AWTException Io) {
			TestSetup.repControl.getReporter().AssertFail("File Not Saved ");
			return null;
		}
	}


	public SeleniumActions NavigateBack() {
		driver.navigate().back();
		return this;
	}

	public SeleniumActions scrollTillElement(ObjectLocator locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", FindElement(locator));
		return this;
	}

	public void verifyTableContents(ObjectLocator locator, String strFieldName, String strExpectedValue) {
		WebElement verifyElement = FindElement(locator);
		WebTable table = WebTable.getTable(verifyElement);
		Boolean bFlag = false;
		for (int rowcounter = 0; rowcounter < table.getRowCount(); rowcounter++) {
			if (table.getRow(rowcounter).getCell(0).getText().contains(strFieldName)) {
				bFlag = true;
				TestSetup.repControl.getReporter().ActionLog("required field present " + strFieldName);
				if (table.getRow(rowcounter).getCell(1).getText().contains(strExpectedValue)) {
					TestSetup.repControl.getReporter().ActionLog("Validation success for " + strFieldName + " and " + strExpectedValue);
				} else {
					TestSetup.repControl.getReporter().AssertFail("Verification failed for " + strFieldName + " actual value is "
							+ table.getRow(rowcounter).getCell(1).getText().contains(strExpectedValue));
				}
				break;
			}
		}
		if (!bFlag) {
			TestSetup.repControl.getReporter().AssertFail("Table dosnot contain the Filed for " + strFieldName);
		}
	}

	public String GetCurrentURL() {

		String strURL = driver.getCurrentUrl();
		return strURL;
	}

	public SeleniumActions verifyObjectContainsText(ObjectLocator locator, String strStringToCheck) {

		driverwait(Integer.parseInt(PropManager.getProperty("Control_Wait")));
		if (IsElementPresent(locator)) {
			// ActionLog(locator.locatorDescription + " found successfully");
			if (GetText(locator).contains(strStringToCheck)) {
				TestSetup.repControl.getReporter().ActionLog(locator.locatorDescription + " Label is correct");
			} else {
				TestSetup.repControl.getReporter().AssertFail(locator.locatorDescription + " label txt not present");
			}

		} else {
			TestSetup.repControl.getReporter().AssertFail(locator.locatorDescription + " Element not present on the page");
		}
		return this;
	}

	public boolean IsElementPresentInUI(ObjectLocator locator) {
		try {
			driver.findElement(locator.Locator);
			TestSetup.repControl.getReporter().ActionLog(locator.locatorDescription + "element present");
			return true;

		} catch (org.openqa.selenium.NoSuchElementException e) {
			TestSetup.repControl.getReporter().ActionLog(locator.locatorDescription + "element not present");
			return false;
		}
	}

	

	public boolean IsSelected(ObjectLocator locator) {

		return FindElement(locator).isSelected();
	}

	public SeleniumActions CheckCheckbox(ObjectLocator locator) {
		if (!FindElement(locator).isSelected()) {
			Click(locator);
		}
		return this;
	}

	public SeleniumActions UnCheckCheckbox(ObjectLocator locator) {
		if (FindElement(locator).isSelected()) {
			Click(locator);
		}
		return this;
	}
	public SeleniumActions pageRefresh() {
driver.navigate().refresh();
		return this;
	}

}