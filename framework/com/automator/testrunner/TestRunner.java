package com.automator.testrunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlSuite.ParallelMode;

import com.automator.utils.PropManager;

public class TestRunner {

	public static void main(String args[]) throws IOException {
		
		if(args.length==0)
		{
			HashMap<String,String> argMap=new HashMap<>();
			if(!PropManager.getProperty("Index").isEmpty())
			{ 
				argMap.put("-i:", PropManager.getProperty("Index"));			
			}
			if(!PropManager.getProperty("Suite").isEmpty())
			{ 
				argMap.put("-s:", PropManager.getProperty("Suite"));			
			}
			if(!PropManager.getProperty("Application").isEmpty())
			{ 
				argMap.put("-a:", PropManager.getProperty("Application"));				
			}
			if(!PropManager.getProperty("Module").isEmpty())
			{ 
				argMap.put("-m:", PropManager.getProperty("Module"));
			}
			if(!PropManager.getProperty("Testcase").isEmpty())
			{ 
				argMap.put("-t:", PropManager.getProperty("Testcase"));
			}
			
			args=new String[argMap.size()];
			int counter=0;
			for(String key:argMap.keySet())
			{
				args[counter]=key + argMap.get(key);
				counter++;
			}
		}		
		
		String testNGXML=ExecutionParser.buildTestNGXML(args);
		System.out.println(testNGXML);
		
		TestNG testng = new TestNG();
		List<String> suites=new ArrayList<String>();
		suites.add(testNGXML);
		testng.setTestSuites(suites);
		
		String parallelmode=PropManager.getProperty("parallelmode");
		int parallelThreads=PropManager.getInteger("parallelthreads", 0);
		
		if(null!=ParallelMode.getValidParallel(parallelmode) && parallelThreads > 0)
		{
			testng.setParallel(ParallelMode.getValidParallel(parallelmode));
			testng.setThreadCount(parallelThreads);
		}		
		
		testng.run();		
	}
}
