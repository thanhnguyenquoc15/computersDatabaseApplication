package com.lib;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;

public class BrowserFactory {

	private static WebDriver driver;
	
	private BrowserFactory(){
		
	}
	//constructor
	public static WebDriver driver(){
		return driver;
	}
	
	public static WebDriver createDriver(String browserName) {
		browserName = browserName.toLowerCase().trim();
		switch (browserName) {
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "win64geckodriver.exe");
			// driver=new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			// driver=new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
			break;
		case "chrome":
			System.setProperty("webdriver.chrome.driver",
					ReadData.SUITE_PATH + "\\src\\main\\java\\com\\utilities\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			break;
		default:
			System.setProperty("webdriver.chrome.driver",
					ReadData.SUITE_PATH + "\\src\\main\\java\\com\\utilities\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		}
		return driver;
	}

}
