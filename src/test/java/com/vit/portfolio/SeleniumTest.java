package com.vit.portfolio;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {

    private static WebDriver driver;
    private static JavascriptExecutor js;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    static void teardown() {
        if (driver != null) driver.quit();
    }

    // helper: scroll to element and click via JS (avoids navbar overlap)
    private void jsClick(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        js.executeScript("arguments[0].click();", element);
    }

    // TEST 1: Dashboard loads successfully
    @Test
    @Order(1)
    void testDashboardLoads() {
        driver.get(baseUrl() + "/dashboard");
        String title = driver.getTitle();
        Assertions.assertTrue(
            title.contains("Portfolio"),
            "Page title should contain 'Portfolio' but was: " + title
        );
    }

    // TEST 2: Dashboard has KPI cards
    @Test
    @Order(2)
    void testDashboardHasKpiCards() {
        driver.get(baseUrl() + "/dashboard");
        WebElement kpiSection = driver.findElement(By.className("kpi-card"));
        Assertions.assertNotNull(kpiSection, "KPI cards should be present on dashboard");
    }

    // TEST 3: Add Asset page loads
    @Test
    @Order(3)
    void testAddAssetPageLoads() {
        driver.get(baseUrl() + "/assets/add");
        WebElement form = driver.findElement(By.tagName("form"));
        Assertions.assertNotNull(form, "Add asset form should be present");
    }

    // TEST 4: Add a new asset through the form
    @Test
    @Order(4)
    void testAddAsset() {
        driver.get(baseUrl() + "/assets/add");

        driver.findElement(By.id("ticker")).sendKeys("SELENIUM");
        driver.findElement(By.id("name")).sendKeys("Selenium Test Stock");

        WebElement typeSelect = driver.findElement(By.id("type"));
        new Select(typeSelect).selectByValue("STOCK");

        driver.findElement(By.id("quantity")).sendKeys("5");
        driver.findElement(By.id("buyPrice")).sendKeys("1000");

        // Use JS click to avoid navbar overlap
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        jsClick(submitBtn);

        Assertions.assertTrue(
            driver.getCurrentUrl().contains("/dashboard"),
            "Should redirect to dashboard after adding asset"
        );
    }

    // TEST 5: Search works on dashboard
    @Test
    @Order(5)
    void testSearchFunctionality() {
        driver.get(baseUrl() + "/dashboard");

        WebElement searchInput = driver.findElement(By.name("search"));
        searchInput.sendKeys("SELENIUM");

        // Use JS click to avoid navbar overlap
        WebElement searchBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        jsClick(searchBtn);

        String pageSource = driver.getPageSource();
        Assertions.assertTrue(
            pageSource.contains("SELENIUM"),
            "Search results should contain the searched ticker"
        );
    }
}