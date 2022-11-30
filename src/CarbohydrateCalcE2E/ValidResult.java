package CarbohydrateCalcE2E;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;


public class ValidResult {
	public WebDriver driver = new ChromeDriver();
	String appUrl = "https://www.calculator.net/carbohydrate-calculator.html";
	
	String ageInputXPath =  "//*[@id=\"cage\"]";
	String femaleInputXPath =  "//*[@id=\"calinputtable\"]/tbody/tr[2]/td[2]/label[2]/span";
	String maleInputXPath =  "//*[@id=\"calinputtable\"]/tbody/tr[2]/td[2]/label[1]/span";
	
	String heightInputXPath =  "//*[@id=\"cheightmeter\"]";
	String weightInputXPath =  "//*[@id=\"ckg\"]";
	
	String activityInputXPath = "//*[@id=\"cactivity\"]";
	
	String calculateButtonXPath =  "//*[@id=\"content\"]/div[5]/table[4]/tbody/tr[3]/td[2]/input[2]";
	
	String resultXPath =  "//*[@id='content']/h2";
	String resultBigTextXpath = "//*[@id='content']/p[2]";
	
	String errorTextXpath = "//*[@id=\"content\"]/div[3]/font";
	
	String resultTableXpath = "//*[@id=\"content\"]/table";

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	public void fillInput(String xPath, String value) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
		WebElement input = driver.findElement(By.xpath(xPath));
		input.clear();
		input.sendKeys(value);
	}
	
	public void fillAge(String value) {
		fillInput(ageInputXPath, value);
	}
	
	public void fillHeight(String value) {
		fillInput(heightInputXPath, value);
	}

	public void fillWeight(String value) {
		fillInput(weightInputXPath, value);
	}
	
	public void selectActivity(String value) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(activityInputXPath)));
		WebElement input = driver.findElement(By.xpath(activityInputXPath));
		
		Select select = new Select(input);

		select.selectByValue(value);
	}
	
	public void selectGender(String value) {
		if(value.equals("female")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(femaleInputXPath)));
		    WebElement femaleInput = driver.findElement(By.xpath(femaleInputXPath));
		    femaleInput.click();
		}else if (value.equals("male")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(maleInputXPath)));
		    WebElement maleInput = driver.findElement(By.xpath(maleInputXPath));
		    maleInput.click();
		}
	}
	
	public void clickCalculateButton() {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(calculateButtonXPath)));
		WebElement calculateButton = driver.findElement(By.xpath(calculateButtonXPath));
	    calculateButton.click();
	}
	
	public String getTextFrom(String xPath) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
		WebElement element = driver.findElement(By.xpath(xPath));
		return element.getText();
	}
	
	@BeforeSuite
	public void goToUrl() {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		driver.manage().window().maximize();
	}
	
	@Test
	public void calculateWithBasicDetails() {
		driver.get(this.appUrl);

		fillAge("20");
		selectGender("female");
		
		fillHeight("181");
		fillWeight("61");
		
		selectActivity("1.2");
		clickCalculateButton();
		
		Assert.assertEquals(getTextFrom(resultXPath), "Result");
		Assert.assertEquals(getTextFrom(resultBigTextXpath), "It is recommended that carbohydrates comprise 40-75% of daily caloric intake.");
		Assert.assertEquals(getTextFrom(resultTableXpath), "Goal Daily Calorie Allowance 40%* 55%* 65%* 75%*\n" + 
				"Weight Maintenance 1,776 Calories 189 grams 261 grams 308 grams 355 grams\n" + 
				"Lose 0.5 kg/week 1,276 Calories 136 grams 187 grams 221 grams 255 grams\n" + 
				"Lose 1 kg/week 776 Calories 83 grams 114 grams 135 grams 155 grams\n" + 
				"Gain 0.5 kg/week 2,276 Calories 243 grams 334 grams 395 grams 455 grams\n" + 
				"Gain 1 kg/week 2,776 Calories 296 grams 407 grams 481 grams 555 grams");
	}
	
	@Test
	public void checkAgeError() {
		driver.get(this.appUrl);

		fillAge("4");
		selectGender("female");
		
		fillHeight("181");
		fillWeight("61");
		
		selectActivity("1.2");
		clickCalculateButton();
		
		Assert.assertEquals(getTextFrom(errorTextXpath), "Please provide an age between 18 and 80.");
	}
	
	@AfterSuite
	public void closeBrowser() {
		driver.close();
	}
}
