
package exselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestaGoogle {

	protected WebDriver driver;
	
    @BeforeEach
    public void createDriver() {  
    
		driver = new ChromeDriver();
        driver.get("https://www.google.com.br");
    }	

	@Test
	public void test() {
        WebElement pesquisa = driver.findElement(By.name("q"));
        pesquisa.sendKeys("teste");
        pesquisa.submit();
        assertEquals("teste - Pesquisa Google", driver.getTitle(), "teste - Pesquisa Google"); // para testar página precisa de um assert
	}
	
    @AfterAll
    public static void quitDriver() {
      // driver.quit();
    }
}
