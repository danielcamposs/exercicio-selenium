package exselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestaLogin {

    protected static WebDriver driver;

    @BeforeEach
    public void createDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://automationexercise.com");
    }

    @ParameterizedTest(name = "Test Case 3 - Login Incorreto: {0}")
    @MethodSource("TestesLoginIncorreto")
    public void testLoginUserWithIncorrectEmailAndPassword(String descricaoCenario, String email, String password) {
      
        assertTrue(driver.getTitle().contains("Automation Exercise"), "A página inicial não carregou corretamente.");

        driver.findElement(By.cssSelector("a[href='/login']")).click();

        WebElement loginHeader = driver.findElement(By.xpath("//h2[text()='Login to your account']"));
        assertTrue(loginHeader.isDisplayed(), "O cabeçalho 'Login to your account' não está visível.");

        WebElement inputEmail = driver.findElement(By.cssSelector("input[data-qa='login-email']"));
        WebElement inputPassword = driver.findElement(By.cssSelector("input[data-qa='login-password']"));

        inputEmail.clear();
        inputEmail.sendKeys(email);
        
        inputPassword.clear();
        inputPassword.sendKeys(password);

        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        WebElement mensagemErro = driver.findElement(By.xpath("//p[contains(text(),'Your email or password is incorrect!')]"));
        assertTrue(mensagemErro.isDisplayed(), "A mensagem de erro não foi exibida.");
        assertEquals("Your email or password is incorrect!", mensagemErro.getText());
    }

      private static Stream<Arguments> TestesLoginIncorreto() {
        return Stream.of(
            Arguments.of("1. Senha no Limite Inferior de Tamanho (1 char)", "usuario_teste_01@teste.com", "x"),
            Arguments.of("2. E-mail com Limite Inferior de Tamanho (1 char)", "a@domain.com", "SenhaIncorreta123"),
            Arguments.of("3. E-mail com 2 caracteres especiais (. e _)", "user.test_qa@domain.com", "SenhaIncorreta123"),
            Arguments.of("4. Credenciais Padrão Inexistentes", "izucamposd@gmail.com", "SenhaIncorreta123"),
            Arguments.of("5. E-mail em Caixa Alta com Senha Extensa", "USUARIO_INEXISTENTE@TESTE.COM", "SenhaMuitoLongaComCaracteresEspeciais#2026")
        );
    }

    @AfterAll
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}