package StepDefination;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import java.text.DecimalFormat;
import java.util.Random;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Files.payload;

//create deal, update status

public class createLead
{

	public static String accessToken;
	public static String phoneNumber;
	public static String leadId ;
	public static String unmaskedNumber;
	public static String LeadId_multiplesource;
	public static String response;
	public static String leadCategory;
	public static String transaction;
	
	RequestSpecification requestSpecification;
	@Given("Login to the application")
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
	

	@And("Mobile Number Generated")
	public static String generatePhoneNumber()
    {
		Random rand = new Random();
		int num1 = (rand.nextInt(3) + 6) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
		int num2 = rand.nextInt(743);
		int num3 = rand.nextInt(10000);

		DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
		DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

		phoneNumber = df3.format(num1)+ df3.format(num2) +df4.format(num3);
		return phoneNumber;
    }
	public static String generatetransaction()
    {
		Random rand = new Random();
		int num1 = (rand.nextInt(3) + 6) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
		int num2 = rand.nextInt(743);
		int num3 = rand.nextInt(10000);

		DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
		DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

		transaction = df3.format(num1)+ df3.format(num2) +df4.format(num3);
		return transaction;
    }
	
	@When("Lead Creation for {string} is Initiated")
	public static void leadCreation(String category) throws io.cucumber.java.PendingException
	{
		System.out.println(DefiningEnv.client);
		RestAssured.baseURI= payload.BaseURL();
		String response = given().header("content-type", "application/json")
				.header("Keycloak-Authorization",accessToken)
				.header("client",DefiningEnv.client)
			.body(payload.BuyLeadCreate(phoneNumber, category)).
		
		when()
			.post("sbm/api/buy_leads/").
		
		then()
			.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		leadId = js.getString("id");
		
		leadCategory = category;
		
		//System.out.println("Executed successfully");
		System.out.println("Buy Lead Id is :"+leadId);
	}
	
	@When("Lead Created Successfully")
	public void Lead_creat_succesfully()
	{
		System.out.println("Lead Created");
	}
	@Then("Contact number unmasked")
	public  static  void getUnmaskedContactNumber()
	{
		RestAssured.baseURI= payload.BaseURL();
		String response = given().header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client",DefiningEnv.client).
		when().
			get("sbm/api/buy_leads/unmask_contact_number/?buy_lead=" + createLead.leadId + "&contact_number_type=contact_number&event_type=Lead-Info-Strip").
		then().
			assertThat().statusCode(200).extract().asPrettyString();
		JsonPath js = new JsonPath(response);
		unmaskedNumber = js.getString("contact_number");
		System.out.println("Unmasked Contact number is = "+unmaskedNumber);

	}
	
	
	 @Given("Applicable Status")
	    public void applicableStatus() {
	        payload.get("api/buy_leads/" + createLead.leadId + "/applicable_status/");
	    }

	    @And("^Update status of lead$")
	    public void updateStatusOfLead() {
	        String url = "sbm/api/buy_leads/" + createLead.leadId + "/update_status/";
	        //System.out.println(url);
	        String body = "{\n" +
	                "    \"next_task_time\": \""+payload.getDateTime()+"\",\n" +
	                "    \"status\": {\n" +
	                "        \"description\": \"Token Done\",\n" +
	                "        \"name\": \"buy-request-workflow-buy-request-workflow-token-done\"\n" +
	                "    },\n" +
	                "    \"comment\": \"test\",\n" +
	                "    \"is_priority_max\": false,\n" +
	                "    \"set_reminder\": false\n" +
	                "}";
	        payload.putData(url, body);
	    }
	@Then("NDOA")
	public void ndoa() {
		JsonPath output = payload.get("api/buy_leads/"+createLead.leadId+"/");
		String NDOA = output.getString("next_date_of_action");
		System.out.println("NDOA = "+  NDOA);
	}
	    
	    @When("Lead Creation for {string}")
		public static void leadCreationmultiplesource(String source) throws io.cucumber.java.PendingException
		{
			String phoneNumber = createLead.generatePhoneNumber();
			 createLead.enviroment_should_be_up_and_running();
			RestAssured.baseURI= payload.BaseURL();
			String response = given().header("content-type", "application/json")
					.header("Keycloak-Authorization",accessToken)
				.body(payload.BuyLeadCreateMultipleSource(phoneNumber, leadCategory,source)).
			
			when()
				.post("sbm/api/buy_leads/").
			
			then()
				.assertThat().statusCode(201).extract().response().asString();
			
			//JsonPath js = new JsonPath(response);
			//leadId = js.getString("id");
			JsonPath js = new JsonPath(response);
			LeadId_multiplesource = js.getString("id");
			System.out.println("id ="+LeadId_multiplesource);
			
			
			//leadCategory = category;
			
			//System.out.println("Executed successfully");
			//System.out.println(privateleadId);
		}
		
		@And("Validating Drop Lead Functionality")
		   public void dropLead() {
		      String url = "sbm/api/buy_leads/" + LeadId_multiplesource + "/update_status/";
		     // System.out.println(url);
		      String body ="{\"status\":{\"description\":\"Customer unavailable | Cx not answering calls after few conversations\",\"name\":\"buy-request-workflow-buy-request-workflow-customer-unavailable-cx-not-answering-calls-after-few-conversations\"},\"comment\":\"\"}";
		      payload.putData(url, body);
		   System.out.println("Lead is dropped");
		   }

}
