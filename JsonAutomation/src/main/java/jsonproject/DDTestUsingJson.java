package jsonproject;


import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DDTestUsingJson {
	
	WebDriver driver;
	
	@BeforeClass
	void setup(){
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
				
	}
	@AfterClass
	void teardown(){
		driver.close();
	}
	
	@Test(dataProvider="dp")
	void login(String data)
	{
	String users[]=data.split(",");
		driver.get("https://classic.crmpro.com/index.html");
		driver.findElement(By.name("username")).sendKeys(users[0]);
		driver.findElement(By.name("password")).sendKeys(users[1]);
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		String act_title=driver.getTitle();
		String exp_title="CRMPRO";
		Assert.assertEquals(act_title,exp_title);
	}

	@DataProvider(name="dp")
	public String[] readJson() throws IOException, ParseException{
		
		JSONParser jsonparser=new JSONParser();
		FileReader file= new FileReader(".\\jsonfiles\\testdata.json");
		
		Object obj=jsonparser.parse(file);
		
		JSONObject jsonobj=(JSONObject)obj;
		JSONArray array=(JSONArray)jsonobj.get("userlogins");
		
		String arr[]=new String[array.size()];
		
		for(int i=0;i<array.size();i++){
			JSONObject userlogin=(JSONObject)array.get(i);
			String user=(String)userlogin.get("username");
			String pwd=(String)userlogin.get("password");
			
			arr[i]=user+","+pwd;
			
		}
		
		return arr;		
	}
}
