package exselenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestaProdutos {

    private static final String BASE_URL = "http://automationexercise.com";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void createDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testSearchProduct() {
        driver.get(BASE_URL);
        assertTrue(driver.getTitle().contains("Automation Exercise"),
                "A página inicial não carregou corretamente.");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/products']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_product")))
                .sendKeys("Blue Top");
        driver.findElement(By.id("submit_search")).click();

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'productinfo')]//p[normalize-space()='Blue Top']")));
        assertTrue(resultado.isDisplayed(), "O produto pesquisado não foi encontrado.");
    }

    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
