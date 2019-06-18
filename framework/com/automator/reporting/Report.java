package com.automator.reporting;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;
import org.testng.Reporter;


import com.automator.utils.PropManager;
import com.automator.web.resources.SeleniumActions;
import com.automator.reporting.ReportingVariables.Status;

public class Report {
	
	private TestSuite currentSuite;
	private SoftAssert SoftAssert;
	private HashMap<String, Integer> iterationHolder;
	private boolean isFailedTest;
	private String currentTestName;
	
	private int getIteration(String testName)
	{
		if(iterationHolder.containsKey(testName))
		{
			return iterationHolder.get(testName);
		}
		return 0;
	}
	
	
	public void startSuite(String name,int threads) {
		if(null==iterationHolder)
		{
			iterationHolder=new HashMap<>();
		}
		currentSuite=new TestSuite();
		currentSuite.setName(name);
		currentSuite.setStartTime(new Date());
		currentSuite.setThreads(threads);
	}
	
	public void endSuite() throws IOException {
		currentSuite.setEndTime(new Date());
		new ReportWriter(currentSuite).writeReport();
	}
	
	private String getTestName()
	{
		return getCurrentTestName();
		/*String testName="";
		for(StackTraceElement ste: Thread.currentThread().getStackTrace())
		{
			if(ste.getMethodName().equalsIgnoreCase("invoke0"))
			{
				return testName;
			}
			else
			{
					testName=ste.getMethodName();
			}
		}		
		return "";*/
	}
	
	private String getModuleName()
	{
		for(StackTraceElement ste: Thread.currentThread().getStackTrace())
		{
			if(ste.getClassName().contains("pageobjects"))
			{
				return ste.getMethodName();
			}
		}
		return getTestName();
	}
	
	private String getStepName()
	{
		for(StackTraceElement ste: Thread.currentThread().getStackTrace())
		{
			if(ste.getClassName().contains("Actions"))
			{
				return ste.getMethodName();
			}
		}
		return getModuleName();
	}
	
	//public void startNewTest() {	
				
	//	startNewTest(getTestName());
	//}
	
	public void startNewTest(String testName) {	
		
		currentTestName=testName;
		isFailedTest=false;
		if(iterationHolder.containsKey(testName))
		{
			iterationHolder.put(testName, iterationHolder.get(testName)+1);
		}
		else
		{
			iterationHolder.put(testName, 1);
		}
		currentSuite.setTotalTests(currentSuite.getTotalTests()+1);
		currentSuite.getLsTests().add(new TestCase(testName,getIteration(testName)));
	}
	
	public void startModule() {		
		startModule(getModuleName());
	}
	
	
	public void startModule(String stepName) {
		int testIndex=getTestIndex();
		if(testIndex > -1)
		{
			Step newStep=new Step();
			newStep.setName(stepName);
			newStep.setStartTime(new Date());
			newStep.setModule(true);
			currentSuite.getLsTests().get(testIndex).getLsSteps().add(newStep);
		}
	}
	
	
	public void addTestStep(Date startTime,String data,Status status,String actualResult,String screenShot) {
		int testIndex=getTestIndex();
		if(testIndex > -1)
		{
			Step newStep=new Step();
			newStep.setName(getStepName());
			newStep.setStartTime(startTime);
			newStep.setStatus(status);
			newStep.setActualResults(actualResult);
			newStep.setScreenshot(screenShot);
			newStep.setModule(false);
			newStep.setData(data);
			currentSuite.getLsTests().get(testIndex).getLsSteps().add(newStep);
		}
	}
	
	public void endModule() {
		endModule(getModuleName());
	}
	
	public void endModule(String stepName) {
		int testIndex=getTestIndex();
		if(testIndex > -1)
		{
			int stepSize=currentSuite.getLsTests().get(testIndex).getLsSteps().size();
			Status currStatus=Status.Passed;
			for(int stepCounter=stepSize-1;stepCounter>=0;stepCounter--)
			{
				Step currStep=currentSuite.getLsTests().get(testIndex).getLsSteps().get(stepCounter);
				if(!currStep.isModule())
				{
					if(currStep.getStatus() == Status.Failed)
					{
						currStatus=currStep.getStatus() ;
					}
				}
				else
				{
					if(currStep.getName().equals(stepName))
					{
						currentSuite.getLsTests().get(testIndex).getLsSteps().get(stepCounter).setStatus(currStatus);
						currentSuite.getLsTests().get(testIndex).getLsSteps().get(stepCounter).setEndTime(new Date());
					}
				}
			}
			
		}
	}
	
	public void endTest() {	
		endTest("");
	}
	
	public void endTest(String testName) {
		
		int testIndex=-1;
		if(testName.isEmpty())
		{
			testIndex=getTestIndex();
		}
		else
		{
			testIndex=getTestIndex(testName);	
		}
		
		if(testIndex > -1)
		{
			currentSuite.getLsTests().get(testIndex).setEndTime(new Date());
			Status currStatus=Status.Passed;
			for(Step testSteps:currentSuite.getLsTests().get(testIndex).getLsSteps())
			{
				if(testSteps.getStatus() == Status.Failed || testSteps.getStatus() == Status.Blocked)
				{
					currStatus=testSteps.getStatus() ;
					break;
				}
			}
			currentSuite.getLsTests().get(testIndex).setStatus(currStatus);
			if(Status.Passed==currStatus)
			{
				currentSuite.setPassedTests(currentSuite.getPassedTests()+1);
			}
			else
			{
				currentSuite.setFailedTests(currentSuite.getFailedTests()+1);
			}
		}
	}
	
	private int getTestIndex()
	{
		String name=getTestName();
		return getTestIndex(name);		
	}
	
	private int getTestIndex(String name)	{
		
		int testIndex=0;
		for(TestCase tc:currentSuite.getLsTests())
		{
			if(tc.getName().equals(name) && tc.getIteration()==getIteration(name))
			{
				return testIndex;
			}
			testIndex++;
		}
		return -1;
	}	
	
	//Log message to TestNG report and results
	public void ActionLog(String message) {
			
			if(PropManager.getProperty("ScreenShotLevel").equalsIgnoreCase("all") || PropManager.getProperty("ScreenShotLevel").equalsIgnoreCase("passed"))
			{
				addTestStep(new Date(),"", Status.Passed, message, takeScreenshot());
			}
			else
			{
				addTestStep(new Date(),"", Status.Passed, message, "");
			}
			ReportLog(message,false);
		}
	
	/**
	 * Add a message to TestNG report
	 * @param message - Message
	 * @param header - Is a separator needed?
	 * @return
	 */
	public void ReportLog(String message,boolean header){
		//add to TestNG report		
		if(header)
		{
			message="================" + message + "================";
		}
		Reporter.log(message + "\n");
	}
	
	public String takeScreenshot() {
		try
		{
			String ssFolder=PropManager.getAbsolutPath("ReportStore") + "\\" + PropManager.getProperty("ScreenShotFolder");
			File screenShotFolder=new File(ssFolder);
			if(!screenShotFolder.exists())
			{
				screenShotFolder.mkdirs();
			}
			
			File screenShotName =new SeleniumActions().driverScreenShot(new File(ssFolder + "\\"+PropManager.getTimeStamp()+".png"));
			if(null!=screenShotName)
			{
				return screenShotName.getAbsolutePath();
			}
			else
			{
				return "Screen Shot Failed!";
			}
		}
		catch (Exception e) {
			return e.getMessage();
		}
	
		}
	
	/**
	 * Report a TestNG failure
	 * @param message = Failure message
	 */
	public void AssertFail(String message) {
		
		isFailedTest=true;
		//Report failure to TestNG log
		//ActionLog("[FAILURE] " + message);
		addTestStep( new Date(),"", Status.Failed, message, takeScreenshot());
		//Fail TestNG
		//SoftAssert.assertTrue(false,message);
		SoftAssert.fail(message);
		
	}
	
	public void assertAll() {
		SoftAssert.assertAll();
		
	}
	
	public void initializeSoftAssert() {
		SoftAssert = new SoftAssert();
	}


	public boolean isFailedTest() {
		return isFailedTest;
	}
	
	public String getCurrentTestName()
	{
		if(null==currentTestName)
		{
			currentTestName="";
		}
		return currentTestName;
	}

}
