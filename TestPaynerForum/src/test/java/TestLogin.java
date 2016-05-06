
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class TestLogin {

    private WebDriver driver;

    @Before
    public void SetUp(){
        driver = new FirefoxDriver();
        driver.get("http://localhost:8023/Ultrapopfolk/welcome/index");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        this.Register("pesho", "pesho", "123", "123");

        driver.get("//localhost:8023/Ultrapopfolk/forums/view/id/1");
    }

    private void Register(String username, String email, String password, String repeat){
        WebElement registerButton = driver.findElement(By.id("registerButton"));
        registerButton.click();

        WebElement emailField = driver.findElement(By.id("email"));
        WebElement userNameField = driver.findElement(By.id("userRegister"));
        WebElement passwordField = driver.findElement(By.id("passRegister"));
        WebElement repeatPasswordField = driver.findElement(By.id("passRepeat"));

        String usernameToRegester = "gosho" + UUID.randomUUID().toString();
        String emailToRegester = UUID.randomUUID().toString() + "@abv.bg";
        emailField.sendKeys(emailToRegester);
        userNameField.sendKeys(usernameToRegester);
        passwordField.sendKeys("123");
        repeatPasswordField.sendKeys("123");

        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();
    }


    private void createTopic(){
        WebElement newTopicButton = driver.findElement(By.xpath("/html/body/div/main/section[2]/div/div[1]/a[1]"));

        newTopicButton.click();

        WebElement summary = driver.findElement(By.id("summary"));
        WebElement body = driver.findElement(By.id("body"));
        WebElement tags = driver.findElement(By.id("tags"));

        summary.sendKeys("test topic");
        body.sendKeys("test body");
        tags.sendKeys("tag 1,tag 2,worm");

        WebElement addTopicButton = driver.findElement(By.xpath("/html/body/div/main/div[2]/button"));

        addTopicButton.click();
    }

    @Test
    public void testSuccessfulCreate(){
        this.createTopic();

        WebElement answerButton = driver.findElement(By.id("answerButton"));
        answerButton.click();


        WebElement answerBody = driver.findElement(By.id("answer"));
        answerBody.sendKeys("test answer body");

        WebElement submitAnswerButton = driver.findElement(By.id("submitAnswer"));
        submitAnswerButton.click();

        WebElement answer = driver.findElement(By.xpath("/html/body/div/main/section[2]/div[4]/div/p[2]"));
        assertEquals("test answer body", answer.getText());
    }
}
