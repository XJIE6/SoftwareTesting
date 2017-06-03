package ru.spbau.mit.kravchenkoyura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by YuryKravchenko on 30/05/2017.
 */
public class Page {
    private WebDriver driver;
    private PriceFilter priceFilter;
    private ProducerFilter producerFilter;
    private WebDriverWait wait;

    public Page(WebDriver driver) {
        this.driver = driver;
        priceFilter = new PriceFilter(driver.findElement(By.cssSelector(".input_price_from")).findElement(By.cssSelector(".input__control")),
                driver.findElement(By.cssSelector(".input_price_to")).findElement(By.cssSelector(".input__control")));
        producerFilter = new ProducerFilter(driver.findElement(By.cssSelector(".n-filter-block__list-items")));
        wait = new WebDriverWait(driver, 10);
    }

    public PriceFilter getPriceFilter() {
        return priceFilter;
    }

    public ProducerFilter getProducerFilter() {
        return producerFilter;
    }

    public List<Card> getCards() {
        return driver.findElements(By.cssSelector(".n-snippet-card")).stream().map(Card::new).collect(Collectors.toList());
    }

    private void waitUpdate() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".n-snippet-card")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".n-snippet-card")));
    }

    public class PriceFilter {
        private WebElement elementFrom, elementTo;

        public PriceFilter(WebElement elementFrom, WebElement elementTo) {
            this.elementFrom = elementFrom;
            this.elementTo = elementTo;
        }

        public void setFrom(Integer price) {
            elementFrom.sendKeys(price.toString());
            waitUpdate();
        }

        public void setTo (Integer price) {
            elementTo.sendKeys(price.toString());
            waitUpdate();
        }
    }
    public class ProducerFilter {
        private WebElement element;

        public ProducerFilter(WebElement element) {
            this.element = element;
        }

        public boolean setProducer(String name) {
            Optional<WebElement> elem = element.findElements(By.cssSelector(".checkbox")).stream().filter(e -> e.getText().equals(name)).findAny();
            if (elem.isPresent()) {
                elem.get().click();
                waitUpdate();
                return true;
            }
            return false;
        }
    }
    public class Card {
        private int fromPrice, toPrice;
        private String name;
        public Card(WebElement elem) {
            name = elem.findElement(By.cssSelector(".snippet-card__header-text")).getText();
            fromPrice = parcePrice(elem.findElement(By.cssSelector(".snippet-card__price")).getText());
            toPrice = parcePrice(elem.findElement(By.cssSelector(".snippet-card__subprice")).getText());
        }

        private int parcePrice (String s) {
            int res = 0;
            for (int i = 0; i < s.length(); ++i) {
                if (Character.isDigit(s.charAt(i))) {
                    res *= 10;
                    res += s.charAt(i) - '0';
                }
            }
            return res;
        }

        public int getFromPrice() {
            return fromPrice;
        }

        public int getToPrice() {
            return toPrice;
        }

        public String getName() {
            return name;
        }
    }

}
