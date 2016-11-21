package com.wikipediabookcreator;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WikipediaBookCreator extends Util{
	FirefoxProfile profile = new FirefoxProfile();
	WebDriver driver;
	WebDriverWait wait;
  @Test
  public void createbook() throws IOException, AWTException, InterruptedException {
	  driver.findElement(By.partialLinkText("Create a book")).click();
	  driver.findElement(By.xpath(".//*[@id='mw-content-text']/form/div/div[1]/button")).click();
	  for(int i=0;i<StringCount;i++)
	  {
	  books(Searchbox[i]);
	  }
	  driver.findElement(By.xpath(".//*[@id='coll-book_creator_box']/a[2]")).click();
	  String Heading = driver.findElement(By.xpath(".//*[@id='firstHeading']")).getText();
	  Assert.assertEquals(ManageHeading, Heading);
	  driver.findElement(By.xpath(".//*[@id='titleInput']")).sendKeys(BookTitle);
	  driver.findElement(By.xpath(".//*[@id='subtitleInput']")).sendKeys(SubTitle);
	  driver.findElement(By.xpath(".//*[@id='downloadButton']")).click();
	  wait = new WebDriverWait(driver,10);
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mw-content-text']/p[1]/strong[2]/a")));
	  driver.findElement(By.xpath(".//*[@id='mw-content-text']/p[1]/strong[2]/a")).click();
	  download();
  }

  public void books(String Searchbox){
	  driver.findElement(By.xpath(".//*[@id='searchInput']")).sendKeys(Searchbox);
	  driver.findElement(By.xpath(".//*[@id='searchButton']")).click();
	  driver.findElement(By.xpath(".//*[@id='coll-add_article']")).click();
  }
  public void download() throws AWTException, InterruptedException, IOException{
	   Robot robot = new Robot();
	   robot.mouseMove(1058, 412);
	   robot.mousePress(InputEvent.BUTTON3_MASK);
	   robot.mouseRelease(InputEvent.BUTTON3_MASK);
	   Thread.sleep(1000);
	   robot.keyPress(KeyEvent.VK_P);
	   Thread.sleep(1000);
	   Runtime.getRuntime().exec(filename);
  }
  public void FirefoxProfile(){
	  profile.setPreference("browser.download.folderList", 2);
	  profile.setPreference("browser.download.dir", downloadpath);
	  profile.setPreference("browser.download.manager.showWhenStarting", false);
	  profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf");
	  profile.setPreference("browser.helperApps.alwaysAsk.force", false);
	  profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
	  profile.setPreference("browser.download.manager.focusWhenStarting", false);
	  profile.setPreference("browser.download.manager.useWindow", false);
	  profile.setPreference("browser.download.manager.showAlertOnComplete", false);
	  profile.setPreference("browser.download.manager.closeWhenDone", false);
  }

  @BeforeMethod
  public void Setup() {
	  FirefoxProfile();
	  driver = new FirefoxDriver(profile);
	  driver.manage().window().maximize();
	  driver.get(URL);
	  driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
  }

  @AfterMethod
  public void afterMethod() throws InterruptedException {
	  driver.close();
	  Thread.sleep(2000);
	  Assert.assertTrue(isFileDownloaded(downloadpath,"ExportedBook"), "File Not Found");
  }
  public boolean isFileDownloaded(String downloadPath, String fileName) {
		boolean flag = false;
	    File dir = new File(downloadPath);
	    File[] dir_contents = dir.listFiles();
	  	    
	    for (int i = 0; i < dir_contents.length; i++) {
	        if (dir_contents[i].getName().contains(fileName))
	            return flag=true;
	            }

	    return flag;
	}

}
