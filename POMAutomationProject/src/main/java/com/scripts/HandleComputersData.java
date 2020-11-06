package com.scripts;

import java.io.IOException;
import java.sql.Driver;
import java.text.ParseException;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.frame.CommonFunction;
import com.frame.MainPage;
import com.lib.ReadData;

public class HandleComputersData extends HelperClass {

	protected MainPage MainPageObj;

	protected Logger log = Logger.getLogger(this.getClass().getName());

	protected CommonFunction funcObj = new CommonFunction();

	@Override
	public void performBeforeMethodOperation() {
		// TODO Auto-generated method stub
		MainPageObj = PageFactory.initElements(BrowserFactory.driver(), MainPage.class);
		log.info("-----");
	}

	@Override
	public void performAfterMethodOperation() {
		// TODO Auto-generated method stub
		log.info("-----------");

	}

	@Test(dataProvider = "dataMap", dataProviderClass = ReadData.class)
	public void createComputerScenario(Hashtable testData) throws Exception, ParseException {
		log.info("Test Case:" + testData.get("Test Name").toString() + "=> Start");
		log.info("-----------------------------------------------------------");
		log.info(testData);
		log.info("-----------------------------------------------------------");
		String compName = testData.get("Computer Name").toString();
		String introDate = testData.get("Introduced date").toString();
		String disContDate = testData.get("Discontinued date").toString();
		String companyName = testData.get("Company").toString();
		String confirm = testData.get("Confirm Operation").toString();
		String errorColumn = testData.get("Error Column").toString();
		
		try {
			//create computer
			boolean isCreated = MainPageObj.createComputer(compName, 
															introDate, 
															disContDate, 
															companyName, 
															confirm, 
															errorColumn);
			log.info("Verify computer details data in database");
			if (isCreated) {
				MainPageObj.verifyCompDetails(compName, introDate, disContDate, companyName);
			} else {
				if(!compName.equals("")) Assert.assertTrue(!MainPageObj.filterComp(compName));
			}
	
			
			log.info("Test Case:" + testData.get("Test Name").toString() + "=> Ended");
			log.info("-----------------------------------------------------------");
		} finally {
			if (testData.get("Clean Data").toString().toLowerCase().contains("yes")) {
				try {
					if (!compName.equals("")) MainPageObj.deleteCompAllResult(compName);
				} catch (Exception e) {
					
				}
			}
		}	
	}
	
	
	@Test(dataProvider = "dataMap", dataProviderClass = ReadData.class)
	public void updateComputerScenario(Hashtable testData) throws Exception, ParseException {
		log.info("Test Case:" + testData.get("Test Name").toString() + "=> Start");
		log.info("-----------------------------------------------------------");
		log.info(testData);
		log.info("-----------------------------------------------------------");
		String compName = testData.get("Computer Name").toString();
		String introDate = testData.get("Introduced date").toString();
		String disContDate = testData.get("Discontinued date").toString();
		String companyName = testData.get("Company").toString();
		String confirm = testData.get("Confirm Operation").toString();
		String errorColumn = testData.get("Error Column").toString();
		
		try {
			//create computer
			boolean isUpdated = MainPageObj.updateCompDetails(	compName,
																introDate, 
																disContDate, 
																companyName, 
																confirm, 
																errorColumn);
			log.info("Verify computer details data in database");
			if (isUpdated) {
				MainPageObj.verifyCompDetails(compName, introDate, disContDate, companyName);
			} 
	
			log.info("Test Case:" + testData.get("Test Name").toString() + "=> Ended");
			log.info("-----------------------------------------------------------");
		} finally {
			if (testData.get("Clean Data").toString().toLowerCase().contains("yes")) {
				try {
//					if (!compName.equals("")) MainPageObj.deleteCompAllResult(compName);
				} catch (Exception e) {
					
				}
			}
		}	
	}

	@Test(dataProvider = "dataMap", dataProviderClass = ReadData.class)
	public void verifyDataSorting(Hashtable testData) throws Exception, ParseException {
		log.info("Test Case:" + testData.get("Test Name").toString() + "=> Start");
		log.info("-----------------------------------------------------------");
		log.info(testData);
		log.info("-----------------------------------------------------------");
		String compName = testData.get("Computer Name").toString();
		String sortColumn = testData.get("Sort").toString();
		String order = testData.get("Order").toString();
		
		try {
			//create computer
			MainPageObj.filterComp(compName);
			
			MainPageObj.sortByColumn(sortColumn, order);
	
			log.info("Test Case:" + testData.get("Test Name").toString() + "=> Ended");
			log.info("-----------------------------------------------------------");
		} finally {
			if (testData.get("Clean Data").toString().toLowerCase().contains("yes")) {
				try {
//					if (!compName.equals("")) MainPageObj.deleteCompAllResult(compName);
				} catch (Exception e) {
					
				}
			}
		}	
	}

}