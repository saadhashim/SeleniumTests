package se.jsta.seleniumtest.seleniumtest;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumTest {
	@Test
	public void searchForJstaSeAndKlickOnLink() throws InterruptedException{
		WebDriver firefox =  new FirefoxDriver();
		firefox.get("http://google.se");
		WebElement searchTextbox = firefox.findElement(By.id("lst-ib"));
		searchTextbox.sendKeys("JS Testautomatisering");
		firefox.findElement(By.name("btnG")).click();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.findElement(By.linkText("JS Testautomatisering - Sveriges bästa kurser")).click();
		firefox.findElement(By.xpath("//*[contains(text(),'Kontakta oss')]"));
		String titel = firefox.getTitle();
		Assert.assertEquals(titel, "JS Testautomatisering - Sveriges bästa kurser");
		firefox.close();
	}
	@Test
	public void areAllElementsDisplayed(){
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.get("http://localhost:8000");
		Assert.assertTrue(firefox.findElement(By.id("username")).isDisplayed());
		Assert.assertTrue(firefox.findElement(By.name("password")).isDisplayed());
		Assert.assertTrue(firefox.findElement(By.tagName("h1")).isDisplayed());
		Assert.assertTrue(firefox.findElement(By.linkText("Till valvet")).isDisplayed());
		Assert.assertTrue(firefox.findElement(By.partialLinkText("valvet")).isDisplayed());
		Assert.assertTrue(firefox.findElement(By.xpath("//input[@name='password']")).isDisplayed());
		firefox.close();
	}
	@Test
	public void verifyTextsOnLoginPage(){
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.get("http://localhost:8000");
		WebElement pageTitle = firefox.findElement(By.tagName("h1"));
		WebElement loginButton = firefox.findElements(By.tagName("input")).get(2);
		WebElement valvetButton = firefox.findElement(By.tagName("a"));
		Assert.assertTrue(pageTitle.getText().contains("JSTA"));
		Assert.assertTrue(loginButton.getAttribute("value").equals("Logga in"));
		Assert.assertTrue(valvetButton.getText().equalsIgnoreCase("tIll VaLvEt"));
		firefox.close();
	}
	@Test
	public void compareSaldo(){
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.get("http://localhost:8000");
		firefox.findElement(By.id("username")).sendKeys("admin");
		firefox.findElement(By.id("password")).sendKeys("pwd");
		firefox.findElement(By.xpath("//input[@type='submit']")).click();
		firefox.findElement(By.linkText("Alla kunder")).click();
		String tableText = firefox.findElement(By.xpath("//table")).getText();
		String customer = firefox.findElement(By.xpath("//table//td")).getText();
		String customerSaldo = tableText.split(customer)[1].split("\n")[0];
		firefox.findElement(By.linkText("Kolla saldo")).click();
		firefox.findElement(By.id("name")).sendKeys(customer);
		firefox.findElement(By.xpath("//input[@type='submit']")).click();
		float saldo = Float.valueOf(firefox.findElement(By.xpath("//div[@class='bg-info']/p")).getText());
		float tabellSaldo = Float.valueOf(customerSaldo);
		Assert.assertEquals(saldo, tabellSaldo, 0);
		firefox.close();
	}
	
	
	@Test
	public void interactWithLoginPage()
	{
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.get("http://localhost:8000");
		WebElement h1 = firefox.findElement(By.tagName("h1"));
		WebElement usernameInput = firefox.findElement(By.name("username"));
		WebElement passwordInput = firefox.findElement(By.name("password"));
		WebElement loggaInButton = firefox.findElement(By.xpath("//input[@type='submit']"));
		WebElement tillValvetLink = firefox.findElement(By.xpath("//a"));

		Assert.assertEquals("Välkommen till JSTA-Banken", h1.getText());
		usernameInput.clear();
		usernameInput.sendKeys("admin");
		passwordInput.clear();
		passwordInput.sendKeys("pwd");
		Assert.assertTrue(loggaInButton.isDisplayed());
		Assert.assertTrue(loggaInButton.isEnabled());
		Assert.assertFalse(loggaInButton.isSelected());
		Assert.assertEquals("Logga in", loggaInButton.getAttribute("value"));
		loggaInButton.click();
		firefox.close();
	}
	@Test
	public void changeWindow() throws InterruptedException
	{
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		firefox.get("http://localhost:8000");
		WebElement tillValvetLink = firefox.findElement(By.xpath("//a"));
		tillValvetLink.sendKeys(Keys.ALT,Keys.SHIFT, Keys.ENTER);
		String loginWindow = firefox.getWindowHandle();
		Set<String> windows = firefox.getWindowHandles();
		for(String window : windows){
			if(!window.equals(loginWindow)){
				firefox.switchTo().window(window);
			}
		}
		Assert.assertEquals("Valvet", firefox.findElement(By.xpath("//h2")).getText());
		firefox.switchTo().window(loginWindow);
		Assert.assertTrue(firefox.findElement(By.xpath("//input[@type='submit']")).isEnabled());
		firefox.findElement(By.id("username")).clear();
		firefox.findElement(By.id("username")).sendKeys("admin");
		firefox.findElement(By.id("password")).clear();
		firefox.findElement(By.id("password")).sendKeys("pwd");
		firefox.findElement(By.xpath("//input[@type='submit']")).click();
		firefox.navigate().back();
		Assert.assertTrue(firefox.findElement(By.xpath("//input[@type='submit']")).isEnabled());
		firefox.quit();
	}
	@Test
	public void iframeTest(){
		WebDriver firefox =  new FirefoxDriver();
		firefox.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		firefox.get("http://jsta.se/jstabanken-i-en-iframe/");
		WebDriverWait wait = new WebDriverWait(firefox, 10);
		firefox.switchTo().frame("jstabanken");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit']")));
		firefox.quit();
	}
	@Test
	public void browserTest(){
		System.setProperty("webdriver.chrome.driver", "c:/drivers/chromedriver.exe");
		WebDriver chromeDriver = new ChromeDriver();
		chromeDriver.get("http://localhost:8000");
		Assert.assertEquals("Välkommen till JSTA-Banken", chromeDriver.findElement(By.tagName("h1")).getText());
		chromeDriver.quit();

		System.setProperty("webdriver.ie.driver","c:/drivers/IEDriverServer.exe");
		WebDriver ieDriver = new InternetExplorerDriver();
		ieDriver.get("http://localhost:8000");
		Assert.assertEquals("Välkommen till JSTA-Banken", ieDriver.findElement(By.tagName("h1")).getText());
		ieDriver.quit();
	}
	
	@Ignore
	@Test
	public void browserTestMac(){
		System.setProperty("webdriver.chrome.driver", "/drivers/chromedriver");
		WebDriver chromeDriver = new ChromeDriver();
		chromeDriver.get("http://localhost:8000");
		Assert.assertEquals("Välkommen till JSTA-Banken", chromeDriver.findElement(By.tagName("h1")).getText());
		chromeDriver.quit();
		
		WebDriver safariDriver = new SafariDriver();
		safariDriver.get("http://localhost:8000");
		Assert.assertEquals("Välkommen till JSTA-Banken", safariDriver.findElement(By.tagName("h1")).getText());
		safariDriver.quit();
	}
	@Test
	public void dragAndDrop(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://demos.telerik.com/kendo-ui/dragdrop/index");
		WebElement draggable = driver.findElement(By.id("draggable"));
		WebElement target = driver.findElement(By.id("droptarget"));
		(new Actions(driver)).dragAndDrop(draggable, target).perform();
		Assert.assertEquals("You did great!", target.getText());
		driver.quit();
	}
	@Test
	public void runJavascript(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://jsta.se");
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript("alert('Mission Complete');");
		}
		driver.quit();
	}
	@Test
	public void catchStaleEementException(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://jsta.se");
		WebElement button = driver.findElement(By.xpath("//input[@type='submit']"));
		try{
			button.click();
			driver.quit();
		}catch(StaleElementReferenceException exception){
		}
	}
	@Test
	public void catchNo(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://jsta.se");
		WebElement button = driver.findElement(By.xpath("//input[@type='submit']"));
		takeScreenshot(driver, "ElementNotFound");
		try{
			button.click();
			driver.quit();
		}catch(NoSuchElementException exception){
		}
	}
	
	public WebElement findElementAndTakeScreenshotAtFailure(WebDriver driver, By by){
		try{
			return driver.findElement(by);
		}catch(NoSuchElementException exception){
			takeScreenshot(driver, "ElementNotFound");
			throw exception;
		}
	}
	
	public void takeScreenshot(WebDriver driver, String fileName){
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(
                    "C:\\" + fileName + ".png"));
        } catch (IOException e1) {
            System.out.println("Failed to take a screenshot");
        }
	}
	@Test
	public void dragAndDropWithLogsAndScreenshots(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Går till drag and drop sidan");
		driver.get("http://demos.telerik.com/kendo-ui/dragdrop/index");
		System.out.println("Hittar element");
		WebElement draggable = driver.findElement(By.id("draggable"));
		WebElement target = driver.findElement(By.id("droptarget"));
		System.out.println("Drar och släpper den lilla cirkeln inuti den stora");
		takeScreenshot(driver, "beforeDragAndDrop");
		(new Actions(driver)).dragAndDrop(draggable, target).perform();
		takeScreenshot(driver, "afterDragAndDrop");
		System.out.println("Kollar att den nu står You did great! inuti den stora cirkeln");
		Assert.assertEquals("You did great!", target.getText());
		driver.quit();
	}
	@Test
	public void loginWithPageObject(){
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		LoginPage loginPage = new LoginPage(driver, true);
		loginPage.login();
		driver.navigate().back();
		loginPage.login("fel", "pwd");
		Assert.assertEquals("Invalid credentials!!", loginPage.getErrorText());
		loginPage.clickValvetLink();
		driver.quit();
	}
	@Test
	public void interactWithSelect(){
		WebDriver driver = new FirefoxDriver();
		driver.get("http://localhost:8000");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("pwd");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		driver.findElement(By.linkText("Transaktioner")).click();
		driver.findElement(By.id("name")).sendKeys("saad");
		driver.findElement(By.id("amount")).sendKeys("1000");
		Select transactionType = new Select(driver.findElement(By.id("transaction")));
		transactionType.selectByVisibleText("Sätt in");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		driver.findElement(By.id("name")).sendKeys("saad");
		driver.findElement(By.id("amount")).sendKeys("100");
		transactionType = new Select(driver.findElement(By.id("transaction")));
		transactionType.selectByVisibleText("Ta ut");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		driver.quit();
	}
	
	@Test
	public void ajaxTest(){
		WebDriver driver = new FirefoxDriver();
		driver.get("http://aftonbladet.se");
		waitForAjax(driver);
		Assert.assertNotNull(driver.findElement(By.xpath("//*[contains(text(), 'mord')]")));
		driver.quit();
	}
	
	public void waitForAjax(WebDriver driver) {
	    new WebDriverWait(driver, 40).until(new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver driver) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            return (Boolean) js.executeScript("return jQuery.active == 0");
	        }
	    });
	}
}
