package com.revature.caliber.test.uat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.itextpdf.text.log.SysoCounter;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DownloadPdfFeature {

	private ReportsPage reportsPage;

	@Before // each scenario
	public void setup(){
		ChromeDriverSetup setup = new ChromeDriverSetup();
		reportsPage = new ReportsPage(setup.getDriver());
	}

//	@After // each scenario
//	public void teardown() {
//		reportsPage.closeDriver();
//	}

	@Given("^I am on the Reports page$")
	public void iAmOnTheReportsPage() throws Throwable {
		reportsPage.gotoReportsPage();
		reportsPage.verifyReportsPage();

	}

	@Given("^I have selected the year (\\d+) tab$")
	public void iHaveSelectedTheYearTab(int year) throws Throwable {
		//Implemented the click dropdown in the method
		reportsPage.clickReportYear("2017");
	}

	@Given("^I have selected \"([^\"]*)\" as Trainer$")
	public void iHaveSelectedAsTrainer(String trainer) throws Throwable {
		reportsPage.clickBatchDropdown();
		reportsPage.chooseBatch(trainer);
	}

	@Given("^I have selected all the weeks$")
	public void iHaveSelectedAllTheWeeks() throws Throwable {
		reportsPage.clickWeekDropdown();
		reportsPage.chooseWeekReport("week All");
	}

	@Given("^I have selected \"([^\"]*)\" as Trainees$")
	public void iHaveSelectedAsTrainees(String arg1) throws Throwable {
		reportsPage.clickTraineeDropdown();
		reportsPage.chooseTraineeReport("Ali, Fareed");
	}

	@When("^I click the download button$")
	public void iClickTheDownloadButton() throws Throwable {
		// Same for Cumulative Scores, Technical Skills, and Weekly Progress
		 reportsPage.clickChartDropdownPdf();
		 
	}

	@Then("^a PDF file is downloaded$")
	public void aPDFFileIsDownloaded() throws Throwable {
		reportsPage.clickChartFeedbackDownloadPdf();
		 //or reportsPage.clickChartDownloadPdf();	
		
		reportsPage.closeDriver();
	}

}