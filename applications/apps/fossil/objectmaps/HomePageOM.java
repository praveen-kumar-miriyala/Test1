package apps.fossil.objectmaps;

import com.automator.web.resources.ObjectLocator;
import com.automator.web.resources.ObjectLocator.LocatorType;
import com.automator.web.resources.Page;

public class HomePageOM extends Page {
	
	public ObjectLocator userNameBtn = getLocator(LocatorType.Xpath,
			"//a[@class='dropdown-toggle userNameAlignment lf_Paragraph2 lf_base-text']", "click");
	
	public ObjectLocator logOutBtn = getLocator(LocatorType.Xpath,
			"(//span[@class='lf-logout-text'])[4]", "logOut");
	
	public ObjectLocator userNameTxtBx = getLocator(LocatorType.ID,
			"login-username", "Entered userName");
	public ObjectLocator passwordTxtBx = getLocator(LocatorType.ID,
			"login-password", "Entered password");
	public ObjectLocator signInBtn = getLocator(LocatorType.ID,
			"btn-login", "Signin");
	
	public ObjectLocator siteSelection = getLocator(LocatorType.Xpath,".//*[@id='siteDropDown']/p-dropdown/div/div[3]/span","select");
	public ObjectLocator siteSelection1 = getLocator(LocatorType.Xpath,"(//div/input[@type='text'])[2]","select");

	public ObjectLocator siteName1 = getLocator(LocatorType.Xpath,
			"//span[contains(text(),'KitchenHome')]", "Entered siteName");
	
	
		
	/*public ObjectLocator modalDialogHomePage = getLocator(LocatorType.Xpath,
			"//div[contains(@class,'modal-sign-up')]//div[contains(@class,'modal-dialog')]",
			"Home Page modal dialog");
	public ObjectLocator modalDialogCloseHomePage = getLocator(LocatorType.Xpath,
			"//div[contains(@class,'modal-sign-up')]//div[contains(@class,'modal-dialog')]//button[@class='close']",
			"Home page modal dialog close button");
	public ObjectLocator linkViewAll = getLocator(LocatorType.Xpath,
			"//span[text()='$']/ancestor::h2[contains(@class,'media-tile__header')]/following::div//a[text()='VIEW ALL']",
			"VIEW ALL link");
	public ObjectLocator linkItem = getLocator(LocatorType.Xpath,
			"//a[contains(@class,'link-product-result-path') and text()='$']", "Item name link");
	public ObjectLocator buttonAddToBag = getLocator(LocatorType.Xpath,
			"//span[text()='add to bag']/parent::button", "Add to bag button");
	public ObjectLocator linkCheckout = getLocator(LocatorType.Xpath,
			"//div[@class='btn-group-checkout']//a[text()='Checkout']", "Checkout link");
	public ObjectLocator linkProceedToCheckout = getLocator(LocatorType.Xpath,
			"//a[contains(text(),'checkout')]", "Checkout link");

	public ObjectLocator selectShippingAddressField = getLocator(LocatorType.Xpath,
			"//section[@class='shipping-address-container']//label[contains(text(),'$')]/following-sibling::div//select",
			"Dropdown field in shipping address section");
	public ObjectLocator inputShippingAddressField = getLocator(LocatorType.Xpath,
			"//section[@class='shipping-address-container']//label[contains(text(),'$')]/following-sibling::input",
			"Input field in shipping address section");

	public ObjectLocator buttonContinueToBilling = getLocator(LocatorType.Xpath,
			"//button[contains(text(),'Continue to Step 2: Billing')]", "Continue to Step 2: Billing button");

	public ObjectLocator radioSelectCreditCard = getLocator(LocatorType.Xpath,
			"//input[@type='radio' and @value='creditCardEnabled']", "CreditCard radio");

	public ObjectLocator inputPaymentField = getLocator(LocatorType.Xpath,
			"//label[contains(text(),'$')]/following-sibling::input", "Input field in payment section");

	public ObjectLocator selectPaymentField = getLocator(LocatorType.Xpath,
			"//label[contains(text(),'Expiration Date')]/following-sibling::div//select[@title='$']",
			"Select field in payment section");

	public ObjectLocator checkboxSameAsShippingData = getLocator(LocatorType.Xpath,
			"//section[contains(@class,'billing-address-container')]//input[@type='checkbox']",
			"Check box Same as shipping address?");

	public ObjectLocator inputEmail = getLocator(LocatorType.Xpath,
			"//label[contains(text(),'Email')]/following-sibling::input[@id='email1']", "Email input field");

	public ObjectLocator buttonContinueToReview = getLocator(LocatorType.Xpath,
			"//button[contains(text(),'Continue to step 3: Review')]", "Continue to Step 2: Review button");

	public ObjectLocator spanInputErrorMessage = getLocator(LocatorType.Xpath,
			"//span[contains(@class,'input-message') and text()='$']", "Input error message");
*/
}
