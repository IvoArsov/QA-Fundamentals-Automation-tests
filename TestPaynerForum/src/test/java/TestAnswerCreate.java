import org.junit.*;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestAnswerCreate {

    WebDriver driver;

    @Before
    public void SetUp(){
        driver = new FirefoxDriver();
        driver.get("http://localhost:8023/Ultrapopfolk/welcome/index");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @Test
    public void testAnswerCreateAsVisitor(){
        WebElement topic = driver.findElement(By.xpath("/html/body/div/main/table/tbody/tr[14]/td[1]/a"));
        topic.click();

        WebElement theme = driver.findElement(By.xpath("/html/body/div/main/section[2]/table/tbody/tr[2]/td[1]/a"));
        theme.click();

        WebElement answerButton = driver.findElement(By.id("answerButton"));
        answerButton.click();

        WebElement nameBar = driver.findElement(By.id("author"));
        nameBar.sendKeys("name" + UUID.randomUUID().toString());
        WebElement answerBar = driver.findElement(By.id("answer"));
        answerBar.sendKeys("answer test");

        WebElement submitAnswerButton = driver.findElement(By.id("submitAnswer"));
        submitAnswerButton.click();

        WebElement answer = driver.findElement(By.xpath("/html/body/div/main/section[2]/div[5]/div/p[2]"));
        assertEquals("answer test", answer.getText());
    }

    @Test
    public void testAnswerCreateAsRegisteredUser(){
        WebElement registerButton = driver.findElement(By.id("registerButton"));
        registerButton.click();

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(UUID.randomUUID().toString() + "@abv.bg");

        WebElement usernameField = driver.findElement(By.id("userRegister"));
        usernameField.sendKeys("user " + UUID.randomUUID().toString());

        WebElement passwordField = driver.findElement(By.id("passRegister"));
        passwordField.sendKeys("123");

        WebElement repeatPasswordField = driver.findElement(By.id("passRepeat"));
        repeatPasswordField.sendKeys("123");

        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();

        WebElement topic = driver.findElement(By.xpath("/html/body/div/main/table/tbody/tr[14]/td[1]/a"));
        topic.click(); // SEX

        WebElement theme = driver.findElement(By.xpath("/html/body/div/main/section[2]/table/tbody/tr[2]/td[1]/a"));
        theme.click(); // moga li da zabremeneq

        WebElement answerButton = driver.findElement(By.id("answerButton"));
        answerButton.click();

        //WebElement nameBar = driver.findElement(By.id("author"));
        //nameBar.sendKeys("name" + UUID.randomUUID().toString());
        WebElement answerBar = driver.findElement(By.id("answer"));
        answerBar.sendKeys("answer test");

        WebElement submitAnswerButton = driver.findElement(By.id("submitAnswer"));
        submitAnswerButton.click();

        WebElement answer = driver.findElement(By.xpath("/html/body/div/main/section[2]/div[5]/div/p[2]"));
        assertEquals("answer test", answer.getText());
    }
}
