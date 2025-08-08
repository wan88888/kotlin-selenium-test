# Kotlin Selenium 测试框架

一个基于 Kotlin 和 Selenium WebDriver 的现代化 Web 自动化测试框架，采用 Page Object Model 设计模式，提供智能等待策略和灵活的配置管理。

## 🚀 特性

- **现代化技术栈**: 使用 Kotlin + Selenium 4 + JUnit 5
- **Page Object Model**: 清晰的页面对象模式，提高代码可维护性
- **智能等待策略**: 自适应等待机制，提高测试稳定性
- **灵活配置管理**: 外部化配置，支持多环境
- **丰富的基础方法**: 扩展的 BasePage 类，包含常用操作
- **自动化浏览器管理**: 使用 WebDriverManager 自动管理驱动
- **无头模式支持**: 适合 CI/CD 环境

## 📁 项目结构

```
src/test/kotlin/
├── core/                    # 核心组件
│   ├── ConfigReader.kt      # 配置读取器
│   └── DriverFactory.kt     # WebDriver 工厂类
├── hooks/                   # 测试钩子
│   └── Hooks.kt            # JUnit 5 扩展钩子
├── pages/                   # 页面对象
│   ├── BasePage.kt         # 基础页面类
│   └── LoginPage.kt        # 登录页面类
├── tests/                   # 测试用例
│   ├── BaseTest.kt         # 基础测试类
│   └── LoginTest.kt        # 登录测试用例
└── utils/                   # 工具类
    ├── TestConstants.kt     # 测试常量
    └── WaitUtils.kt        # 智能等待工具

src/test/resources/
└── config/
    └── config.properties    # 配置文件
```

## 🏗️ 架构设计

### 核心组件

#### 1. DriverFactory (WebDriver 管理)
- 单例模式管理 WebDriver 实例
- 支持 Chrome 和 Firefox 浏览器
- 自动配置无头模式和浏览器选项
- 集成 WebDriverManager 自动管理驱动程序

#### 2. ConfigReader (配置管理)
- 统一管理所有配置项
- 支持多种等待时间配置
- 提供类型安全的配置访问方法

#### 3. BasePage (页面基类)
- 提供 30+ 常用 Selenium 操作方法
- 包含元素查找、点击、输入、等待等基础功能
- 支持鼠标操作、JavaScript 执行、下拉框操作等高级功能

#### 4. WaitUtils (智能等待)
- 三种等待类型：FAST (10s)、MEDIUM (15s)、LONG (30s)
- 自适应等待策略，根据操作类型选择合适的等待时间
- 支持页面加载、Ajax 请求、元素状态变化等多种等待场景

### 设计模式

- **Page Object Model**: 每个页面对应一个页面类，封装页面元素和操作
- **Factory Pattern**: DriverFactory 使用工厂模式创建和管理 WebDriver
- **Singleton Pattern**: ConfigReader 和 DriverFactory 采用单例模式
- **Template Method**: BaseTest 提供测试模板方法

## 🛠️ 环境要求

- **Java**: JDK 17 或更高版本
- **Kotlin**: 2.0.20
- **Gradle**: 8.8 或更高版本
- **浏览器**: Chrome 或 Firefox（自动管理驱动程序）

## 📦 依赖项

```gradle
dependencies {
    testImplementation 'org.jetbrains.kotlin:kotlin-test'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
    testImplementation 'org.seleniumhq.selenium:selenium-java:4.25.0'
    testImplementation 'io.github.bonigarcia:webdrivermanager:5.6.2'
}
```

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd kotlin-selenium-test
```

### 2. 配置环境
编辑 `src/test/resources/config/config.properties`：

```properties
# 应用配置
baseUrl=https://www.saucedemo.com

# 测试数据
validUsername=standard_user
validPassword=secret_sauce

# 浏览器配置
defaultBrowser=chrome
implicitWait=5
explicitWait=8

# 智能等待配置
fastWaitTimeout=10
mediumWaitTimeout=15
longWaitTimeout=30
```

### 3. 运行测试
```bash
# 运行所有测试
./gradlew test

# 运行特定测试类
./gradlew test --tests LoginTest

# 运行特定测试方法
./gradlew test --tests LoginTest.testSuccessfulLogin
```

### 4. 查看测试报告
测试完成后，在 `build/reports/tests/test/index.html` 查看详细报告。

## 📝 编写测试用例

### 1. 创建页面对象
```kotlin
class MyPage(driver: WebDriver) : BasePage(driver) {
    private val submitButton = By.id("submit")
    private val inputField = By.name("input")
    
    fun enterText(text: String): MyPage {
        sendKeys(inputField, text)
        return this
    }
    
    fun clickSubmit(): MyPage {
        clickElement(submitButton)
        return this
    }
}
```

### 2. 编写测试用例
```kotlin
@ExtendWith(Hooks::class)
class MyTest : BaseTest() {
    
    @Test
    @DisplayName("测试用例描述")
    fun testExample() {
        val myPage = MyPage(driver)
        
        myPage.enterText("测试文本")
              .clickSubmit()
        
        // 使用智能等待
        WaitUtils.waitForUrlContains(driver, "success", WaitUtils.WaitType.FAST)
        
        assertTrue(driver.currentUrl?.contains("success") == true)
    }
}
```

## ⚙️ 配置说明

### 浏览器配置
- `defaultBrowser`: 默认浏览器 (chrome/firefox)
- `implicitWait`: 隐式等待时间（秒）
- `explicitWait`: 显式等待时间（秒）

### 智能等待配置
- `smartWaitPollingInterval`: 轮询间隔（毫秒）
- `smartWaitMaxRetries`: 最大重试次数
- `fastWaitTimeout`: 快速等待超时时间（秒）
- `mediumWaitTimeout`: 中等等待超时时间（秒）
- `longWaitTimeout`: 长等待超时时间（秒）

## 🔧 高级功能

### 智能等待策略
```kotlin
// 根据操作类型选择等待时间
WaitUtils.waitForUrlContains(driver, "inventory", WaitUtils.WaitType.FAST)
WaitUtils.waitForPageLoad(driver, WaitUtils.WaitType.LONG)
WaitUtils.waitForAjaxComplete(driver, WaitUtils.WaitType.MEDIUM)

// 自定义条件等待
WaitUtils.smartWait(driver, { 
    loginPage.getErrorMessage().isNotEmpty() 
}, ConfigReader.getFastWaitTimeout())
```

### 丰富的页面操作
```kotlin
// 基础操作
clickElement(locator)
sendKeys(locator, text)
getText(locator)

// 高级操作
doubleClickElement(locator)
rightClickElement(locator)
hoverOverElement(locator)
dragAndDrop(sourceLocator, targetLocator)
scrollToElement(locator)

// JavaScript 操作
jsClickElement(locator)
jsSetValue(locator, value)

// 下拉框操作
selectByVisibleText(locator, text)
selectByValue(locator, value)
selectByIndex(locator, index)
```

## 📊 测试报告

框架自动生成详细的测试报告，包括：
- 测试执行概览
- 成功/失败统计
- 详细的错误信息
- 执行时间统计

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 常见问题

### Q: 如何切换浏览器？
A: 修改 `config.properties` 中的 `defaultBrowser` 配置项为 `chrome` 或 `firefox`。

### Q: 如何在 CI/CD 中运行？
A: 框架默认启用无头模式，可直接在 CI/CD 环境中运行。

### Q: 如何调试测试失败？
A: 查看 `build/reports/tests/test/index.html` 中的详细错误信息和堆栈跟踪。

### Q: 如何添加新的等待策略？
A: 在 `WaitUtils` 类中添加新的等待方法，并在 `config.properties` 中添加相应配置。

---

**Happy Testing! 🎉**