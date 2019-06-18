package com.automator.web.resources;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ObjectLocator {

	public WebElement element;
	public LocatorType locatorType;
	public String locatorValue = "";
	public String locatorDescription;
	By Locator;

	/**
	 * Create a new object locator
	 * @param LocatorType - Type of locator
	 * @param LocatorDescription - Description of the location
	 */
	public ObjectLocator(LocatorType locatorType, String LocatorDescription)
	{
		this.locatorType = locatorType;
		this.locatorDescription = LocatorDescription;
	}
	
	/**
	 * Locator enum types
	 * @author harish.saidu
	 *
	 */
	public enum LocatorType
	{
		LinkText,PartiallinkText,ID,CSS,Name,Xpath,TagName
	}

	/**
	 * Create a new object locator
	 * @param LocatorType - Type of locator
	 * @param LocatorValue - Value of the locator
	 * @param LocatorDescription - Description of the location
	 */
	public ObjectLocator(LocatorType LocatorType, String LocatorValue, String LocatorDescription)
	{
		this.locatorType = LocatorType;
		this.locatorValue = LocatorValue;
		Locator = GetObjectLocator(LocatorType, LocatorValue);
		this.locatorDescription = LocatorDescription;

	}
	
	/**
	 * Replaces '$' in locator string with specified ReplaceString
	 * @param ReplaceString
	 * @return
	 */
	public ObjectLocator ReplaceLocator(String ReplaceString)

	{
		String Temp = locatorValue.replace("$", ReplaceString);
		Locator = GetObjectLocator(locatorType, Temp);
		return this;

	}

	/** 
	 * Form the ObjectLocator By
	 * @param locatorType - Type of locator
	 * @param locatorValue - Locator value
	 * @return
	 */
	public By GetObjectLocator(LocatorType locatorType, String locatorValue) {

		switch (locatorType) {
		case LinkText:
			Locator = By.linkText(locatorValue);
			break;
		case PartiallinkText:
			Locator = By.partialLinkText(locatorValue);
			break;
		case ID:
			Locator = By.id(locatorValue);
			break;
		case CSS:
			Locator = By.cssSelector(locatorValue);
			break;
		case Name:
			Locator = By.name(locatorValue);
			break;
		case Xpath:
			Locator = By.xpath(locatorValue);
			break;
		case TagName:
			Locator = By.tagName(locatorValue);
			break;
		}
		return Locator;

	}

	

}
