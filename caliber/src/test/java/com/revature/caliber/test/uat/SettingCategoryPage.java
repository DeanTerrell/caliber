package com.revature.caliber.test.uat;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class SettingCategoryPage {
	//private PhantomJSDriver driver;
	private WebDriver driver;
	
	public SettingCategoryPage(WebDriver driver) {
		super();
		this.driver = (ChromeDriver)driver;
		this.driver.get("http://localhost:8080/caliber#/vp/category");
	}
	
	/**
	 * Navigates to the Setting's Category page
	 * @throws InterruptedException 
	 */
    public void gotoSettingCategoryPage() throws InterruptedException {
        driver.get("http://localhost:8080/caliber/#/vp/category");
        Thread.sleep(1000);
        driver.switchTo().activeElement();
        //System.out.println("CurrentURL = " + driver.getCurrentUrl());
        //System.out.println(driver.getTitle());
    }
    
    /**
     * Verifies that we are located on the Setting's Category page
     */
    public void verifyCategoryPage() {
        assertEquals("http://localhost:8080/caliber/#/vp/category", driver.getCurrentUrl());
    }
    
    /**
     * Clicks on the Create button to create a new category
     * @throws InterruptedException
     */
    public void clickCreateCategoryBtn() throws InterruptedException {
    	driver.findElement(By
    			.id("createCategoryModal")).click();
    	Thread.sleep(1000);
    	driver.switchTo().activeElement();
    	//driver.switchTo().activeElement();
    	
    	/*
        driver.findElement(
                By.cssSelector("body > div > ui-view > ui-view > div:nth-child(1) > div > div > div > ul > li > a"))
                .click();
        Thread.sleep(500);
        driver.switchTo().activeElement();
        */
    }
    
    /**
     * Finds the Category name field, and edits it by adding the new name
     * @param name
     * @throws InterruptedException 
     */
    public void inputCategoryName(String name) throws InterruptedException {
    	Thread.sleep(1000);
    	driver.findElement(By.cssSelector("#addCategoryModal > div > div > div.modal-body > div > div.row > div > input")).sendKeys(name);
    	/*
        driver.findElement(By.id("categoryName")).sendKeys(name);
        */
    	//driver.findElement(By.id("categoryName")).click();
    	/*
    	driver.findElement(By
    			.id("categoryName")).sendKeys(name);
    	driver.findElement(By
    			.xpath("//*[@id=\"categoryName\"]")).sendKeys(name);
    	*/
    }
    
    /**
     * Clicks on the save button to save the new category
     */
    public void clickCategorySaveBtn() {
    	driver.findElement(By.id("closeBtn")).click();
    	/*driver.findElement(By
    			.id("submitBtn")).click();*/
    	/*
        driver.findElement(
                By.cssSelector("#addCategoryModal > div > div > div.modal-body > div > div.modal-footer > input"))
                .click();
        */
    }
    
    /**
     * Creates a jpg file of the screenshot to check that we created the category
     * @throws IOException
     */
    public void verifyCategoryAdded() throws IOException {
    	File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    	FileUtils.copyFile(srcFile,
    			new File("~/Desktop/addedCategory.jpg"), true);
    }
    
    /**
     * Clicks on the x-button to close out of the Create Category modal
     */
    public void closeCattegoryWithXButton() {
    	driver.findElement(By
    			.id("XBtn")).click();
    	/*
    	driver.findElement(
    			By.cssSelector("#addCategoryModal > div > div > div.modal-header > button"))
    			.click();
    	*/
    }
    
    /**
     * Closes the Create Category modal by clicking the Close button
     */
    public void closeCategoryWithCloseButton() {
    	driver.findElement(By
    			.id("closeBtn")).click();

    	/*
    	driver.findElement(
    			By.cssSelector("#addCategoryModal > div > div > div.modal-body > div > div.modal-footer > button"))
    			.click();
    	*/
    }
    
    /**
     * Verifies that the modal was closed using the x-button
     * @throws IOException
     */
    public void verifyClosedOutByX() throws IOException {
    	File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    	FileUtils.copyFile(srcFile,
    			new File("~/Desktop/xOutCategoryNotCreated.jpg"), true);
    }
    
    /**
     * Verifies that the modal was closed using the Close button
     * @throws IOException
     */
    public void verifyClosedOutByCloseButton() throws IOException {
    	File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    	FileUtils.copyFile(srcFile,
    			new File("~/Desktop/closeButtonCategoryNotCreated.jpg"), true);
    }
    
    public void closeDriver() {
        driver.quit();
    }
}
