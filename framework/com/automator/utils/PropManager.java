package com.automator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class PropManager {
	
	private static File propfile = new File(System.getProperty("user.dir") + "\\" + "config.properties");
	public static Properties props;	
	
	/**
	 * Load the property files in to property object
	 */
	private static void loadProperties()
	{
		if(null==props)
		{
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(propfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			// Loading File into inputStream
			props = new Properties();
			try {
				props.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	
	/**
	 * Read properties from property file - enter parameter in double quotes
	 * @param key - Property Key
	 * @return - String
	 */
	public static String getProperty(String key) {
		loadProperties();
		return props.getProperty(key);
	}
	
	/**
	 * Reads properties from property file and formats path
	 * @param key - Property Key
	 * @return - String
	 */
	public static String getAbsolutPath(String key) {
		loadProperties();
		String path= props.getProperty(key);
		path=path.replace("%ProjDir%", System.getProperty("user.dir"));
		return path;
				
	}
	
	/**
	 * Reads properties from property file and formats to number
	 * @param key - Property Key
	 * @return - String
	 */
	public static int getInteger(String key,int defaultVal) {
		loadProperties();
		try{
			return Integer.parseInt(props.getProperty(key));
		}
		catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * Returns a random number
	 * @return
	 */
	private static int getRandomNumber(int upperBound)
	{
		Random randNumber=new Random();		
		return randNumber.nextInt(upperBound);
		
	}
	
	/**
	 * Generate password with specified conditions
	 * 
	 * @return
	 */
	public static String generatePassword(String prefix, int minLength) {
		String newPwd = prefix + Integer.toString(getRandomNumber(100000));
		while (newPwd.length() < minLength) {
			newPwd += "a";
		}
		return newPwd;
	}
	
	public Date GetCurrentDateTimeAndAdjust(String DateFormate, int mins) {
		// "dd/MM/yyyy HH:mma"
		Date date1 = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormate);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, mins);
		Date date = cal.getTime();
		String vale = dateFormat.format(date);
		System.out.println(vale + "format check");
		try {
			date1 = dateFormat.parse(vale);
			// date1=dateFormat.f
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date1;

	}

	public Date DateFormat(String strDate, String dateFormat) {
		Date startDate = null;
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		try {
			startDate = df.parse(strDate);

			// dDate = df.parse(StartDate);
			System.out.println(startDate + " date format value");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startDate;
	}
	
	public static boolean checkFileExits(String filePath)
	{
		File checkFile = new File(filePath);
		return checkFile.exists();
	}
	
	public static String getTimeStamp()
	{
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	}

}
