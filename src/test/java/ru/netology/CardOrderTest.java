package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void whenAppIsCompleted() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("name")).sendKeys("и-м-я Фамилия");
        driver.findElement(By.name("phone")).sendKeys("79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".paragraph")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void whenAppIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.className("button")).click();
        boolean pageNotChanged = driver.findElement(By.className("button")).isDisplayed();
        assertTrue(pageNotChanged);
    }

    @Test
    void whenNameIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("phone")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualMessage = driver.findElement(By.className("input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualMessage);
    }

    @Test
    void whenNameIsIncorrect() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("name")).sendKeys("None");
        driver.findElement(By.name("phone")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualMessage = driver.findElement(By.className("input__sub")).getText();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void whenPhoneIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("name")).sendKeys("ИМЯ");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        List<WebElement> text = driver.findElements(By.className("input__sub"));
        String actualMessage = text.get(1).getText();
        String expectedMessage = "Поле обязательно для заполнения";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void whenPhoneIsIncorrect() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("name")).sendKeys("ИМЯ");
        driver.findElement(By.name("phone")).sendKeys("0");
        driver.findElement(By.className("button")).click();
        List<WebElement> text = driver.findElements(By.className("input__sub"));
        String actualMessage = text.get(1).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedMessage, actualMessage);

    }


    @Test
    void whenCheckboxUnchecked() {
        driver.get("http://localhost:9999");
        driver.findElement(By.name("name")).sendKeys("и-м-я Фамилия");
        driver.findElement(By.name("phone")).sendKeys("+79991112233");
        driver.findElement(By.className("button")).click();
        String actualColor = driver.findElement(By.cssSelector(".input_invalid")).getCssValue("color");
        String expectedColor = "rgba(255, 92, 92, 1)";
        assertEquals(expectedColor, actualColor);
    }

}

