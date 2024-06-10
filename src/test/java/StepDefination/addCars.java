package StepDefination;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
//import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

/*import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;*/

import Files.payload;

// add car to interest, get car from recommended, pin car, 
//remove car, reschedule visit, create visit

public class addCars
{ 
	public static String sell_lead_id; //"780427"; //assured= "46100"; //= "54,701"; //="39954";//
	public static int dealAmount = 380000; //1773702;   //Assured= 250000;//= 550000; //=375000;//     
	public static String retrivedSellLead;
	 public static String visit_id;
	 public static int carinterest_id;
	 //private static String time;
	
	@Given("Fetch recommended cars from preferences")
	
	public static void preferencesSet() 
	{
		RestAssured.baseURI= payload.BaseURL();
		given().header("content-type", "applicatiuon/json")
		.header("Keycloak-Authorization",createLead.accessToken)
		.header("client",DefiningEnv.client)
		.queryParam("city","delhi")
		.queryParam("min_price","50000")
		.queryParam("max_price","4100000")
		.queryParam("min_year","2009")
		.queryParam("max_year","2022")
		.queryParam("rto","dl,hr")
		//.queryParam("procurement_category", "assured")
		.queryParam("car_status", "available").
		
		when().
		get("api/inventory/model-lead-count/").
		
		then().statusCode(200).extract().asPrettyString();
		
		//System.out.println("Cars retrived from preferences");
	}
	@And("Search cars from inventory and save")
	public static void getCarsFromInventory() {
		RestAssured.baseURI= payload.BaseURL();
		String response = given().header("content-type", "application/json").header("Keycloak-Authorization",createLead.accessToken)
		.queryParam("page_size", "100").queryParam("procurement_category", createLead.leadCategory)
		.queryParam("group", "inside-sales-admin")
		.queryParam("car_status","available").queryParam("hub", "delhi-kirti-nagar-moments").
		when().get("/api/inventory/new/v3/").
		then().statusCode(200).extract().asPrettyString();;
		JsonPath js = new JsonPath(response);
		
		
		int min = 0;
		int max = 10;
		int x = (int)(Math.random()*(max-min+1)+min);
		
		
		sell_lead_id= js.getString("results["+x+"].id");
		System.out.println(+x+"th sell lead retrived from Inventory after applying filter is: "+sell_lead_id);
		try {

		float dealAmmount = js.getFloat("results["+x+"].listing_price");
		dealAmount = (int)dealAmmount;
		}
		finally {
			double dealAmmount = js.getDouble("results["+x+"].listing_price");
		}
		System.out.println("Deal Price "+dealAmount);
	}
	
	@And("get a car from recommended cars")
	
	public static void recommendedCars()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		 given().header("content-type", "application/json")
			.header("Keycloak-Authorization",createLead.accessToken).
			queryParam("buy_lead", createLead.leadId).

		when().
			get("sbm/api/recommended_cars/").
			
		then().
			assertThat().statusCode(200).extract().asPrettyString();
	}
		/*JsonPath js = new JsonPath(response);
		
		int min = 0;
		int max = 4;
		int x = (int)(Math.random()*(max-min+1)+min);
		
		//System.out.println("Value "+x+" random");
		
		try {
			retrivedSellLead = js.getString("data["+x+"].id");
		float dealAmmount = js.getFloat("data["+x+"].listing_price");
		
	
		// have to use int Deal Amount else sendpaymentlink throws 500
		dealAmount = (int)dealAmmount;
		
		//System.out.println("Deal amount of the car is "+dealAmount);
		}
		finally {
			retrivedSellLead = js.getString("data["+x+"].id");
			double dealAmmount = js.getDouble("data["+x+"].listing_price");
			
		
			// have to use int Deal Amount else sendpaymentlink throws 500
			dealAmount = (int)dealAmmount;
			
			//System.out.println("Deal amount of the car is "+dealAmount);
		}
		
		System.out.println("Car retrieved is "+retrivedSellLead); 
		//String[] carDetails = {carID,dealAmount}; 
		//return carDetails;
	}*/
	
	@When("cancel the deals on the sell lead")
	
	public static void cancelDeal()
	{
		RestAssured.baseURI= payload.BaseURL();
		given().
			header("content-type","application/json")
			.header("Keycloak-Authorization",createLead.accessToken).
			queryParam("sell_lead_id",sell_lead_id).
			body("{\r\n"
					+ "  \"reason\": \"Automation CRM Test Cancel\"\r\n"
					+ "}").
		when().
			put("api/carpurchases/"+sell_lead_id+"/cancel_sell_lead_existing_deals/").
		then().
			assertThat().statusCode(200);
			
		System.out.println("deal on car"+sell_lead_id+"has been cancelled");
		
	}
//
	@Then("Car added to interest")

	public static void addToInterest()
	{
		RestAssured.baseURI= payload.BaseURL();
		given().
			header("content-type","application/json")
			.header("Keycloak-Authorization",createLead.accessToken).header("client",DefiningEnv.client).
		body(payload.addingCarToInterest()).

		when().
			post("sbm/api/car_interests/bulk_create/").

		then().
			assertThat().statusCode(200);

		System.out.println("Car has been added to interest");
	}

/*	@Then("Car added to interest")

	public static void addToInterest() {
		String body = "[{\"buy_lead\":\"" + createLead.leadId + "\",\"lead_id\":" + sell_lead_id + ",\"context_type\":\"proxycontextmodel\",\"context_id\":\"sales-agent\",\"added_from\":\"recommended_cars\"}]";
		System.out.println(body);
		payload.post(body, "sbm/api/car_interests/bulk_create/").prettyPrint();

	}
	*/
	
	/*@Then("check if Car is Available")
	public static void carInterests()
	{
		RestAssured.baseURI= payload.BaseURL();
		String IntrestedCarResponse =
		
		given().
			header("content-type","application/json").header("Keycloak-Authorization",createLead.accessToken).header("client",DefiningEnv.client).
			queryParam("buy_lead", createLead.lead_id).
		when().	
			get("api/car_interests/").
		
		then().
			log().all().assertThat().statusCode(200).extract().asString();
					
		JsonPath js = new JsonPath(IntrestedCarResponse);
		String tagStatus = js.getString("[0].tag_status");
					
		System.out.println(tagStatus);
		if(tagStatus != "available")
		{
			addCars.recommendedCars();
		}
	}*/
	
	/*@Given("^Schedule visit for car$")
    public void scheduleVisitForCar() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        //System.out.println(dtf.format(now));
        time = dtf.format(now);
        String testDriveSlots = getTestDriveSlots(time);
        String body = "{\"at_home\":false,\"delivery_interested\":false,\"visit_address\":{\"block_society_name\":\"\",\"landmark\":\"\",\"house_flat_number\":\"\",\"pin_code\":\"\"},\"sell_lead\":" + sell_lead_id + ",\"visit_hub_distance\":null,\"context_type\":\"buylead\",\"visit_type\":\"test-drive\",\"location_type\":\"hub\"," +
                "\"context_id\":" + createLead.leadId + ",\"location_id\":2,\"scheduled_time\":\"" + testDriveSlots + "\"}";
        //System.out.println(body);
        JsonPath post = payload.post(body, "api/visits/schedule/");
        visit_id = String.valueOf(post.getInt("id"));
        //System.out.println(visit_id);
//        visit_id = Visit.visit_id;
    }*/

    @And("^Cancelled visit for car$")
    public void cancelledVisitForCar() {
        //System.out.println();
        String url = "api/visits/" + visit_id + "/mark_status/";
        String body = "{\"reason\":\"Car unfit for test drive\",\"sub_event\":\"pitch-for-rescheduling-now\",\"reason_id\":64," +
                "\"id\":" + visit_id + ",\"status\":\"hub-visit-hub-visit-visit-cancelled\",\"comments\":\"gdgf\"}";
        //System.out.println(body);
        payload.putData(url, body);
    }

    @And("^Pin added car$")
    public void pinAddedCar() {
        JsonPath response = payload.get("api/car_interests/?buy_lead=" + createLead.leadId + "");
        carinterest_id = response.getInt("[0].id");
        //System.out.println(carinterest_id);
        String body = "{\"id\":" + carinterest_id + ",\"pinned\":true}";
        //System.out.println(body);
        payload.post(body, "sbm/api/car_interests/" + carinterest_id + "/pin/");
    }

    @Then("^Remove car from car interest$")
    public void removeCarFromCarInterest() {
        String body = "{\"buy_lead_id\":" + createLead.leadId + ",\"sell_lead_id\":" + sell_lead_id + ",\"car_interest_id\":" + carinterest_id + "}";
        //System.out.println(body);
        payload.post(body, "sbm/api/car_interests/remove_interested_car/");
    }

    /*public static String getTestDriveSlots(String date) throws IllegalArgumentException {
        String slot;
        String response = given().header("content-type", "application/json")
                .header("Keycloak-Authorization", createLead.accessToken).
                queryParam("lead_id", addCars.sell_lead_id).queryParam("start_date", date).
                queryParam("is_home", false).queryParam("lat", "28.6691565").queryParam("lon", "77.45375779999999").
                queryParam("visit_type", "test-drive").
                when().get("api/visits/get_slots/").then().
                assertThat().statusCode(200).extract().asString();

        JsonPath js = new JsonPath(response);
        boolean slotAvailable = js.getBoolean("is_slot_available");

        if (slotAvailable) {
            int i;
            int numberofSlots = js.getInt("data[0].slots.size()");
            for (i = 0; i <= numberofSlots; i++) {
                //System.out.println(i);
                boolean available = js.getBoolean("data[0].slots[" + i + "].availability");
                if (available) break;
            }

            slot = js.getString("data[0].slots[" + i + "].start_time");
            //System.out.println("if" + slot);
        } else {
            //adding one more day in the date in quer param
            LocalDate nextDate = LocalDate.now();
            nextDate = nextDate.plusDays(1);
            time = nextDate.toString();
            //System.out.println("DATE HAS BEEN CHANGED");
            slot = getTestDriveSlots2(response);
           // System.out.println("else" + slot);
        }
        String slotTime = time + 'T' + slot;
        //System.out.println("return" + slotTime);
        return slotTime;
    }

    public static String getTestDriveSlots2(String res) {
        JsonPath js = new JsonPath(res);
        int i;
        int numberofSlots = js.getInt("data[0].slots.size()");
        for (i = 0; i <= numberofSlots; i++) {
           // System.out.println(i);
            boolean available = js.getBoolean("data[0].slots[" + i + "].availability");
            if (available) break;
        }
        String slot2 = js.getString("data[0].slots[" + i + "].start_time");
        //System.out.println("if" + slot2);
        return slot2;
    }*/
}