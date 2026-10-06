package exselenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestaRegistroParametrizado {

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

    @ParameterizedTest(name = "Cadastro com usuario: {0}")
    @MethodSource("dadosDeCadastro")
    public void testRegisterUser(String nome, String sobrenome, String senha, String pais,
            String estado, String cidade, String cep, String telefone) {
        String email = "qa_param_" + System.currentTimeMillis() + "@test.com";

        driver.get(BASE_URL);
        assertTrue(driver.getTitle().contains("Automation Exercise"),
                "A pagina inicial nao carregou corretamente.");

        driver.findElement(By.cssSelector("a[href='/login']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[text()='New User Signup!']")));

        driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(nome);
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(email);
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//b[text()='Enter Account Information']")));

        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys(senha);
        new Select(driver.findElement(By.id("days"))).selectByValue("15");
        new Select(driver.findElement(By.id("months"))).selectByValue("5");
        new Select(driver.findElement(By.id("years"))).selectByValue("1995");

        driver.findElement(By.id("first_name")).sendKeys(nome);
        driver.findElement(By.id("last_name")).sendKeys(sobrenome);
        driver.findElement(By.id("address1")).sendKeys("Rua dos Testes, 123");
        new Select(driver.findElement(By.id("country"))).selectByVisibleText(pais);
        driver.findElement(By.id("state")).sendKeys(estado);
        driver.findElement(By.id("city")).sendKeys(cidade);
        driver.findElement(By.id("zipcode")).sendKeys(cep);
        driver.findElement(By.id("mobile_number")).sendKeys(telefone);
        driver.findElement(By.cssSelector("button[data-qa='create-account']")).click();

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//b[text()='Account Created!']"))).isDisplayed(),
                "A conta nao foi criada.");

        driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Delete Account')]"))).click();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//b[text()='Account Deleted!']"))).isDisplayed(),
                "A conta nao foi removida ao final do teste.");
    }

    private static Stream<Arguments> dadosDeCadastro() {
        return Stream.of(
                Arguments.of("Ana", "Silva", "SenhaAna123!", "Canada", "Ontario", "Toronto",
                        "M5V 2T6", "+14165550199"),
                Arguments.of("Bruno", "Santos", "SenhaBruno123!", "India", "Delhi", "New Delhi",
                        "110001", "919876543210"));
    }

    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
