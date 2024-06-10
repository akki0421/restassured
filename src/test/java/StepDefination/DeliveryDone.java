package StepDefination;
import static io.restassured.RestAssured.given;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.Base64;
import java.util.Base64;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


//delivery visit, delivery done, cancel deal

public class DeliveryDone {
	
		
		public static Response requestBody;
		public static String name;
		public static String hubVisitBase;
		public static String sellLeadIdBase;
		public static String dealIdBase;
		public static String leadIdBase;
		public static String hubaccessToken;
		public static String HubDeliveryVisitId;
		public static String time;
		//public static String fnfmileageValue = "33";
		public static String redashMilleage;
		public static String username;
		public static String password;
		
		
		@Given("To convert buylead Id into base")
		public static String buyleadIdBase64(){
			//System.out.println(deliveryleadId);
			//String buyleadBase64Encoded = Base64.getEncoder().encodeToString(createLead.leadId.getBytes());
			//System.out.println("QnV5TGVhZE5vZGU6"+buyleadBase64Encoded);
			//leadIdBase= "QnV5TGVhZE5vZGU6"+buyleadBase64Encoded;
			//return leadIdBase;
			
			String lead= "BuyLeadNode:"+createLead.leadId;
			leadIdBase=Base64.getEncoder().encodeToString(lead.getBytes());
			//System.out.println(lead);
			//System.out.println(leadIdBase);
			return leadIdBase;
		}
		
		@And("To convert dealId into base")
		public static String dealIdBase64(){
			
			String deal= "CarPurchaseNode:"+DealCreate.dealId;
			//System.out.println(DealCreate.dealId);
			dealIdBase = Base64.getEncoder().encodeToString(deal.getBytes());
			//System.out.println(deal);
			//System.out.println(dealIdBase);
			return dealIdBase;
		}
		
		
		@And("Login to the Hub App")
		 public static String Tokengeneration() {
			getHubInspector();
			  String tokenRes = given().header("Content-Type", "application/x-www-form-urlencoded").header("Keycloak-Authorization","accessToken")
				      
				        .formParam("username",username)
				        .formParam("password",password)
				        .formParam("grant_type", "password").when().post("https://hu-dv-api.spinny.com/api/login").then().statusCode(200).extract().response().asString();
				
			  JsonPath js = new JsonPath(tokenRes);
				String CreatedToken = js.getString("message.access_token");	
			  hubaccessToken = "Bearer " + CreatedToken;
//			 System.out.println(hubaccessToken);
			 
					return hubaccessToken;
		} 
		
		public static void getHubInspector()
		{
			if(createLead.leadCategory.equals("assured"))
			{
				 username = "sb";
				 password = "zz";
			}
			else if("luxury".equals(createLead.leadCategory))
			{
				 username = "Yash.kumar";
				 password = "A@111111";
			}
			else {
				 username = "Harish.singh";
				 password = "A@111111";
			}
		}
		
		
		@When("Visit Created is initiated")
		  
		   public static String Visitcreate()
			
		  {
			 GetCurrentTimeStamp();
			 RestAssured.baseURI = "https://hu-dv-api.spinny.com/api/hub/graphql";
			  String query = "{\"operationName\":\"addNewHUbVisitMutation\",\"variables\":{\"assignmentId\":\"VXNlck5vZGU6MTA2NjAxOQ==\",\"hubId\":\"SHViTm9kZToy\",\"buyLeadInput\":{\"id\":\""+leadIdBase+"\",\"mobileNumber\":\""+createLead.phoneNumber+"\",\"fullName\":\"Test Consumer\",\"email\":\"\",\"gender\":\"male\",\"addressLine1\":\"Delhi\",\"address\":\"Delhi, India\",\"lat\":28.7040592,\"lng\":77.10249019999999,\"cityName\":\"Delhi\",\"filters\":{\"fuelType\":[],\"model\":[],\"make\":[],\"bodyType\":[],\"transmission\":[],\"rto\":[],\"color\":[],\"noOfOwners\":[],\"procurementCategory\":[]},\"isSpinnyAppDownloaded\":false,\"knowsAnyExCustomer\":false,\"isCustomerBuyingFirstCar\":false},\"visitInput\":{\"scheduledTime\":\""+time+"\",\"visitType\":{\"name\":\"delivery\"},\"numPeopleAccomp\":0},\"atHome\":false,\"startVisit\":true,\"dealId\":\""+dealIdBase+"\"},\"query\":\"mutation addNewHUbVisitMutation($hubId: ID!, $buyLeadInput: BuyLeadInput!, $visitInput: VisitInput!, $assignmentId: ID, $startVisit: Boolean, $atHome: Boolean, $expectedBudget: Int, $mileage: Int, $noOfOwners: Int, $dealId: ID) {\\n  addNewVisitMutationV2(assignmentId: $assignmentId, buyLeadInput: $buyLeadInput, hubId: $hubId, visitInput: $visitInput, startVisit: $startVisit, atHome: $atHome, expectedBudget: $expectedBudget, mileage: $mileage, noOfOwners: $noOfOwners, dealId: $dealId) {\\n    ok\\n    visitStarted\\n    message\\n    visit {\\n      id\\n      numPeopleAccomp\\n      pk\\n      visitStatus\\n      rmStartTime\\n      rmAssignedTime\\n      assignedTo {\\n        id\\n        username\\n        __typename\\n      }\\n      buyLead {\\n        id\\n        pk\\n        __typename\\n      }\\n      visitcall {\\n        startTime\\n        endTime\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}";
				String response = given().header("content-type", "application/json").header("Keycloak-Authorization", hubaccessToken)
					.body(query).
		 when()
		 .post("https://hu-dv-api.spinny.com/api/hub/graphql").
				then().
				assertThat().statusCode(200).extract().response().asString();
				
		        JsonPath js2 = new JsonPath(response);
				
		        HubDeliveryVisitId = js2.getString("data.addNewVisitMutationV2.visit.pk");
				//System.out.println(HubDeliveryVisitId);
				return HubDeliveryVisitId;
			  
			}
		@Then ("Visit Created Successfully")
		public static void Visitcreated()

		{ 
			System.out.println("Visitid has been created successfully");
		}
		
		public static String GetCurrentTimeStamp() {
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	        LocalDateTime now = LocalDateTime.now();
	        System.out.println(dtf.format(now));
	        time = dtf.format(now);
	        return time;
	    }
		

		@Given("Generating the OTP")
		
		public static String DatabaseConnection() throws ClassNotFoundException, SQLException {
			

		//System.out.println(HubDeliveryVisitId);	
		String url = "jdbc:mysql://sp-spinnyweb-dev-aurora-db-ap-south-1b.crevgiuvg8xk.ap-south-1.rds.amazonaws.com:3306/spinny?useSSL=false&serverTimezone=UTC";
		String uname = "akshayanand";
		String pwd ="CX1s2ybSF_F725gA";
		String query = "select * from otp_otp where content_id = '"+HubDeliveryVisitId+"'";


			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection(url,uname,pwd);
			
			Statement st = con.createStatement();

			ResultSet rs= st.executeQuery(query);

			rs.next();
			rs.getString(1);

			 name = rs.getString("otp_value");

			//System.out.println(name);

			st.close();
			con.close();

			return name;
			} 
		
		@And("To convert hubvisitId into base")
		public static String hubVisitIdBase64(){

			String hubVisitId= "VisitNode:"+HubDeliveryVisitId;
			//System.out.println(HubDeliveryVisitId);

			hubVisitBase = Base64.getEncoder().encodeToString(hubVisitId.getBytes());
			//System.out.println(hubVisitBase);
			return hubVisitBase;
		}
		public static String sellLeadBase64() {
			String sellLeadId= "LeadNode:"+addCars.sell_lead_id;
			//System.out.println(sellLeadId);

			sellLeadIdBase = Base64.getEncoder().encodeToString(sellLeadId.getBytes());
			//System.out.println(sellLeadIdBase);
			return sellLeadIdBase;
		}
	
		
		@And ("Get car mileage")
		/*public static void getCarMileage()
		{
			RestAssured.baseURI = "https://sales-backend-test.spinnyworks.in/loan/";
			String response = given().header("content-type","application/json").header("Keycloak-Authorization",createLead.accessToken).
			header("client",DefiningEnv.client).
			body(payload.getMileage()).
			when().
			post("/api/hub/graphql").then().log().all().
			assertThat().log().all().statusCode(200).extract().asString();
			
			JsonPath j1 = new JsonPath(response);
			
			fnfmileageValue = j1.getInt("data.leadDetails.edges[0].node.serviceHistoryChecks[0].mileage");
			
		}*/
		
		public static void setCarMileageValue() {
			sellLeadBase64();
		RestAssured.baseURI = "https://hu-dv-api.spinny.com" ;
		String Response = given().
		header("content-type","application/json").header("Keycloak-Authorization",hubaccessToken).header("client","hub")
		.body("{\r\n"
				+ "    \"carPurchaseId\": \""+dealIdBase+"\",\r\n"
				+ "    \"visitId\": \""+hubVisitBase+"\",\r\n"
				+ "    \"leadId\": \""+sellLeadIdBase+"=\",\r\n"
				+ "    \"deliveryValidationStages\": {\r\n"
				+ "        \"saveQuestionnaireFeedback\": {\r\n"
				+ "            \"questionnaireName\": \"hub-questionnaire-mark-delivery\",\r\n"
				+ "            \"feedbacks\": [\r\n"
				+ "                {\r\n"
				+ "                    \"questionId\": \"UXVlc3Rpb25Ob2RlOjY=\",\r\n"
				+ "                    \"answerTitle\": \"Yes\"\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"questionId\": \"UXVlc3Rpb25Ob2RlOjc=\",\r\n"
				+ "                    \"answerTitle\": \"Yes\"\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        },\r\n"
				+ "        \"saveDeliveryChecklist\": {\r\n"
				+ "            \"checklistItems\": [\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"RC copy given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"PUC copy given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Form 30 signed by the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Provided second key to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Provided toolkit, reflector, stepney to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Photocopy of Form 29 and 30 given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        },\r\n"
				+ "        \"mileageUpdateWhileDelivery\": {\r\n"
				+ "            \"newMileage\": 33\r\n"
				+ "        },\r\n"
				+ "        \"saveEmissionCopy\": {\r\n"
				+ "            \"fileUploadId\": \"RmlsZVVwbG9hZE5vZGU6QzZ5dzVlcWNSMnFKc29uM1NrQ2llQQ==\"\r\n"
				+ "        },\r\n"
				+ "        \"addDeliveryImage\": {\r\n"
				+ "            \"fileUploadId\": \"RmlsZVVwbG9hZE5vZGU6eTZ2ODMwVHBSZHFibnliRjdtTUZfdw==\"\r\n"
				+ "        },\r\n"
				+ "        \"verifyDeliveryOtp\": {\r\n"
				+ "            \"otpValue\": \""+name+"\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}").when().	
		post("/api/hub/delivery/validate_delivery/").

		then().
			assertThat().log().all().statusCode(200).extract().asString();
	/*	JsonPath js1 = new JsonPath(Response);
		
		 Response = js1.getString("data.deliveryValidationStages.mileageUpdateWhileDelivery.message");
		// System.out.println( Response);
		
		 String []mil=Response.split(" ");
		 for(int i=0; i< mil.length; i++) {
			  System.out.println(mil[15]);
		 }
		 fnfmileageValue= mil[15];*/

		}
		public static String carMileage() throws ClassNotFoundException, SQLException {
			String url = "jdbc:mysql://sp-spinnyweb-dev-aurora-db-ap-south-1b.crevgiuvg8xk.ap-south-1.rds.amazonaws.com:3306/spinny?useSSL=false&serverTimezone=UTC";
			String uname = "akshayanand";
			String pwd ="CX1s2ybSF_F725gA";
			String query = "select listing_leadprofile.mileage ,listing_leadprofile.id from listing_lead inner join listing_leadprofile on listing_lead.profile_id= listing_leadprofile.id where listing_lead.id='"+addCars.sell_lead_id+"'";


				Class.forName("com.mysql.cj.jdbc.Driver");

				Connection con = DriverManager.getConnection(url,uname,pwd);
				
				Statement st = con.createStatement();

				ResultSet rs= st.executeQuery(query);

				rs.next();
				rs.getString(1);

				 redashMilleage = rs.getString("mileage");

				System.out.println(redashMilleage);

				st.close();
				con.close();

				return redashMilleage;
			
		}
		
		@And("Validating the Delivery")

		public static void Settingdeliveryquestionnaire() throws ClassNotFoundException, SQLException
		
		{ 
		//GetDeliveryStatus();
			carMileage();
		
		RestAssured.baseURI = "https://hu-dv-api.spinny.com" ;
		
		given().
		header("content-type","application/json").header("Keycloak-Authorization",hubaccessToken).header("client","hub")
		.body("{\r\n"
				+ "    \"carPurchaseId\": \""+dealIdBase+"\",\r\n"
				+ "    \"visitId\": \""+hubVisitBase+"\",\r\n"
				+ "    \"leadId\": \""+sellLeadIdBase+"\",\r\n"
				+ "    \"deliveryValidationStages\": {\r\n"
				+ "        \"saveQuestionnaireFeedback\": {\r\n"
				+ "            \"questionnaireName\": \"hub-questionnaire-mark-delivery\",\r\n"
				+ "            \"feedbacks\": [\r\n"
				+ "                {\r\n"
				+ "                    \"questionId\": \"UXVlc3Rpb25Ob2RlOjY=\",\r\n"
				+ "                    \"answerTitle\": \"Yes\"\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"questionId\": \"UXVlc3Rpb25Ob2RlOjc=\",\r\n"
				+ "                    \"answerTitle\": \"Yes\"\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        },\r\n"
				+ "        \"saveDeliveryChecklist\": {\r\n"
				+ "            \"checklistItems\": [\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"RC copy given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"PUC copy given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Form 30 signed by the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Provided second key to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Provided toolkit, reflector, stepney to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"checklistItem\": \"Photocopy of Form 29 and 30 given to the customer\",\r\n"
				+ "                    \"boolValue\": true\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        },\r\n"
				+ "        \"mileageUpdateWhileDelivery\": {\r\n"
				+ "            \"newMileage\": "+redashMilleage+"\r\n"
				+ "        },\r\n"
				+ "        \"saveEmissionCopy\": {\r\n"
				+ "            \"fileUploadId\": \"RmlsZVVwbG9hZE5vZGU6QzZ5dzVlcWNSMnFKc29uM1NrQ2llQQ==\"\r\n"
				+ "        },\r\n"
				+ "        \"addDeliveryImage\": {\r\n"
				+ "            \"fileUploadId\": \"RmlsZVVwbG9hZE5vZGU6eTZ2ODMwVHBSZHFibnliRjdtTUZfdw==\"\r\n"
				+ "        },\r\n"
				+ "        \"verifyDeliveryOtp\": {\r\n"
				+ "            \"otpValue\": \""+name+"\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}")
		.when().	
	post("/api/hub/delivery/validate_delivery/").

	then().
	assertThat().statusCode(200);


	}
/*
	public static void GetDeliveryStatus() {
			 
			 RestAssured.baseURI = "https://hu-dv-api.spinny.com" ;
		     
			
			String Response = given().

				header("content-type","application/json").header("Keycloak-Authorization",hubaccessToken).header("client","hub").
				queryParam("dealId",dealIdBase).queryParam("visitId", hubVisitBase).queryParam("leadId",leadIdBase)

				.when().	
				get("/api/hub/delivery/delivery_validation_status/").
			
			then().
				assertThat().statusCode(200).extract().response().asPrettyString();
			
			
			JsonPath js2 = new JsonPath(Response);
			
			 Response = js2.getString("x.data.deliveryValidationStages.mileageUpdateWhileDelivery.message");
			 
			
			 System.out.println( Response);
			
			
	}*/
	@When("Final Hub Delivery")
	public static void finalhubDelivery()
	{
		RestAssured.baseURI = "https://hu-dv-api.spinny.com/" ;
		given().
		log().all().header("content-type", "application/json").
		header("Keycloak-Authorization", DeliveryDone.hubaccessToken)
		.body("{\"deal_id\":"+DealCreate.dealId+",\"deal_status\":\"DELIVERED\"}").
		when().post("api/hub/carpurchase/update_deal/").
		then().log().all().
		assertThat().statusCode(200);	
	}
	


	@Then ("Hub Delivery is done Successfully")

	public static void visitcreation()

	{ 
		System.out.println("Hub Delivery is done Successfully");
	}
	
	@Given("Raise cancellation request")
	public static void raiseCancellationRequest()
	{
		RestAssured.baseURI= payload.BaseURL();
		 given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body(payload.CancellationRequest())
		.when().
		post("api/carpurchases/request_deal_cancellation/").
		then().
		assertThat().statusCode(200);
		
		//JsonPath js = new JsonPath(response);
		//String status = js.getString("true");
		//System.out.println("Deal Cancellation request is submitted");
		}
	
	 @When("Rejecting cancellation request")
	 public static void rejectingCancellation() 
	 {
		given().header("Keycloak-Authorization",createLead.accessToken)
		 .header("content-type","application/json")
		 .header("client",DefiningEnv.client)
		 .body(payload.RejectCancellationRequest())
		 .when().
		 post("api/carpurchases/"+DealCreate.dealId+"/reject_deal_cancellation_request/")
		 .then()
		 .assertThat().statusCode(200);
		 
		// JsonPath js = new JsonPath(response);
			//String status = js.getString("success");
			//System.out.println("Deal Cancellation Request cancelled successfully");
	 }
		 
		
	 @Then("Approving cancellation request")
	 public static void approvingCancellation()
	 {
		 raiseCancellationRequest();
		given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body(payload.ApprovingCancellationRequest())
		.when().
		post("api/carpurchases/"+DealCreate.dealId+"/update_status/")
		.then()
		.assertThat().statusCode(200);
		 
		//JsonPath js = new JsonPath(response);
		//String status = js.getString("updated");
		System.out.println("Deal cancelled successfully");
	 }
	 
	 @Given("Raise return request")
		public static void raiseReturnRequest()
		{
			RestAssured.baseURI= payload.BaseURL();
			 given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.body(payload.ReturnRequest())
			.when().
			post("api/carpurchases/request_car_return/").
			then().
			assertThat().statusCode(200);
			
			//JsonPath js = new JsonPath(response);
			//String status = js.getString("true");
			//System.out.println("Deal Cancellation request is submitted");
			} 
			
		 @Then("Approving return request")
		 public static void approvingReturn()
		 {
			given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.body(payload.ApprovingReturnRequest())
			.when().
			post("api/carpurchases/"+DealCreate.dealId+"/update_status/")
			.then().log().all().
			assertThat().statusCode(200);
			 
			//JsonPath js = new JsonPath(response);
			//String status = js.getString("updated");
			System.out.println("Car Returned successfully");
		 }
}
