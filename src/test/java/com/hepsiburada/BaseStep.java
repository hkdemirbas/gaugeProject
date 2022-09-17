package com.hepsiburada;

import com.hepsiburada.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import com.hepsiburada.helper.ElementHelper;
import com.hepsiburada.helper.StoreHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class BaseStep extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseStep.class);

    private static String SAVED_ATTRIBUTE;

    public Actions actions = new Actions(driver);
    private String compareText;


    public BaseStep() {
        PropertyConfigurator
                .configure(BaseStep.class.getClassLoader().getResource("log4j.properties"));
    }

    @Step({"<url> adresine git"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }
    @Step("<text> textini <key> elementine yaz.")
    public void sendKeysText(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            highlightElement(findElement(key));
            logger.info(key + "islem basarili " + text + "yazildi.");
        }
    }
    @Step({"<long> milisaniye bekle."})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Elementine tıkla <key>"})
    public void clickElement(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            hoverElement(element);
            waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine tiklandi.");
        }
    }
    @Step("<int> saniye bekle.")
    public void waitSec(int seconds) throws InterruptedException {
        try {
            logger.info(seconds + "saniye beklendi.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Step("<key> elementini kontrol et.")
    public void checkElement(String key) {
        assertTrue(findElement(key).isDisplayed(), "Aranilan element bulunamadi.");
        highlightElement(findElement(key));
    }
    @Step("<key> elementinin üzerinde bekle.")
    public void hover(String key) {
        hoverElement(findElement(key));

    }

    WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return driver.findElements(infoParam);
    }
    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }
    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private void sendKeyEnter(String key) {
        findElement(key).sendKeys(Keys.ENTER);

    }

    private void sendKeyDown(String key) {
        findElement(key).sendKeys(Keys.DOWN);

    }
    protected void goToEndOfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void goToTopOfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }
    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement webElement = driver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }
    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }
    public void doubleclick(WebElement elementLocator) {
        Actions actions = new Actions(driver);
        actions.doubleClick(elementLocator).perform();
    }
    public String chooseDate() {
        Calendar now = Calendar.getInstance();
        int tarih = now.get(Calendar.DATE) + 2;
        return String.valueOf(tarih);
    }
    protected void waitClickable(By elementBy) {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webDriverWait.until(ExpectedConditions.elementToBeClickable(elementBy));
    }
    protected String readText(By elementBy) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        highlightElement(driver.findElement(elementBy));
        return driver.findElement(elementBy).getText();
    }
    public void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                "color: red; border: 1px dashed red;");
    }
}
