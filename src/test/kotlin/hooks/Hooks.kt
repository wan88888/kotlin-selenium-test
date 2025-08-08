package hooks

import core.DriverFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver

open class Hooks {
    protected lateinit var driver: WebDriver
    
    @BeforeEach
    fun setUp() {
        driver = DriverFactory.getDriver("chrome")
    }
    
    @AfterEach
    fun tearDown() {
        try {
            DriverFactory.quitDriver()
        } catch (e: Exception) {
            println("关闭浏览器失败: ${e.message}")
        }
    }
}