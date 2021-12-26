package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void whenAppIsCompleted() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("и-м-я Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[class*=Success_successBlock]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void whenAppIsEmpty() {
        driver.findElement(By.cssSelector("[role='button']")).click();
        boolean pageNotChanged = driver.findElement(By.cssSelector("[role='button']")).isDisplayed();
        assertTrue(pageNotChanged);
    }

    @Test
    void whenNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualMessage);
    }

    @Test
    void whenNameIsIncorrect() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("None");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void whenPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("ИМЯ");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        String expectedMessage = "Поле обязательно для заполнения";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void whenPhoneIsIncorrect() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("ИМЯ");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("0");
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void whenCheckboxUnchecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("и-м-я Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[role='button']")).click();
        boolean actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed();
        assertTrue(actual);
    }

}

