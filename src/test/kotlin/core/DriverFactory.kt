package core

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import java.time.Duration

object DriverFactory {
    private var driver: WebDriver? = null
    
    fun getDriver(browserType: String = ConfigReader.getDefaultBrowser()): WebDriver {
        if (driver == null) {
            driver = createDriver(browserType)
            setupDriver(driver!!)
        }
        return driver!!
    }
    
    private fun createDriver(browserType: String): WebDriver {
        return when (browserType.lowercase()) {
            "chrome" -> {
                WebDriverManager.chromedriver().setup()
                val options = ChromeOptions()
                options.addArguments("--no-sandbox")
                options.addArguments("--disable-dev-shm-usage")
                options.addArguments("--disable-gpu")
                options.addArguments("--window-size=1920,1080")
                options.addArguments("--headless") // 添加无头模式以便在CI环境中运行
                ChromeDriver(options)
            }
            "firefox" -> {
                WebDriverManager.firefoxdriver().setup()
                val options = FirefoxOptions()
                options.addArguments("--width=1920")
                options.addArguments("--height=1080")
                options.addArguments("--headless") // 添加无头模式
                FirefoxDriver(options)
            }
            else -> throw IllegalArgumentException("不支持的浏览器类型: $browserType")
        }
    }
    
    private fun setupDriver(driver: WebDriver) {
        val implicitWait = ConfigReader.getImplicitWait()
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait))
        driver.manage().window().maximize()
    }
    
    fun quitDriver() {
        driver?.quit()
        driver = null
    }
}