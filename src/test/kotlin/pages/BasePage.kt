package pages

import core.ConfigReader
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import java.time.Duration

open class BasePage(protected val driver: WebDriver) {
    private val wait = WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
    
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
    
    // ========== 扩展的基础方法 ==========
    
    /**
     * 获取元素属性值
     */
    protected fun getAttribute(locator: By, attributeName: String): String? {
        return findElement(locator).getAttribute(attributeName)
    }
    
    /**
     * 获取元素CSS属性值
     */
    protected fun getCssValue(locator: By, propertyName: String): String {
        return findElement(locator).getCssValue(propertyName)
    }
    
    /**
     * 检查元素是否启用
     */
    protected fun isElementEnabled(locator: By): Boolean {
        return try {
            findElement(locator).isEnabled
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 检查元素是否被选中（适用于checkbox、radio等）
     */
    protected fun isElementSelected(locator: By): Boolean {
        return try {
            findElement(locator).isSelected
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 双击元素
     */
    protected fun doubleClickElement(locator: By) {
        val element = waitForElementToBeClickable(locator)
        Actions(driver).doubleClick(element).perform()
    }
    
    /**
     * 右键点击元素
     */
    protected fun rightClickElement(locator: By) {
        val element = waitForElementToBeClickable(locator)
        Actions(driver).contextClick(element).perform()
    }
    
    /**
     * 鼠标悬停
     */
    protected fun hoverOverElement(locator: By) {
        val element = waitForElementToBeVisible(locator)
        Actions(driver).moveToElement(element).perform()
    }
    
    /**
     * 拖拽元素
     */
    protected fun dragAndDrop(sourceLocator: By, targetLocator: By) {
        val source = waitForElementToBeVisible(sourceLocator)
        val target = waitForElementToBeVisible(targetLocator)
        Actions(driver).dragAndDrop(source, target).perform()
    }
    
    /**
     * 滚动到元素位置
     */
    protected fun scrollToElement(locator: By) {
        val element = findElement(locator)
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true);", element)
    }
    
    /**
     * 使用JavaScript点击元素
     */
    protected fun jsClickElement(locator: By) {
        val element = findElement(locator)
        (driver as JavascriptExecutor).executeScript("arguments[0].click();", element)
    }
    
    /**
     * 使用JavaScript设置元素值
     */
    protected fun jsSetValue(locator: By, value: String) {
        val element = findElement(locator)
        (driver as JavascriptExecutor).executeScript("arguments[0].value = arguments[1];", element, value)
    }
    
    /**
     * 清除输入框内容
     */
    protected fun clearField(locator: By) {
        val element = findElement(locator)
        element.clear()
    }
    
    /**
     * 选择下拉框选项（通过可见文本）
     */
    protected fun selectByVisibleText(locator: By, text: String) {
        val element = findElement(locator)
        Select(element).selectByVisibleText(text)
    }
    
    /**
     * 选择下拉框选项（通过值）
     */
    protected fun selectByValue(locator: By, value: String) {
        val element = findElement(locator)
        Select(element).selectByValue(value)
    }
    
    /**
     * 选择下拉框选项（通过索引）
     */
    protected fun selectByIndex(locator: By, index: Int) {
        val element = findElement(locator)
        Select(element).selectByIndex(index)
    }
    
    /**
     * 获取下拉框当前选中的选项文本
     */
    protected fun getSelectedOptionText(locator: By): String {
        val element = findElement(locator)
        return Select(element).firstSelectedOption.text
    }
    
    /**
     * 等待元素消失
     */
    protected fun waitForElementToDisappear(locator: By) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
    }
    
    /**
     * 等待文本出现在元素中
     */
    protected fun waitForTextToBePresentInElement(locator: By, text: String) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text))
    }
    
    /**
     * 等待元素属性包含指定值
     */
    protected fun waitForAttributeContains(locator: By, attribute: String, value: String) {
        wait.until(ExpectedConditions.attributeContains(locator, attribute, value))
    }
    
    /**
     * 切换到iframe
     */
    protected fun switchToFrame(locator: By) {
        val frame = findElement(locator)
        driver.switchTo().frame(frame)
    }
    
    /**
     * 切换回默认内容
     */
    protected fun switchToDefaultContent() {
        driver.switchTo().defaultContent()
    }
    
    /**
     * 处理Alert弹窗
     */
    protected fun acceptAlert(): String? {
        return try {
            val alert = wait.until(ExpectedConditions.alertIsPresent())
            val text = alert.text
            alert.accept()
            text
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 取消Alert弹窗
     */
    protected fun dismissAlert(): String? {
        return try {
            val alert = wait.until(ExpectedConditions.alertIsPresent())
            val text = alert.text
            alert.dismiss()
            text
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 在Alert中输入文本
     */
    protected fun sendKeysToAlert(text: String) {
        val alert = wait.until(ExpectedConditions.alertIsPresent())
        alert.sendKeys(text)
        alert.accept()
    }
    
    /**
     * 获取页面标题
     */
    protected fun getPageTitle(): String {
        return driver.title
    }
    
    /**
     * 获取当前URL
     */
    protected fun getCurrentUrl(): String {
        return driver.currentUrl
    }
    
    /**
     * 刷新页面
     */
    protected fun refreshPage() {
        driver.navigate().refresh()
    }
    
    /**
     * 浏览器后退
     */
    protected fun navigateBack() {
        driver.navigate().back()
    }
    
    /**
     * 浏览器前进
     */
    protected fun navigateForward() {
        driver.navigate().forward()
    }
    
    /**
     * 打开新标签页
     */
    protected fun openNewTab() {
        (driver as JavascriptExecutor).executeScript("window.open();")
    }
    
    /**
     * 切换到指定标签页
     */
    protected fun switchToTab(tabIndex: Int) {
        val tabs = driver.windowHandles.toList()
        if (tabIndex < tabs.size) {
            driver.switchTo().window(tabs[tabIndex])
        }
    }
    
    /**
     * 关闭当前标签页
     */
    protected fun closeCurrentTab() {
        driver.close()
    }
    
    /**
     * 截图（返回字节数组）
     */
    protected fun takeScreenshot(): ByteArray {
        return (driver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
    }
}