package StepDefination;

import static io.restassured.RestAssured.given;

import Files.payload;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class camundaResponse {
	public static String assignmentGroup;
	public static String prefferedassignmentGroup;
	public static String actualassignmentGroup;
	
    @Given("Assignment Group from camunda")
	public static void getAssignmentGroup() throws InterruptedException
	{
		// TODO Auto-generated method stub
	
		RestAssured.baseURI= "http://sp-camunda.dev.internal.spinny:80/engine-rest/";
	String response =  given().header("content-type","application/json")
		 .auth().basic("sploanadmin","iPhone.000")
		 //  .header("Keycloak-Authorization",createLead.accessToken)
		   .body(payload.getCamundaResponse())
		   .when().post("/decision-definition/key/South_Cluster_1/evaluate")
		   .then().assertThat().statusCode(200).extract().asPrettyString();
		// System.out.println(response);
		 JsonPath js = new JsonPath(response);
		  assignmentGroup = js.getString("[0].group_id.value"); 
		 System.out.println(assignmentGroup); 
	}
    @When("Assignment Group from CRM")
	public static void filterOutBuylead()
	{
		// TODO Auto-generated method stub
	
		  RestAssured.baseURI= "https://spntest.spinnyworks.in";
  String response = given().header("content-type","application/json")
		  .header("Keycloak-Authorization",createLead.accessToken)
		  .queryParam("buy_lead_id", DatabaseConnection.buyLead)
		   .when().get("/api/buy_leads/buy_lead_assignment_meta_data/")
		   .then().assertThat().statusCode(200).extract().asPrettyString();
		 System.out.println(response);
		 JsonPath js1 = new JsonPath(response);
		  prefferedassignmentGroup = js1.getString("results[0].preferred_group_assignment"); 
		  System.out.println(prefferedassignmentGroup);
		  if (assignmentGroup.equals (prefferedassignmentGroup)) {
			  JsonPath js2= new JsonPath(response);
			  actualassignmentGroup = js2.get("results[0].actual_group_assignment");
			  	System.out.println(actualassignmentGroup);  
		  }
		  else {
			  System.out.println("assignment failed");
		  }
			  
		 
	}
	
}
