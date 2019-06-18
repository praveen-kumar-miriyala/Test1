package com.automator.web.resources;

import com.automator.setup.TestSetup;
import com.automator.web.resources.ObjectLocator.LocatorType;

public abstract class Page extends SeleniumActions {
	
	public void startModule()
	{
		TestSetup.repControl.getReporter().startModule();
	}
	
	public void endModule()
	{
		TestSetup.repControl.getReporter().endModule();
	}
	
	public void ActionLog(String message)
	{
		TestSetup.repControl.getReporter().ActionLog(message);
	}
	
	public void AssertFail(String message)
	{
		TestSetup.repControl.getReporter().AssertFail(message);
	}
	
	public ObjectLocator getLocator(LocatorType lt,String detail,String desc)
	{
		return new ObjectLocator(lt, detail,desc);
	}
}
