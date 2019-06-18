package com.automator.windows.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.automator.utils.PropManager;;

public class WindowsDriverFactory {

	WiniumDriver Driver;
	int winiumport = 9999;
	Process winiumService;
	File winiumServerFile = new File(
			System.getProperty("user.dir") + "\\BrowserServers\\Winium.Desktop.Driver.exe");

	// start the driver
	public void initializeDriver() throws IOException {

		DesktopOptions dtOptions = new DesktopOptions();
		dtOptions.setApplicationPath(PropManager.getAbsolutPath("DesktopExe"));
		Driver = new WiniumDriver(new URL("http://localhost:1001"), dtOptions);

	}

	/**
	 * Get current winiumdriver instance
	 * 
	 * @return winiumdriver
	 */

	public WiniumDriver getDriver() {
		return Driver;
	}

	/**
	 * close the current driver and kill session
	 */
	public void closeDriver() {

		if (null != Driver) {
			Driver.close();
			Driver.quit();
			Driver = null;
		}
	}

	/**
	 * Start the winium service
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startService() throws IOException, InterruptedException {
		
		if (null == winiumService || !winiumService.isAlive() ) {
			try {
				winiumport = Integer.parseInt(PropManager.getProperty("WiniumPort"));
			} catch (Exception e) {
				winiumport = 9999;
			}
			winiumService = new ProcessBuilder(winiumServerFile.getAbsolutePath(), "--port",
					Integer.toString(winiumport)).start();
		}
	}

	/**
	 * Stop winium service
	 * 
	 * @throws IOException
	 */
	public void stopService() throws IOException {

		if (null != winiumService) {
			if (winiumService.isAlive()) {
				winiumService.destroy();
				winiumService = null;
			}
		}

	}
}
