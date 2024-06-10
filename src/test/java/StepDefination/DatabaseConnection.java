package StepDefination;


import java.sql.*;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class DatabaseConnection {
	public static String name;
	public static boolean carStatus;
	public static int marketingId;
	public static String marketingTime;
	public static int id;
	public static String buyLead;
	@Given ("get user status")
	public static void userStatus()
		{
			// TODO Auto-generated method stub
		createLead.generatePhoneNumber();
			  RestAssured.baseURI= "https://spntest.spinnyworks.in";
			 given().header("content-type","application/json")
			   .header("platform","web")
			 //  .header("Keycloak-Authorization",createLead.accessToken)
			   .body(payload.getUserStatus())
			   .when().post("/api/c/user_status/")
			   .then().assertThat().statusCode(200);
		}
	@And ("sending otp")
			   public static void sendOtpRequest()
			   {
			// TODO Auto-generated method stub
			  RestAssured.baseURI= "https://spntest.spinnyworks.in";
			  String response = given().header("content-type","application/json")
			   .header("platform","web")
			  // .header("Keycloak-Authorization",createLead.accessToken)
			   .body(payload.sendOtpBody())
			   .when().post("api/c/user/otp-request/")
			   .then().assertThat().statusCode(200).extract().asPrettyString();
			   System.out.println(response);
			}	
	@When ("Login to consumer")
			   public static void userLogin() throws ClassNotFoundException, SQLException
			   {
			// TODO Auto-generated method stub
				   getOtp();  
			  RestAssured.baseURI= "https://spntest.spinnyworks.in";
			  String response = given().log().all().header("content-type","application/json")
			   .header("platform","web")
			  // .header("Keycloak-Authorization",createLead.accessToken)
			   .body(payload.sendLoginDetails())
			   .when().post("api/c/user/register/")
			   .then().assertThat().statusCode(200).extract().asPrettyString();
			   System.out.println(response);
			   }
	
	
	public static void getMarketingId()
	   {
	// TODO Auto-generated method stub
		getTime();
	  RestAssured.baseURI= "https://spntest.spinnyworks.in";
	  String response = given().log().all().header("content-type","application/json")
	   .header("platform","web")
	   .body(payload.getMarketingAttribution())
	  // .header("Keycloak-Authorization",createLead.accessToken)
	   .when().post("api/marketing/")
	   .then().assertThat().statusCode(201).log().all().extract().asPrettyString();
	   System.out.println(response);
	   JsonPath js5 = new JsonPath(response);
		 marketingId = js5.getInt("id");
			System.out.println("marketing attribution id = "+marketingId);
	   
	   }
	@Then ("buyLead Created from consumer")
	
	public static void callback()
	   {
	// TODO Auto-generated method stub
		getMarketingId();
		RestAssured.baseURI= "https://spntest.spinnyworks.in";
	  String response = given().log().all().header("content-type","application/json")
	   .header("platform","web")
	   .body(payload.getCallBack())
	  // .header("Keycloak-Authorization",createLead.accessToken)
	   .when().post("api/c/forms/callback/")
	   .then().assertThat().statusCode(201).log().all().extract().asPrettyString();
	   System.out.println(response);
	   JsonPath js4 = new JsonPath(response);
		 buyLead = js4.getString("buy_lead_id");
			System.out.println("buyLead Id = "+buyLead);
	   
	   }
		
		public static void getTime() 
		{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			System.out.println(dtf.format(now));
			marketingTime = dtf.format(now);
		}
	
		public static void getOtp() throws ClassNotFoundException, SQLException 
		{
			String url = "jdbc:mysql://sp-spinnyweb-dev-aurora-db-ap-south-1b.crevgiuvg8xk.ap-south-1.rds.amazonaws.com:3306/spinny?useSSL=false&serverTimezone=UTC";
			String uname = "akshayanand";
			String pwd ="CX1s2ybSF_F725gA";
			String query = "select * from spinny_auth_mobileverificationcode order by -id limit 1;";
		
		
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			Connection con = DriverManager.getConnection(url,uname,pwd);
		
		
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(query);
			rs.next();
			rs.getString(1);
			name = rs.getString("code");
		
			System.out.println(name);
		
			st.close();
			con.close();
		
		}
		
	}
