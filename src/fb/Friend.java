/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fb;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author C
 */
public class Friend implements Runnable{
    public WebDriver driver1;
    private volatile boolean shutdown;
    public String username;
    public String password;
    public int member;
    public GUI gui;
    public int batche;
    Friend(String str1, String str2, int i, GUI gui){
        username = str1;
        password = str2;
        this.gui = gui;
        batche = i;
    }
    
    @Override
    public void run() {
        while (!shutdown) {
            Login();            
            GotoGroup();
            FriendRequest();
            gui.BatcheText(batche+1);
            gui.LogText("End"); 
            gui.FriendButton();
            driver1.quit();
            shutdown = true;
        }
    }
    
    public void Login() {
        
        String user_dir = System.getProperty("user.dir");
        //String exePath = user_dir + "//chromedriver.exe";
        String exePath = "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        ChromeOptions option = new ChromeOptions();
        option.addArguments("disable-infobars");
        //option.addArguments("--window-position=-32000,-32000");
        driver1 = new ChromeDriver(option);
        driver1.get("https://www.facebook.com/");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
        }
        driver1.findElement(By.id("email")).sendKeys(this.username);
        driver1.findElement(By.id("pass")).sendKeys(this.password);
        driver1.findElement(By.id("loginbutton")).click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(driver1.getCurrentUrl().toString().equals("https://www.facebook.com/")){      
             gui.LogText("Login Sucess");   
             
        }
        else {
            gui.LogText("Login Failed");
            gui.FriendButton();
            driver1.quit();
            shutdown = true;
        }
            
           
        
        
    }
    
    public void GotoGroup() {
        //Group Button Click in HomePage
        try {
            for (int i = 1; i < 50; i++)
            {
                String path = "(//div[@class='linkWrap noCount'])[" + Integer.toString(i) + "]";
                String text = driver1.findElement(By.xpath(path)).getText();
                if (text.equals("Groups"))
                {
                    driver1.findElement(By.tagName("body")).click();
                    driver1.findElement(By.xpath("(//div[@class='linkWrap noCount'])[" + Integer.toString(i) + "]")).click();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }  
        } catch (Exception e) {
        }
        
        //Groups tab click and Member tab click
        try {

            driver1.findElement(By.xpath("//div[@class='_4xjz']")).click();
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        
        //First Group select
        try {
             gui.LogText( "Member:" + driver1.findElement(By.xpath("//div[@class='_266w']")).getText());
             driver1.findElement(By.xpath("//div[@class='_266w']")).click();
             Thread.sleep(3000);
        } catch (Exception e) {
        }
        //Member Select
        try {
            for (int i = 1; i < 10; i++) {
                String path = "(//span[@class='_2yav'])[" + Integer.toString(i) + "]";
                String text = driver1.findElement(By.xpath(path)).getText();
                if (text.equals("Members"))
                {
                    driver1.findElement(By.xpath(path)).click();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }
        
        String mem = driver1.findElement(By.xpath("//span[@class='_50f8']")).getText();
        gui.LogText("Members" + mem);
        mem = mem.replace(",", "");
        member =  Integer.parseInt(mem);
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
        }       

    }
    
    public void FriendRequest(){
        
        for (int i = 0; i <= batche; i++) {
            driver1.findElement(By.xpath("//a[@class='pam uiBoxLightblue uiMorePagerPrimary']")).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        for (int i = (batche-1) * 100 + 1; i <= (batche-1) * 100 + 100; i++) {     
            String parentHandle = driver1.getWindowHandle(); 
            String path = "(//div[@class='fsl fwb fcb'])[" + Integer.toString(i) + "]//a";
            String url = driver1.findElement(By.xpath(path)).getAttribute("href");
            JavascriptExecutor js = (JavascriptExecutor)driver1;
            js.executeScript("window.open()");
            for (String winHandle : driver1.getWindowHandles()) {
                driver1.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
            }
            driver1.get(url);
            try {
                Thread.sleep(500);
                driver1.findElement(By.tagName("body")).click();
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                driver1.findElement(By.xpath("//button[@class='_42ft _4jy0 FriendRequestAdd addButton _4jy4 _4jy1 selected _51sy']")).click();
                String name = driver1.findElement(By.xpath("//div[@class='_4a8n']")).getText();
                gui.LogText("Sent request ( " + name + " )");             

            } catch (Exception e) {
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            driver1.close();
            driver1.switchTo().window(parentHandle);
        }
    }

    
    public void Start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void shutdown() { 
        
        try {
            driver1.quit();
        } catch (Exception e) {
        } 
        shutdown = true;
    } 
    
}
