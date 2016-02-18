package se.jsta.seleniumtest.seleniumtest;

import static org.openqa.selenium.By.*;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class LoginPage {
	WebDriver driver;
	String url = "http://localhost:8000";
	By heading = tagName("h1");
	By errorLabel = xpath("//div[@class='bg-danger']/p");
	By usernameInput = name("username");
	By passwordInput = name("password");
	By loginButton = xpath("//input[@type='submit']");
	By valvetLink = tagName("a");
	
	public LoginPage(WebDriver driver, boolean navigateToPage){
		this.driver = driver;
		if(navigateToPage){
			navigateToPage();
		}
	}
	
	public void assertIsOnPage(){
		Assert.assertEquals("Välkommen till JSTA-Banken", getHeadingText());
	}
	
	private void navigateToPage(){
		driver.get(url);
	}
	
	public StartPage login(){
		return login("admin", "pwd");
	}
	
	public StartPage login(String userName, String password){
		setUserName(userName);
		setPassword(password);
		clickLoginButton();
		return new StartPage(driver);
	}
	
	
	public void setUserName(String userName){
		WebElement element = driver.findElement(usernameInput);
		element.clear();
		element.sendKeys(userName);
	}
	
	public void setPassword(String password){
		WebElement element = driver.findElement(passwordInput);
		element.clear();
		element.sendKeys(password);
	}
	
	public void clickLoginButton(){
		driver.findElement(loginButton).click();
	}
	
	public ValvetPage clickValvetLink(){
		driver.findElement(valvetLink).click();
		return new ValvetPage(driver);
	}
	
	public String getHeadingText(){
		return driver.findElement(heading).getText().trim();
	}
	
	public String getErrorText(){
		return driver.findElement(errorLabel).getText().trim();
	}
	
	public String getUserName(){
		return driver.findElement(usernameInput).getText().trim();
	}
	
	public void validateTexts(){
		Assert.assertEquals("Välkommen till JSTA-Banken", getHeadingText());
		Assert.assertEquals("Användarnamn", driver.findElement(usernameInput).getAttribute("placeholder"));
		Assert.assertEquals("Lösenord", driver.findElement(passwordInput).getAttribute("placeholder"));
		Assert.assertEquals("Logga in", driver.findElement(loginButton).getAttribute("value"));
		Assert.assertEquals("Till valvet", driver.findElement(valvetLink).getText());
	}
	
	
}
