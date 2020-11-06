package com.frame;

import java.util.Hashtable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.lib.BrowserFactory;

public class MainPage {

	// contrustor
//	public MainPage(WebDriver dr) {
//		this.driver = BrowserFactory.driver();
//	}

	private WebDriver driver = BrowserFactory.driver();

	CommonFunction funcObj = new CommonFunction();

	// Define logger
	protected Logger log = Logger.getLogger(this.getClass().getName());

	// Page Objects

	@FindBy(id = "add")
	public WebElement addComputerBtn;
	
	@FindBy(id = "searchbox")
	public WebElement filterBox;

	@FindBy(id = "searchsubmit")
	public WebElement filterBtn;

	@FindBy(xpath = "//li[@class='next']/a")
	public WebElement nextPage;

	@FindBy(xpath = "//li[@class='prev']/a")
	public WebElement prevPage;

	@FindBy(id = "name")
	public WebElement computerNameTextBox;

	@FindBy(id = "introduced")
	public WebElement introTextBox;

	@FindBy(id = "discontinued")
	public WebElement disContTextBox;

	@FindBy(id = "company")
	public WebElement companySelectBox;

	@FindBy(xpath = "//*[@value='Create this computer']")
	public WebElement createCompBtn;
	
	@FindBy(xpath = "//*[@value='Save this computer']")
	public WebElement saveBtn;

	@FindBy(xpath = "//*[@class='btn' and contains(text(),'Cancel')] ")
	public WebElement cancelBtn;
	
	@FindBy(xpath = "//*[@value='Delete this computer']")
	public WebElement deleteCompBtn;

	// columnHeaderXpath
	// thead//a[contains(text(),'Computer name')]

	/**
	 * description: method to create a new computer will return TRUE if computer is
	 * created with a warning message otherwise return FALSE
	 * @param columnToVerifyError: Name of the column that will have error message
	 */
	public boolean createComputer(String computerName, 
								String introDate, 
								String disContDate, 
								String companyName,
								String operation, 
								String columnToVerifyError) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		log.info("Click Add Button");
		this.addComputerBtn.click();

		log.info("Fill in Computer Name");
		funcObj.wait(1);
		this.computerNameTextBox.sendKeys(computerName);

		log.info("Fill in Introduced Date");
		this.introTextBox.sendKeys(introDate);

		log.info("Fill in Discontinued Date");
		this.disContTextBox.sendKeys(disContDate);
		
		log.info("Choose a Company");
		funcObj.selectDropDownOption(driver, companySelectBox, companyName);

		log.info("Click button: " + operation);

		//if we click create button
		if (operation.toLowerCase().contains("create")) {
			createCompBtn.click();

			List<WebElement> errors = driver.findElements(By.xpath("//div[@class='clearfix error']"));
			Set<String> errorColumns = new HashSet<String>();
			for (WebElement error : errors) {
				String column = error.findElement(By.xpath(".//label")).getText().toLowerCase();
				errorColumns.add(column);
				log.info(column + " has error.");
			}		
	
			Boolean errorUI = errors.size() != 0;
			Boolean errorData = columnToVerifyError.length() > 1;
			switch ("ErrorUI = " + errorUI.toString() + " - ErrorData = " + errorData.toString()) {

			// if we click create button And there is error on UI
			case "ErrorUI = true - ErrorData = true":
				Assert.assertTrue(errorColumns.contains(columnToVerifyError.toLowerCase()),
						"There is no error on column" + columnToVerifyError + "as expected");
				log.info("Error on column: " + columnToVerifyError + " is displayed as expected");
				cancelBtn.click();
				return false;
				
			case "ErrorUI = true - ErrorData = false":
				Assert.assertTrue(false, "There is an unexpected error while create Computer");
				break;
				
			// if we click create button and there is no error on UI
			case "ErrorUI = false - ErrorData = true":
				// code
				Assert.assertTrue(false, "There should be error on column " + columnToVerifyError);
				break;
				
			default: //case "false-false":
				try {
					driver.findElement(By.xpath("//strong[contains(text(),'Done!')]"));
					log.info("Computer :" + computerName + " created Successfully");
					return true;
				} catch (Exception e) {
					log.info("No successful warning is displayed");
					Assert.assertTrue(false, "Should have a successful message!");
				}
			
			}
		//if we click cancel button
		} else if (operation.toLowerCase().contains("cancel")) {
			log.info("Computer will not be created, click Cancel");
			cancelBtn.click();
			return false;
		}
		return false;
	}

	
	
	/**
	 * description: 
	 * 
	 * @param 
	 * return TRUE is computers is existed
	 */
	public boolean filterComp(String computerName) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		this.filterBox.clear();
		this.filterBox.sendKeys(computerName);
//		funcObj.wait(1);
		log.info("Filter computer with: " + computerName);
		this.filterBtn.click();
		String result = driver.findElement(By.xpath("//section//h1")).getText();
		log.info(result);
		List<WebElement> computers = driver.findElements(By.linkText(computerName));
		if(computers.size() > 0) {	
			return true;
		}
		return false;
	}
	
	/**
	 * description: 
	 * 
	 * @param 
	 */
	public boolean deleteCompAllResult(String computerName) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		List<WebElement> computers = null;
//		for (int i = 0; i < compAmount; i++) {
		do {
			//find computer to delete
			this.filterComp(computerName);
			
			computers = driver.findElements(By.linkText(computerName));

			for (WebElement computer : computers) {
				if (computer.getText().equals(computerName)) {
					computer.click();
					this.deleteCompBtn.click();
					try {
						driver.findElement(By.xpath("//strong[contains(text(),'Done!')]"));
						log.info("Computer :" + computerName + " deleted Successfully");
						break;
					} catch (Exception e) {
						log.info("No successful warning is displayed");
						Assert.assertTrue(false, "Should have a successful message!");
					}
				}
			}
		} while (computers.size() > 0);
		return true;
	}
	
	/**
	 * description: 
	 * 
	 * @param 
	 */
	public boolean deleteComp1stResult(String computerName) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		List<WebElement> computers = null;
//		for (int i = 0; i < compAmount; i++) {

		// find computer to delete
		this.filterComp(computerName);
		
		// delete the only the 1st matched computer in list found
//		computers = driver.findElements(By.xpath("//td/a[contains(text(),'" + computerName + "')]"));
		computers = driver.findElements(By.linkText(computerName));
		for (WebElement computer : computers) {
			if (computer.getText().equals(computerName)) {
				computer.click();
				this.deleteCompBtn.click();
				try {
					driver.findElement(By.xpath("//strong[contains(text(),'Done!')]"));
					log.info("Computer :" + computerName + " deleted Successfully");
					return true;
				} catch (Exception e) {
					log.info("No successful warning is displayed");
					Assert.assertTrue(false, "Should have a successful message!");
				}
			}
		}
		return false;
	}
	
	/**
	 * description: 
	 * 
	 * @param 
	 */
	public void verifyCompDetails(	String computerName, 
									String introDate, 
									String disContDate, 
									String company) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		this.filterComp(computerName);
		
		List<WebElement> computers = null;
		
		computers = driver.findElements(By.xpath("//tbody//tr"));
//		computers = driver.findElements(By.linkText(computerName));
		for (WebElement computer : computers) {
			if (computer.findElement(By.xpath(".//td[2]")).getText().equals(computerName)) {
				String introData = computer.findElement(By.xpath(".//td[2]")).getText();
				introData = funcObj.convertDateFormat(introData);
				log.info("Compare if Introduced date is match");
				log.info(introData + " .vs. " + introDate);
				Assert.assertEquals(introData, introDate, "Introduced date is not match");

				String disContData = computer.findElement(By.xpath(".//td[3]")).getText();
				disContData = funcObj.convertDateFormat(disContData);
				log.info("Compare if Discontinued date is match");
				log.info(disContData + " .vs. " + disContDate);
				Assert.assertEquals(disContData, disContDate, "Discontinued date is not match");

				log.info("Compare if Company Name is match");
				String companyData = computer.findElement(By.xpath(".//td[4]")).getText();
				if (companyData.equals("-"))
					companyData = companyData.replace("-", "");
				log.info(companyData + " .vs. " + company);
				Assert.assertEquals(companyData, company, "Company name is not match");
			}
		}
		
	}
	
	
	/**
	 * description: 
	 * 
	 * @param 
	 */
	public boolean updateCompDetails(String computerName, 
									String introDate, 
									String disContDate, 
									String companyName,
									String operation, 
									String columnToVerifyError) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		List<WebElement> computers = null;
		// find computer to update
		this.filterComp(computerName);

		computers = driver.findElements(By.linkText(computerName));
		for (WebElement computer : computers) {
			if (computer.getText().equals(computerName)) {
				computer.click();
				// updating codes here
				funcObj.wait(1);
				if (!computerName.equals("")) {
					log.info("Fill in Computer Name");
					this.computerNameTextBox.sendKeys(computerName);
				}

				if (!introDate.equals("")) {
					log.info("Fill in Introduced Date");
					this.introTextBox.sendKeys(introDate);
				}

				if (!computerName.equals("")) {
					log.info("Fill in Discontinued Date");
					this.disContTextBox.sendKeys(disContDate);
				}

				if (!computerName.equals("")) {
					log.info("Choose a Company");
					funcObj.selectDropDownOption(driver, companySelectBox, companyName);
				}

				log.info("Click button: " + operation);

				// if we click save button
				if (operation.toLowerCase().contains("save")) {
					this.saveBtn.click();

					List<WebElement> errors = driver.findElements(By.xpath("//div[@class='clearfix error']"));
					Set<String> errorColumns = new HashSet<String>();
					for (WebElement error : errors) {
						String column = error.findElement(By.xpath(".//label")).getText().toLowerCase();
						errorColumns.add(column);
						log.info(column + " has error.");
					}

					Boolean errorUI = errors.size() != 0;
					Boolean errorData = columnToVerifyError.length() > 1;
					switch ("ErrorUI = " + errorUI.toString() + " - ErrorData = " + errorData.toString()) {

					// if we click save button And there is error on UI
					case "ErrorUI = true - ErrorData = true":
						Assert.assertTrue(errorColumns.contains(columnToVerifyError.toLowerCase()),
								"There is no error on column" + columnToVerifyError + "as expected");
						log.info("Error on column: " + columnToVerifyError + " is displayed as expected");
						cancelBtn.click();
						return false;

					case "ErrorUI = true - ErrorData = false":
						Assert.assertTrue(false, "There is an unexpected error while create Computer");
						break;

					// if we click create button and there is no error on UI
					case "ErrorUI = false - ErrorData = true":
						// code
						Assert.assertTrue(false, "There should be error on column " + columnToVerifyError);
						break;

					default: // case "false-false":
						try {
							driver.findElement(By.xpath("//strong[contains(text(),'Done!')]"));
							log.info("Computer :" + computerName + " created Successfully");
							return true;
						} catch (Exception e) {
							log.info("No successful warning is displayed");
							Assert.assertTrue(false, "Should have a successful message!");
						}
					}
				// if we click cancel button
				} else if (operation.toLowerCase().contains("cancel")) {
					log.info("Computer will not be created, click Cancel");
					cancelBtn.click();
					return false;
				}
				return false;

			}
		}
		return false;
	}
	
	
	/**
	 * description:
	 * 
	 * @param
	 */
	public boolean sortByColumn(String sortColumn, String order) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement columnHeader = driver.findElement(By.xpath("//thead"));
		WebElement column = columnHeader.findElement(By.linkText(sortColumn));

		order = order.toLowerCase();
		switch (order) {
		case "ascending":
			log.info("Click once on " + sortColumn + " to sort in ascending order");
			column.click();
			break;
		case "descending":
			log.info("Click twice on " + sortColumn + " to sort in ascending order");
			column.click();
			funcObj.wait(2);
			columnHeader.findElement(By.linkText(sortColumn)).click();
			break;
		default:
			log.info("Data is not sorting");
			return false;
		}
		
		if(!sortColumn.equals("Introduced")||
				!sortColumn.equals("Discontinued")) {
			this.verifySortOrder(sortColumn, "String", order);
		} else {
			this.verifySortOrder(sortColumn, "Date", order);;
		}

		return true;
	}
	
	/**
	 * description:
	 * 
	 * @param
	 */
	private void verifySortOrder(String columnName, String dataType, String sortOrder) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

		int colIndex = getColumnIndex(columnName);
		String xpath = "//tbody//tr//td[" + colIndex + "]";
		sortOrder = sortOrder.toLowerCase().trim();

		//add an loop here to go through all pages to continue verify
		
		List<WebElement> values = driver.findElements(By.xpath(xpath));
		// get the 1st row value to compare
		String tmpVal = "";
		try {
			tmpVal = values.get(0).getText().toString();
		} catch (Exception e)
		{
			log.info("error");
		}
		int equalTimes = 0;
		// start to compare
		for (WebElement value : values) {
			
			//if 5 continuous row have same data then we can quit
			if(equalTimes >5) break;
			
			String currtVal = value.getText().toString();
			if (dataType.equals("date")) {
				currtVal = funcObj.convertDateFormat(currtVal);
			}
			log.info(currtVal);
			switch (sortOrder) {
			case "ascending":
				// should be minus
				// get current row value
				Assert.assertTrue(tmpVal.compareTo(currtVal) <= 0, "Data doesn't sort follow ascending order");
				equalTimes = 0;
				break;
			case "descending":
				// should be plus
				Assert.assertTrue(tmpVal.compareTo(currtVal) >= 0, "Data doesn't sort follow descending order");
				equalTimes = 0;
				break;
			default:
				equalTimes++;
				break;
			}
			tmpVal = currtVal;
		}

	}
	
	/**
	 * description:
	 * 
	 * @param
	 */
	private int getColumnIndex(String columnName) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		List<WebElement> columns = driver.findElements(By.xpath("//thead//th"));
		
		//in case cant get Index
		int index = -1;
		
		for(int i = 1; i <= columns.size(); i++) {
//			if(columns.get(i-1).toString().equals(columnName)) index = i;
			if(columns.get(i-1).getText().toString().equals(columnName)) index = i;
		}
		
		return index;
	}
	
}
