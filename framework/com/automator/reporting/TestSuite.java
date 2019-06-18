package com.automator.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestSuite {
	
	private String name;
	private Date startTime;
	private Date endTime;
	private int threads;
	private int totalTests;
	private int passedTests;
	private int failedTests;
	
	private List<TestCase> lsTests;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public List<TestCase> getLsTests() {
		if(null==lsTests)
		{
			lsTests=new ArrayList<>();
		}
		return lsTests;
	}

	public void setLsTests(List<TestCase> lsTests) {		
		this.lsTests = lsTests;
	}

	public int getTotalTests() {
		return totalTests;
	}

	public void setTotalTests(int totalTests) {
		this.totalTests = totalTests;
	}

	public int getPassedTests() {
		return passedTests;
	}

	public void setPassedTests(int passedTests) {
		this.passedTests = passedTests;
	}

	public int getFailedTests() {
		return failedTests;
	}

	public void setFailedTests(int failedTests) {
		this.failedTests = failedTests;
	}
	
	

}
