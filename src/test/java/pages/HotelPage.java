package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;  
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class HotelPage {
	WebDriver driver;  //Declaring WebDriver Globally 
	Properties prop;
	public HotelPage(WebDriver driver) throws Exception {
		this.driver=driver;
		FileInputStream File = new FileInputStream("src\\test\\resources\\reads\\setup.properties");
		prop = new Properties();
		prop.load(File);  
	}
	 
	public void goToHotels() throws InterruptedException  {  
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("HotelNav"))).click();    //click on the hotels from Nav Bar on home page to go to hotels section
	} 
	
	public void emptyCity() throws InterruptedException { 
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("CityS"))).click();        //clicking on the city field to remove default and make it empty
	}
	
	public void enterCityCidCod() throws Exception { 
		//Using data Driven to Read city name, check in date and check out date from setup.properties file
		FileInputStream File = new FileInputStream("src\\test\\resources\\reads\\setup.properties");
		Properties prop = new Properties(); //storing each key and its value from setup file
		prop.load(File);   //loading the prop from file
		String cid = prop.getProperty("Cid"); //getting data from setup file
		String cod = prop.getProperty("Cod"); 
		String city = prop.getProperty("City"); 
		File.close();     //closing the file after reading data
		By CheckInDate = By.xpath("//td[@data-month='9']/a[text()='"+cid+"']");
		By CheckOutDate = By.xpath("//td[@data-month='10']/a[text()='"+cod+"']");
		driver.findElement(By.id(prop.getProperty("CityInput"))).sendKeys(city,Keys.DOWN); 
		Thread.sleep(1000); 
		driver.findElement(By.id(prop.getProperty("CheckIn"))).click(); //clicking on check in to open calendar drop box
		driver.findElement(CheckInDate).click(); 
		driver.findElement(CheckOutDate).click();
	}
	
	public void AddAdult() throws InterruptedException {  
		Thread.sleep(1000);
		driver.findElement(By.id(prop.getProperty("addAdult"))).click();   //clicking on + sign of adult to increase number of adults
	}
	public void removeAdult() throws InterruptedException {  
		Thread.sleep(1000);
		driver.findElement(By.id(prop.getProperty("removeAdult"))).click(); //clicking on - sign of adult to decrease number of adults from room 1
	}
	public void removeAdult2() throws InterruptedException {  
		Thread.sleep(2000);
		driver.findElement(By.id(prop.getProperty("removeAdult2"))).click(); //clicking on - sign of adult to decrease number of adults from room 2
	}
	
	public void AddChild() throws InterruptedException {  
		driver.findElement(By.id(prop.getProperty("addChild"))).click();  //clicking on + sign of adult to add a child
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("childAge"))).click();  //clicking on child age drop down to select age of child
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("childAge11"))).click(); //selecting child's age as 11
//		Select age = new Select(driver.findElement(addChild)); //I tried this but wasn't working
//		age.selectByVisibleText("11");
		Thread.sleep(2000);
		driver.findElement(By.id(prop.getProperty("doneOnRoom"))).click();  //click o done of the Guest and Room Drop Box
	}
	
	public void AddRoom() throws InterruptedException {
		driver.findElement(By.id(prop.getProperty("addRoom"))).click(); //clicking on add room ro add a different room
		Thread.sleep(1000);
		driver.findElement(By.id(prop.getProperty("removeAdult"))).click(); //removing a adult from it as default was 2
		driver.findElement(By.id(prop.getProperty("doneOnRoom"))).click();  //click o done of the Guest and Room Drop Box
	}
	public void clickSearch() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("search"))).click(); //click on search for Hotels search according to the data given
	}
	
	public void sortByPriceHL() {
		driver.findElement(By.id(prop.getProperty("popularity"))).click();// click on the popularity drop down 
		driver.findElement(By.id(prop.getProperty("popularity"))).sendKeys(Keys.DOWN,Keys.ENTER); //Selecting Price Low to High from the drop down
	}
	 
	public void bookRoom1() throws InterruptedException {//booking the room from in first Hotel from the search results
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("ViewRoom1"))).click();//clicking on view room
		//Handling Windows as After clicking on View Room it opens In new tab So to tell the driver to switch to child tab
        Set<String> allwindows = driver.getWindowHandles();//storing address of windows in a Set
        Iterator<String> ite = allwindows.iterator();      //Using a iterator to traverse over the Set allwodows
        while(ite.hasNext())
        {
            String childwindow = ite.next();              //checks if the iterator has new element and stores in string
            driver.switchTo().window(childwindow);        // switching to the new child Tab
            Thread.sleep(3000);
        } 
		driver.findElement(By.xpath(prop.getProperty("BookNow1"))).click(); //click on Book Now
		Thread.sleep(2000);
	}
	 
	public void enterGuestDetails(String tit, String fname, String lname, String email, String mob) {//This Method is to Enter Guest details while booking a room 
		Select title = new Select(driver.findElement(By.id(prop.getProperty("title1")))); //Using a drop down to select title of a person
		title.selectByValue(tit);  //Select by value attribute of the Drop down
		driver.findElement(By.id(prop.getProperty("Fname"))).sendKeys(fname);
		driver.findElement(By.id(prop.getProperty("Lname"))).sendKeys(lname);
		driver.findElement(By.id(prop.getProperty("Email"))).sendKeys(email);
		driver.findElement(By.id(prop.getProperty("Mob"))).sendKeys(mob); 
	} 
	 
	public void screenshot(String Sname) throws IOException { //This method Takes Screenshot wherever called
		 TakesScreenshot screenshot = ((TakesScreenshot)driver); //creating an object
		 File src = screenshot.getScreenshotAs(OutputType.FILE); //Storing the source in src from where to take screenshot
		 File destnation = new File("src\\test\\resources\\screenshots\\"+Sname+".png"); //Giving destination folder "Sname is name of the image that will be provided while calling this function"
		 FileUtils.copyFile(src, destnation); //Copying the from source to destination
	}
	 
	public boolean isAlertPresent(){  //This Method Will check Alert is present or Not and return a boolean
	    try { 
	        driver.switchTo().alert(); 
	        return true; 
	    }catch (NoAlertPresentException Ex){ 
	        return false; 
	    }   
	}
 
	
}





 