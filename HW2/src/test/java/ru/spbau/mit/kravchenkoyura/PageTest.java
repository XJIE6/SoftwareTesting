package ru.spbau.mit.kravchenkoyura;

import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

/**
 * Created by YuryKravchenko on 30/05/2017.
 */
public class PageTest {

    @Test
    public void filterTest() throws Exception {
        int fromPrice = 1234;
        int toPrice = 5678;
        String producer = "Gmini";

        WebDriver driver = new FirefoxDriver();
        driver.get("https://market.yandex.ru/catalog/54761/list?local-offers-first=0&deliveryincluded=0&onstock=0");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo")));

        Page page = new Page(driver);

        page.getPriceFilter().setFrom(fromPrice);
        page.getPriceFilter().setTo(toPrice);
        assertTrue(page.getProducerFilter().setProducer(producer));

        List<Page.Card> cards = page.getCards();
        for (Page.Card c : cards) {
            if (c.getFromPrice() != 0) {
                assertFalse(c.getFromPrice() > toPrice);
            }
            if (c.getToPrice() != 0) {
                assertFalse(c.getToPrice() < fromPrice);
            }
            assertTrue(c.getName().contains(producer));
        }
    }
}


