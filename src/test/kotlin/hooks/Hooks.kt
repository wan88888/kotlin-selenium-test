package hooks

import core.DriverFactory
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.openqa.selenium.WebDriver

class Hooks : BeforeEachCallback, AfterEachCallback {
    companion object {
        @JvmStatic
        lateinit var driver: WebDriver
    }
    override fun beforeEach(context: ExtensionContext?) {
        driver = DriverFactory.getDriver()
    }
    
    override fun afterEach(context: ExtensionContext?) {
        try {
            DriverFactory.quitDriver()
        } catch (e: Exception) {
            println("关闭浏览器失败: ${e.message}")
        }
    }
}