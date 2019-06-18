package com.automator.setup;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.winium.WiniumDriver;

import com.automator.web.resources.WebDriverFactory;
import com.automator.windows.resources.WindowsDriverFactory;

public class DriverFactory {
	
	static WebDriverFactory webFactory;
	static WindowsDriverFactory windowsFactory;

	// initialize the driver
	public static boolean initializeDriver(String browser) {
		
		browser=browser.toLowerCase();
		switch (browser) {
		case "firefox":
		case "ie":
		case "chrome":			
			try
			{
				webFactory=new WebDriverFactory();
				webFactory.initializeDriver(browser);
				return true;
			}
			catch (Exception e) {
				return false;
			}
		case "desktop":
			try
			{
				windowsFactory=new WindowsDriverFactory();
				windowsFactory.initializeDriver();
				return true;
			}
			catch (Exception e) {
				return false;
			}
		default:
			return false;

		}
	}

	/**
	 * Get current webdriver instance
	 * 
	 * @return webdriver
	 */
	public static WebDriver getWebDriver() {
		return webFactory.getDriver();
	}
	
	/**
	 * Get current webdriver instance
	 * 
	 * @return webdriver
	 */
	public static WiniumDriver getWinDriver() {
		return windowsFactory.getDriver();
	}

	/**
	 * close the current driver and kill session
	 */
	public static void closeWebDriver() {
		webFactory.closeDriver();
	}
	
	/**
	 * close any pending drivers
	 * @throws IOException 
	 */
	public static void disposeAllWebDrivers() throws IOException {
		webFactory.disposeAllWebDrivers();
	}
	
	
	/**
	 * close the current driver and kill session
	 */
	public static void closeWinDriver() {
		windowsFactory.closeDriver();
	}
	
	public static void closeWinService() throws IOException {
		windowsFactory.stopService();
	}

	
}
