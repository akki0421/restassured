package StepDefination;
import static io.restassured.RestAssured.given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

// requesting deal, adding buyer, adding address of the buyer, making token payment
// making partial and full payment, approving pay

public class DealCreate
{
	public static String buyerId;
	public static String name;
	public static String buyerEmail = "j@gmail.com";
	public static String dealId ;
	public static String paymentId;
	public static int tokenAmount;
	
	public static int receivablePkValue;
	public static float balanceAmount;
	public static double feePercentage;
	
	@Given("Adding a Buyer for the deal")
	public static void callingBuyerMethod()
	{
		addBuyer(createLead.phoneNumber);
	}
	
	public static void addBuyer(String phoneNumber)
	{	
		RestAssured.baseURI= payload.BaseURL();
		given().
			header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
		body(payload.addingBuyer(phoneNumber)).
		when().
			post("sda/api/carpurchases/v2/buyer/bulk_update/").
		then().
			assertThat().statusCode(200);
		
		System.out.println("Buyer added successfully");
	}
	
	@When("Getting Buyer Details")
	public static String getBuyer() 
	{
		RestAssured.baseURI= payload.BaseURL();
		String GetBuyerResponse = given().
			header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.queryParam("buy_lead",createLead.leadId).
		when()
			.get("sda/api/carpurchases/v2/buyer").
		then().
			assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = new JsonPath(GetBuyerResponse);
	
		buyerId = js2.getString("id[0]");
		/*String BracketbuyerId = js2.getString("id");
		String removeBracket = BracketbuyerId.replace("[","");
		buyerId = removeBracket.replace("]","");*/
		
		System.out.println("Buyer Id is: "+buyerId);
		return buyerId;
	}
	
	@And("Adding Address of the Buyer")
	public static void addAddress()  
	{
		RestAssured.baseURI= payload.BaseURL();
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client).
		body(payload.addBuyerAddress()).
		when().
			put("api/carpurchases/v2/buyer/"+buyerId+"/").
		then().
			assertThat().statusCode(200);
		
		//System.out.println("Buyer address has been added successfully");
	}
	
	@Then("Requesting Deal")
	public static String[] createDeal() throws InterruptedException
	{
		tokenAmount = getTokenAmount();
		
		RestAssured.baseURI= payload.BaseURL();
		
		String createDealResponse = given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			//.header("client","consumer-website")
			.body(payload.requestDeal()).
		when().
			post("api/carpurchases/v2/send_payment_link/").
		then().
			assertThat().statusCode(201).extract().asPrettyString();
		
		JsonPath js2 = new JsonPath(createDealResponse);
		dealId = js2.getString("deal.id");
		
		//System.out.println(createDealResponse);
		Thread.sleep(5000);
		paymentId = paymentsApi();
		
		String[] Id = {dealId,paymentId};
		
		System.out.println("Deal ID is:"+dealId);
			
		return Id;
		
	}
	
	public static int getTokenAmount()
	{
		int token;
		
		if(createLead.leadCategory.equals("assured"))
		{
			token = 20000;
		}
		else if("luxury".equals(createLead.leadCategory))
		{
			token = 50000;
		}
		else
			token = 10000;
		
		return token;
	}

	public static String paymentsApi()
	{	
		RestAssured.baseURI= "http://sp-payments-rest.dev.internal.spinny/";
		
		String response = given()
			.header("client",DefiningEnv.client).
			queryParam("deal_id",dealId).
		when()
			.get("payments_old/orders/get_order_details_from_deal_id").
		then()
			.assertThat().statusCode(200).extract().asString();
		
		JsonPath js2 = new JsonPath(response);
		
		String paymentId = js2.getString("id");
		System.out.println(paymentId);
		return paymentId;
	}
	
	@Given("Making token Payment")
	public static void TokenPayment()
	{	
		RestAssured.baseURI= payload.BaseURL();
		
		given()
			.header("content-type", "application/json")
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("client",DefiningEnv.client).
			body(payload.TokenPayment(DealCreate.tokenAmount)).
		when()
			.post("api/payments/orders/"+paymentId+"/add_payment/").
		then()
			.assertThat().statusCode(200).extract().asString();
		
		System.out.println("Token Payment Done");
		
	}
	
	@And("Making Partial Payment with payment type {string}")
	public static void PartialPayment(String paymentType)
	{
		
		double fee = getPaymentFee(paymentType);
		RestAssured.baseURI= payload.BaseURL();
		
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.body(payload.PartialPayment(paymentType,fee))
		.when()
			.post("api/payments/orders/"+paymentId+"/add_payment/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Partial payment has been done");
	}
	
	@And("Open deal in payment")
	public static void EditDealinPayment() throws InterruptedException
	{
		Thread.sleep(5000);
		RestAssured.baseURI= "https://payments-api.dev.ispinnyworks.in/";
	
		String response5 = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.auth().basic("test.user","@7SWqfyKpQ")
			.body(payload.paymentUrl())
		.when()
			.post("api/sp_payments/")
		.then().log().all()
			.assertThat().statusCode(200).extract().response().asString();
	
		//System.out.println("Deal has been opened for payment approval");
	
		JsonPath js5 = new JsonPath(response5);
		receivablePkValue = js5.getInt("data.listPaymentReceivables.edges[0].node.pk");
	
		//System.out.println("pk value is"+receivablePkValue);
	}
	
	@And("Approving the payment type {string}")	
	public static void ApprovePayment(String paymentType)
	{
		RestAssured.baseURI= "https://payments-api.dev.ispinnyworks.in/";
		
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.auth().basic("test.user","@7SWqfyKpQ")
			.body(payload.approvingPayment(paymentType))
		.when()
			.post("api/sp_payments/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Partial Payment has been approved");
	}
	
	@And("Getting Balance Amount of the deal")
	public static void GetBalancePayment()
	{
		RestAssured.baseURI= "https://payments-api.dev.ispinnyworks.in";
		
		String response7 = given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.auth().basic("test.user","@7SWqfyKpQ")
			.body(payload.gettingBalance())
		.when()
			.get("api/carpurchases/"+dealId+"/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js6 = new JsonPath(response7);
		balanceAmount = js6.getFloat("payments.deal_balance_amount");
		
		//System.out.println("Remaining amount is "+balanceAmount);
	}
	
	@When("Making Full Payment")
	public static void FullPayment()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.body(payload.FullPayment())
		.when()
			.post("api/payments/orders/"+paymentId+"/add_payment/")
		.then()
			.assertThat().statusCode(200);
		
		System.out.println("full payment has been done");
	}
	
	@Then("Approving full payment")
	public static void ApproveFullPayment()
	{
		RestAssured.baseURI= "https://payments-api.dev.ispinnyworks.in/";
		
		given().header("Keycloak-Authorization",createLead.accessToken)
		 	.header("content-type","application/json")
		 	.auth().basic("test.user","@7SWqfyKpQ")
		 	.body(payload.approvingFullPayment())
		.when()
			.post("api/sp_payments/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Full Payment Approved");
	}
	
	public static double getPaymentFee(String paymentType)
	{
		RestAssured.baseURI= payload.BaseURL();

		String response =given().header("Keycloak-Authorization",createLead.accessToken)
		 	.header("content-type","application/json")
		 	.header("client",DefiningEnv.client)
		 	.body(payload.paymentFees(paymentType))
		.when()
			.post("api/payments/orders/platform_fees_config/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		feePercentage = js.getDouble("[0].fees_percentage");
		
		System.out.println(feePercentage);
		
		return feePercentage;
	}
	
	
	// Switch Buyer- called in agreement feature.
	@And("Creating a New Buyer to Switch")
	public static void callingSwitchBuyer()
	{
		String phoneNumber = createLead.generatetransaction();
		addBuyer(phoneNumber);
		String newBuyerID = getBuyer();
		switchBuyer(newBuyerID);
		System.out.println("Buyer "+newBuyerID+" Switched, now switching it back");
		switchBuyer(buyerId);
	}
	
	public static void switchBuyer(String buyerID)
	{
		RestAssured.baseURI= payload.BaseURL();
		
		given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body(payload.switchBuyer(buyerID))
	.when()
		.post("loan/api/loan_service/graphql")
	.then()
		.assertThat().statusCode(200);
		
	}
	
	 @Then("canceling request for requested deal")
	 public static void approvingCancellation()
	 {
		given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body(payload.ApprovingCancellationRequest())
		.when().
		post("api/carpurchases/"+dealId+"/update_status/")
		.then()
		.assertThat().statusCode(200);
		 
		//JsonPath js = new JsonPath(response);
		//String status = js.getString("updated");
		System.out.println("Deal cancelled successfully");
	 }
	

}