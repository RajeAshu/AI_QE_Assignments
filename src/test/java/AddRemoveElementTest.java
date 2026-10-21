import com.microsoft.playwright.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class AddRemoveElementTest {

    WebDriver driver;
    WebDriverWait wait;
    public static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
   public static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static final String BROWSERSTACK_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";


    By addRemoveLink = By.xpath("//a[@href='/add_remove_elements/']");
    By addElementBtn = By.xpath("//button[text()='Add Element']");
    By deleteElementBtn = By.xpath("//button[text()='Delete']");



    By checkboxesLink = By.xpath("//a[@href='/checkboxes']");
    By checkbox1 = By.xpath("//form[@id='checkboxes']/input[1]");
    By checkbox2 = By.xpath("//form[@id='checkboxes']/input[2]");


    By dropdownLink = By.xpath("//a[@href='/dropdown']");
    By dropdownSelect = By.id("dropdown");

    By dragAndDrop = By.xpath("//a[@href='/drag_and_drop']");
    By columnA = By.id("column-a");
    By columnB = By.id("column-b");


    public void markTestStatus(String status, String reason, WebDriver driver) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            String script = String.format(
                    "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"%s\", \"reason\": \"%s\"}}",
                    status, reason
            );
            jse.executeScript(script);
        } catch (Exception e) {
            System.out.println("Error reporting to BrowserStack: " + e.getMessage());
        }
    }

    @Parameters({"os", "osVersion", "browserName", "browserVersion"})
    @BeforeMethod
    public void setUp(@Optional("") String os, @Optional("") String osVersion,
                      @Optional("") String browserName, @Optional("") String browserVersion)
            throws MalformedURLException {


        DesiredCapabilities obj_caps = new DesiredCapabilities();
        HashMap<String, Object> obj_stackOptions = new HashMap<>();

        String finalOs = (os == null || os.isEmpty()) ? "Windows" : os;
        String finalOsVersion = (osVersion == null || osVersion.isEmpty()) ? "11" : osVersion;
        String finalBrowser = (browserName == null || browserName.isEmpty()) ? "chrome" : browserName;
        String finalBrowserVersion = (browserVersion == null || browserVersion.isEmpty()) ? "latest" : browserVersion;

        obj_stackOptions.put("os", finalOs);
        obj_stackOptions.put("osVersion", finalOsVersion);
        obj_stackOptions.put("projectName", "CCST BrowserStack Demo");
        obj_stackOptions.put("buildName", "TestNG Build 1.0");
        obj_stackOptions.put("sessionName", "HerokuApp Add Remove Elements Test");

        obj_caps.setCapability("bStack:options", obj_stackOptions);
        obj_caps.setCapability("browserName", finalBrowser);
        obj_caps.setCapability("browserVersion", finalBrowserVersion);

        driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), obj_caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();


    }

    @Test(priority = 1)
    public void testAddRemoveElementsInteraction() throws InterruptedException {
        try {

            driver.get("https://the-internet.herokuapp.com/");
            Thread.sleep(1000);


            WebElement link = wait.until(
                    ExpectedConditions.elementToBeClickable(addRemoveLink)
            );
            link.click();


            wait.until(ExpectedConditions.urlContains("/add_remove_elements/"));


            WebElement addBtn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(addElementBtn)
            );
            addBtn.click();
            System.out.println("PASSED: Clicked 'Add Element' successfully.");
            Thread.sleep(1000);


            WebElement deleteBtn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteElementBtn)
            );
            Assert.assertTrue(deleteBtn.isDisplayed(), "Verification Failed: The 'Delete' element button did not generate dynamically.");
            System.out.println("PASSED: Dynamic 'Delete' action button verified as visible.");


            deleteBtn.click();
            System.out.println("PASSED: 'Delete' button clicked and cleared successfully.");
            Thread.sleep(1000);

            markTestStatus("passed", "Add/Remove element lifecycle validated successfully.", driver);

        } catch (Exception e) {
            markTestStatus("failed", "Test execution broken: " + e.getMessage(), driver);
            throw e;
        }
    }


    @Test(priority = 2)
    public void testCheckboxesInteraction() throws InterruptedException {
        try {
            driver.get("https://the-internet.herokuapp.com/");
            Thread.sleep(1000);

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(checkboxesLink));
            link.click();

            wait.until(ExpectedConditions.urlContains("/checkboxes"));

            WebElement chk1 = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox1));
            WebElement chk2 = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox2));


            Assert.assertFalse(chk1.isSelected(), "Checkbox 1 should be unchecked by default.");
            Assert.assertTrue(chk2.isSelected(), "Checkbox 2 should be checked by default.");


            chk1.click();
            chk2.click();
            Thread.sleep(1000);


            Assert.assertTrue(chk1.isSelected(), "PASSED: Checkbox 1 is now selected.");
            Assert.assertFalse(chk2.isSelected(), "PASSED: Checkbox 2 is now deselected.");

            markTestStatus("passed", "Checkboxes states toggled and verified successfully.", driver);

        } catch (Exception e) {
            markTestStatus("failed", "Checkboxes test failed: " + e.getMessage(), driver);
            throw e;
        }
    }

    @Test(priority = 3)
    public void testDropdownSelection() throws InterruptedException {
        try {
            driver.get("https://the-internet.herokuapp.com/");
            Thread.sleep(1000);

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dropdownLink));
            link.click();

            wait.until(ExpectedConditions.urlContains("/dropdown"));

            WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownSelect));
            Select dropdown = new Select(selectElement);


            dropdown.selectByVisibleText("Option 2");
            Thread.sleep(1000);


            WebElement selectedOption = dropdown.getFirstSelectedOption();
            Assert.assertEquals(selectedOption.getText(), "Option 2", "Dropdown selection verification failed.");
            System.out.println("PASSED: Dropdown Option 2 selected and verified successfully.");

            markTestStatus("passed", "Dropdown option picked and validated successfully.", driver);

        } catch (Exception e) {
            markTestStatus("failed", "Dropdown test failed: " + e.getMessage(), driver);
            throw e;
        }
    }

    @Test(priority = 4)
    public void testDragAndDrop() throws InterruptedException {
        try {
            driver.get("https://the-internet.herokuapp.com/");
            Thread.sleep(1000);


            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dragAndDrop));
            link.click();
            wait.until(ExpectedConditions.urlContains("/drag_and_drop"));

            WebElement sourceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(columnA));
            WebElement targetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(columnB));


            Actions actions = new Actions(driver);
            actions.dragAndDrop(sourceElement, targetElement).perform();
            Thread.sleep(1000);


            String headerText = sourceElement.findElement(By.tagName("header")).getText();
            if (headerText.equals("B")) {
                System.out.println("PASSED: Column A is dragged to Column B successfully");
                markTestStatus("passed", "Drag and drop executed and validated successfully.", driver);
            } else {
                System.out.println("FAILED: Column A is not dragged to Column B");
                Assert.fail("Drag and drop validation failed. Column text is: " + headerText);
            }

        } catch (Exception e) {
            markTestStatus("failed", "Drag and Drop test failed: " + e.getMessage(), driver);
            throw e;
        }
    }


    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


