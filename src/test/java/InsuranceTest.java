import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author Ежова Наталья
 */

public class InsuranceTest {

    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");

        driver = new ChromeDriver();
        baseUrl = "https://www.sberbank.ru/ru/person";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testInsurance() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//span[text()='Страхование']")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Путешествия и покупки')][contains(@class,'lg-menu__sub-link')]")).click();

        assertEquals("Страхование путешественников",
                driver.findElement(By.xpath("//h3[text()='Страхование путешественников']")).getText());

        Set<String> oldWindowsSet = driver.getWindowHandles();

        driver.findElement(By.xpath("//P[@class='kit-button kit-button_color_green kit-button_size_m']")).click();

        Set <String> newWindowsSet = driver.getWindowHandles();
        newWindowsSet.removeAll(oldWindowsSet);
        String newWindowHandle = newWindowsSet.iterator().next();
        driver.switchTo().window(newWindowHandle);

        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        WebElement title = driver.findElement(By.xpath("//*[text() = 'Выбор полиса']"));
        wait.until(ExpectedConditions.visibilityOf(title));

        driver.findElement(By.xpath("//span[text()='Оформить']")).click();

        fillField(By.name("insured0_surname"), "IVANOVA");
        fillField(By.name("insured0_name"), "OLGA");
        fillField(By.name("insured0_birthDate"), "01011990");
        fillField(By.name("surname"), "ИВАНОВА");
        fillField(By.name("name"), "ОЛЬГА");
        fillField(By.name("middlename"), "ИВАНОВНА");
        fillField(By.name("birthDate"), "01011990");
        driver.findElement(By.xpath("//INPUT[@name='female']")).click();
        fillField(By.xpath("//INPUT[@ng-model='formdata.insurer.documentList[0].DOCSERIES']"), "1234");
        fillField(By.xpath("//INPUT[@ng-model='formdata.insurer.documentList[0].DOCNUMBER']"), "123456");
        fillField(By.name("issueDate"), "10102016");
        fillField(By.name("issuePlace"), "UFMS");

        assertEquals("IVANOVA", driver.findElement(By.name("insured0_surname")).getAttribute("value"));
        assertEquals("OLGA", driver.findElement(By.name("insured0_name")).getAttribute("value"));
        assertEquals("01.01.1990", driver.findElement(By.name("insured0_birthDate")).getAttribute("value"));
        assertEquals("ИВАНОВА", driver.findElement(By.name("surname")).getAttribute("value"));
        assertEquals("ОЛЬГА", driver.findElement(By.name("name")).getAttribute("value"));
        assertEquals("ИВАНОВНА", driver.findElement(By.name("middlename")).getAttribute("value"));
        assertEquals("01.01.1990", driver.findElement(By.name("birthDate")).getAttribute("value"));
        assertEquals("1234", driver.findElement(By.xpath("//INPUT[@ng-model='formdata.insurer.documentList[0].DOCSERIES']")).getAttribute("value"));
        assertEquals("123456", driver.findElement(By.xpath("//INPUT[@ng-model='formdata.insurer.documentList[0].DOCNUMBER']")).getAttribute("value"));
        assertEquals("10.10.2016", driver.findElement(By.name("issueDate")).getAttribute("value"));
        assertEquals("UFMS", driver.findElement(By.name("issuePlace")).getAttribute("value"));

        driver.findElement(By.xpath("//span[text()='Продолжить']")).click();

        assertEquals("Заполнены не все обязательные поля",
                driver.findElement(By.xpath("//DIV[@ng-show='tryNext && myForm.$invalid'][text()='Заполнены не все обязательные поля']")).getText());
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    private void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

}
