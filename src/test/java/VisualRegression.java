import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VisualRegression {

    WebDriver driver;

    @DataProvider(name = "urls")
    public static Object[][] urls() {
        return new Object[][]{
                {"https://demo.guru99.com/test/newtours/index.php", "homePage"},
                {"https://demo.guru99.com/test/newtours/reservation.php", "flightsPage"}
        };
    }

    @BeforeTest
    public void setup() {
        try {
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/src/images/diffimages"));
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/src/images/screenshots"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://demo.guru99.com/test/newtours/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.switchTo().frame("gdpr-consent-notice");
        driver.findElement(By.xpath("//span[contains(text(),'Accept All')]")).click();
        driver.switchTo().defaultContent();
    }

    @Test
    public void test1() {
        ScreenCaptureUtility.takePageScreenShot(driver, "HomePage");
    }

    @Test
    public void test2() {
        WebElement logo = driver.findElement(By.xpath("//img[@alt='Mercury Tours']"));
        ScreenCaptureUtility.takeElementScreenShot(driver, "logo", logo);
    }

    @Test
    public void compareImages() {
        ScreenCaptureUtility.takePageScreenShot(driver, "actualHomePage");
        Assert.assertTrue(ScreenCaptureUtility.areImagesEqual("expectedHomePage", "actualHomePage"));
    }

    @Test
    public void compareImagesFail() {
        driver.findElement(By.name("userName")).sendKeys("FAILED");
        ScreenCaptureUtility.takePageScreenShot(driver, "actualHomePage");
        Assert.assertTrue(ScreenCaptureUtility.areImagesEqual("expectedHomePage", "actualHomePage"));
    }

    @Test(dataProvider = "urls")
    public void prepareBaseLine(String url, String name) {
        driver.get(url);
        ScreenCaptureUtility.prepareBaseline(driver, name);
    }

    @Test(dataProvider = "urls")
    public void regression(String url, String name) {
        driver.get(url);
        ScreenCaptureUtility.takePageScreenShot(driver, name);
        Assert.assertTrue(ScreenCaptureUtility.areImagesEqual(name, name));
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
