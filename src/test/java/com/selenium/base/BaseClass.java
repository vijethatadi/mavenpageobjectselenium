package com.selenium.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import com.google.common.io.Files;

public class BaseClass implements ITestListener{
	public static WebDriver driver;
	public static String url;
	public static Logger logger = Logger.getLogger("BaseClass");
	
	public static void writeLogs(String msg)
	{
		logger.info(msg);
	}
	public static void writeErrorLogs(Throwable t)
	{
		String s=Arrays.toString(t.getStackTrace());
		String s1=s.replaceAll(",", "\n");
		logger.error(s1);
				}
	
	public static void launchBrowser(String url)
	{
		System.setProperty("webdriver.chrome.driver", "./src/test/utilities/chromedriver.exe") ;
		//System.setProperty("webdriver.chrome.driver", "C:\\vijetha seleniumtraining\\seleniumextracted\\chromedriver_win32\\chromedriver.exe");
		 driver = new ChromeDriver();
		
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
	}
	@BeforeMethod
	public static void launchBrowser() throws IOException

	{	
		String url = getConfigData("url");
		String browser=getConfigData("browser");
		if(browser.equalsIgnoreCase("chrome"))
		{
			
		
		System.setProperty("webdriver.chrome.driver", "./src/test/utilities/chromedriver.exe") ;
		driver = new ChromeDriver();
		
		}
		
		else if(browser.equalsIgnoreCase("firefox"))
			{
			
			System.setProperty("webdriver.gecko.driver", "./src/test/utilities/geckodriver.exe") ;
			driver = new FirefoxDriver();
			
			}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(url);
	}
	
	public static void launchAndLogin(String userName,String passWord)

	{
		launchBrowser("https://demo.actitime.com");
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='pwd']")).sendKeys(passWord);
		driver.findElement(By.xpath("//a[@id='loginButton']")).click();
		
		
	}
	
	public static boolean loginIntoBrowser() throws IOException, InterruptedException
	{
		boolean result = true;
		driver.findElement(By.xpath(getLocatorData("login","username_editbox"))).sendKeys(getTestData("login","username_editbox"));
		driver.findElement(By.xpath(getLocatorData("login","password_editbox"))).sendKeys(getTestData("login","password_editbox"));
		driver.findElement(By.xpath(getLocatorData("login","ok_Button"))).click();
		//Thread.sleep(7000);
		//Explicit wait on a condition
		WebDriverWait wait= new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoutLink")));
		result  = driver.findElement(By.xpath(getLocatorData("home","logout_Button"))).isDisplayed();
		
		//writeLogs("logoutlink is there"+ logout);
		//writeLogs(result);
		return result;
	}
	
	public static boolean loginIntoBrowserWithArguments(String username,String password) throws IOException
	{	boolean result=true;
	driver.findElement(By.xpath(getLocatorData("login","username_editbox"))).sendKeys(username);
	driver.findElement(By.xpath(getLocatorData("login","password_editbox"))).sendKeys(password);
	driver.findElement(By.xpath(getLocatorData("login","ok_Button"))).click();
	//Explicit wait on a condition
	WebDriverWait wait= new WebDriverWait(driver,10);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoutLink")));
	result  = driver.findElement(By.xpath(getLocatorData("home","logout_Button"))).isDisplayed();
	
	

	
	
	
		return result;
	}
	
	public static void takeScreenShot(String fileName)
	{
	
		
		TakesScreenshot ts= (TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		File dest = new File("./src/test/Results/"+fileName+".png");
		try {
			Files.copy(src,dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getConfigData(String key) throws IOException

	{
		String value="";
		
		//In-built class from java
		File f = new File("./src/test/data/config.properties");
		
		FileInputStream fis = new FileInputStream(f);
		
		/*
		 * try {
		 
			FileInputStream fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		 //ready with the file and pass this to onw of In-built class in JAVA
		
		Properties prop = new Properties();
		
		//load all the properties in value-key with method
		
		
		prop.load(fis);
		value=prop.getProperty(key);
		
		return value;
	}
	
	public static String getLocatorData(String pageName,String elementName) throws IOException
	{
		String locator="";
		File f = new File("./src/test/data/locatorData.xlsx");
		FileInputStream fis = new FileInputStream(f);
		//creating object of workbook
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		//creating object of worksheet and getting worksheet
		XSSFSheet ws = wb.getSheet("sheet1");
		//to fetch the number of rows used in sheet
		int rows = ws.getLastRowNum();
		//writeLogs("no of used rows in a sheet1 " + rows);
		//fetch data thru the colunms ans rows by using for loop
		for(int i=1;i<=rows;i++)
		{
			String page= ws.getRow(i).getCell(0).getStringCellValue();
			String element= ws.getRow(i).getCell(1).getStringCellValue();
			
			if(pageName.equalsIgnoreCase(page) && elementName.equalsIgnoreCase(element) )
			{
			/*writeLogs(page);
			writeLogs(element);
			writeLogs(locatorData);
			*/
				
				 locator = ws.getRow(i).getCell(2).getStringCellValue();
				 break;
			}
			
			
		}
		
		
		
		
		wb.close();
		return locator;
		
	}

	public static String getTestData(String pageName,String elementName) throws IOException
	{
		String data="";
		File f = new File("./src/test/data/testdata.xlsx");
		FileInputStream fis = new FileInputStream(f);
		//creating object of workbook
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		//creating object of worksheet and getting worksheet
		XSSFSheet ws = wb.getSheet("sheet1");
		//to fetch the number of rows used in sheet
		int rows = ws.getLastRowNum();
		//writeLogs("no of used rows in a sheet1 " + rows);
		//fetch data thru the colunms ans rows by using for loop
		for(int i=1;i<=rows;i++)
		{
			String page= ws.getRow(i).getCell(0).getStringCellValue();
			String element= ws.getRow(i).getCell(1).getStringCellValue();
			
			if(pageName.equalsIgnoreCase(page) && elementName.equalsIgnoreCase(element) )
			{
			/*writeLogs(page);
			writeLogs(element);
			writeLogs(locatorData);
			*/
				
				 data = ws.getRow(i).getCell(2).getStringCellValue();
				 break;
			}
			
			
		}
		
		
		
		
		wb.close();
		return data;
		
	}
	@AfterMethod
	public static void closeBrowser()
	{
		driver.quit();
	}

	public static void writeResultsToFile(String testCaseName,String testCaseResult) throws Exception
	{
		File f = new File("./src/test/results/results.txt");
		FileWriter fw = new FileWriter(f,true);
		fw.write(testCaseName + "======" + testCaseResult + "\n");
		fw.flush();
		fw.close();
	}
	//*************Listeners starts from here***********
	
	public void onFinish(ITestContext arg0) {
		
		writeLogs("i am listener after finish the suite");
	}
	
	public void onStart(ITestContext arg0) {
		writeLogs("i am listener on starting the suite the suite");
		
	}
	
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
		
	}
	
	public void onTestFailure(ITestResult arg0) {
		
		writeLogs("i am listener during the failure of the testcase  "+arg0.getName());
		try {
			writeResultsToFile(arg0.getName(), "Fialed");
			takeScreenShot(arg0.getName());
			writeErrorLogs(arg0.getThrowable());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	public void onTestSkipped(ITestResult arg0) {
		
		
	}
	
	public void onTestStart(ITestResult arg0) {
		
		writeLogs("i am listener on starting the testcase"+arg0.getName());
	}
	
	public void onTestSuccess(ITestResult arg0) {
		
		writeLogs("i am listener on success of the testcase "+arg0.getName());
		try {
			writeResultsToFile(arg0.getName(),"pass");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
}
