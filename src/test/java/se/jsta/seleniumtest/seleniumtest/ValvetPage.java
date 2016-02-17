package se.jsta.seleniumtest.seleniumtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;

public class ValvetPage {
	WebDriver driver;
	public ValvetPage(WebDriver driver) {
		this.driver = driver;
		System.out.println(RandomStringUtils.randomAlphanumeric(20).toUpperCase());
	}

}
