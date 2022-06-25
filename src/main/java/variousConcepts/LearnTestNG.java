package variousConcepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LearnTestNG {

	WebDriver driver;
	String browser;
	String url;

	// Element list
	By USER_NAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By LOGIN_BUTTON_FIELD = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASHBOARD_HEADER_FIELD = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By ADD_CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"account\"]");
	By COMPANY_FIELD = By.xpath("//*[@id=\"cid\"]");
	By EMAIL_FIELD = By.xpath("//*[@id=\"email\"]");
	By COUNTRY_FIELD = By.xpath("//*[@id=\"country\"]");

	// Test data
	String userName = "demo@techfios.com";
	String password = "abc123";
	String dashboarHeader = "Dashboard";

	@BeforeClass
	public void readConfig() {

		// InputStream //BufferedReader //Scanner //FileReader

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Firefox")) {
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.get(url);
	}

	@Test
	public void loginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(LOGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), dashboarHeader,
				"Dashboard is not available.");
	}
	
	

	@Test
	public void logCustomer() throws InterruptedException {

		loginTest();
		driver.findElement(CUSTOMER_MENU_BUTTON_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_BUTTON_FIELD).click();
		Thread.sleep(3000);
		boolean fullNameField = driver.findElement(FULL_NAME_FIELD).isDisplayed();
		Assert.assertTrue(fullNameField, "Add customer page is not available");
	
		driver.findElement(FULL_NAME_FIELD).sendKeys("Selenium" + generateRandomNo(999));
		selectFromDropdown(COMPANY_FIELD, "Techfios");
		driver.findElement(EMAIL_FIELD).sendKeys("abc" + generateRandomNo(9999) + "@techfios.com");
		selectFromDropdown(COUNTRY_FIELD, "Afghanistan");


	}

	public int generateRandomNo(int boundryNo) {
		Random rnd = new Random();
		int randomNumber = rnd.nextInt(boundryNo);
		return randomNumber;

	}

	public void selectFromDropdown(By byLocator, String visibleText) {
		
		Select sel1 = new Select(driver.findElement(byLocator));
		sel1.selectByVisibleText(visibleText);
		
	}

	

	// @AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
