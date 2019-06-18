package apps.fossil.tests;

import org.testng.annotations.Test;
import com.automator.setup.TestSetup;
import com.automator.utils.Application;

import apps.fossil.pageobjects.HomePage;

@Application("fossil")
public class FossilEComm extends TestSetup {

	HomePage home;

	@Test(dataProvider = "iterator")
	public void itemCreation(Integer iteration, Object inputData) throws Exception {

		home = new HomePage();

		home.navigate(use("URL"))
		.login(use("userName"),use("password"))
		
	    .siteSelection(use("SiteName"));
	     

		
		
		
		/*	.closeDefaultModalDialog()
			.verifyText(use("subMenu"))
			.goToMenu(use("subMenu"));
			.clickOnViewAllLink(use("itemCategory"))
			.clickOnItemLink(use("itemName"))
			.addToBag()
			.clickOnCheckout()
			.clickOnProceedToCheckout()
			.addShippingAddressDetails(use("country"), use("firstName"), use("lastName"), use("streetAddress"),
						use("streetAddressLine2"), use("city"), use("state"), use("zipCode"), use("phoneNumber"))
			.clickOnContinueToBilling().addBillingDetails(use("cardNumber"), use("expirationMonth"),
						use("expirationYear"), use("cvvValue"), use("email"))
			.clickOnContinueToReview()
			.verifyErrorMessage(use("errorMessage"));*/

	}
		
}
