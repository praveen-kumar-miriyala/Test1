package com.automator.utils;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import com.automator.setup.TestSetup;

public class VerificationListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    	
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result)
    {
    	if(TestSetup.repControl.getReporter().getCurrentTestName().equals(method.getTestMethod().getMethodName()))
    	{
	    	if(TestSetup.repControl.getReporter().isFailedTest())
	    	{
	    		result.setStatus(ITestResult.FAILURE);
	    	}
    	}
    }
}