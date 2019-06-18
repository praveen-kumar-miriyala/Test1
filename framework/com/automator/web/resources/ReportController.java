package com.automator.web.resources;

import com.automator.reporting.Report;

public class ReportController {
	
	private Report reporter;

	public Report getReporter() {
		
		if(null==reporter)
		{
			reporter=new Report();
		}
		return reporter;
	}
	
	

}
