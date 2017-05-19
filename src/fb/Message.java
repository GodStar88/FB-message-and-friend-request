/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fb;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeObject.keys;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author C
 */


public class Message implements Runnable{
    public WebDriver driver1;
    private volatile boolean shutdown;
    public String username;
    public String password;
    public int member;
    public GUI gui;
    
    Message(String str1, String str2, GUI gui){
        username = str1;
        password = str2;
        this.gui = gui;
    }


    @Override
    public void run() {
        Login();
        while (!shutdown) {                      
            SendMessage();
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
    
    public void Login() {
        
        String user_dir = System.getProperty("user.dir");
        //String exePath = user_dir + "//chromedriver.exe";
        String exePath = "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath); //run chrome
        ChromeOptions option = new ChromeOptions();
        option.addArguments("disable-infobars");              //disable test automation message
        option.addArguments("--disable-notifications");       //disable notifications
        option.addArguments("--disable-web-security");        //disable save password windows
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        option.setExperimentalOption("prefs", prefs);
        option.addArguments("--window-position=-32000,-32000"); //don't show chrome windows
        driver1 = new ChromeDriver(option);
        driver1.get("https://www.facebook.com/");               //goto facebook.com
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
             gui.LogText1("Login Sucess");
             try {
                   Thread.sleep(500);
               } catch (InterruptedException ex) {
                   Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
               }
             //driver1.findElement(By.tagName("body")).click();
        }
        else {
            gui.LogText1("Login Failed");
            gui.MessageButton();
            driver1.quit();
            shutdown = true;
        }
 
    }
    
    public void SendMessage(){


        //message get and send message
        try { 
            
            //new message button click
            try {
                driver1.findElement(By.xpath("//div[@class='uiToggle _4962 _1z4y _330i _4kgv hasNew']")).click();
            } catch (Exception e) {
            }
            try {                                           
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            //new message button click
            try {
                driver1.findElement(By.xpath("//div[@class='uiToggle _4962 _1z4y _330i hasNew']")).click();
            } catch (Exception e) {
            }
            try { 
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            //get user name and message
            String message = "";
            String name = "";
            try {
                name = driver1.findElement(By.xpath("//div[@class='author']")).getText();
                message = driver1.findElement(By.xpath("//div[@class='snippet preview']")).getText();

            } catch (Exception e) {
            }
            driver1.findElement(By.xpath("//div[@class='snippet preview']")).click();
            gui.LogText1( name + " : " + message);
            try { 
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            //send message
            try {
                driver1.findElement(By.xpath("//div[@class='_5rpu']")).sendKeys("Hello");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                }
                driver1.findElement(By.xpath("//div[@class='_5rpu']")).sendKeys(Keys.ENTER);
                gui.LogText1("Sent message to " + name + " : 'Hello'");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            //click message window
            try {
                driver1.findElement(By.xpath("(//a[@class='_3olu _3olv close button'])[2]")).click();
            } catch (Exception e) {
            }
            driver1.get("https://www.facebook.com/");
            try {                            
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            } 
            checkAlert();
            //driver1.findElement(By.tagName("body")).click();
        } catch (Exception e) {
        }
        
        //sleep 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Get accept friend        
        try {
            //new accept friend button click
            driver1.findElement(By.xpath("//div[@class='uiToggle _4962 _3nzl _24xk hasNew']")).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            driver1.findElement(By.xpath("//tr[@class='_51mx']")).click();
            try {
                
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            driver1.findElement(By.xpath("//li[@class='fbProfileBrowserListItem']")).click();
            try {                
                Thread.sleep(2000);                
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            String name = "";
            name = driver1.findElement(By.xpath("//div[@class='_4a8n']")).getText();
            gui.LogText1( name + "  accepted your friend request.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                driver1.findElement(By.xpath("(//a[@class='_42ft _4jy0 _4jy4 _517h _51sy'])[1]")).click();
                try {                
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception e) {
            }
           
            try {
                driver1.findElement(By.xpath("(//a[@class='_42ft _4jy0 _4jy4 _517h _51sy'])[2]")).click();
                try {                
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception e) {
            }           

            
            try {
                driver1.findElement(By.xpath("//div[@class='_5rpu']")).sendKeys("Hello");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
                }
                driver1.findElement(By.xpath("//div[@class='_5rpu']")).sendKeys(Keys.ENTER);
                gui.LogText1("Send message to " + name + " : 'Hello'");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            driver1.findElement(By.xpath("(//a[@class='_3olu _3olv close button'])[2]")).click();
            try {                            
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            } 
            driver1.get("https://www.facebook.com/");
            try {                            
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
            }
            checkAlert();
            //driver1.findElement(By.tagName("body")).click();
        } catch (Exception e) {
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Friend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void checkAlert() {
    try {
        WebDriverWait wait = new WebDriverWait(driver1, 2);
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver1.switchTo().alert();
        alert.accept();
    } catch (Exception e) {
        //exception handling
    }
}
}
