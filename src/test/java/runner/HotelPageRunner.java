package runner;
 
import java.io.FileInputStream;
import java.io.IOException;  
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.HotelPage;

public class HotelPageRunner extends BaseClass{
	 
	HotelPage hp;                               //declaring hp globally so that it can be accessed in every test
	
	@Test(enabled = true, priority = 1)         //enabled to true so that this test will run and giving it priority 1 to run this test First
	public void goToLinkTest() throws InterruptedException, IOException { //Testing if it is going on the Hotels from the NavBar on Home Screen
		hp = new HotelPage(driver);             //creating an object of HotelPage
		hp.goToHotels();                        //calling goToHotels from HotelPage
		Assert.assertTrue(driver.getCurrentUrl().contains("hotels"));//Using assrtTrue (returns true if condition is true) to check if the it is navigated to the correct URL after clicking 
		hp.screenshot("NavToHotel");            //Calling screenshot method from hp this Takes ScreenShot at of the end page after Test and the png file name as mentioned in the parameter here
	} 
	
	@Test(enabled = true, priority = 2)
	public void searchByEmptyCity() throws Exception {//This test is to search for Hotels By Keeping the City field Empty so that it gives error alert
		goToLinkTest();           
		hp.emptyCity();           
		hp.clickSearch();  
		Alert alert = driver.switchTo().alert();      //Handling the alert here switching to the alert 
	 	String al = alert.getText();     
	 	alert.accept();                            //pressing alert OK
	 	Assert.assertEquals("Please select city location name",al); //Getting the text of alert and asserting if its correct with assertEquals
	 	hp.screenshot("EmptyCity");
	}
	
	@Test(enabled = true, priority = 3)
	public void searchWithAllData() throws Exception { //This test is by giving all correct data and searching and seeing if the search is valid
		goToLinkTest();   
		hp.emptyCity();
		hp.enterCityCidCod(); 
		hp.AddAdult();  
		hp.clickSearch();
		Assert.assertTrue(driver.getCurrentUrl().contains("Mumbai"));//Checking if search is relevant to the word searched by
		hp.screenshot("AllDataSearch");
	}
	
	@Test(enabled = true, priority = 4) 
	public void searchWithAllnchild() throws Exception { //This test is to search by giving all valid data plus adding a child and its age in room for checking additional functionality
		goToLinkTest(); 
		hp.emptyCity();
		hp.enterCityCidCod();
		hp.AddAdult(); 
		hp.AddChild();
		hp.screenshot("AllnChild");                     //Taking screenshot where child is getting added and age is given as 11
		hp.clickSearch();
		Assert.assertTrue(driver.getCurrentUrl().contains("Mumbai")); 
	}
	
	
	@Test(enabled = true, priority = 5)
	public void searchWithTwoRoom() throws Exception {  //This test is to search by giving all valid data plus adding a room to make it 2 rooms for checking additional functionality
		goToLinkTest(); 
		hp.emptyCity();
		hp.enterCityCidCod(); 
		hp.AddAdult(); 
		hp.removeAdult2();
		hp.AddRoom();
		hp.screenshot("TwoRoomSearch");                 //Taking screenshot where room is getting added 
		hp.clickSearch();
		Assert.assertTrue(driver.getCurrentUrl().contains("Mumbai")); 
	}
	
	@Test(enabled = true, priority = 6)
	public void sortByPriceLH() throws Exception {     //This test is to check if searched result is getting sorted by price high to low
		searchWithTwoRoom();
		hp.sortByPriceHL(); 
		String LtoH = driver.findElement(By.xpath("//select[@id='drpHighList']/option[2]")).getText();//Getting the text of sorted drop down
		Assert.assertEquals("Price - Low to High", LtoH); //Checking if the result is sorted
		hp.screenshot("PriceLowToHigh");               
		
	}
	@Test(enabled = true, priority = 7)
	public void bookRoom() throws Exception {     // This test is to check the book now button functionality and it is taking us to a new tab of guest entering details
		searchWithAllData();
		Thread.sleep(2000);
		hp.bookRoom1(); 
		Assert.assertEquals("Traveller Detail", driver.getTitle());       //asserting the test by checking title name
		hp.screenshot("TillBookNow");
	}
	
	  
	@Test(enabled = true, priority = 8)
	public void FourStar() throws Exception {        //This test is to check if available filter of 4 star hotel is getting applied or not
		searchWithAllnchild();
		driver.findElement(By.xpath("//div[@class='sidebar__inner']/div[6]/label[2]/span[text()='4']")).click(); 
		Thread.sleep(3000);
		boolean star = driver.findElement(By.xpath("//*[@id=\"hotelListDiv\"]/div[1]/div[2]/div/a/div/h3/span[2]/span/i")).isDisplayed();
		Assert.assertTrue(star);
		hp.screenshot("FourStarHotels");
	}
	
	@Test(enabled = true, priority = 9)
	public void EnterValidGuestDetailTest() throws Exception { //This Test will Check if all valid details of Guest is getting accepted or not
		bookRoom(); 
		//Using Data Driven Framework 
		XSSFWorkbook wb =  new XSSFWorkbook(new FileInputStream("src\\test\\resources\\reads\\data.xlsx")); //Using a excel workbook to pass the data
		XSSFSheet st=wb.getSheet("guest");                     //getting the sheet 
		Thread.sleep(2000);
		String title = st.getRow(1).getCell(0).toString();     //Storing data from excel in a string by row and cell number and converting it to a string
		String fname = st.getRow(1).getCell(1).toString();
		String lname = st.getRow(1).getCell(2).toString();
		String email = st.getRow(1).getCell(3).toString();
		String mob = st.getRow(1).getCell(4).toString();
		wb.close();                                            //Closing the Excel Workbook
		hp.enterGuestDetails(title, fname, lname, email, mob); //Passing the data of excel to enterGuestDetails method
		driver.findElement(By.id("btnTravellerContinue")).click();//clicking continue to pay button
		Thread.sleep(1000);
		String payFinal = driver.findElement(By.xpath("//*[@id=\"card\"]/div[9]/div[2]")).getText();
		Assert.assertEquals("Make Payment", payFinal);         //asserting if its taking us to the next page of making payment in new tab 
		hp.screenshot("PayPageAfterGuestDetails");
	}
	
	@Test(enabled = true, priority = 10)
	public void EnterInvalidEmailTest() throws Exception {    //This Test will Check if InValid Email in details of Guest is getting accepted or not
		bookRoom(); 
		XSSFWorkbook wb =  new XSSFWorkbook(new FileInputStream("src\\test\\resources\\reads\\data.xlsx"));
		XSSFSheet st=wb.getSheet("guest");
		Thread.sleep(2000);
		String title = st.getRow(2).getCell(0).toString();
		String fname = st.getRow(2).getCell(1).toString();
		String lname = st.getRow(2).getCell(2).toString();
		String email = st.getRow(2).getCell(3).toString();
		String mob = st.getRow(2).getCell(4).toString();
		wb.close();
		hp.enterGuestDetails(title, fname, lname, email, mob);  
		Thread.sleep(2000);
		driver.findElement(By.id("btnTravellerContinue")).click();  
//	 	hp.screenshot("InvalidEmailAlert");      //Taking ss where it gives a alert msg of invalid email  
		Alert alert = driver.switchTo().alert();  
	 	String al = alert.getText();
	 	alert.accept();
	 	Assert.assertEquals("please enter the valid emailid",al);//checking if its a Valid Alert
	 	hp.screenshot("InvalidEmailNotAccepted");
	}
	
	@Test(enabled = true, priority = 11)
	public void EnterInvalidMobNoTest() throws Exception { //This Test Checks if it Takes Invalid Mobile is accepted or not
		bookRoom(); 
		XSSFWorkbook wb =  new XSSFWorkbook(new FileInputStream("src\\test\\resources\\reads\\data.xlsx"));
		XSSFSheet st=wb.getSheet("guest");
		Thread.sleep(2000);
		String title = st.getRow(3).getCell(0).toString();
		String fname = st.getRow(3).getCell(1).toString();
		String lname = st.getRow(3).getCell(2).toString();
		String email = st.getRow(3).getCell(3).toString();
		String mob = st.getRow(3).getCell(4).toString();
		wb.close();
		hp.enterGuestDetails(title, fname, lname, email, mob); 
		hp.screenshot("GivenInValidMob");
		driver.findElement(By.id("btnTravellerContinue")).click();
		Thread.sleep(1000); 
		boolean ap = hp.isAlertPresent(); //Here in the boolean variable we store result of the method isAlertPresent
	 	Assert.assertTrue(ap);            //returns True if Alert is Present -- But this Gives False as alert is Not seen Even after giving Wrong Mobile No
	 	hp.screenshot("InValidMobAccepted");
	}
	
	@Test(enabled = true, priority = 12)
	public void NoGuestDetailsTest() throws Exception { //This Test Will check to proceed for payment By giving No details of the Guest. This should give Alert msg
		bookRoom();
		driver.findElement(By.id("btnTravellerContinue")).click(); 
		Thread.sleep(1000);
		Alert alert = driver.switchTo().alert();  
	 	String al = alert.getText();
	 	alert.accept();
	 	Assert.assertEquals("please enter the first name of guest 1",al);
	 	hp.screenshot("EmptyGuest");
	}
	
	@Test(enabled = true, priority = 13)
	public void uncheckTnCTest() throws Exception { //This Test is to Uncheck  the Terms & condition CheckBox and Proceed to payment
		bookRoom();
		driver.findElement(By.xpath("//label[@class='ctr_cbox lh18']/span[2]")).click(); //Clicks On checkbox to uncheck 
		driver.findElement(By.id("btnTravellerContinue")).click(); 
		Thread.sleep(1000);
		Alert alert = driver.switchTo().alert();  
	 	String al = alert.getText();
	 	alert.accept();
	 	Assert.assertEquals("please accept T and C",al);
	 	hp.screenshot("UncheckTnC");
	}	
	 
}
