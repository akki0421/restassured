package StepDefination;

import static io.restassured.RestAssured.given;

import java.io.File;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;

public class Refund {
 
	@When("Adding bank details")
	public static void addBankDetails() {
		File testFile = new File("src/test/java/Docs/aadhar_front");
		RestAssured.baseURI= payload.BaseURL();
		
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.formParam("buy_lead_id",createLead.leadId)
			.formParam("account_holder_name", "Deepanshu")
			.formParam("account_type", "SAVING")
			.formParam("account_number","424242424242")
			.formParam("ifsc_code", "SBIN0050620")
			.formParam("dealId", DealCreate.dealId)
			.formParam("cancelled-cheque","binary").multiPart(testFile)
			.when().post("/api/carpurchases/bank_details/")
			.then().assertThat().statusCode(200).extract().asString();
	}
	
	@And("Getting bank buyer details")
	public static void getBuyerDetails() {
		RestAssured.baseURI= payload.BaseURL();
		given()
		.header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.queryParam("buy_lead_id", createLead.leadId)
		.when().get("/api/carpurchases/bank_details/")
		.then().assertThat().statusCode(200).extract().asString();
	}
	
	@Then("Initiating refund")
	public static void initiateRefund() {
		RestAssured.baseURI= payload.BaseURL();
		given()
		.header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body("deal_id: "+DealCreate.dealId+"\r\n"
				+ "refund_info: [{\"account_number\":\"424242424242\",\"ifsc\":\"SBIN0050620\",\"payment_type\":\"Deal Cancellation\",\"refund_amount\":\"10000\"}]")
		.when().post("/api/carpurchases/initiate_refund_bank_details/")
		.then().assertThat().statusCode(200).extract().asString();
	}
}
	
