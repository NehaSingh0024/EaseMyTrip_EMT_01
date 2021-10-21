package runner;
   
import java.io.FileInputStream; 
import java.util.Properties;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod; 
import org.testng.annotations.BeforeMethod;

public class BaseClass { //This Class is for Opening and Closing of Browser for opening the browser
	public WebDriver driver; 
	String browser; 
	String URL;
	@BeforeMethod	//Using BeforeMethod So that this Run Before each Test 
	public void beforeMethod() throws Exception //This method is for opening Browser
	{
		FileInputStream File = new FileInputStream("src\\test\\resources\\reads\\setup.properties");
		Properties prop = new Properties();
		prop.load(File); 
		browser = prop.getProperty("browser"); 
	    URL = prop.getProperty("URL"); 
		File.close(); 
		switch(browser) {
		case "Chrome":
			System.setProperty("webdriver.chrome.driver","src\\test\\resources\\drivers\\chromedriver.exe");
			driver=new ChromeDriver();  
			break;
		case "Firefox":
			System.setProperty("webdriver.gecko.driver","src\\test\\resources\\drivers\\geckodriver.exe");
			driver=new FirefoxDriver(); 
			break;  
		case "Edge":
			System.setProperty("webdriver.edge.driver","src\\test\\resources\\drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
			break;	
		} 
		driver.manage().window().maximize();  //maximizing the window
		driver.get(URL); //get the URL of EaseMyTrip.com
	}
	 
	@AfterMethod   //Using AfterMethod So that this Run After each Test to close the browser
	public void afterMethod() throws InterruptedException { //This method is for Closing the Browser
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //This was not working in EaseMyTrip.com
		Thread.sleep(4000);//So I used Thread.sleep to pauses the execution and helps us to know what has happened during the pause and the time is given in milliseconds 
//		driver.quit();
		
		if (driver == null) {
	        return;
	    }
		driver.quit();
		driver = null;
	} 
}
