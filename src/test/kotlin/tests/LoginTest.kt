package tests

import core.ConfigReader
import hooks.Hooks
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pages.LoginPage
import utils.WaitUtils
import utils.TestConstants

@ExtendWith(Hooks::class)
class LoginTest : BaseTest() {
    
    @Test
    @DisplayName("使用有效凭据进行正常登录测试")
    fun testSuccessfulLogin() {
        val loginPage = LoginPage(driver)
        val baseUrl = ConfigReader.getBaseUrl()
        
        // 打开登录页面
        loginPage.openLoginPage(baseUrl)

        
        // 执行登录操作
        loginPage.login(getValidUsername(), getValidPassword())
        
        // 等待页面跳转（使用快速等待）
        WaitUtils.waitForUrlContains(driver, TestConstants.INVENTORY_URL_FRAGMENT, WaitUtils.WaitType.FAST)

        
        // 验证登录成功
        assertTrue(loginPage.isLoginSuccessful(), "登录应该成功")
        assertTrue(driver.currentUrl?.contains("inventory") == true, "应该跳转到产品页面")
        assertFalse(loginPage.isErrorMessageDisplayed(), "不应该显示错误信息")
    }
    

    
    @Test
    @DisplayName("使用无效用户名登录测试")
    fun testLoginWithInvalidUsername() {
        val loginPage = LoginPage(driver)
        val baseUrl = ConfigReader.getBaseUrl()
        
        // 打开登录页面
        loginPage.openLoginPage(baseUrl)
        
        // 使用无效用户名登录
        loginPage.login(getInvalidUsername(), getValidPassword())
        
        // 等待错误信息显示（使用智能等待）
        WaitUtils.smartWait(driver, { loginPage.getErrorMessage().isNotEmpty() }, ConfigReader.getFastWaitTimeout())

        
        // 验证登录失败
        assertFalse(loginPage.isLoginSuccessful(), "使用无效用户名登录应该失败")
        assertTrue(loginPage.isErrorMessageDisplayed(), "应该显示错误信息")
        
        val errorMessage = loginPage.getErrorMessage()
        assertTrue(errorMessage.isNotEmpty(), "错误信息不应该为空")
        assertTrue(errorMessage.contains(TestConstants.INVALID_CREDENTIALS_ERROR), "应该显示用户名密码不匹配的错误")
    }
}