package com.selenium.tests;

import org.testng.annotations.Test;

import com.selenium.base.BaseClass;
import com.selenium.pages.LoginPage;

public class LoginTest extends BaseClass{
	
	@Test
	public static void login001()
	{
		LoginPage loginpage= new LoginPage(driver);
		loginpage.setUserName("admin");
		loginpage.setPassWord("manager");
		loginpage.clickLoginButton();
	}
	@Test
	public static void login002()
	{
		LoginPage loginpage= new LoginPage(driver);
		loginpage.setUserName("admin1");
		loginpage.setPassWord("manager");
		loginpage.clickLoginButton();
	}
}
