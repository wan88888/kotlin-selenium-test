package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

open class BasePage(protected val driver: WebDriver) {
    private val wait = WebDriverWait(driver, Duration.ofSeconds(10))
    
    protected fun findElement(locator: By): WebElement {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator))
    }
    
    protected fun findElements(locator: By): List<WebElement> {
        return driver.findElements(locator)
    }
    
    protected fun clickElement(locator: By) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click()
    }
    
    protected fun sendKeys(locator: By, text: String) {
        val element = wait.until(ExpectedConditions.presenceOfElementLocated(locator))
        element.clear()
        element.sendKeys(text)
    }
    
    protected fun getText(locator: By): String {
        return findElement(locator).text
    }
    
    protected fun isElementDisplayed(locator: By): Boolean {
        return try {
            findElement(locator).isDisplayed
        } catch (e: Exception) {
            false
        }
    }
    
    protected fun waitForElementToBeVisible(locator: By): WebElement {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
    }
    
    protected fun waitForElementToBeClickable(locator: By): WebElement {
        return wait.until(ExpectedConditions.elementToBeClickable(locator))
    }
}