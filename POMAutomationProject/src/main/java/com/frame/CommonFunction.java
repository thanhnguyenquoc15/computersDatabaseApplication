package com.frame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CommonFunction {

	// Define Logger and driver
	protected Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 This method is used to select value from dropdown list.
	 * @param   driver Selenium WebDriver object
	 * @param   element Dropdown/Combo box object
	 * @param   sItem Item to select from dropdown
	 * @return  String - returns selected value from dropdown .
	 */
	public boolean selectDropDownOption(WebDriver driver, WebElement element, String sItem)
	{
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		try
		{
			Select selectedOption = new Select(element);
			//selectedOption.selectByValue(sItem);
			selectedOption.selectByVisibleText(sItem);
			return true;
		} catch (Exception e)
		{
			//new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
	}
	
	/**
	 * This method is used to wait execution or process till user defined time.
	 * @param secs time specified in seconds
	 * @return Noting .
	 *
	 */
	public void wait(int secs)
	{
		// call a native sleep
		try
		{
			log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
			log.info("Adding wait for: " + secs + " seconds..");
			Thread.sleep(secs * 1000);
		} catch (Exception e)
		{
//			log.error("Exception occurred in {createDIR} method...");
		}
	}
	

	public String convertDateFormat(String date, String inFormat, String outFormat) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		String outputDate = "errorWhileConvertDate";
		try {    		    
		    DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern(inFormat);
		    DateTimeFormatter newFormat = DateTimeFormatter.ofPattern(outFormat);
		    outputDate = LocalDate.parse(date, oldFormat).format(newFormat);		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		 return outputDate;	
	}

	public String convertDateFormat(String date) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());		
//		String outputDate = "errorWhileConvertDate";
		String outputDate = "";
		try {		    

		    DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
		    DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    outputDate = LocalDate.parse(date, oldFormat).format(newFormat);	
		    
		} catch (Exception e) {
//		    e.printStackTrace();
		}
		log.info(outputDate);
		return outputDate;	
	}
	
	
	
}
