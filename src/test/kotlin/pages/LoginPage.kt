package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class LoginPage(driver: WebDriver) : BasePage(driver) {
    
    // 页面元素定位器
    private val usernameField = By.id("user-name")
    private val passwordField = By.id("password")
    private val loginButton = By.id("login-button")
    private val errorMessage = By.cssSelector("[data-test='error']")
    private val logoElement = By.className("login_logo")
    
    // 页面操作方法
    fun openLoginPage(url: String): LoginPage {
        driver.get(url)
        // 只等待登录表单的核心元素加载完成
        waitForElementToBeVisible(usernameField)
        waitForElementToBeVisible(loginButton)
        return this
    }
    
    fun enterUsername(username: String): LoginPage {
        sendKeys(usernameField, username)
        return this
    }
    
    fun enterPassword(password: String): LoginPage {
        sendKeys(passwordField, password)
        return this
    }
    
    fun clickLoginButton(): LoginPage {
        clickElement(loginButton)
        return this
    }
    
    fun login(username: String, password: String): LoginPage {
        enterUsername(username)
        enterPassword(password)
        clickLoginButton()
        return this
    }
    
    fun isErrorMessageDisplayed(): Boolean {
        return isElementDisplayed(errorMessage)
    }
    
    fun getErrorMessage(): String {
        return if (isErrorMessageDisplayed()) {
            getText(errorMessage)
        } else {
            ""
        }
    }
    
    fun isLoginSuccessful(): Boolean {
        return try {
            // 检查URL是否包含inventory，表示登录成功
            driver.currentUrl?.contains("inventory") ?: false
        } catch (e: Exception) {
            false
        }
    }
}