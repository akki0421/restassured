package StepDefination;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

// includes changing of RTO state, buyer documents updation and deletion
// and buyer agreements updation and deletion.

public class dealDetails 
{
	public static String PAN;
	public static String NomineeID;
	public static String NomineeAge;
	public static String delivery_hub;
	public static String purchase_time;
	public static String target_delivery_time;
	public static Boolean loan_confirmation;
	public static String dealAgreementID;
	public static String buyerContactNum;
	public static String buyerState;
	public static String res;
	public static String buyerAddress;
	public static String buyerName;
	public static String buyerType;
	
	@Given("Getting Deal Details, RTO")
	public static void getDealDetails()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		String response = given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client).
		when().
			get("api/carpurchases/"+DealCreate.dealId+"/").
		then().
			assertThat().statusCode(200).extract().asString();
		
		JsonPath js = new JsonPath(response);
		String rtoValue = js.getString("rto_transfer_possible");
		
		
		if(rtoValue != "true")
		{
			delivery_hub= js.getString("delivery_hub");
			purchase_time= js.getString("purchase_time");
			target_delivery_time= js.getString("target_delivery_time");
			loan_confirmation= js.getBoolean("loan_confirmation");
			//buyerState = js.get("agreement_contact_details.city.state"); - cannot use, we are getting state in caps and RTO api expects it in all small
			dealDetails.updateDealDetails();
		
			//System.out.println("RTO has been update to TRUE");
		}else {
			System.out.println("RTO is already set to TRUE");
		}
		
	}
	
	//setting RTO to true
	public static void updateDealDetails()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		given()
		.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body(payload.dealUpdate()).
		when().
			put("api/carpurchases/"+DealCreate.dealId+"/").
		then().
			assertThat().statusCode(200);		
	}
	
	@When("Update Buyer Details with GST {string}")
	public static void updateBuyerDetails(String GST)
	{
		RestAssured.baseURI= payload.BaseURL();
		
		String[] GSTDetails = getGSTdetails(GST);
		
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").header("client",DefiningEnv.client).
			body(payload.buyerUpdate(GSTDetails[1],PAN,GST)).
		when().
			put("sda/api/carpurchases/v2/buyer/"+DealCreate.buyerId+"/").
		then().
			assertThat().statusCode(200);
		
		//System.out.println("GST, Gender and Name in buyer details has been added");
	}
	
	public static String[] getGSTdetails(String GST)
	{
			given().
				header("Keycloak-Authorization",createLead.accessToken).
				header("content-type","application/json").
				header("client",DefiningEnv.client).
				queryParam("verification_type","gstin").queryParam("number_to_verify",GST).
			when().
				get("api/carpurchases/verify_buyer_identity_info/").
			then().
				assertThat().statusCode(200).extract().asPrettyString();
			
			PAN = GST.substring(2,12);
			
			String[] buyerDetails = getPANdetails(PAN);
			return buyerDetails;
	}
	
	public static String[] getPANdetails(String PANupdated)
	{	
		String res = given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client",DefiningEnv.client).
			queryParam("verification_type","pan_card").				
			queryParam("number_to_verify",PANupdated).
		when().
			get("api/carpurchases/verify_buyer_identity_info/").
		then().
			assertThat().statusCode(200).extract().asPrettyString();
	
		JsonPath js = new JsonPath(res);
		buyerType = js.getString("buyer_type");
		buyerName = js.getString("name");
	
		String[] buyerDetails = {buyerType,buyerName};
		//System.out.println(buyerDetails[0]+","+buyerDetails[1]);
	
		return buyerDetails;
	}
	
	
	@And("Setting insurance Details")
	public static void callinggetInsuranceRenewal() throws NullPointerException
	{
		try
		{	
			String insuranceRenewal = null;
			Thread.sleep(120000);
			insuranceRenewal = getInsuranceRenewal();	
			
			
			if(insuranceRenewal == null)
			{
				insuranceRenewal = getInsuranceRenewal();
				
				if(insuranceRenewal == null)
				{
					System.out.println("Not able to get value of insurance renewal");
					NomineeID = null;
				}
				
				else if (insuranceRenewal == "true")
				{
					setNomineeDetails();
					getNomineeDetails();
					uploadInsurancecopy();
					setInsuranceValues();
					getInsuranceDetails();
				}
				else
					System.out.println("Insurance Renewal is set to false in 2nd attempt");
			}
			
			else if(insuranceRenewal == "true")
			{
				setNomineeDetails();
				getNomineeDetails();
				uploadInsurancecopy();
				setInsuranceValues();
				getInsuranceDetails();
			//System.out.println("Insurance details has been filled");
			}
			
			else
				System.out.println("Insurance Renewal is set to false");
		
		} 
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static String getInsuranceRenewal() throws InterruptedException
	{
		RestAssured.baseURI= payload.BaseURL();
		
		res = given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client","consumer-website").
			queryParam("id",DealCreate.dealId).
		when().
			get("sad/api/carpurchases/insurance/").
		then().
			statusCode(200).extract().asPrettyString();
			
		JsonPath js = new JsonPath(res);
		String insurance = null;
		
		insurance =	js.getString("insurance_renewal");
		
		System.out.println(insurance);
		
		return insurance;
	}
	
	
	public static void setNomineeDetails()
	{
		given().
		header("Keycloak-Authorization",createLead.accessToken).
		header("content-type","application/json").
		header("client",DefiningEnv.client).
		queryParam("id",DealCreate.dealId).
	body(payload.setNominee()).
	when().
		post("sda/api/carpurchases/insurance/").
	then().
		assertThat().statusCode(200).extract().asPrettyString();
		
	}
	
	public static void getNomineeDetails()
	{
		String res = given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client",DefiningEnv.client).
			queryParam("id",DealCreate.dealId).
		when().
			get("sda/api/carpurchases/insurance/").
		then().
			assertThat().statusCode(200).extract().asPrettyString();
		
		JsonPath js = new JsonPath(res);
		NomineeID = js.getString("nominee_details.id");
		NomineeAge = js.getString("nominee_details.age");
	}
	
	public static void setInsuranceValues()
	{
		given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client",DefiningEnv.client).
			queryParam("id",DealCreate.dealId).
		body(payload.setInsurance()).
		when().
			post("sda/api/carpurchases/insurance/").
		then().
			assertThat().statusCode(200).extract().asPrettyString();

	}
	
	public static void uploadInsurancecopy()
	{
		
		File testFile = new File("src/test/java/Docs/aadhar_front");
		
		given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","multipart/form-data").
			header("client",DefiningEnv.client).
			formParam("id",DealCreate.dealId).multiPart(testFile).
		when().
			post("api/carpurchases/upload_insurance_copy/").
		then().
			assertThat().statusCode(200).extract().asPrettyString();

	}
	public static void getInsuranceDetails()
	{
		given().
			header("Keycloak-Authorization",createLead.accessToken).
			header("content-type","application/json").
			header("client",DefiningEnv.client).
			queryParam("id",DealCreate.dealId).
		when().
			get("sda/api/carpurchases/insurance/").
		then().
			assertThat().statusCode(200).extract().asPrettyString();

	}
	
	@When("Upload and {string} pre delivery docs")
	public static void preDeliveryDocs(String status)
	{
		RestAssured.baseURI= payload.BaseURL();
		
		File testFile = new File("src/test/java/Docs/aadhar_front");
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
				.header("content-type","multipart/form-data")
				.header("client",DefiningEnv.client)
				.formParam("label","form30").multiPart(testFile)
			.when()
				.post("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
			.then()
				.assertThat().statusCode(200).extract().response().asString();
			
			JsonPath js = new JsonPath(response);
			String form29 = js.getString("doc_data[0].id");
			String docLabel = js.getString("doc_data[0].label");
			
			//System.out.println(form29);

			if (status.equals("reject"))
			{
				rejectDoc(form29);
			}
			else if (status.equals("Verify"))
			{
				verifyDoc(form29);
			}
			else
			{
				deleteDoc(form29,docLabel);
			}

		
	}
	
	@When("Upload and {string} Adhaar Front")
	public static void adhaarFrontUpload(String status)
	{
		RestAssured.baseURI= payload.BaseURL();
		
		File testFile = new File("src/test/java/Docs/aadhar_front");
		
		
		
		
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","multipart/form-data")
			.header("client",DefiningEnv.client)
			.formParam("label","aadhar_front").multiPart(testFile)
		.when()
			.post("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		
		String adhaarFrontId = js.getString("doc_data[0].id");
		String docLabel = js.getString("doc_data[0].label");
		
		//System.out.println(adhaarFrontId);

		if (status.equals("reject"))
		{
			rejectDoc(adhaarFrontId);
		}
		else if (status.equals("Verify"))
		{
			verifyDoc(adhaarFrontId);
		}
		else
		{
			deleteDoc(adhaarFrontId,docLabel);
		}
	}
	
	@Given("Upload and {string} Adhaar Back")
	public static void adhaarBackUpload(String status) 
	{
		RestAssured.baseURI= payload.BaseURL();
	
		File testFile = new File("src/test/java/Docs/aadhar_back");
		
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","multipart/form-data")
			.header("client",DefiningEnv.client)
			.formParam("label","aadhar_back")
			.multiPart(testFile).
		when().
			post("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/").
		then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		
		String adhaarBackId = js.getString("doc_data[0].id");
		String docLabel = js.getString("doc_data[0].label");

		if (status.equals("reject"))
		{
			rejectDoc(adhaarBackId);
		}
		else if (status.equals("Verify"))
		{
			verifyDoc(adhaarBackId);
		}
		else
		{
			deleteDoc(adhaarBackId,docLabel);
		}
		
	}
	
	@And("Upload and {string} PAN")
	public static void panUpload(String status) 
	{
		RestAssured.baseURI= payload.BaseURL();
		
		File testFile = new File("src/test/java/Docs/pancard");	
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","multipart/form-data")
			.header("client",DefiningEnv.client)
			.formParam("label","pancard")
			.multiPart(testFile)
		.when()
			.post("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		
		String panId = js.getString("doc_data[0].id");
		String docLabel = js.getString("doc_data[0].label");
		
		if (status.equals("reject"))
		{
			rejectDoc(panId);
		}
		else if (status.equals("Verify"))
		{
			verifyDoc(panId);
		}
		else
		{
			deleteDoc(panId,docLabel);
		}
	}
	
	/*@Then("Upload and {string} Supporting Doc")
	public static void SupportingDocUpload(String status) 
	{
		
		localAddressProof();
		RestAssured.baseURI= payload.BaseURL();
		
		File testFile = new File(System.getProperty("user.dir")+"\\src\\test\\java\\Docs\\pancard");	
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","multipart/form-data")
			.header("client",DefiningEnv.client)
			.formParam("label","rent_agreement_with_house_owners_aadhaar_card")
			.multiPart(testFile)
		.when()
			.post("api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		
		String supprting_docId = js.getString("doc_data[0].id");
		String docLabel = js.getString("doc_data[0].label");
		
		if (status.equals("reject"))
		{
			rejectDoc(supprting_docId);
		}
		else if (status.equals("Verify"))
		{
			verifyDoc(supprting_docId);
			verifySupportingDoc();
		}
		else
		{
			deleteDoc(supprting_docId,docLabel);
		}
		
	}*/
	
	
	@Then("Setting Same state RTO to TRUE")
	public static void sameStateTrue() 
	{
		RestAssured.baseURI= payload.BaseURL();
	
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body("{\"same_state_aadhaar_address\":true}").
		when().
			put("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/").
		then()
			.assertThat().statusCode(200);
	}
	
	public static void verifyDoc(String fileId) 
	{
		RestAssured.baseURI= payload.BaseURL();
	
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body("{\"file_id\":"+fileId+",\"remarks\":\"\",\"status\":\"verified\"}").
		when().
			patch("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Doc has been verified");
	}
	
	public static void rejectDoc(String fileId) 
	{
		String remark = "Document Not Clear";
		
		RestAssured.baseURI= payload.BaseURL();
	
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body("{\"file_id\":"+fileId+",\"remarks\":\""+remark+"\",\"status\":\"rejected\"}").
		when().
			patch("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Doc has been rejected");
	}
	
	public static void deleteDoc(String fileId, String docLabel) 
	{
		RestAssured.baseURI= payload.BaseURL();
	
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body("{\"file_id\":"+fileId+",\"label\":\""+docLabel+"\"}").
		when().
			delete("sda/api/carpurchases/v2/"+DealCreate.dealId+"/document/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Doc has been deleted");
	}
	
	/*public static void localAddressProof()
	{
		RestAssured.baseURI= payload.BaseURL();
		given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body("{\r\n"
				+ "    \"parent_label\": \"supporting_document\",\r\n"
				+ "    \"child_label\": \"rent_agreement_with_house_owners_aadhaar_card\"\r\n"
				+ "}").
		when()
		.put("api/carpurchases/v2/"+DealCreate.dealId+"/child_label/")
		.then()
		.assertThat().statusCode(200);
	}
	public static void verifySupportingDoc() {
		RestAssured.baseURI= payload.BaseURL();
		given().header("Keycloak-Authorization",createLead.accessToken)
		.header("content-type","application/json")
		.header("client",DefiningEnv.client)
		.body("{\r\n"
				+ "    \"is_parent_label\": true,\r\n"
				+ "    \"label\": \"supporting_document\"\r\n"
				+ "}").
		when()
		.put("api/carpurchases/v2/"+DealCreate.dealId+"/label_verification/")
		.then()
		.assertThat().statusCode(200);
	}*/
	
	@Given("Download Buyer Agreement")
	public static void downloadAgreement() throws IOException 
	{
		RestAssured.baseURI= payload.BaseURL();
		Response res = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client)
			.queryParam("pdf","1")
			.queryParam("agreement_label","deal_agreement").
		when().
			get("api/carpurchases/v2/"+DealCreate.dealId+"/download_agreement/").andReturn();
					
		byte[] bytes = res.asByteArray();
		
		File file  = new File(System.getProperty("user.dir")+"\\src\\test\\java\\Downloads\\agreement.pdf");
		
		Files.write(file.toPath(),bytes);
	}	
	
	
	@Then("Upload and Verify Buyer Agreement")
	public static void uploadAgreement()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		File testFile = new File(System.getProperty("user.dir")+"\\src\\test\\java\\Downloads\\agreement.pdf");
		
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","multipart/form-data").
			header("client",DefiningEnv.client).
			formParam("label","deal_agreement").multiPart(testFile)
		.when().
			post("sda/api/carpurchases/v2/"+DealCreate.dealId+"/agreement/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		dealAgreementID = js.getString("doc_data[0].id");
		
		//System.out.println("Deal Agreement Uploaded");
		
		verifyAgreement(dealAgreementID);	
	}
	
	
	public static void verifyAgreement(String fileId) 
	{
		RestAssured.baseURI= payload.BaseURL();
	
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
			body("{\"file_id\":"+fileId+",\"remarks\":\"\",\"status\":\"verified\"}").
		when().
			patch("sda/api/carpurchases/v2/"+DealCreate.dealId+"/agreement/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Deal Agreement has been verified");
	}
	
	@And("Edit Buyer Details with PAN {string}")
	//to verify new agreement generation
	public static void editBuyerDetails(String PANupdated)
	{
		getActiveBuyer();
		
		RestAssured.baseURI= payload.BaseURL();
		
		String[] PANDetails = dealDetails.getPANdetails(PANupdated);
		
		//System.out.println(PANDetails[0]+","+PANDetails[1]);
		
		given()
			.header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").header("client","dcf").
			body(payload.editBuyer(PANDetails[1],PANupdated,PANDetails[0],buyerContactNum)).
		when().
			put("sda/api/carpurchases/v2/buyer/"+DealCreate.buyerId+"/").
		then().
			assertThat().statusCode(200);
		
		//System.out.println("PAN, Gender and Name in buyer details has been added");
	}
	
	@Given("Delete Old Agreement")
	public static void deleteAgreement()
	{
		RestAssured.baseURI= payload.BaseURL();
		
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json")
			.header("client",DefiningEnv.client).
			body("{\"file_id\":"+dealAgreementID+",\"label\":\"deal_agreement\"}").
		when().
			delete("sda/api/carpurchases/v2/"+DealCreate.dealId+"/agreement/")
		.then()
			.assertThat().statusCode(200);
		
		//System.out.println("Old Deal Agreement has been deleted");
	}
	
	public static void getActiveBuyer()
	{
		RestAssured.baseURI = payload.BaseURL();
		
		String response = given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
		when().
			get("sda/api/carpurchases/v2/"+DealCreate.dealId+"/active_buyer/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
	
		JsonPath js = new JsonPath(response);
		
		buyerContactNum = js.getString("contact_number");
		buyerAddress = js.getString("address");
		
		rcDeliveryAddress();
	}
	
	public static void rcDeliveryAddress()
	{
		RestAssured.baseURI = payload.BaseURL();
		
		given().header("Keycloak-Authorization",createLead.accessToken)
			.header("content-type","application/json").
			header("client",DefiningEnv.client).
		when().
		body(payload.rcAddress()).
			put("sda/api/carpurchases/v2/buyer/"+DealCreate.buyerId+"/")
		.then()
			.assertThat().statusCode(200).extract().response().asString();
	
	}
}
