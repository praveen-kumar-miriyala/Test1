package com.automator.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.automator.reporting.ReportingVariables.Status;

public class TestCase {
	
	private String name;
	private Date startTime;
	private Date endTime;
	private int iteration;
	private Status status;
	
	private List<Step> lsSteps;
	
	public TestCase(String name,int iteration) {
		this.name=name;
		this.startTime=new Date();
		this.iteration=iteration;		
	}

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

	
	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public List<Step> getLsSteps() {
		if(null==lsSteps)
		{
			lsSteps=new ArrayList<Step>();
		}
		return lsSteps;
	}

	public void setLsSteps(List<Step> lsSteps) {
		this.lsSteps = lsSteps;
	}
	
	
	

}
