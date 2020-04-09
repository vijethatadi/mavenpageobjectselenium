package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
	public WebDriver driver;

	public LoginPage(WebDriver driver)
	{
		PageFactory.initElements(driver,this);
	}
	@FindBy(name="username")
	private WebElement userName;
	@FindBy(xpath="//input[@name='pwd']")
	private WebElement passWord;
	@FindBy(id="loginButton")
	private WebElement loginButton;
	public  void setUserName(String userNameData)
	{
		userName.sendKeys(userNameData);
	}
	public  void setPassWord(String passWordData)
	{
		passWord.sendKeys(passWordData);
	}
	public void clickLoginButton()
	{
		loginButton.click();
	}
}
