package StepDefination;

import static io.restassured.RestAssured.given;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Leadlistingpagefiltes {
	
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
	
	@Given("Create access token For Lead Listing")
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
	
	
 @And("Fetch sell-lead id Lead listing page")
	
	public static void Lead_listing_page() 
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		RestAssured.baseURI= payload.BaseURL();
		
		String response = given().header("content-type","application/json")
				
				.header("Keycloak-Authorization",accessToken)
				//.queryParam("customer_name","madhuri")
				//.queryParam("customer_number","9966987666")
				.queryParam("city","delhi-ncr")
				.queryParam("category","assured")
				.queryParam("priority", "p0" )
				//.queryParam("lead_quality", "Q1")
				.queryParam("derived_source","webarticle")
				//.queryParam("sub_source","whatsapp_open_text" )
				//.queryParam("status", "buy-request-workflow-buy-request-workflow-user-activity-recieved")
			     .queryParam("hotness_scale","1")
				.queryParam("buy_lead_type", "General")
				.queryParam("assigned_to", "913345")
				.queryParam("added_on_after", time1 )
				.queryParam("added_on_before", time2)
				.queryParam("next_date_of_action_after", time1 )
				.queryParam("next_date_of_action_before", time2)
				.queryParam("last_date_of_action_after", time1 )
				.queryParam("last_date_of_action_before", time2)
				//.queryParam("visit_date_after", time1 )
				//.queryParam("visit_date_before", time2)
				//.queryParam("sell_lead_id", "69785")
				.queryParam("has_deal", "false")
				.queryParam("customer_events", "customer-requested-visit")
				//.queryParam("exchange_possibility", "true")
				//.queryParam("exchange_status", "buy-request-workflow-buy-request-workflow-exchange-in-progress")
				//.queryParam("watcher_id", "913345")
		     	.queryParam("lead_qualified_type","SQL")
			     .when().get("/sbm/api/buy_leads/v2/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.buy_lead_id");
				String SellLeadcount = js.getString("count");	
				System.out.println("Sell lead retrived after applying filter from Lead Listing Page: "+retrivedSellLead);
				System.out.println("Sell lead count for Lead Listing Page: "+SellLeadcount);
				//System.out.println(response);
	}
 
 
 @And("Fetch sell-lead id for Lead Assignment page")
	
	public static void  Lead_Assignment_page() 
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		RestAssured.baseURI= payload.BaseURL();
		
		String response = given().header("content-type","application/json")
				
				.header("Keycloak-Authorization",accessToken)
				//.queryParam("customer_name","madhuri")
				//.queryParam("customer_number","9966987666")
				.queryParam("city","delhi-ncr")
				.queryParam("category","assured")
				//.queryParam("buy_lead_id","333643")
			    .queryParam("created_at_after", time1 )
				.queryParam("created_at_before", time2)
				.queryParam("updated_at_after", time1 )
				.queryParam("updated_at_before", time2)
				.queryParam("last_date_of_action_after", time1 )
		        .queryParam("curr_lead_quality_type", "MQL")
				.queryParam("prev_lead_quality_type", "IN_QUEUE")
			
			     .when().get("/sbm/api/buy_leads/buy_lead_assignment_meta_data/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedSellLead= js.getString("results.buy_lead_id");
				String SellLeadcount = js.getString("count");	
				System.out.println("Sell lead retrived after applying filter from Lead Assignment page: "+retrivedSellLead);
				System.out.println("Sell lead count for Lead Assignment page: "+SellLeadcount);
				//System.out.println(response);
	}
 @And("Fetch sell-lead id Deal listing page")
	
	public static void deal_listing_page() 
	{
		TimeBeforeMonth();
		GetCurrentTimeStamp(); 
		
		RestAssured.baseURI= payload.BaseURL();
		
		String response = given().header("content-type","application/json")
				
				.header("Keycloak-Authorization",accessToken)
				.queryParam("customer_name","Test")
				//.queryParam("customer_number","9284332942")
				.queryParam("city","delhi-ncr")
				.queryParam("category","assured")
				.queryParam("loan_facilitation", "false" )
				.queryParam("deal_type", "GENERIC")
				//.queryParam("deal_type","EXCHANGE")
				//.queryParam("registration_number","KA03ADF5346" )
				.queryParam("status", "deal-flow-deal-flow-token-collected")
				//.queryParam("status", "deal-flow-deal-flow-token-collected")
			    // .queryParam("id","28147")
			//	.queryParam("buy_lead", "336691")
				.queryParam("buy_lead_assigned_to", "913345")
				//.queryParam("added_on_after", time1 )
				//.queryParam("added_on_before", time2)
			//	.queryParam("next_date_of_action_after", time1 )
				//.queryParam("next_date_of_action_before", time2)
				//.queryParam("last_date_of_action_after", time1 )
				//.queryParam("last_date_of_action_before", time2)
				//.queryParam("visit_date_after", time1 )
				//.queryParam("visit_date_before", time2)
		         //  .queryParam("sell_lead", "779399")
				.queryParam("payment_status", "TOKEN_DONE")
				//.queryParam("exchange_status", "buy-request-workflow-buy-request-workflow-exchange-in-progress")
				//.queryParam("watcher_id", "913345")
		     	//.queryParam("lead_qualified_type","SQL")
			     .when().get("sda/api/deals/").
				then().assertThat().statusCode(200).extract().asPrettyString();
				JsonPath js = new JsonPath(response);
				String retrivedDeal= js.getString("results[0].deal_id");
				String dealCount = js.getString("count");	
				System.out.println("Deal retrived after applying filter from Lead Listing Page: "+retrivedDeal);
				System.out.println("Deal count for Lead Listing Page: "+dealCount);
				//System.out.println(response);
	}

}