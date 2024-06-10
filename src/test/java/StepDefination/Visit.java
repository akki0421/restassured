package StepDefination;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import static io.restassured.RestAssured.given;


// Create Visit, Schedule TD, Hotness
// Loan Id, Loan finance, Loan eligibility, Approve Loan

public class Visit {
	
    public static String time;
//    public static Object scheduledTime;
    public static boolean HTD;
    public static String timeSlot;
//		public static String visit_id;

    @Given("Creating {string} type visit")
    public static void createVisits(String visitType) throws io.cucumber.java.PendingException {
        RestAssured.baseURI = payload.BaseURL();

        GetCurrentTimeStamp();
        //scheduledTime();
        System.out.println("time  is " + time);

        given().header("content-type", "application/json")
                .header("Keycloak-Authorization", createLead.accessToken).
                body(payload.visit(visitType, time)).
                when().
                post("api/visits/").
                then().log().all().
                assertThat().statusCode(200).extract().asPrettyString();
        //System.out.println("on " + createLead.leadId);

    }

    public static void GetCurrentTimeStamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
       // System.out.println(dtf.format(now));
        time = dtf.format(now);
//        return time;
    }

//    public static Object scheduledTime() {
//        System.out.println(java.time.Clock.systemUTC().instant());
//        scheduledTime = java.time.Clock.systemUTC().instant();
//        return scheduledTime;
//    }

    @When("Schedule {string} test drive")
    public static void scheduleTestDrive(String tdType) {
        GetCurrentTimeStamp();
        timeSlot = getTestDriveSlots(tdType, time);
        //if(td=htd){ add another car -> schedule td} else { schedule td}
       

        try {
                given().header("content-type", "application/json")
                .header("Keycloak-Authorization", createLead.accessToken).
                body(payload.testDrive()).
                when().
                post("api/visits/schedule/").
                then().
                assertThat().statusCode(200);
                
                System.out.println("TD created");
        }
        
        finally {
        	given().header("content-type", "application/json")
            .header("Keycloak-Authorization", createLead.accessToken).
            body(payload.testDrive()).
            when().
            post("api/visits/schedule/").
            then().
            assertThat().statusCode(400);
        	
        	System.out.println("sell lead sold");
        }
       
                
        //System.out.println("Scheduled TD on " + timeSlot);
//			JsonPath js = new JsonPath(response);
//			visit_id = String.valueOf(js.getInt("id"));
//			System.out.println(visit_id);

    }


    public static String getTestDriveSlots(String testDriveType, String date) throws java.lang.IllegalArgumentException {
        String slot;
        HTD = getTDtype(testDriveType);

        String response = given().header("content-type", "application/json")
                .header("Keycloak-Authorization", createLead.accessToken).
                queryParam("lead_id", addCars.sell_lead_id).queryParam("start_date", date).
                queryParam("is_home", HTD).queryParam("lat", "28.6691565").queryParam("lon", "77.45375779999999").
                queryParam("visit_type", "test-drive").
                when().
                get("api/visits/get_slots/").

                then().
                assertThat().statusCode(200).extract().asString();

        JsonPath js = new JsonPath(response);
        boolean slotAvailable = js.getBoolean("is_slot_available");

        if (slotAvailable == true) 
        {
            int i;
            int numberofSlots = js.getInt("data[0].slots.size()");
            for (i = 0; i <= numberofSlots; i++) 
            {
                //System.out.println(i);
                boolean slotAvailablity = js.getBoolean("data[0].slots[" + i + "].availability");
                
                if (slotAvailablity == true) 
                {
                    break;
                }
            }

            slot = js.getString("data[0].slots[" + i + "].start_time");
            //System.out.println("if" + slot);
        } 
        else 
        {
            //adding one more day in the date in query param
            LocalDate nextDate = LocalDate.now();
            nextDate = nextDate.plusDays(1);
            time = nextDate.toString();
            System.out.println(nextDate+ " next date "+time);
            
            slot = getTestDriveSlots2(time);
            //System.out.println("else" + slot);
            //System.out.println("Need another day");
        }
        
        String slotTime = time + 'T' + slot;
        //System.out.println("return" + slotTime);
        return slotTime;
    }

    public static String getTestDriveSlots2(String time) {

    	String res = given().header("content-type", "application/json")
                .header("Keycloak-Authorization", createLead.accessToken).
                queryParam("lead_id", addCars.sell_lead_id).queryParam("start_date", time).
                queryParam("is_home", HTD).queryParam("lat", "28.6691565").queryParam("lon", "77.45375779999999").
                queryParam("visit_type", "test-drive").
                when().
                get("api/visits/get_slots/").

                then().
                assertThat().statusCode(200).extract().asString();
    	
        JsonPath js = new JsonPath(res);
        //boolean slotAvailable = js.getBoolean("is_slot_available");

        int i;
        int numberofSlots = js.getInt("data[0].slots.size()");
        for (i = 0; i <= numberofSlots; i++) 
        {
        	//System.out.println(i);
            boolean slotAvailablity = js.getBoolean("data[0].slots[" + i + "].availability");
            
            if (slotAvailablity == true) 
            {
                break;
            }
        }

        String slot2 = js.getString("data[0].slots[" + i + "].start_time");
       // System.out.println("if" + slot2);

        return slot2;
    }

    public static boolean getTDtype(String TD) {
        boolean TdValue;
        TdValue = TD.equals("home");
        return TdValue;
    }

    
	@When("Visit hotness for Hub Admin")
    public static void visitHotness() {
    	RestAssured.baseURI = payload.BaseURL();
    	String response= given().header("content-type", "application/json")
         .header("Keycloak-Authorization", createLead.accessToken).
         queryParam("page_size", "40").
         queryParam("hub","delhi-kirti-nagar-moments").
         queryParam("scheduled_time_before",time).
         queryParam("scheduled_time_after", time).
         queryParam("visit_type", "test-drive").
         when().
         get("api/visits/for_work").
         then().
         assertThat().statusCode(200).extract().asPrettyString();
    	 JsonPath js = new JsonPath(response);
    	 int count = js.getInt("count");
    	 if (count>0) {
    		for (int i=0; i<count; i++) {
    			//JsonPath js1 = new JsonPath(response);
    			String visitHotness= js.getString("results["+i+"].visit_hotness_scale");
    			//System.out.println(visitHotness);
    			if(visitHotness == "High") {
					Collection<String> arr= new ArrayList<String>();
    				//System.out.println(arr.add(js1.getString("results["+i+"].id"))); 
					String id=js.getString("results["+i+"].id");
				 arr.add(id);
				System.out.println("Hign Priority visit: " +id);
				 }
    		}
    		System.out.println("No High priority visit at this moment");
    	 }else System.out.println("No testdrive available on " +time+ " for delhi kirti nagar moments Hub");
    }
	
	//LOAN
	
	 String lead_id = createLead.leadId;
	    String loan_id;

	    @Given("^Updating loan finance true$")
	    public void updatingLoanFinanceTrue() {
	        String url = "api/buy_leads/" + lead_id + "/update_finance/";
	        String body = "{\"finance\":true}";
	        payload.putLoan(url, body);
	    }

	    @And("^Turn loan eligibility yes$")
	    public void turnLoanEligibilityYes() {
	        String body = "{\"operationName\":\"updateBuyerDetails\",\"variables\":{\"buyer\":{\"creditLoanFeasibility\":true,\"creditLoanFeasibilityReason\":\"vvbnbn\"},\"buyerId\":" + DealCreate.buyerId + "},\"query\":\"mutation updateBuyerDetails($buyerId: Int, $buyer: BuyerInput) {\\n  updateBuyerDetails(buyerId: $buyerId, buyer: $buyer) {\\n    ok\\n    __typename\\n  }\\n}\\n\"}";
	        payload.postLoan(body, "api/loan_service/graphql");
	    }

	    @And("^Updating buyer information$")
	    public void updatingBuyerInformation() {
	        String body = "{\"operationName\":\"updateBuyer\",\"variables\":{\"buyerId\":" + DealCreate.buyerId + ",\"buyerDetailsInput\":{\"name\":\"" + "manisha" + "\",\"email\":\"m@gmail.com\",\"pinCode\":\"110003\",\"landmark\":\"asdf\",\"cityName\":\"South Delhi\",\"dateOfBirth\":\"1992-02-01T11:09:00.000Z\",\"addressLine1\":\"123\",\"contactNumber\":\"" + createLead.phoneNumber + "\",\"panCardNumber\":\"BAKPA0309A\",\"gender\":\"male\",\"employmentType\":\"Self-employed\",\"state\":\"Delhi\"}},\"query\":\"mutation updateBuyer($buyerId: Int!, $buyerDetailsInput: BuyerDetailsInput) {\\n  updateBuyer(buyerId: $buyerId, buyerDetailsInput: $buyerDetailsInput) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}";
	        payload.postLoan(body, "api/loan_service/dcf/graphql");
	    }

	    @When("^Created Loan id$")
	    public void createdLoanId() {
	        String body = "{\"operationName\":\"createLoanApplication\",\"variables\":{\"buyerId\":" + DealCreate.buyerId + "},\"query\":\"mutation createLoanApplication($buyerId: Int!) {\\n  createLoanApplication(buyerId: $buyerId) {\\n    ok\\n    loanApplication {\\n      id\\n      pk\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}";

	        JsonPath post = payload.postLoan(body, "api/loan_service/graphql");
	        loan_id = String.valueOf(post.getInt("data.createLoanApplication.loanApplication.pk"));

	        System.out.println("loan_id --- "+loan_id);
	    }

	    @Then("^Approved Loan$")
	    public void approvedLoan() {
	        RestAssured.baseURI = payload.BaseURL();
	        File testFile = new File(System.getProperty("user.dir") + "\\src\\test\\java\\Docs\\aadhar_front");
	        given().header("Keycloak-Authorization", createLead.accessToken)
	                .header("content-type", "multipart/form-data")
	                .header("client", "loan")
	                .multiPart("operations", "{\"operationName\":\"createBankLoanApp\",\"variables\":{\"bankLoanApplication\":{\"spinnyLoanApplicationId\":\""+loan_id+"\",\"status\":\"4\",\"loanBankId\":936,\"rateOfInterest\":10,\"tenureInMonths\":10,\"approvedAmount\":\"100000\",\"netDisbursedAmount\":\"99000\",\"dsaExists\":false,\"processingCharge\":null,\"loanSurakshaAmount\":null,\"gps\":\"1000\",\"valuationCharge\":null,\"stampDutyCharge\":null,\"pddCharge\":null,\"collateralCharge\":null,\"docsCharge\":null,\"otherCrossSellCharge\":null,\"cancellationReason\":\"\",\"approvalFile\":{\"file\":null,\"label\":\"Approval File\"}}},\"query\":\"mutation createBankLoanApp($bankLoanApplication: BankLoanApplicationInput) {\\n  createBankLoanApp(bankLoanApplication: $bankLoanApplication) {\\n    ok\\n    bankLoanApplication {\\n      updatedAt\\n      status {\\n        key\\n        value\\n        __typename\\n      }\\n      cancellationReason\\n      rateOfInterest\\n      dsa\\n      dsaExists\\n      tenureInMonths\\n      approvedAmount\\n      netDisbursedAmount\\n      processingCharge\\n      gps\\n      loanSurakshaAmount\\n      valuationCharge\\n      stampDutyCharge\\n      pddCharge\\n      collateralCharge\\n      docsCharge\\n      otherCrossSellCharge\\n      loanBank {\\n        name\\n        displayName\\n        pk\\n        __typename\\n      }\\n      comment\\n      pk\\n      deal {\\n        id\\n        pk\\n        __typename\\n      }\\n      fileUpload {\\n        id\\n        rawurl\\n        moburl\\n        taburl\\n        filename\\n        fileType\\n        uploadDate\\n        uploader {\\n          pk\\n          id\\n          fullName\\n          __typename\\n        }\\n        __typename\\n      }\\n      loginSource\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}")
	                .multiPart("map", "{\"1\":[\"variables.bankLoanApplication.approvalFile.file\"]}")
	                .multiPart("1", testFile)
	                .when()
	                .post("api/loan_service/graphql")
	                .then()
	                .assertThat().statusCode(200);

//	        JsonPath js = new JsonPath(response);

	    }
}

