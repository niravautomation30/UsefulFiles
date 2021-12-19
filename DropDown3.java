package selenium;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DropDown3 {

	static WebDriver driver;

	@Test(dataProvider = "getdate")
	public void test(String d, String m, String y) throws InterruptedException {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.get("https://corporate.spicejet.com/default.aspx");
//		doFromandToSelection("ctl00_mainContent_ddl_originStation1_CTXT", "Pune");
//		
//		doFromandToSelection("ctl00_mainContent_ddl_destinationStation1_CTXT", "Delhi");		

		String dayToBeSelected = d;
		String monthToBeSelected = m;
		String yearToBeSelected = y;
		String completeDateToBeSelected = String.join("-", dayToBeSelected, monthToBeSelected, yearToBeSelected);
		System.out.println("Depature date =" + completeDateToBeSelected);

		LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
		System.out.println("currentDate=" + currentDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
		LocalDate formattedDepartDate = LocalDate.parse(completeDateToBeSelected, formatter);
		System.out.println("formattedDepartDate =" + formattedDepartDate);
		boolean isValidDate = true;

		if (formattedDepartDate.isBefore(currentDate)) {
			System.out.println("Depart Date cannot be a past date.");
			isValidDate = false;
		}
		LocalDate maxDate = currentDate.plusYears(1);

		if (formattedDepartDate.isAfter(maxDate)) {
			System.out.println("Depart Date is not allow more than year from curren date.");
			isValidDate = false;
		}
		if (isValidDate) {
			driver.findElement(By.xpath("//input[@name='ctl00$mainContent$view_date1']")).click();

			By year = By.xpath("//div[@class='ui-datepicker-title']/span[2]");
			By month = By.xpath("//div[@class='ui-datepicker-title']/span[1]");
			By nextBtn = By.xpath("//a/span[text()='Next']");
			By prevBtn = By.xpath("//a/span[text()='Prev']");
			By date = By.xpath("//a[text()='" + dayToBeSelected + "']");

			String curerntYear = driver.findElement(year).getText();
			String curerntMonth = driver.findElement(month).getText();

			doClickCalender(curerntYear, yearToBeSelected, nextBtn, year);
			doClickCalender(curerntMonth, monthToBeSelected, nextBtn, month);

			driver.findElement(date).click();
			driver.quit();
		}

	}

	public static void doFromandToSelection(String loc, String state) {
		driver.findElement(By.id(loc)).click();
		List<WebElement> allUL = driver.findElements(By.xpath("(//div[@id='dropdownGroup1']/div/ul)"));
		for (int i = 1; i <= allUL.size(); i++) {
			List<WebElement> ll = driver.findElements(By.xpath("(//div[@id='dropdownGroup1']/div/ul)[" + i + "]/li"));
			for (WebElement e : ll) {
//				System.out.println(e.getText());				
				if (e.getText().contains(state)) {
					e.click();
					break;
				}
			}
		}
	}

	public static void doClickCalender(String crntYearOrMonth, String slctYearOrMonth, By nextLoc, By locator)
			throws InterruptedException {
		if (!crntYearOrMonth.equals(slctYearOrMonth)) {
			boolean flag = false;
			do {
				if (driver.findElement(nextLoc).isEnabled()) {
					driver.findElement(nextLoc).click();
					Thread.sleep(3000);
					System.out.println("click");
					String afterNext = driver.findElement(locator).getText();
					System.out.println("AfterNext=" + afterNext);
					flag = afterNext.equals(slctYearOrMonth);
				}
			} while (!flag);
		}
	}
	
	@DataProvider
	public Object[][] getdate(){
		return new Object[][]{
			{"11","December","2021"},
			{"31","December","2022"},
			{"11","April","2022"}
		};
	}

}
