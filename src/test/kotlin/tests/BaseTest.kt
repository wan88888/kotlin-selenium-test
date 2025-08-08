package tests

import core.ConfigReader
import hooks.Hooks
import pages.LoginPage
import org.openqa.selenium.WebDriver

/**
 * 测试基类，提供通用的测试方法和数据
 */
open class BaseTest {
    
    protected val driver: WebDriver
        get() = Hooks.driver
    
    protected fun createLoginPage(): LoginPage {
        return LoginPage(driver)
    }
    
    protected fun openLoginPage(): LoginPage {
        val loginPage = createLoginPage()
        val baseUrl = ConfigReader.getBaseUrl()
        return loginPage.openLoginPage(baseUrl)
    }
    
    protected fun getValidUsername(): String {
        return ConfigReader.getProperty("validUsername")
    }
    
    protected fun getValidPassword(): String {
        return ConfigReader.getProperty("validPassword")
    }
    
    protected fun getInvalidUsername(): String {
        return ConfigReader.getProperty("invalidUsername")
    }
    
    protected fun getInvalidPassword(): String {
        return ConfigReader.getProperty("invalidPassword")
    }
}