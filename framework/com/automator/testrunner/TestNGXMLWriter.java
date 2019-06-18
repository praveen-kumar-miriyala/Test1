package com.automator.testrunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.automator.utils.PropManager;

public class TestNGXMLWriter {

	private enum ParallelModes
	{
		methods,tests,classes,instances;
	}
	
	public static String writeTestNGXML(HashMap<String,List<ExecutionTests>> hmTests,String suiteName) throws IOException
	{
		StringBuilder tnGSB=new StringBuilder();
		tnGSB.append(writeRoot(suiteName));		
		for(String testName:hmTests.keySet())
		{
			tnGSB.append(writeTests(testName, hmTests.get(testName)));
		}		
		tnGSB.append(closeRoot());
		
		String xmlFolder=PropManager.getAbsolutPath("testngxmlfolder");
		File xmlFolderObj = new File(xmlFolder);
		if (!xmlFolderObj.exists()) {
			xmlFolderObj.mkdirs();
		}
		File xmlName = new File(xmlFolder + "\\testng_" + PropManager.getTimeStamp() + ".xml");

		FileWriter xmlWriter = new FileWriter(xmlName);
		xmlWriter.write(tnGSB.toString());
		xmlWriter.close();
		
		return xmlName.getAbsolutePath();
		
	}
	
	private static String writeRoot(String suiteName)
	{
		StringBuilder tnGSB=new StringBuilder();
		tnGSB.append("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >");
		String parallelmode=PropManager.getProperty("parallelmode");
		int parallelThreads=PropManager.getInteger("parallelthreads", 0);
		
		if(null!=ParallelModes.valueOf(parallelmode) && parallelThreads > 1)
		{
			tnGSB.append("<suite name=\""+suiteName+"\" parallel=\""+parallelmode+"\" thread-count=\""+parallelThreads+"\" verbose=\"1\" >");	
		}		
		else
		{
			tnGSB.append("<suite name=\""+suiteName+"\" verbose=\"1\" >");
		}
		return tnGSB.toString();
	}
	
	private static String writeTests(String testName,List<ExecutionTests> lsExecTests)
	{
		if(testName.trim().isEmpty())
		{
			testName="NoName";
		}
		StringBuilder tnGSB=new StringBuilder();
		tnGSB.append("<test name=\""+testName+"\" >");
		tnGSB.append("<classes>");
		
		HashMap<String, StringBuilder> hmClassData=new HashMap<>();
		
		for(ExecutionTests et:lsExecTests)
		{
			if(!hmClassData.containsKey(et.getModule()))
			{
				hmClassData.put(et.getModule(), new StringBuilder());
				hmClassData.get(et.getModule()).append("<class name=\"apps."+et.getApplication().toLowerCase()+".tests." + et.getModule() + "\">");
				hmClassData.get(et.getModule()).append("<methods>");
			}
			hmClassData.get(et.getModule()).append("<include name=\""+et.getTestCase()+"\" />");			
		}
		
		for(String classes:hmClassData.keySet())
		{
			hmClassData.get(classes).append("</methods></class>");
			tnGSB.append(hmClassData.get(classes).toString());
		}
		
		tnGSB.append("</classes></test>");
		return tnGSB.toString();
	}
		
	
	private static String closeRoot()
	{
		return "</suite>";
	}
}
