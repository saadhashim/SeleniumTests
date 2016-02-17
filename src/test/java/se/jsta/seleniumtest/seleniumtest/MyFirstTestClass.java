package se.jsta.seleniumtest.seleniumtest;

import org.junit.Test;

import junit.framework.Assert;

public class MyFirstTestClass {

	@Test
	public void myFirstJunitTest(){
		Assert.assertEquals(1, 1);
	}
	
	
}
