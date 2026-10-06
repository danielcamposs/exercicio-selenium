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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestaRegistro {

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
    public void testRegisterUserCompleteFlow() {
        String nome = "Usuario Teste Padrao";
        String senha = "SenhaValida123!";
        String idTeste = "CT03";
        String emailDinamico = "qa_test_" + System.currentTimeMillis() + "_" + idTeste + "@test.com";

        driver.get(BASE_URL);
        assertTrue(driver.getTitle().contains("Automation Exercise"), "A pagina inicial nao carregou corretamente.");

        openSignupPage();
        fillSignupForm(nome, emailDinamico);
        fillAccountInformation(senha);
        fillAddress(nome);
        createAndDeleteAccount();
    }

    private void openSignupPage() {
        driver.findElement(By.xpath("//a[contains(text(), 'Signup / Login')]")).click();
        assertTrue(waitForVisible(By.xpath("//h2[text()='New User Signup!']")).isDisplayed());
    }

    private void fillSignupForm(String nome, String email) {
        driver.findElement(By.xpath("//input[@data-qa='signup-name']")).sendKeys(nome);
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys(email);
        driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
        assertTrue(waitForVisible(By.xpath("//b[text()='Enter Account Information']")).isDisplayed());
    }

    private void fillAccountInformation(String senha) {
        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys(senha);
        new Select(driver.findElement(By.id("days"))).selectByValue("15");
        new Select(driver.findElement(By.id("months"))).selectByValue("5");
        new Select(driver.findElement(By.id("years"))).selectByValue("1995");
        driver.findElement(By.id("newsletter")).click();
        driver.findElement(By.id("optin")).click();
    }

    private void fillAddress(String nome) {
        driver.findElement(By.id("first_name")).sendKeys(nome);
        driver.findElement(By.id("last_name")).sendKeys("Automacao");
        driver.findElement(By.id("company")).sendKeys("Empresa QA");
        driver.findElement(By.id("address1")).sendKeys("Rua dos Testes, 123");
        driver.findElement(By.id("address2")).sendKeys("Apto 101");
        new Select(driver.findElement(By.id("country"))).selectByValue("Canada");
        driver.findElement(By.id("state")).sendKeys("Ontario");
        driver.findElement(By.id("city")).sendKeys("Toronto");
        driver.findElement(By.id("zipcode")).sendKeys("M5V 2T6");
        driver.findElement(By.id("mobile_number")).sendKeys("+14165550199");
    }

    private void createAndDeleteAccount() {
        driver.findElement(By.xpath("//button[@data-qa='create-account']")).click();
        assertTrue(waitForVisible(By.xpath("//b[text()='Account Created!']")).isDisplayed());
        driver.findElement(By.xpath("//a[@data-qa='continue-button']")).click();
        assertTrue(waitForVisible(By.xpath("//a[contains(text(), 'Logged in as')]")).isDisplayed());
        driver.findElement(By.xpath("//a[contains(text(), 'Delete Account')]")).click();
        assertTrue(waitForVisible(By.xpath("//b[text()='Account Deleted!']")).isDisplayed());
        driver.findElement(By.xpath("//a[@data-qa='continue-button']")).click();
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}