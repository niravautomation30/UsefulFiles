package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Match;
import org.sikuli.script.Mouse;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class PerformAction {

	public WebDriver driver;
	public Screen s = new Screen();

	public PerformAction(WebDriver driver) {
		this.driver = driver;
	}

	public By getBy(String locatorType, String locatorValue) {

		By locator = null;

		switch (locatorType.toLowerCase()) {
		case "id":
			locator = By.id(locatorValue);
			break;
		case "name":
			locator = By.name(locatorValue);
			break;
		case "xpath":
			locator = By.xpath(locatorValue);
			break;
		case "css":
			locator = By.cssSelector(locatorValue);
			break;
		case "linktext":
			locator = By.linkText(locatorValue);
			break;
		case "partiallinktext":
			locator = By.partialLinkText(locatorValue);
			break;
		case "cssselector":
			locator = By.cssSelector(locatorValue);
			break;

		default:
			break;
		}

		return locator;

	}

	public WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public WebElement getElement(String locatorType, String locatorValue) {
		return driver.findElement(this.getBy(locatorType, locatorValue));
	}

	public void delayExecution(long timeInMiliSeconds) {
		float timeInSecond = timeInMiliSeconds / (float) 1000.0;
		System.out.println("Stopping for " + timeInSecond + " seconds");
		try {
			Thread.sleep(timeInMiliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isElementEnableAndPresent(By locator) {
		boolean result = false;
		try {
			WebElement element = this.getElement(locator);
			List<WebElement> elements = this.getElements(locator);
			if (element.isEnabled() && element.isDisplayed() && elements.size() > 0) {
				result = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkElementToBeLoaded(By locator, int checkTimes) {
		boolean bFind = false;
		int i = 0;
		int maxCheckTimes = checkTimes;
		try {
			do {
				if (this.isElementEnableAndPresent(locator)) {
					bFind = true;
					break;
				}
				this.delayExecution(2000);
				i++;
			} while (i < maxCheckTimes);

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return bFind;
	}

	public void scrollIntoView(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void doubleClick(By locator) {
		if (this.checkElementToBeLoaded(locator, 10)) {
			WebElement element = this.getElement(locator);
			Actions action = new Actions(driver);
			action.moveToElement(element).doubleClick().build().perform();
		}
	}

	public void clickByActions(By locator) {
		if (this.checkElementToBeLoaded(locator, 10)) {
			WebElement element = this.getElement(locator);
			Actions action = new Actions(driver);
			action.moveToElement(element).click().build().perform();
		}
	}

	public void javaScriptClick(By locator) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement element = this.getElement(locator);
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void javaScriptScrollIntoViewAndClick(By locator) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement element = this.getElement(locator);
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].scrollIntoView(true);", element);
				executor.executeScript("arguments[0].click();", element);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void javaScriptScrollIntoViewAndDoubleClick(By locator) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement element = this.getElement(locator);
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].scrollIntoView(true);", element);
				this.doubleClick(locator);
				this.delayExecution(1000);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void enterTextField(By locator, String text) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement element = this.getElement(locator);
				this.scrollIntoView(element);
				element.clear();
				element.sendKeys(text);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public WebElement waitForPageElementToLoad(By locator, long maxWaitingTimeInSeconds) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxWaitingTimeInSeconds);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return element;
	}

	public WebElement waitForPageElementToBeEnabled(By locator, long maxWaitingTimeInSeconds) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxWaitingTimeInSeconds);
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return element;
	}

	public boolean waitForPageElementToBeInvisible(By locator, long maxWaitTimeInSeconds) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, maxWaitTimeInSeconds);
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}
	}

	public String getText(By locator) {
		return this.getElement(locator).getText().trim();
	}

	public void verifyText(By locator, String expectedText, long maxWaitingTimeInSeconds) {
		WebElement element = null;
		try {
			element = this.waitForPageElementToLoad(locator, maxWaitingTimeInSeconds);
			String actual = this.getText(locator);
			if (actual.equalsIgnoreCase(expectedText.trim())) {
				System.out.println(expectedText + " is matched");
			} else {
				System.out.println("Expected:" + expectedText + " is not matched with Actual:" + actual);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void verifyTextContains(By locator, String expectedText, long maxWaitingTimeInSeconds) {
		WebElement element = null;
		try {
			element = this.waitForPageElementToLoad(locator, maxWaitingTimeInSeconds);
			String actual = this.getText(locator);
			if (actual.contains(expectedText.trim())) {
				System.out.println(expectedText + " is verified");
			} else {
				System.out.println("Expected:" + expectedText + " is not verified with Actual:" + actual);
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public String getParentWindow() {
		return driver.getWindowHandle();
	}

	public void switchToNewWindow() {
		System.out.println("Attempt switch to new window");
		System.out.println("Window handles count:" + driver.getWindowHandles().size());
		this.delayExecution(2000);
		String parentHandle = this.getParentWindow();
		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(parentHandle)) {
				driver.switchTo().window(winHandle);
			}
		}
		System.out.println("Switch to new window completed");
	}

	public void switchToWindowByHandler(String winHandle) {
		driver.switchTo().window(winHandle);
	}

	public void selectFromDropDownByOption(By locator, String optionValue) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement dropdown = this.getElement(locator);
				List<WebElement> options = dropdown.findElements(By.tagName("option"));
				for (WebElement option : options) {
					System.out.println("Options :" + option.getText());
					if (option.getText().trim().equalsIgnoreCase(optionValue.trim())) {
						option.click();
						this.delayExecution(1000);
						System.out.println("Selected option from dropdown :" + optionValue);
						break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(optionValue + " is not selected from element " + locator);
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void selectFromDropDownByValueAttribute(By locator, String optionValue) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement dropdown = this.getElement(locator);
				List<WebElement> options = dropdown.findElements(By.tagName("option"));
				for (WebElement option : options) {
					System.out.println("Options values :" + option.getAttribute("value"));
					if (option.getAttribute("value").trim().equalsIgnoreCase(optionValue.trim())) {
						option.click();
						this.delayExecution(1000);
						System.out.println("Selected value from dropdown :" + optionValue);
						break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(optionValue + " is not selected from element " + locator);
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void selectFromDropDownByValueAttributePartialMatch(By locator, String optionValue) {
		try {
			if (this.checkElementToBeLoaded(locator, 10)) {
				WebElement dropdown = this.getElement(locator);
				List<WebElement> options = dropdown.findElements(By.tagName("option"));
				for (WebElement option : options) {
					System.out.println("Options values :" + option.getAttribute("value"));
					if (option.getAttribute("value").trim().toUpperCase().contains(optionValue.trim().toUpperCase())) {
						option.click();
						this.delayExecution(1000);
						System.out.println("Selected value from dropdown :" + optionValue);
						break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(optionValue + " is not selected from element " + locator);
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public int totalWebElementFound(By locator, long timeInMiliSeconds) {
		try {
			this.delayExecution(timeInMiliSeconds);
			List<WebElement> elements = this.getElements(locator);
			return elements.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public void pressEnter(By locator) {
		this.getElement(locator).sendKeys(Keys.ENTER);
	}

	public void actionsEnter(By locator) {
		WebElement element = this.getElement(locator);
		Actions actionProvider = new Actions(driver);
		actionProvider.moveToElement(element).sendKeys(Keys.ENTER);
	}

	public void robotTab() throws AWTException {
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_TAB);
	}

	public void robotEnter() throws AWTException {
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
	}

	public void robotEsc() throws AWTException {
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ESCAPE);
	}

	public void waitForLoadingSpinnerInvisible(By locator) {
		boolean loadingImg = false;
		while (!loadingImg) {
			if (this.getElement(locator).getAttribute("style").contains("display: none;")) {
				loadingImg = true;
				this.delayExecution(2000);
			} else {
				loadingImg = false;
				this.delayExecution(2000);
			}
		}
	}

	public String copyToClipBoard() {
		try {
			Actions actionProvider = new Actions(driver);
			Action keydown = actionProvider.keyDown(Keys.CONTROL).sendKeys("a").pause(4000).keyUp(Keys.CONTROL).build();
			keydown.perform();

			keydown = actionProvider.keyDown(Keys.CONTROL).sendKeys("c").pause(3000).keyUp(Keys.CONTROL).build();
			keydown.perform();
			this.delayExecution(3000);
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			try {
				return cb.getData(DataFlavor.stringFlavor).toString();
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-1";
	}

	public void useRobotToUploadFile(String saveToPath){
		try{
			Thread.sleep(2000);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("window.focus();");
			Robot r = new Robot();
			Thread.sleep(2000);

			StringSelection ss = new StringSelection(saveToPath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			System.out.println("===========File Path==============="+ saveToPath);
			Thread.sleep(2000);
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_V);
			System.out.println("===========Hit CTRL + V===============");

			Thread.sleep(2000);
			r.keyRelease(KeyEvent.VK_V);
			r.keyRelease(KeyEvent.VK_CONTROL);			
			System.out.println("===========Release CTRL + V===============");

			Thread.sleep(2000);
			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
			System.out.println("===========Hit ENTER===============");
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String robotCopy() throws InterruptedException, AWTException {
		Robot r = new Robot();
		Thread.sleep(2000);
		r.keyPress(KeyEvent.VK_CONTROL);
		Thread.sleep(100);
		r.keyPress(KeyEvent.VK_C);
		Thread.sleep(100);
		r.keyPress(KeyEvent.VK_C);
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_C);
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(100);
	}

	public String robotCtrlShiftEnd() throws InterruptedException, AWTException {
		Robot r = new Robot();
		Thread.sleep(100);
		if(Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)){
			System.out.println("===========Num Lock Key Is On===============");
			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
		}
		r.keyPress(KeyEvent.VK_SHIFT);
		Thread.sleep(100);
		r.keyPress(KeyEvent.VK_CONTROL);
		Thread.sleep(100);
		r.keyPress(KeyEvent.VK_END);
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_END);
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_SHIFT);
		Thread.sleep(100);
	}
	
	public String getTextUsingSystemClipboard() throws FindFailed, InterruptedException, AWTException, UnsupportedFlavorException, IOException {
		Thread.sleep(1000);
		this.robotCopy();
		Thread.sleep(100);
		String data="";
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = defaultToolkit.getSystemClipboard();
		DataFlavor dataFlavor = DataFlavor.stringFlavor;
		if(clipboard.isDataFlavorAvailable(dataFlavor)){
			Thread.sleep(300);
			Object text = clipboard.getData(dataFlavor);
			data = (String)text;
			clipboard.setContents(new StringSelection(""), null);
		} else {
			System.out.println("===========No isDataFlavorAvailable===============");
		}
		return data;
	}

	public int getRandomNumberFromRange(int startRange, int finishRange) {
		Random	rdm = new Random();
		return rdm.nextInt((finishRange + 1 - startRange)) + startRange;
	}

	public void zoomOut_In(Double percentge) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("document.body.style.zoom = '"+percentge+"'");
	}

	public void zoomPageOut() {
		driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
	}

	
	public void zoomPageIn() {
		driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
	}

	
	public void zoomPageOut() {
		driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
	}
	
	// *************Sikuli Functions Start*************************

	public void sikuliWait(Pattern img, double timeInSecond) throws FindFailed {
		s.wait(img, timeInSecond);
	}

	public boolean checkImgToBeLoaded(Pattern img, int checkTimes) {
		boolean bFind = false;
		int i = 0;
		int maxCheckTimes = checkTimes;
		try {
			do {
				if (s.exists(img) != null) {
					bFind = true;
					break;
				}
				this.delayExecution(2000);
				i++;
			} while (i < maxCheckTimes);

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return bFind;

	}

	public void sikuliClick(Pattern img, int timeInSecond, String msg) throws FindFailed {
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.click(img);
				System.out.println(msg + " Image is Clicked by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and clicked by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void sikuliDoubleClick(Pattern img, int timeInSecond, String msg) throws FindFailed {
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.doubleClick(img);
				System.out.println(msg + " Image is Clicked by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and clicked by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void sikuliType(Pattern img, int timeInSecond, String msg, String text) {
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.click(img);
				s.type(text);
				System.out.println(msg + " Image is Clicked and typed value by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and entered by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void sikuliDelayType(Pattern img, int timeInSecond, String msg, String text) {
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.click(img);
				s.delayType(200);
				s.type(text);
				System.out.println(msg + " Image is Clicked and typed value by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and entered by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void sikuliClearBeforeType(Pattern img, int timeInSecond, String msg, String text) {		
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.click(img);
				s.type("a", KeyModifier.CTRL);
				s.type(Key.BACKSPACE);
				s.type(text);
				System.out.println(msg + " Image is Clicked and typed value by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and entered by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void sikuliClearBeforeDelayType(Pattern img, int timeInSecond, String msg, String text) {
		try {
			if (this.checkImgToBeLoaded(img, timeInSecond)) {
				// s.wait(img, timeInSecond);
				s.highlight(1);
				s.click(img);
				s.type("a", KeyModifier.CTRL);
				s.type(Key.BACKSPACE);
				s.delayType(200);
				s.type(text);
				System.out.println(msg + " Image is Clicked and typed value by sikuli action.");
			} else {
				System.out.println(msg + " Image is not loaded and entered by sikuli action.");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void sikuliTab() {
		s.type(Key.TAB);
	}

	public void sikuliEnter() {
		s.type(Key.ENTER);
	}

	public void sikuliScrollUp(int steps) {
		s.wheel(Mouse.WHEEL_UP, steps);
	}

	public void sikuliScrollDown(int steps) {
		s.wheel(Mouse.WHEEL_DOWN, steps);
	}
	
	public String sikuliExtractTextOffsetByRect(Pattern img, int x, int y, int width, int height, double similar) throws FindFailed {
		Screen screen= new Screen();
		Pattern pattern = new Pattern(img).similar(similar); //similar : 0.5
		Match match = screen.find(pattern).exists(img);
		Region region = new Region(match.getX()+x, match.getY()+y, width, height);
		String text = region.text().replaceAll("[\\[\\]\\(\\)\\|\\!\\*]", "").trim();
		System.out.println("Image details are :"+text);
		return text;
	}
	
	public void sikuliMatchRegionContains(Pattern img, String expected) throws FindFailed {
		Match c = s.find(img).exists(img);
		Region r = new Region(c.getRect());
		String actual = r.text().replaceAll("[\\[\\]]", "").trim();
		try {
			if (actual.contains(expected.trim())) {
				System.out.println("Actual value: "+actual+" contains Expected: "+expected);
			} else {
				System.out.println("Actual value: "+actual+" does not contains Expected: "+expected);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sikuliVerifyContainsTextByRec(Pattern img, int x, int y, int width, int height, double similar, String expected) throws FindFailed {
		String actual = this.sikuliExtractTextOffsetByRect(img, x, y, width, height, similar);
		try {
			if (actual.replaceAll("\n", "").contains(expected.trim())) {
				System.out.println("Actual value: "+actual+" contains Expected: "+expected);
			} else {
				System.out.println("Actual value: "+actual+" does not contains Expected: "+expected);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sikuliExtractTextByPosition(Pattern img, String fetchTextFrom, int width, int height, int numberOfTimes) throws FindFailed {
		Screen screen= new Screen();
		Pattern pattern = new Pattern(img).similar(0.5); //similar : 0.5
		Match match = screen.find(pattern).exists(img);
		Region region = new Region(match.getRect());
		Region r = null;
		String text = "";
		String multiText = "";
		if(numberOfTimes > 1){
			for(int i=0: i < numberOfTimes; i++){
				switch(fetchTextFrom.toLowerCase()){
					case "below":
						r = region.below(height)
						break;
					case "below":
						r = region.above(height)
						break;
					case "below":
						r = region.left(width)
						break;
					case "below":
						r = region.right(width)
						break;
					default:
						break;
				}
				r.highlight(1);
				text = r.text().trim();
				System.out.println("The Image Details Are ==="+ text);
				multiText = multiText +  text + (((numberOfTimes-1) > i) ? ":":"");
				region = Region.create(r);
			}
		} else {
				switch(fetchTextFrom.toLowerCase()){
					case "below":
						r = region.below(height)
						break;
					case "below":
						r = region.above(height)
						break;
					case "below":
						r = region.left(width)
						break;
					case "below":
						r = region.right(width)
						break;
					default:
						break;
				}
				r.highlight(1);
				text = r.text().trim();
				System.out.println("The Image Details Are ==="+ text);
		}
		if(numberOfTimes > 1){
			return multiText;
		} else {
			return text;
		}
		
	}
	// *************Sikuli Functions End*************************
}
