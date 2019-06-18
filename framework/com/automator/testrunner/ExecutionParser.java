package com.automator.testrunner;

import com.automator.utils.ExcelHelper;
import com.automator.utils.PropManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExecutionParser {

	private static String testCaseFile;
	private static String testCaseSheet;
	private static String[] filterArray = new String[6];
	private static Integer[] excelColArray = new Integer[6];

	public static String buildTestNGXML(String args[]) throws IOException {
		testCaseFile = System.getProperty("user.dir") + "\\testcases\\" + PropManager.getProperty("TestFileName");
		testCaseSheet = PropManager.getProperty("TestSheetName");
		parseArgs(args);

		if (PropManager.checkFileExits(testCaseFile)) {
			return TestNGXMLWriter.writeTestNGXML(
					getExecutionTests(ExcelHelper.readExcelAsArray(testCaseFile, testCaseSheet)), testCaseSheet);

		} else {
			return "[Error] Test Case File " + testCaseFile + " not found!";
		}
	}

	private static HashMap<String, List<ExecutionTests>> getExecutionTests(List<List<String>> lsExcelData) {
		HashMap<String, List<ExecutionTests>> hmExecTests = new HashMap<String, List<ExecutionTests>>();
		boolean headerSet=false;
		for (List<String> lsRow : lsExcelData) {
			if (!headerSet) {
				setHeaderIndex(lsRow);
				headerSet=true;
			} else {
				if (meetsFilter(lsRow)) {
					if (!hmExecTests.containsKey(lsRow.get(excelColArray[1]))) {
						hmExecTests.put(lsRow.get(excelColArray[1]), new ArrayList<ExecutionTests>());
					}
					ExecutionTests et = new ExecutionTests();
					et.setId(lsRow.get(excelColArray[0]));
					et.setSuiteName(lsRow.get(excelColArray[1]));
					et.setApplication(lsRow.get(excelColArray[2]));
					et.setModule(lsRow.get(excelColArray[3]));
					et.setTestCase(lsRow.get(excelColArray[4]));
					hmExecTests.get(lsRow.get(excelColArray[1])).add(et);
				}
			}
		}
		return hmExecTests;
	}

	private static void setHeaderIndex(List<String> lsHeaderRow) {
		int headCounter = 0;
		for (String headerText : lsHeaderRow) {
			switch (headerText.toLowerCase()) {
			case "sl#":
				excelColArray[0] = headCounter;
				break;
			case "suitename":
				excelColArray[1] = headCounter;
				break;
			case "application":
				excelColArray[2] = headCounter;
				break;
			case "module":
				excelColArray[3] = headCounter;
				break;
			case "testcase":
				excelColArray[4] = headCounter;
				break;
			case "execute":
				excelColArray[5] = headCounter;
				break;
			default:
				break;
			}
			headCounter++;
		}
	}

	private static boolean meetsFilter(List<String> lsRow) {
		if(lsRow.get(excelColArray[5]).equalsIgnoreCase("yes"))
		{
			for (int filterCounter = 0; filterCounter <= 5; filterCounter++) {
				if (null != filterArray[filterCounter] && !filterArray[filterCounter].trim().isEmpty()) {
					//if (!lsRow.get(excelColArray[filterCounter]).equalsIgnoreCase(filterArray[filterCounter])) {
					//	return false;
					//}				
					if (!filterArray[filterCounter].contains(lsRow.get(excelColArray[filterCounter]))) {
						return false;
					}
				}
			}
			return true;
		}
		else
		{
			return false;
		}
		
	}

	private static void parseArgs(String args[]) {
		if (null != args && args.length > 0) {

			for (String argument : args) {
				String chkArg = argument.toLowerCase();
				if (chkArg.startsWith("-p:")) {
					//test case workbook name
					testCaseFile = argument.replace("-p:", "");
				} else if (chkArg.startsWith("-n:")) {
					//sheet in test case work book
					testCaseSheet = argument.replace("-n:", "");
				} else if (chkArg.startsWith("-i:")) {
					//Index
					filterArray[0] = argument.replace("-i:", "");
				} else if (chkArg.startsWith("-s:")) {
					//Suite
					filterArray[1] = argument.replace("-s:", "");
				} else if (chkArg.startsWith("-a:")) {
					//Application
					filterArray[2] = argument.replace("-a:", "");
				} else if (chkArg.startsWith("-m:")) {
					//Module
					filterArray[3] = argument.replace("-m:", "");
				} else if (chkArg.startsWith("-t:")) {
					//Test Case
					filterArray[4] = argument.replace("-t:", "");
				}
			}
		}
		//ExecuTE Flag
		filterArray[5] = "Yes";
	}

}
