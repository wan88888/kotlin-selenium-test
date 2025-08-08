package tests

import core.ConfigReader
import hooks.Hooks
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pages.LoginPage

class LoginTest : Hooks() {
    
    @Test
    @DisplayName("使用有效凭据进行正常登录测试")
    fun testSuccessfulLogin() {
        val loginPage = LoginPage(driver)
        val baseUrl = ConfigReader.getBaseUrl()
        
        // 打开登录页面
        loginPage.openLoginPage(baseUrl)

        
        // 执行登录操作
        loginPage.login("standard_user", "secret_sauce")
        
        // 等待页面跳转
        Thread.sleep(2000)

        
        // 验证登录成功
        assertTrue(loginPage.isLoginSuccessful(), "登录应该成功")
        assertTrue(loginPage.getCurrentUrl().contains("inventory"), "应该跳转到产品页面")
        assertFalse(loginPage.isErrorMessageDisplayed(), "不应该显示错误信息")
    }
    
    @Test
    @DisplayName("验证登录页面元素显示")
    fun testLoginPageElements() {
        val loginPage = LoginPage(driver)
        val baseUrl = ConfigReader.getBaseUrl()
        
        // 打开登录页面
        loginPage.openLoginPage(baseUrl)

        
        // 验证页面URL（处理尾部斜杠）
        val currentUrl = loginPage.getCurrentUrl().trimEnd('/')
        val expectedUrl = baseUrl.trimEnd('/')
        assertEquals(expectedUrl, currentUrl, "应该在正确的登录页面")
        
        // 验证没有错误信息显示
        assertFalse(loginPage.isErrorMessageDisplayed(), "初始状态不应该显示错误信息")
    }
    
    @Test
    @DisplayName("使用无效用户名登录测试")
    fun testLoginWithInvalidUsername() {
        val loginPage = LoginPage(driver)
        val baseUrl = ConfigReader.getBaseUrl()
        
        // 打开登录页面
        loginPage.openLoginPage(baseUrl)
        
        // 使用无效用户名登录
        loginPage.login("invalid_user", "secret_sauce")
        
        // 等待错误信息显示
        Thread.sleep(1000)

        
        // 验证登录失败
        assertFalse(loginPage.isLoginSuccessful(), "使用无效用户名登录应该失败")
        assertTrue(loginPage.isErrorMessageDisplayed(), "应该显示错误信息")
        
        val errorMessage = loginPage.getErrorMessage()
        assertTrue(errorMessage.isNotEmpty(), "错误信息不应该为空")
        assertTrue(errorMessage.contains("Username and password do not match"), "应该显示用户名密码不匹配的错误")
    }
}