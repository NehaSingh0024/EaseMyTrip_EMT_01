package pages;

import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;  
import org.apache.commons.io.FileUtils; 
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class HotelPage {
	WebDriver driver;  //Declaring WebDriver Globally 
	public HotelPage(WebDriver driver) {
		this.driver=driver;
	}
	//Using By to get the elements locators  
	By HotelNav = By.xpath("//*[@id=\"myTopnav\"]/div[1]/ul/li[2]/a"); 
	By City = By.xpath("//span[@class='hp_city']");
	By CityInput = By.id("txtCity");
	By CheckIn = By.id("txtcid");
	By CheckOut = By.id("txtcod");  
	By addAdult = By.id("Adults_room_1_1_plus"); 
	By removeAdult = By.id("Adults_room_1_1_minus"); 
	By removeAdult2 = By.id("Adults_room_2_2_minus");
	By addChild = By.id("Children_room_1_1_plus");
	By childAge = By.xpath("//select[@id='Child_Age_1_1']");
	By addRoom = By.id("addhotelRoom");
	By doneOnRoom = By.id("exithotelroom");
	By search = By.xpath("//input[@class='htlhp_btn']");
	By popularity = By.id("drpHighList");        
	By title1 = By.id("ddlGender1");
	By Fname = By.id("txtFirstName1");
	By Lname = By.id("txtLastName1");
    By Email = By.id("txtEmailId");
    By Mob = By.id("txtCPhone"); 
 
	public void goToHotels()  {  
		driver.findElement(HotelNav).click();    //click on the hotels from Nav Bar on home page to go to hotels section
	}
	
	public void emptyCity() throws InterruptedException { 
		driver.findElement(City).click();        //clicking on the city field to remove default and make it empty
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
		driver.findElement(CityInput).sendKeys(city,Keys.DOWN); 
		Thread.sleep(1000); 
		driver.findElement(CheckIn).click(); //clicking on check in to open calendar drop box
		driver.findElement(CheckInDate).click(); 
		driver.findElement(CheckOutDate).click();
	}
	
	public void AddAdult() throws InterruptedException {  
		driver.findElement(addAdult).click();   //clicking on + sign of adult to increase number of adults
	}
	public void removeAdult() throws InterruptedException {  
		driver.findElement(removeAdult).click(); //clicking on - sign of adult to decrease number of adults from room 1
	}
	public void removeAdult2() throws InterruptedException {  
		driver.findElement(removeAdult).click(); //clicking on - sign of adult to decrease number of adults from room 2
	}
	
	public void AddChild() throws InterruptedException { 
		driver.findElement(addChild).click();  //clicking on + sign of adult to add a child
		driver.findElement(childAge).click();  //clicking on child age drop down to select age of child
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[3]/div[2]/div//*[@id=\"Child_Age_1_1\"]/option[11]")).click(); //selecting child's age as 11
//		Select age = new Select(driver.findElement(addChild)); //I tried this but wasn't working
//		age.selectByVisibleText("11");
		Thread.sleep(2000);
		driver.findElement(doneOnRoom).click();  //click o done of the Guest and Room Drop Box
	}
	
	public void AddRoom() throws InterruptedException {
		driver.findElement(addRoom).click(); //clicking on add room ro add a different room
		Thread.sleep(1000);
		driver.findElement(removeAdult).click(); //removing a adult from it as default was 2
		driver.findElement(doneOnRoom).click();  //click o done of the Guest and Room Drop Box
	}
	public void clickSearch() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(search).click(); //click on search for Hotels search according to the data given
	}
	
	public void sortByPriceHL() {
		driver.findElement(popularity).click();// click on the popularity drop down 
		driver.findElement(popularity).sendKeys(Keys.DOWN,Keys.ENTER); //Selecting Price Low to High from the drop down
	}
	 
	public void bookRoom1() throws InterruptedException {//booking the room from in first Hotel from the search results
		driver.findElement(By.xpath("//*[@id=\"hotelListDiv\"]/div[1]/div[2]/div/div/a/div[5]")).click();//clicking on view room
		//Handling Windows as After clicking on View Room it opens In new tab So to tell the driver to switch to child tab
        Set<String> allwindows = driver.getWindowHandles();//storing address of windows in a Set
        Iterator<String> ite = allwindows.iterator();      //Using a iterator to traverse over the Set allwodows
        while(ite.hasNext())
        {
            String childwindow = ite.next();              //checks if the iterator has new element and stores in string
            driver.switchTo().window(childwindow);        // switching to the new child Tab
            Thread.sleep(3000);
        } 
		driver.findElement(By.xpath("//div[@class='btnhcol']/a[2]")).click(); //click on Book Now
		Thread.sleep(2000);
	}
	 
	public void enterGuestDetails(String tit, String fname, String lname, String email, String mob) {//This Method is to Enter Guest details while booking a room 
		Select title = new Select(driver.findElement(title1)); //Using a drop down to select title of a person
		title.selectByValue(tit);  //Select by value attribute of the Drop down
		driver.findElement(Fname).sendKeys(fname);
		driver.findElement(Lname).sendKeys(lname);
		driver.findElement(Email).sendKeys(email);
		driver.findElement(Mob).sendKeys(mob); 
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





 