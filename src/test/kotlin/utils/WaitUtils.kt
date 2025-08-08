package utils

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.TimeoutException
import core.ConfigReader
import java.time.Duration

/**
 * 智能等待工具类，提供各种自适应等待方法
 */
object WaitUtils {
    
    enum class WaitType {
        FAST,    // 快速等待：3秒
        MEDIUM,  // 中等等待：8秒
        LONG     // 长等待：15秒
    }
    
    /**
     * 等待URL包含指定文本（自适应超时）
     */
    fun waitForUrlContains(driver: WebDriver, text: String, waitType: WaitType = WaitType.MEDIUM) {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        wait.until(ExpectedConditions.urlContains(text))
    }
    
    /**
     * 等待页面标题包含指定文本（自适应超时）
     */
    fun waitForTitleContains(driver: WebDriver, title: String, waitType: WaitType = WaitType.MEDIUM) {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        wait.until(ExpectedConditions.titleContains(title))
    }
    
    /**
     * 智能等待，用于替换Thread.sleep
     * 使用配置化的轮询间隔和重试次数
     */
    fun smartWait(driver: WebDriver, condition: () -> Boolean, maxWaitSeconds: Long = ConfigReader.getMediumWaitTimeout()) {
        val pollingInterval = ConfigReader.getSmartWaitPollingInterval()
        val endTime = System.currentTimeMillis() + (maxWaitSeconds * 1000)
        
        while (System.currentTimeMillis() < endTime) {
            try {
                if (condition()) {
                    return
                }
            } catch (e: Exception) {
                // 忽略条件检查中的异常，继续等待
            }
            Thread.sleep(pollingInterval)
        }
    }
    
    /**
     * 自适应元素等待 - 根据元素类型选择合适的等待策略
     */
    fun waitForElementSmart(driver: WebDriver, locator: By, waitType: WaitType = WaitType.MEDIUM): WebElement? {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        
        return try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        } catch (e: TimeoutException) {
            null
        }
    }
    
    /**
     * 等待元素可点击（带重试机制）
     */
    fun waitForElementClickableWithRetry(driver: WebDriver, locator: By, waitType: WaitType = WaitType.MEDIUM): WebElement? {
        val maxRetries = ConfigReader.getSmartWaitMaxRetries()
        val timeout = getTimeoutByType(waitType)
        
        repeat(maxRetries) { attempt ->
            try {
                val wait = WebDriverWait(driver, Duration.ofSeconds(timeout / maxRetries))
                return wait.until(ExpectedConditions.elementToBeClickable(locator))
            } catch (e: TimeoutException) {
                if (attempt == maxRetries - 1) {
                    return null
                }
                Thread.sleep(ConfigReader.getSmartWaitPollingInterval())
            }
        }
        return null
    }
    
    /**
     * 等待页面加载完成（检查document.readyState）
     */
    fun waitForPageLoad(driver: WebDriver, waitType: WaitType = WaitType.LONG) {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        
        wait.until { webDriver ->
            (webDriver as org.openqa.selenium.JavascriptExecutor)
                .executeScript("return document.readyState") == "complete"
        }
    }
    
    /**
     * 等待Ajax请求完成（检查jQuery.active）
     */
    fun waitForAjaxComplete(driver: WebDriver, waitType: WaitType = WaitType.MEDIUM) {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        
        wait.until { webDriver ->
            try {
                val result = (webDriver as org.openqa.selenium.JavascriptExecutor)
                    .executeScript("return typeof jQuery !== 'undefined' ? jQuery.active === 0 : true")
                result as Boolean
            } catch (e: Exception) {
                true // 如果没有jQuery，认为Ajax已完成
            }
        }
    }
    
    /**
     * 智能等待元素文本变化
     */
    fun waitForTextChange(driver: WebDriver, locator: By, expectedText: String, waitType: WaitType = WaitType.MEDIUM): Boolean {
        val timeout = getTimeoutByType(waitType)
        val pollingInterval = ConfigReader.getSmartWaitPollingInterval()
        val endTime = System.currentTimeMillis() + (timeout * 1000)
        
        while (System.currentTimeMillis() < endTime) {
            try {
                val element = driver.findElement(locator)
                if (element.text.contains(expectedText)) {
                    return true
                }
            } catch (e: Exception) {
                // 元素可能还未出现，继续等待
            }
            Thread.sleep(pollingInterval)
        }
        return false
    }
    
    /**
     * 等待元素属性值变化
     */
    fun waitForAttributeChange(driver: WebDriver, locator: By, attribute: String, expectedValue: String, waitType: WaitType = WaitType.MEDIUM): Boolean {
        val timeout = getTimeoutByType(waitType)
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeout))
        
        return try {
            wait.until(ExpectedConditions.attributeContains(locator, attribute, expectedValue))
            true
        } catch (e: TimeoutException) {
            false
        }
    }
    
    /**
     * 根据等待类型获取超时时间
     */
    private fun getTimeoutByType(waitType: WaitType): Long {
        return when (waitType) {
            WaitType.FAST -> ConfigReader.getFastWaitTimeout()
            WaitType.MEDIUM -> ConfigReader.getMediumWaitTimeout()
            WaitType.LONG -> ConfigReader.getLongWaitTimeout()
        }
    }
    
    /**
     * 性能监控 - 记录等待时间
     */
    fun <T> measureWaitTime(operation: () -> T): Pair<T, Long> {
        val startTime = System.currentTimeMillis()
        val result = operation()
        val endTime = System.currentTimeMillis()
        return Pair(result, endTime - startTime)
    }
}