package com.automator.reporting;

import java.util.Date;

import com.automator.reporting.ReportingVariables.Status;

public class Step {
	
	private String name;
	private Date startTime;
	private Date endTime;
	private String data;	
	private Status status;	
	private String actualResults;	
	private String screenshot;
	private boolean module;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getActualResults() {
		return actualResults;
	}

	public void setActualResults(String actualResults) {
		this.actualResults = actualResults;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	public boolean isModule() {
		return module;
	}

	public void setModule(boolean module) {
		this.module = module;
	}

	
}
