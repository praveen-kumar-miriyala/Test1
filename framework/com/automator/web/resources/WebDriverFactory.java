package com.automator.web.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverFactory {

	//private WebDriver Driver;	
	private HashMap<Long,WebDriver> hmDriverHolder=new HashMap<Long, WebDriver>();
	//IE server file path
	File ieDriver32 = new File(System.getProperty("user.dir") + "\\drivers\\IEDriverServer_x32.exe");
	File ieDriver64 = new File(System.getProperty("user.dir") + "\\drivers\\IEDriverServer_x64.exe");
	//Chrome server file path
	File chromeDriver = new File(System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
	//Gecko Driver file path
	File geckoDriver32 = new File(System.getProperty("user.dir") + "\\drivers\\geckodriver_x32.exe");
	File geckoDriver64 = new File(System.getProperty("user.dir") + "\\drivers\\geckodriver_x64.exe");

	//start the driver 
	public void initializeDriver(String browser) throws IOException {

		//WebDriver Driver;
		switch (browser) {
		case "firefoxold":
			//Driver = new FirefoxDriver();
			setThreadDriver(new FirefoxDriver());
			break;
		case "htmlunit":
			//Driver = new HtmlUnitDriver();
			setThreadDriver(new HtmlUnitDriver());
			break;
		case "firefox32":
			System.setProperty("webdriver.gecko.driver", geckoDriver32.getAbsolutePath());			
			//Driver = new FirefoxDriver();
			setThreadDriver(new FirefoxDriver());
			break;
		case "firefox64":
			System.setProperty("webdriver.gecko.driver", geckoDriver64.getAbsolutePath());			
			//Driver = new FirefoxDriver();
			setThreadDriver(new FirefoxDriver());
			break;
		case "ie32":
			System.setProperty("webdriver.ie.driver", ieDriver32.getAbsolutePath());
			//Driver = new InternetExplorerDriver();
			setThreadDriver(new InternetExplorerDriver());
			break;
		case "ie64":
			System.setProperty("webdriver.ie.driver", ieDriver64.getAbsolutePath());
			//Driver = new InternetExplorerDriver();
			setThreadDriver(new InternetExplorerDriver());
			break;
		case "chrome":
			System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			//Driver = new ChromeDriver(options);
			setThreadDriver(new ChromeDriver(options));
			break;		
		}
		
		
	}

	private void setThreadDriver(WebDriver driver)
	{		
		hmDriverHolder.put(Thread.currentThread().getId(), driver);
		if(null!=driver)
		{
			driver.manage().window().maximize();
		}
	}
	
	/**
	 * Get current webdriver instance
	 * @return webdriver
	 */	
	public WebDriver getDriver() {
		//return Driver;
		return hmDriverHolder.get(Thread.currentThread().getId());
	}

	/** 
	 * close the current driver and kill session
	 */
	public void closeDriver() {
		
		closeDriver(hmDriverHolder.get(Thread.currentThread().getId()));
	}
	
	private void closeDriver(WebDriver Driver) {
		
		if(null!=Driver)
		{
			Driver.close();
			Driver.quit();
		}
	}
	
	public void disposeAllWebDrivers() throws IOException {
		
		hmDriverHolder.clear();
		Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
	}
	
}
