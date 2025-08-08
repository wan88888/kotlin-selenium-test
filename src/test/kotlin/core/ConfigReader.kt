package core

import java.io.FileInputStream
import java.util.*

object ConfigReader {
    private val properties = Properties()
    
    init {
        val configFile = "src/test/resources/config/config.properties"
        try {
            FileInputStream(configFile).use { input ->
                properties.load(input)
            }
        } catch (e: Exception) {
            throw RuntimeException("配置文件加载失败: $configFile", e)
        }
    }
    
    fun getProperty(key: String): String {
        return properties.getProperty(key) ?: throw RuntimeException("配置项 '$key' 未找到")
    }
    
    fun getBaseUrl(): String = getProperty("baseUrl")
}