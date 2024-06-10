package StepDefination;

import static io.restassured.RestAssured.given;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class opsdashboardfilters {
	
	public static String time1;
	public static String time2;
	public static String accessToken;
	
	
	public static void TimeBeforeMonth()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		time.add(Calendar.MONTH,-1);
		//System.out.println("Start Date  : " + dateFormat.format(time.getTime()));
		time1= dateFormat.format(time.getTime());
		//System.out.println("Start Date =" +time1);
		 
	} 	
	
	public static void GetCurrentTimeStamp() 
	{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
     
        time2 = dtf.format(now);
        //System.out.println("End date : " + time2);
    }
	
	@Given("Creat acces token")
	public static void enviroment_should_be_up_and_running()
	{
		Response tokenRes = given().header("Content-Type", "application/x-www-form-urlencoded")
		        .formParam("client_id", "sp-buylead-backend")
		        .formParam("client_secret", "ab52b7d7-7d6a-42eb-8f94-b38a7dfaa702")
		        .formParam("scope", "openid")
		        .formParam("username", "test.user")
		        .formParam("password", "@7SWqfyKpQ")
		        .formParam("grant_type", "password").when().post("https://sso-dev.spinny.com/auth/realms/internal/protocol/openid-connect/token").then().statusCode(200).extract().response();
		
		String created_Token = tokenRes.jsonPath().get("access_token").toString();
		accessToken = "Bearer " + created_Token;
		
		//System.out.println(accessToken);
		System.out.println("Login Successful");	
	}
	
	
	
	
	@When("Fetch sell-lead id for unlisted-cars")
	
	public static void Unlistedcars() 
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		RestAssured.baseURI= payload.BaseURL();
		
		String response = given().header("content-type","application/json")
				
				.header("Keycloak-Authorization",accessToken)
				.header("client","dev")
				//.queryParam("page","1")
				//.queryParam("registration_no", "KA23AA3732")
				//.queryParam("makes","25")
				.queryParam("models","280")
				.queryParam("dispatch_date_after", time1 )
				.queryParam("dispatch_date_before", time2)
				.queryParam("custody_marked_date_after",time1)
				.queryParam("custody_marked_date_before",time2 )
				.queryParam("car_custody_hub", "1")
				.queryParam("city","Gurgaon")
				.queryParam("lead_procurement_category", "assured")
				.queryParam("is_dispatch_marked", "true")
				//.queryParam("is_dispatch_marked", "false")
				.queryParam("is_custody_marked", "true")
				//.queryParam("is_custody_marked", "false")
				//.queryParam("is_post_refurb_inspection_done", "true")
				//.queryParam("is_post_refurb_inspection_done", "false")
				//.queryParam("marked_not_applicable_for_listing", "true")
				//.queryParam("marked_not_applicable_for_listing", "false")
				.queryParam("listing_status","unlisted")
				.when().get("/api/leads/v2/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.id");
				String SellLeadcount = js.getString("count");	
				System.out.println("Sell lead retrived after applying filter from Unlisted-cars is: "+retrivedSellLead);
				System.out.println("Sell lead count for Unlisted-cars is: "+SellLeadcount);
				
	}
	
	
	@And("Fetch sell-lead id for unpublished-cars")
	public static void Unpublishsedcars ()
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
            RestAssured.baseURI= payload.BaseURL();
		
            String response = given().header("content-type","application/json")
				.header("Keycloak-Authorization",accessToken)
				.header("client","dev")
				//.queryParam("page","1")
				//  .queryParam("registration_no", "KA08BB6787")
				.queryParam("makes","25")
				.queryParam("models","280")
				.queryParam("dispatch_date_after", time1)
				.queryParam("dispatch_date_before", time2)
				.queryParam("custody_marked_date_after", time1)
				.queryParam("custody_marked_date_before", time2)
				.queryParam("car_custody_hub", "1")
				.queryParam("city","Gurgaon")
				.queryParam("lead_procurement_category", "assured")
				//.queryParam("marked_not_applicable_for_listing","true")
				.queryParam("marked_not_applicable_for_listing","false")
				.queryParam("unpublished_on_after", time1)
				.queryParam("unpublished_on_before",time2)
				.queryParam("listing_status","unpublished")
				.when().get("/api/leads/v2/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.id");
				String SellLeadcount = js.getString("count");
				System.out.println("Sell lead retrived after applying filter from Unpublished-cars is: "+retrivedSellLead);
				System.out.println("Sell lead count for Unpublished-cars is: "+SellLeadcount);
	}
	
	
	@And("Fetch sell-lead id for published-cars")
	public static void Publishedcars()
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		 RestAssured.baseURI= payload.BaseURL();
		 
		 
		String response = given().header("content-type","application/json")
				.header("Keycloak-Authorization",accessToken)
				.header("client","dev")
				//.queryParam("page","1")
				//  .queryParam("registration_no", "KA08BB6787")
				.queryParam("makes","25")
				.queryParam("models","280")
				.queryParam("dispatch_date_after", time1)
				.queryParam("dispatch_date_before", time2)
				.queryParam("custody_marked_date_after", time1)
				.queryParam("custody_marked_date_before", time2)
				.queryParam("car_custody_hub", "1")
				.queryParam("city","Gurgaon")
				.queryParam("lead_procurement_category", "assured")
				.queryParam("is_custody_marked", "true")
				.queryParam("unpublished_on_after", time1)
				.queryParam("unpublished_on_before", time2)
				.queryParam("published_on_after", time1)
				.queryParam("published_on_before", time2)
				.queryParam("listing_status","published")
				.when().get("/api/leads/v2/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.id");
				String SellLeadcount = js.getString("count");
				//System.out.println(time1);
				//System.out.println(time2);
				System.out.println("Sell lead retrived after applying filter from Published-cars is: "+retrivedSellLead);
				System.out.println("Sell lead count for Published-cars is: "+SellLeadcount);
	}
	
	
	@And("Fetch sell-lead id for in-progress-cars")
	public static void  Inprogresscars()
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
	
		RestAssured.baseURI= payload.BaseURL();
	
	
			String response = given().header("content-type","application/json")
				.header("Keycloak-Authorization",accessToken)
				.header("client","dev")
				//.queryParam("page","1")
				//  .queryParam("registration_no", "KA08BB6787")
				.queryParam("makes","25")
				.queryParam("models","280")
				.queryParam("dispatch_date_after", time1)
				.queryParam("dispatch_date_before", time2)
				.queryParam("custody_marked_date_after", time1)
				.queryParam("custody_marked_date_before", time2)
				.queryParam("car_custody_hub", "1")
				.queryParam("city","Gurgaon")
				.queryParam("lead_procurement_category", "assured")
				//.queryParam("listing_status", "scheduled_for_publish")       //task status
				.queryParam("listing_status", "in_progress")
				//.queryParam("listing_status", "could_not_publish")
				.queryParam("unpublished_on_after", time1)
				.queryParam("unpublished_on_before", time2)
				.queryParam("published_on_after", time1)
				.queryParam("published_on_before", time2)
				.queryParam("listing_status","intermediate")
				.when().get("/api/leads/v2/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.id");
				String SellLeadcount = js.getString("count");	
				System.out.println("Sell lead retrived after applying filter from Inprogress-cars is: "+retrivedSellLead);
				System.out.println("Sell lead count for Inprogress-cars is: "+SellLeadcount);
	}
	
	@Then("Fetch sell-lead id for all-cars")
	public static void Allcars()
	{
		
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		RestAssured.baseURI= payload.BaseURL();
		
		
		String response = given().header("content-type","application/json")
			.header("Keycloak-Authorization",accessToken)
			.header("client","dev")
			//.queryParam("page","1")
			//  .queryParam("registration_no", "KA08BB6787")
			.queryParam("makes","25")
			.queryParam("models","280")
			.queryParam("dispatch_date_after", time1)
			.queryParam("dispatch_date_before", time2)
			.queryParam("custody_marked_date_after", time1)
			.queryParam("custody_marked_date_before", time2)
			.queryParam("car_custody_hub", "1")
			.queryParam("city","Gurgaon")
			.queryParam("lead_procurement_category", "assured")
			//.queryParam("listing_status", "scheduled_for_publish")       //Task status
			//.queryParam("listing_status", "in_progress")
			//.queryParam("listing_status", "could_not_publish")
			//.queryParam("listing_status", "sold")
			//.queryParam("listing_status", "unpublished")
			.queryParam("listing_status", "published")
			//.queryParam("listing_status", "upcoming")
			.queryParam("unpublished_on_after", time1)
			.queryParam("unpublished_on_before", time2)
			.queryParam("published_on_after", time1)
			.queryParam("published_on_before", time2)
			.queryParam("page","1")
			.when().get("/api/leads/v2/").
			then().assertThat().statusCode(200).extract().asPrettyString();
			JsonPath js = new JsonPath(response);
			String retrivedSellLead= js.getString("results.id");
			String SellLeadcount = js.getString("count");	
			System.out.println("Sell lead retrived after applying filter from All-cars is: "+retrivedSellLead);
			System.out.println("Sell lead count for All-cars is: "+SellLeadcount);
	}
	

	
	
}
