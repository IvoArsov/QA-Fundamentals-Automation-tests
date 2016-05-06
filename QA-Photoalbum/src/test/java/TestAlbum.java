import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestAlbum {

    private static final String DEFAULT_PASSWORD = "123";
    private String currentUsername;
    private String currentCategory;
    private String currentAlbum;

    private WebDriver driver;

    @Before
    public void setUp(){
        this.driver = new FirefoxDriver();
        this.driver.get("http://localhost:8023/PhotoAlbum/home/");
        this.driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        this.driver.manage().window().maximize();

        this.createCategory();
        this.register();
        this.login(this.currentUsername, DEFAULT_PASSWORD);
        this.createAlbum(this.currentCategory);
    }

    private void register(){
        this.driver.get("http://localhost:8023/PhotoAlbum/users/register");

        WebElement usernameField = this.driver.findElement(By.id("inputUserName"));
        WebElement passwordField = this.driver.findElement(By.id("inputPassword"));
        String uniqueUsername = UUID.randomUUID().toString();

        usernameField.sendKeys(uniqueUsername);
        passwordField.sendKeys(DEFAULT_PASSWORD);

        this.currentUsername = uniqueUsername;

        WebElement registerButton = this.driver.findElement(By.name("register"));
        registerButton.click();
    }

    private void login(String username, String password){
        this.driver.get("http://localhost:8023/PhotoAlbum/users/login");

        WebElement usernameField = this.driver.findElement(By.id("inputUserName"));
        WebElement passwordField = this.driver.findElement(By.id("inputPassword"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        WebElement loginButton = this.driver.findElement(By.name("login"));
        loginButton.click();
    }

    private void createCategory(){
        this.login("admin", "admin");
        this.driver.get("http://localhost:8023/PhotoAlbum/categories/add");

        WebElement categoryNameField = this.driver.findElement(By.name("name"));
        WebElement createCategoryButton = this.driver.findElement(By.name("create"));

        String uniqueCategoryName = UUID.randomUUID().toString();
        this.currentCategory = uniqueCategoryName;

        categoryNameField.sendKeys(uniqueCategoryName);
        createCategoryButton.click();
        this.logout();
    }

    private void logout(){
        this.driver.get("http://localhost:8023/PhotoAlbum/users/logout");
    }

    private void createAlbum(String categoryName){
        this.driver.get("http://localhost:8023/PhotoAlbum/albums/add");

        WebElement albumName = this.driver.findElement(By.name("name"));
        WebElement categoryNameField = this.driver.findElement(By.name("categoryName"));
        WebElement descriptionField = this.driver.findElement(By.name("description"));
        WebElement createAlbumButton = this.driver.findElement(By.name("create"));

        String uniqueAlbumName = UUID.randomUUID().toString();
        this.currentAlbum = uniqueAlbumName;

        albumName.sendKeys(uniqueAlbumName);
        categoryNameField.sendKeys(categoryName);
        descriptionField.sendKeys("aaa");
        createAlbumButton.click();
    }

    private void navigateToAlbum(String albumName){
        this.driver.get("http://localhost:8023/PhotoAlbum/albums/showall");

        List<WebElement> albumNamesElements = this.driver.findElements(By.className("album-title"));

        for ( WebElement albumNameElement : albumNamesElements ) {
            if (albumNameElement.getText().equals(albumName)) {
                albumNameElement.click();
                break;
            }
        }
    }

    private void navigateToComment(){
        WebElement commentButton = this.driver.findElement(By.xpath("/html/body/div/div[4]/div/span[2]/a"));
        commentButton.click();
    }

    private void createComment(String commentText){
        WebElement commentTextField = this.driver.findElement(By.name("comment"));
        WebElement createCommentButton = this.driver.findElement(By.name("create"));

        commentTextField.sendKeys(commentText);
        createCommentButton.click();
    }

    @Test
    public void testComment_withoutLoggedUser_expectedErrorBox(){
        this.logout();
        this.navigateToAlbum(this.currentAlbum);
        this.navigateToComment();
        this.createComment("novo");

        WebElement errorBox = this.driver.findElement(By.xpath("/html/body/div/form/div/div[2]"));
        Assert.assertEquals("You are not logged in", errorBox.getText());
    }

    @Test
    public void testComment_allValid_expectedCommentShouldCreated(){
        this.navigateToAlbum(this.currentAlbum);
        this.navigateToComment();
        this.createComment("po-novo");

        DateFormat dateFormat = new SimpleDateFormat("d-MMMM-yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        WebElement commentInfo = this.driver.findElement(By.xpath("/html/body/div/div[4]/ul/li"));
        String expectedComment = String.format("Comment text: %s, User: %s, Created On: %s",
                "po-novo",
                this.currentUsername,
                currentDate
                );

        Assert.assertEquals(expectedComment, commentInfo.getText());
    }

    @After
    public void tearDown(){
        this.driver.quit();
    }

}
