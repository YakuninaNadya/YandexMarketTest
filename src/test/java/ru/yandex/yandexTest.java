package ru.yandex;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class yandexTest {
    private ChromeDriver driver;
    private String link = "https://yandex.ru";
    private static final Logger logger = LogManager.getLogger(yandexTest.class);
    private WebDriverWait wait;
    WebElement inner;
    List<WebElement> list;
    String browser = "CHROME";


    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/Users/nadezhdayakunina/Downloads/chromedriver");

        logger.info("Before " + browser);
        driver = new ChromeDriver();
        driver.get(link); // перешли на сайт
        driver.manage().window().maximize(); // раскрыли на весь экран
        System.out.println(((HasCapabilities) driver).getCapabilities());

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 4, 125);
    }

    private void waitForPreloader(By locator, int n) {
        ExpectedConditions.numberOfElementsToBe(locator, n);
    }

    private void waitForPreloader2(By locator, int n) {
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, n));
    }

    @Test
    public void firstTest() {
        logger.info("Переходим в Маркет");
        driver.findElement(By.linkText("Маркет")).click(); //  переходим в Маркет

        logger.info("Переходим в Электронику");
        driver.findElement(By.xpath("//span[contains(@class, 'n-w-tab__control-caption')]  [contains(text(),'Электроника')]")).click(); // проходим в раздел Электроники

        logger.info("Переходим в Телевизоры");
        driver.findElement(By.xpath("//a[@href='/catalog--televizory/59601/list?hid=90639']")).click(); // переходим в раздел Телевизоров

        logger.info("Устанавливаем цену");
        driver.findElement(By.xpath("/html/body/div[1]/div[6]/div[2]/div[2]/div/div/div/div[3]/div/div/div[2]/div[1]" +
                "/div/div/fieldset/div[1]/ul/li[2]/p/input")).sendKeys("50000"); // устанавливаем максимальную цену

        waitForPreloader(By.cssSelector(".preloadable__preloader"), 0);

        logger.info("Устанавливаем диагональ");
        WebElement element = driver.findElement(By.xpath("//*[@id='15069594_15069596']")); // устанавливаем диагональ
        JavascriptExecutor executor1 = driver;
        executor1.executeScript("arguments[0].scrollIntoView();", element);
        new Actions(driver).moveToElement(element).click().perform();


        waitForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        waitForPreloader2(By.cssSelector(".preloadable__preloader"), 0);

        logger.info("Находим элементы с пометкой \"Выбор покупателя\"");
        list = driver.findElements(By.xpath(".//span[contains(@class, 'n-reasons-to-buy__label')]  [contains(text(),'Выбор покупателей')]"));

        logger.info("Добавляем товары к сравнению");
        String information;

        for (int i = 0; i < 5; ) {
            inner = list.get(i).findElement(By.xpath("../../../../div/div/div/div/i"));
            new Actions(driver).moveToElement(inner).click().build().perform();
            wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 1));
            waitForPreloader(By.cssSelector(".popup-informer__pane"), 1);
           information = driver.findElement(By.cssSelector("div.popup-informer__title")).getAttribute("innerHTML");
           logger.info(information);
            i++;
        }

        waitForPreloader2(By.cssSelector(".popup-informer__pane"), 1);

        WebElement element2 = driver.findElement(By.cssSelector(".popup-informer__close"));
        JavascriptExecutor executor2 = driver;
        executor2.executeScript("arguments[0].click();", element2);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 1));
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 0));

        waitForPreloader2(By.cssSelector(".preloadable__preloader"), 0);

        logger.info("Переходим к сравнению");
        driver.findElement(By.cssSelector("a.header2-menu__item_type_compare")).click();

        logger.info("Проверяем количество элементов на странице");
        waitForPreloader(By.cssSelector(".preloadable__preloader"), 0);

        logger.info("Элементов на странице: " + driver.findElements(By.cssSelector("div.n-compare-head__image")).size());


    }

    @After
    public void finish() {
        logger.info("Quit");
        driver.quit();
    }
}
