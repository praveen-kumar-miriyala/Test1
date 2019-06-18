package apps.fossil.pageobjects;

import java.time.LocalDateTime;

import apps.fossil.objectmaps.HomePageOM;

public class HomePage extends HomePageOM {
	
	public HomePage navigate(String url) throws Exception {
		
		startModule();		
		ActionLog("Launch Application start time " + LocalDateTime.now());
		OpenURl(url).Waitforpageload();
		ActionLog("Launch Application Stop time " + LocalDateTime.now());
		endModule();
		
		
		return this;
	}
public HomePage login(String userName, String password) throws Exception {
	pageRefresh();
	Waitforpageload();
	driverwait(50000);

		if(GetPageTitle().equals("DigitalCatalog")){
			Click(userNameBtn);
			driverwait(2000);
			Click(logOutBtn);
			driverwait(2000);
		}
		driverwait(2000);
	EnterText(userNameTxtBx,userName);
	driverwait(80000);
	EnterText(passwordTxtBx,password);
	Click(signInBtn);
	driverwait(2000);

 return this;
	}
	
public HomePage siteSelection(String siteName) throws Exception {
	driverwait(20000);
	Click(siteSelection);
	driverwait(2000);
	IsElementPresent(siteSelection1);

	driverwait(2000);
	EnterText(siteSelection1, siteName);
	driverwait(2000);

	Click(siteName1);

	driverwait(2000);
	
 return this;
	}

	/*
	public HomePage goToMenu(String menuName) {
		startModule();
		Click(menuHomePage.ReplaceLocator(menuName));
		endModule();
		return this;
	}
*/	
	/*public HomePage verifyText(String menuName) {
		startModule();
		verifyTextInObject(linkViewAll.ReplaceLocator(menuName), "Failure");
		endModule();
		return this;
	}

	public HomePage closeDefaultModalDialog() {
		startModule();
		if (IsElementPresentInUI(modalDialogHomePage)) {
			Click(modalDialogCloseHomePage);
		}
		endModule();
		return this;
	}

	public HomePage clickOnViewAllLink(String linkName) {
		Click(linkViewAll.ReplaceLocator(linkName));
		return this;

	}

	public HomePage clickOnItemLink(String linkName) {
		Click(linkItem.ReplaceLocator(linkName));
		return this;
	}

	public HomePage addToBag() {
		Click(buttonAddToBag);
		return this;
	}

	public HomePage clickOnCheckout() {
		WaitForWebElement(linkCheckout).Click(linkCheckout);
		return this;

	}

	public HomePage clickOnProceedToCheckout() {
		WaitForWebElement(linkProceedToCheckout).Click(linkProceedToCheckout);
		return this;

	}

	public HomePage addShippingAddressDetails(String country, String firstName, String lastName, String streetAddress,
			String streetAddressLine2, String city, String state, String zipCode, String phoneNumber) {
			SelectByVisibleText(selectShippingAddressField.ReplaceLocator("Country"), country)
				.EnterText(inputShippingAddressField.ReplaceLocator("First Name"), firstName)
				.EnterText(inputShippingAddressField.ReplaceLocator("Last Name"), lastName)
				.EnterText(inputShippingAddressField.ReplaceLocator("Street Address"), streetAddress)
				.EnterText(inputShippingAddressField.ReplaceLocator("Street Address Line 2"), streetAddressLine2)
				.EnterText(inputShippingAddressField.ReplaceLocator("City"), city)
				.SelectByVisibleText(selectShippingAddressField.ReplaceLocator("State"), state)
				.EnterText(inputShippingAddressField.ReplaceLocator("Zip Code"), zipCode)
				.EnterText(inputShippingAddressField.ReplaceLocator("Phone Number"), phoneNumber);
		return this;
	}

	public HomePage clickOnContinueToBilling() {
		WaitForWebElement(buttonContinueToBilling).Click(buttonContinueToBilling);
		return this;
	}

	public HomePage addBillingDetails(String cardNumber, String expirationMonth, String expirationYear, String cvvValue,
			String email) {
		// radioSelectCreditCard
		WaitForWebElement(inputPaymentField.ReplaceLocator("Card Number"))
				.EnterText(inputPaymentField.ReplaceLocator("Card Number"), cardNumber)
				.SelectByVisibleText(selectPaymentField.ReplaceLocator("Expiration month"), expirationMonth)
				.SelectByVisibleText(selectPaymentField.ReplaceLocator("Expiration year"), expirationYear)
				.EnterText(inputPaymentField.ReplaceLocator("CVV"), cvvValue).CheckCheckbox(checkboxSameAsShippingData)
				.EnterText(inputEmail, email);
		return this;
	}

	public HomePage clickOnContinueToReview() {
		Click(buttonContinueToReview);
		return this;
	}

	public HomePage verifyErrorMessage(String message) {
		if (IsElementPresentInUI(spanInputErrorMessage.ReplaceLocator(message))) {
			ActionLog("Message " + message + "found on page!");
		} else {
			AssertFail("Failed to find the error message - " + message);
		}
		return this;
	}*/

}
