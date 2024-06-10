package StepDefination;


import Utility.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class ExchangeProcess {
    public static int created_leadId= Integer.parseInt(Exchange.sell_lead) ; // = 569091;
    static String taskID;
    static String taskID_PostInspectionPricing;
    static String taskId_postInspectionNegotiation;
    static String taskId_scheduleProcurement;
    static String taskId_confirmProcurementScheduled;
    static String inspectionId;
    static String inspectionIdForI2;
    static String accessToken;
    static String inspectorId = "913345"; // inspector id for Test.user - 913345
    static String registration_id = Generator.registration_no();
    static int paymentBreakdownId;
    static int paymentRequestId;


    @Given("^User has logged in$")
    public void user_has_logged_in() {
        Response tokenRes = given().header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("client_id", "sp-supply-backend")
                .formParam("client_secret", "e095fe52-dd18-4923-a9bd-215431ad6003")
                .formParam("scope", "openid")
                .formParam("username", "test.user")
                .formParam("password", "@7SWqfyKpQ")
                .formParam("grant_type", "password").when().post("https://sso-dev.spinny.com/auth/realms/internal/protocol/openid-connect/token").then().statusCode(200).extract().response();
        String created_Token = tokenRes.jsonPath().get("access_token").toString();
        accessToken = "Bearer " + created_Token;
    }

    @And("^User has created one lead in supply$")
    public void user_has_created_one_lead_in_supply() {
        baseURI = "https://supply-backend-test.spinny.com/api/";
        Response res = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json")
                .body(new File("src/test/java/Files/PayLoad/createLead.json")).when().post("leads/").then().statusCode(200).extract().response();
        created_leadId = res.jsonPath().get("id");
        System.out.println("Created lead id is - " + created_leadId + ", time Consumed - " + res.getTime());
    }

    @And("^User has booked the Inspection Slot$")
    public void user_has_booked_the_inspection_slot() throws Throwable {
        baseURI = "https://supply-backend-test.spinny.com/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the taskId -
        DocumentContext jsonTaskId = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/fetchTaskId.json"));
        String jsonTaskIdKey = "variables.leadId";
        String jsonTaskIdValue = String.valueOf(created_leadId);
        jsonTaskId.set(jsonTaskIdKey, jsonTaskIdValue);
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json")
                .accept("application/json").body(jsonTaskId.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        taskID = res1.jsonPath().get("data.getAllTasksForLead[0].pk").toString();
        //System.out.println("task id is = " + taskID);


        //1st Call - mutateScheduleInspectionData
        Object[] newSlots;
        newSlots = SlotFinding.slotFinding();

        DocumentContext json = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/mutateScheduleInspectionData.json"));
        String jsonLeadPk = "variables.leadPk";
        String jsonLeadPkValue = String.valueOf(created_leadId);
        String JsonLeadId = "variables.leadId";
        String JsonLeadIdValue = String.valueOf(created_leadId);
        String JsonScheduledStartTime = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue = newSlots[1];
        String JsonSlotResponse = "variables.slotsResponse";
        String JsonSlotResponseValue = newSlots[2].toString();
        String JsonTaskId = "variables.taskId";
        String JsonTaskIdValue = taskID;

        json.set(jsonLeadPk, jsonLeadPkValue);
        json.set(JsonLeadId, JsonLeadIdValue);
        json.set(JsonScheduledStartTime, JsonScheduledStartTimeValue);
        json.set(JsonSlotResponse, JsonSlotResponseValue);
        json.set(JsonTaskId, JsonTaskIdValue);

        given().header("Keycloak-Authorization", accessToken).contentType("application/json").
                accept("application/json").body(json.jsonString()).when().
                post("graphql/").then().statusCode(200).extract().response();


        //2nd Call - updateTaskAndScheduleNextTasks
        DocumentContext json2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/updateTaskAndScheduleNextTasks.json"));
        String jsonLeadPk2 = "variables.leadPk";
        String jsonLeadPkValue2 = String.valueOf(created_leadId);
        String JsonLeadId2 = "variables.leadId";
        String JsonLeadIdValue2 = String.valueOf(created_leadId);
        String JsonScheduledStartTime2 = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue2 = newSlots[1];
        String JsonSlotResponse2 = "variables.slotsResponse";
        String JsonSlotResponseValue2 = newSlots[2].toString();
        String JsonTaskId2 = "variables.taskId";
        String JsonTaskIdValue2 = taskID;

        json2.set(jsonLeadPk2, jsonLeadPkValue2);
        json2.set(JsonLeadId2, JsonLeadIdValue2);
        json2.set(JsonScheduledStartTime2, JsonScheduledStartTimeValue2);
        json2.set(JsonSlotResponse2, JsonSlotResponseValue2);
        json2.set(JsonTaskId2, JsonTaskIdValue2);

        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(json2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();


        //3rd API Call - addComment
        DocumentContext json3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/addComment.json"));
        String jsonLeadPk3 = "variables.leadPk";
        String jsonLeadPkValue3 = String.valueOf(created_leadId);
        String JsonLeadId3 = "variables.leadId";
        String JsonLeadIdValue3 = String.valueOf(created_leadId);
        String JsonScheduledStartTime3 = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue3 = newSlots[1];
        String JsonSlotResponse3 = "variables.slotsResponse";
        String JsonSlotResponseValue3 = newSlots[2].toString();
        String JsonTaskId3 = "variables.taskId";
        String JsonTaskIdValue3 = taskID;

        json3.set(jsonLeadPk3, jsonLeadPkValue3);
        json3.set(JsonLeadId3, JsonLeadIdValue3);
        json3.set(JsonScheduledStartTime3, JsonScheduledStartTimeValue3);
        json3.set(JsonSlotResponse3, JsonSlotResponseValue3);
        json3.set(JsonTaskId3, JsonTaskIdValue3);

        Response res3 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(json3.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();


        //4th API Call - slotsApiMutation
        DocumentContext json4 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/slotsApiMutation.json"));
        String jsonLeadPk4 = "variables.leadPk";
        String jsonLeadPkValue4 = String.valueOf(created_leadId);
        String JsonLeadId4 = "variables.leadId";
        String JsonLeadIdValue4 = String.valueOf(created_leadId);
        String JsonScheduledStartTime4 = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue4 = newSlots[1];
        String JsonSlotResponse4 = "variables.slotsResponse";
        String JsonSlotResponseValue4 = newSlots[2].toString();
        String JsonTaskId4 = "variables.taskId";
        String JsonTaskIdValue4 = taskID;

        json4.set(jsonLeadPk4, jsonLeadPkValue4);
        json4.set(JsonLeadId4, JsonLeadIdValue4);
        json4.set(JsonScheduledStartTime4, JsonScheduledStartTimeValue4);
        json4.set(JsonSlotResponse4, JsonSlotResponseValue4);
        json4.set(JsonTaskId4, JsonTaskIdValue4);

        Response res4 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(json4.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();

        //5th API Call - addPreInspectionNegotiation
        DocumentContext json5 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/addPreInspectionNegotiation.json"));
        String jsonLeadPk5 = "variables.leadPk";
        String jsonLeadPkValue5 = String.valueOf(created_leadId);
        String JsonLeadId5 = "variables.leadId";
        String JsonLeadIdValue5 = String.valueOf(created_leadId);
        String JsonScheduledStartTime5 = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue5 = newSlots[1];
        String JsonSlotResponse5 = "variables.slotsResponse";
        String JsonSlotResponseValue5 = newSlots[2].toString();
        String JsonTaskId5 = "variables.taskId";
        String JsonTaskIdValue5 = taskID;

        json5.set(jsonLeadPk5, jsonLeadPkValue5);
        json5.set(JsonLeadId5, JsonLeadIdValue5);
        json5.set(JsonScheduledStartTime5, JsonScheduledStartTimeValue5);
        json5.set(JsonSlotResponse5, JsonSlotResponseValue5);
        json5.set(JsonTaskId5, JsonTaskIdValue5);

        Response res5 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(json5.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();

        //6th API Call - addSellingReason
        DocumentContext json6 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/addSellingReason.json"));
        String jsonLeadPk6 = "variables.leadPk";
        String jsonLeadPkValue6 = String.valueOf(created_leadId);
        String JsonLeadId6 = "variables.leadId";
        String JsonLeadIdValue6 = String.valueOf(created_leadId);
        String JsonScheduledStartTime6 = "variables.scheduledStartTime";
        Object JsonScheduledStartTimeValue6 = newSlots[1];
        String JsonSlotResponse6 = "variables.slotsResponse";
        String JsonSlotResponseValue6 = newSlots[2].toString();
        String JsonTaskId6 = "variables.taskId";
        String JsonTaskIdValue6 = taskID;

        json6.set(jsonLeadPk6, jsonLeadPkValue6);
        json6.set(JsonLeadId6, JsonLeadIdValue6);
        json6.set(JsonScheduledStartTime6, JsonScheduledStartTimeValue6);
        json6.set(JsonSlotResponse6, JsonSlotResponseValue6);
        json6.set(JsonTaskId6, JsonTaskIdValue6);

        Response res6 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(json6.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
    }

    // Assign inspector - "test.user" to newly created lead.
    @Then("^Assignee Test user inspector to this Inspection$")
    public void assignee_test_user_inspector_to_this_inspection() throws IOException {
        baseURI = "https://supply-backend-test.spinny.com/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the inspection id
        DocumentContext jsonTask = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/getLeadInspections.json"));
        String jsonLeadPk1 = "variables.leadPk";
        String jsonLeadPkValue1 = String.valueOf(created_leadId);
        String JsonLeadId1 = "variables.leadId";
        String JsonLeadIdValue1 = String.valueOf(created_leadId);
        jsonTask.set(jsonLeadPk1, jsonLeadPkValue1);
        jsonTask.set(JsonLeadId1, JsonLeadIdValue1);
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        inspectionId = res1.jsonPath().get("data.leadInspections[0].inspection.pk").toString();
        System.out.println("Inspection id is = " + inspectionId);

        // Assign the Inspector - test.user
        DocumentContext jsonTask2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/updateInspectionAssignee.json"));
        String jsonInspectionId = "variables.inspectionId";
        String jsonInspectionIdValue1 = String.valueOf(inspectionId);
        String jsonInspectorId = "variables.inspectorId";
        String jsonInspectorIdValue1 = String.valueOf(inspectorId);
        jsonTask2.set(jsonInspectionId, jsonInspectionIdValue1);
        jsonTask2.set(jsonInspectorId, jsonInspectorIdValue1);
        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
    }

    @And("^User is completing I1 Inspection$")
    public void user_is_completing_i1_inspection() throws IOException {
        baseURI = "https://spntest.myspinny.com/api";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();


        //1.  Hit the upload report API - /inspections/{id}/upload_report/
        String endPoint1 = "/inspections/" + inspectionId + "/upload_report/";
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(new File("src/test/java/Files/PayLoadForI1/uploadReport.json")).when().put(endPoint1).then().statusCode(200).extract().response();

        //2.  Hit the finish API - inspections/{id}/finish/
        String endPoint2 = "/inspections/" + inspectionId + "/finish/";
        DocumentContext Jt1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/finish.json"));
        String date = CurrentDate.currentDate();
        String jsonStartTime = "start_time";
        String jsonStartTimeValue = date + "T21:42:16.665+05:30";
       // System.out.println("I1 - start_time -" + jsonStartTimeValue);
        String jsonEndTime = "finish_time";
        String jsonEndTimeValue = date + "T21:27:51.539+05:30";
        //System.out.println("I1 - finish_time -" + jsonStartTimeValue);

        Jt1.set(jsonStartTime, jsonStartTimeValue);
        Jt1.set(jsonEndTime, jsonEndTimeValue);
        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt1.jsonString()).when().put(endPoint2).then().statusCode(200).extract().response();

        // //3. Hit the Upload API - /upload/
        // String endPoint3 = "/upload/";
        // Response res3 = given().cookies(cookie).contentType("application/json").accept("application/json").body().when().put(endPoint3).then().statusCode(200).extract().response();

        //4. Hit the profile API - /inspections/{id}/profile/
        String endPoint4 = "/inspections/" + inspectionId + "/profile/";
        DocumentContext Jt2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/profile.json"));
        String jsonRegNo = "registration_no";
        String jsonRegNoValue = registration_id;
        Jt2.set(jsonRegNo, jsonRegNoValue);
        Response res3 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt2.jsonString()).when().put(endPoint4).then().statusCode(200).extract().response();

        //5. Hit the Quality Check API - /inspection/{id}/quality_check/
        String endPoint5 = "/inspections/" + inspectionId + "/quality_check/";
        DocumentContext Jt3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/qualityCheck.json"));
        Response res4 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt3.jsonString()).when().post(endPoint5).then().statusCode(200).extract().response();


        //6. Hit the Tyre API - /inspections/{id}/tyres/
        String endPoint6 = "/inspections/" + inspectionId + "/tyres/";
        DocumentContext Jt4 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/tyre.json"));
        Response res5 = given().header("Keycloak-Authorization", accessToken).header("value", "new-report-service").contentType("application/json").accept("application/json").body(Jt4.jsonString()).when().post(endPoint6).then().statusCode(200).extract().response();


        //7. Hit the Approval API -  inspections/{id}/approvals/
        String endPoint7 = "/inspections/" + inspectionId + "/approvals/";
        DocumentContext Jt5 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/approvals.json"));
        Response res6 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt5.jsonString()).when().post(endPoint7).then().statusCode(200).extract().response();


        //8. Hit the photos API - /inspections/{id}/photos/
        String endPoint8 = "/inspections/" + inspectionId + "/photos/";
        DocumentContext Jt6 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/photos.json"));
        Response res7 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt6.jsonString()).when().post(endPoint8).then().statusCode(200).extract().response();


        //9. Hit the status API - /inspections/{id}/status/
        String endPoint9 = "/inspections/" + inspectionId + "/status/";
        DocumentContext Jt7 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI1/status.json"));
        Response res8 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt7.jsonString()).when().post(endPoint9).then().statusCode(200).extract().response();
        System.out.println("Inspection I1 is completed");

    }

    @And("^Complete the Seller KYC$")
    public void complete_the_seller_kyc() throws IOException {
        //Need to check for the BaseURI Over here.
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        // 1. hitting the Seller PanCard
        DocumentContext jsonTask1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/SellerPanCard.json"));
        String jsonLeadId1 = "variables.leadId";
        String jsonLeadIdValue1 = String.valueOf(created_leadId);
        jsonTask1.set(jsonLeadId1, jsonLeadIdValue1);
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask1.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Seller Pan card captured");

        //2. -  Hitting the RC-RegistrationCertificate - addFile API
        DocumentContext jsonTask2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/RCRegistrationCertificate.json"));
        String jsonContextId1 = "variables.contextId";
        String jsonContextIdValue1 = String.valueOf(created_leadId);
        String jsonLeadPk1 = "variables.leadPk";
        String jsonLeadPkValue1 = String.valueOf(created_leadId);
        jsonTask2.set(jsonContextId1, jsonContextIdValue1);
        jsonTask2.set(jsonLeadPk1, jsonLeadPkValue1);

        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"rc-registration-certificate\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"rc-registration-certificate\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask2.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("RC Copy Uploaded");

        //3. Uploading the Insurance Copy - add file API
        DocumentContext jsonTask3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/InsuranceCopy.json"));
        String jsonContextId3 = "variables.contextId";
        String jsonContextIdValue3 = String.valueOf(created_leadId);
        String jsonLeadPk3 = "variables.leadPk";
        String jsonLeadPkValue3 = String.valueOf(created_leadId);
        jsonTask3.set(jsonContextId3, jsonContextIdValue3);
        jsonTask3.set(jsonLeadPk3, jsonLeadPkValue3);

        Response res3 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"insurance-copy\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"insurance-copy\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask3.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Insurance Copy Uploaded");

        //4. Hitting the API for Mark Not Required for Manual Guide -
        DocumentContext jsonTask_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/ManualGuide.json"));
        String jsonLeadId_A = "variables.leadId";
        String jsonLeadIdValue_A = String.valueOf(created_leadId);
        jsonTask_A.set(jsonLeadId_A, jsonLeadIdValue_A);
        Response res1_A = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_A.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Manual Guide captured");


        //5. Hitting the API for - Pollution Under Control Certificate -
        DocumentContext jsonTask_B = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/PollutionUnderControlCertificate.json"));
        String jsonLeadId_B = "variables.leadId";
        String jsonLeadIdValue_B = String.valueOf(created_leadId);
        jsonTask_B.set(jsonLeadId_B, jsonLeadIdValue_B);
        Response res1_B = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_B.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Pollution Under Control Certificate captured");

        //6. Hitting the API for - Invoice -
        DocumentContext jsonTask_C = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/Invoice.json"));
        String jsonLeadId_C = "variables.leadId";
        String jsonLeadIdValue_C = String.valueOf(created_leadId);
        jsonTask_C.set(jsonLeadId_C, jsonLeadIdValue_C);
        Response res1_C = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_C.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Invoice captured");

        //7. Hitting the API for - Owner Photographs -
        DocumentContext jsonTask_D = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/OwnerPhotographs.json"));
        String jsonLeadId_D = "variables.leadId";
        String jsonLeadIdValue_D = String.valueOf(created_leadId);
        jsonTask_D.set(jsonLeadId_D, jsonLeadIdValue_D);
        Response res1_D = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_D.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Owner Photographs captured");


        //8. Uploading the Seller Local Address proof - addfile API
        DocumentContext jsonTask4 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/SellerLocalAddressProof.json"));
        String jsonContextId4 = "variables.contextId";
        String jsonContextIdValue4 = String.valueOf(created_leadId);
        String jsonLeadPk4 = "variables.leadPk";
        String jsonLeadPkValue4 = String.valueOf(created_leadId);
        jsonTask4.set(jsonContextId4, jsonContextIdValue4);
        jsonTask4.set(jsonLeadPk4, jsonLeadPkValue4);

        Response res4 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"seller-local-address-proof\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"seller-local-address-proof\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask4.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Seller local Address proof Uploaded");

        //9. Uploading the Current Address proof -
        DocumentContext jsonTask5 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/CurrentAddressProof.json"));
        String jsonContextId5 = "variables.contextId";
        String jsonContextIdValue5 = String.valueOf(created_leadId);
        String jsonLeadPk5 = "variables.leadPk";
        String jsonLeadPkValue5 = String.valueOf(created_leadId);
        jsonTask5.set(jsonContextId5, jsonContextIdValue5);
        jsonTask5.set(jsonLeadPk5, jsonLeadPkValue5);

        Response res5 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"current-address-proof\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"current-address-proof\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask5.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Current Address proof Uploaded");

        //10. Uploading the Company Pancard -
        DocumentContext jsonTask6 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/CompanyPanCard.json"));
        String jsonContextId6 = "variables.contextId";
        String jsonContextIdValue6 = String.valueOf(created_leadId);
        String jsonLeadPk6 = "variables.leadPk";
        String jsonLeadPkValue6 = String.valueOf(created_leadId);
        jsonTask6.set(jsonContextId6, jsonContextIdValue6);
        jsonTask6.set(jsonLeadPk6, jsonLeadPkValue6);

        Response res6 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"company-pancard\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"company-pancard\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask6.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Company PanCard Uploaded");


        //11. Hitting the API for - Company GST Certificate
        DocumentContext jsonTask_E = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/CompanyGSTCertificate.json"));
        String jsonLeadId_E = "variables.leadId";
        String jsonLeadIdValue_E = String.valueOf(created_leadId);
        jsonTask_E.set(jsonLeadId_E, jsonLeadIdValue_E);
        Response res1_E = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_E.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Company GST Certificate captured");


        //12. Uploading the Company Letter Head for Ownership and insurance Transfer -
        DocumentContext jsonTask7 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/CompanyLetterHead.json"));
        String jsonContextId7 = "variables.contextId";
        String jsonContextIdValue7 = String.valueOf(created_leadId);
        String jsonLeadPk7 = "variables.leadPk";
        String jsonLeadPkValue7 = String.valueOf(created_leadId);
        jsonTask7.set(jsonContextId7, jsonContextIdValue7);
        jsonTask7.set(jsonLeadPk7, jsonLeadPkValue7);

        Response res7 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"company-letter-head-for-ownership-and-insurance-transfer\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"company-letter-head-for-ownership-and-insurance-transfer\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask7.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Company Letter Head for Ownership and insurance Transfer Uploaded");


        //13. Uploading the Company Current Address proof
        DocumentContext jsonTask8 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/CompanyCurrentAddressProof.json"));
        String jsonContextId8 = "variables.contextId";
        String jsonContextIdValue8 = String.valueOf(created_leadId);
        String jsonLeadPk8 = "variables.leadPk";
        String jsonLeadPkValue8 = String.valueOf(created_leadId);
        jsonTask8.set(jsonContextId8, jsonContextIdValue8);
        jsonTask8.set(jsonLeadPk8, jsonLeadPkValue8);

        Response res8 = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"company-current-address-proof\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"company-current-address-proof\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask8.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Company Current Address Proof Uploaded");


        // Need to ask to supply team - about the below API's
        //14. Uploading the abcdefgh
        DocumentContext jsonTask_F = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/abcdefgh.json"));
        String jsonLeadId_F = "variables.leadId";
        String jsonLeadIdValue_F = String.valueOf(created_leadId);
        jsonTask_F.set(jsonLeadId_F, jsonLeadIdValue_F);
        Response res1_F = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_F.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        //System.out.println("abcdefgh captured");

        //15. Uploading the TestItem
        DocumentContext jsonTask_G = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/TestItem.json"));
        String jsonLeadId_G = "variables.leadId";
        String jsonLeadIdValue_G = String.valueOf(created_leadId);
        jsonTask_G.set(jsonLeadId_G, jsonLeadIdValue_G);
        Response res1_G = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_G.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        //System.out.println("TestItem captured");

        //16. Uploading the TestItem3
        DocumentContext jsonTask_H = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/TestItem3.json"));
        String jsonLeadId_H = "variables.leadId";
        String jsonLeadIdValue_H = String.valueOf(created_leadId);
        jsonTask_H.set(jsonLeadId_H, jsonLeadIdValue_H);
        Response res1_H = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_H.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        //System.out.println("TestItem 3 captured");

        //17. Uploading the Seller Purchase Document
        DocumentContext jsonTask_I = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/SellerPurchaseDocument.json"));
        String jsonLeadId_I = "variables.leadId";
        String jsonLeadIdValue_I = String.valueOf(created_leadId);
        jsonTask_I.set(jsonLeadId_I, jsonLeadIdValue_I);
        Response res1_I = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_I.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Seller Purchase Document captured");

        //18. Uploading the acknowledgement_document Document
        DocumentContext jsonTask_J = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/acknowledgementDocument.json"));
        String jsonLeadId_J = "variables.leadId";
        String jsonLeadIdValue_J = String.valueOf(created_leadId);
        jsonTask_J.set(jsonLeadId_J, jsonLeadIdValue_J);
        Response res1_J = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_J.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("acknowledgement document captured");


        //19. Uploading the Seller's Agreement Document
        DocumentContext jsonTask_K = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/SellerAgreement.json"));
        String jsonContextId_K1 = "variables.contextId";
        String jsonContextIdValue_K1 = String.valueOf(created_leadId);
        String jsonLeadPk_K1 = "variables.leadPk";
        String jsonLeadPkValue_K1 = String.valueOf(created_leadId);
        jsonTask_K.set(jsonContextId_K1, jsonContextIdValue_K1);
        jsonTask_K.set(jsonLeadPk_K1, jsonLeadPkValue_K1);
        Response res1_K = given().header("Keycloak-Authorization", accessToken).contentType("multipart/form-data; boundary=----WebKitFormBoundaryQRoQEFfUvxBFF3Vr")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"sellers-agreement\"}],\"contextId\":\"" + created_leadId + "\",\"label\":\"sellers-agreement\",\"leadPk\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/testImage.png"), "image/png")
                .body(jsonTask_K.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Seller's Agreement document captured");


        //20. Uploading the Loan for closure Document
        DocumentContext jsonTask_L = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/LoanForClosure.json"));
        String jsonLeadId_L = "variables.leadId";
        String jsonLeadIdValue_L = String.valueOf(created_leadId);
        jsonTask_L.set(jsonLeadId_L, jsonLeadIdValue_L);
        Response res1_L = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_L.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Loan for closure Document captured");

        //21. Uploading the Dicky Inside View Document
        DocumentContext jsonTask_M = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/DickyInsideView.json"));
        String jsonLeadId_M = "variables.leadId";
        String jsonLeadIdValue_M = String.valueOf(created_leadId);
        jsonTask_M.set(jsonLeadId_M, jsonLeadIdValue_M);
        Response res1_M = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_M.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Dicky Inside View Document Captured");

        //22. Uploading the Bank Issue No Objection certificate
        DocumentContext jsonTask_N = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayLoadForI2SellerKYC/BankNoObjectionCertificate.json"));
        String jsonLeadId_N = "variables.leadId";
        String jsonLeadIdValue_N = String.valueOf(created_leadId);
        jsonTask_N.set(jsonLeadId_N, jsonLeadIdValue_N);
        Response res1_N = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_N.jsonString()).when().post("file_management/graphql").then().statusCode(200).extract().response();
        System.out.println("Bank Issue No Objection certificate Document Captured");

    }

    @And("^User is completing the post inspection pricing$")
    public void user_is_completing_the_post_inspection_pricing() throws IOException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the taskId -
        DocumentContext jsonTaskId_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/fetchTaskId.json"));
        String jsonTaskIdKey_A = "variables.leadId";
        String jsonTaskIdValue_A = String.valueOf(created_leadId);
        jsonTaskId_A.set(jsonTaskIdKey_A, jsonTaskIdValue_A);
        Response res1_A = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTaskId_A.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        taskID_PostInspectionPricing = res1_A.jsonPath().get("data.getAllTasksForLead[0].pk").toString();
        System.out.println("task id is for Post Inspection pricing = " + taskID_PostInspectionPricing);

        // 1. hitting the applicableForProcurementCallAction API
        DocumentContext jsonTask1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/applicableForProcurmentCallAction.json"));
        String jsonLeadPk1 = "variables.leadPk";
        String jsonLeadPkValue1 = String.valueOf(created_leadId);
        String JsonTaskId1 = "variables.taskId";
        String JsonTaskIdValue1 = String.valueOf(taskID_PostInspectionPricing);
        jsonTask1.set(jsonLeadPk1, jsonLeadPkValue1);
        jsonTask1.set(JsonTaskId1, JsonTaskIdValue1);
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask1.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        //res1.prettyPrint();
        Assert.assertEquals("true", res1.jsonPath().get("data.updateTaskAndScheduleNextTasks.ok").toString());
        System.out.println("Post Inspection pricing - applicableForProcurmentCallAction is Executed Successfully");


        // 2. hitting the addComment API
        DocumentContext jsonTask2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/addComment.json"));
        String jsonLeadPk2 = "variables.leadPk";
        String jsonLeadPkValue2 = String.valueOf(created_leadId);
        String JsonTaskId2 = "variables.taskId";
        String JsonTaskIdValue2 = String.valueOf(taskID_PostInspectionPricing);
        jsonTask2.set(jsonLeadPk2, jsonLeadPkValue2);
        jsonTask2.set(JsonTaskId2, JsonTaskIdValue2);
        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Post Inspection pricing - addComment is Executed Successfully");
    }

    @And("^User is completing the post inspection Negotiation$")
    public void user_is_completing_the_post_inspection_negotiation() throws IOException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        // 1. Complete the Hypothecation
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/Hypothecation.json"));
        String jsonLeadId_1 = "variables.leadId";
        String jsonLeadIdValue_1 = String.valueOf(created_leadId);
        jsonTask_1.set(jsonLeadId_1, jsonLeadIdValue_1);
        Response res_1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_1.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Hypothecation Captured");

        //Fetch the taskId -
        DocumentContext jsonTaskId_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/fetchTaskId.json"));
        String jsonTaskIdKey_A = "variables.leadId";
        String jsonTaskIdValue_A = String.valueOf(created_leadId);
        jsonTaskId_A.set(jsonTaskIdKey_A, jsonTaskIdValue_A);
        Response res1_A = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTaskId_A.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        taskId_postInspectionNegotiation = res1_A.jsonPath().get("data.getAllTasksForLead[0].pk").toString();
        //System.out.println("task id is for Post Inspection Negotiation = " + taskId_postInspectionNegotiation);

        //2. Hitting the API - addProcurementDetails
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/addProcurementDetails.json"));
        String jsonLeadPk_2 = "variables.leadPk";
        String jsonLeadPkValue_2 = String.valueOf(created_leadId);
        String jsonLeadId_2 = "variables.leadId";
        String jsonLeadIdValue_2 = String.valueOf(created_leadId);
        String jsontaskId_2 = "variables.taskId";
        String jsontaskIdValue_2 = String.valueOf(taskId_postInspectionNegotiation);
        jsonTask_2.set(jsonLeadPk_2, jsonLeadPkValue_2);
        jsonTask_2.set(jsonLeadId_2, jsonLeadIdValue_2);
        jsonTask_2.set(jsontaskId_2, jsontaskIdValue_2);
        Response res_2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask_2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        //System.out.println("post inspection Negotiation Completed");
    }

    @And("^Complete the Schedule Procurement$")
    public void complete_the_schedule_procurement() throws IOException, NoSuchFieldException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the taskId -
        DocumentContext jsonTaskId_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/fetchTaskId.json"));
        String jsonTaskIdKey_A = "variables.leadId";
        String jsonTaskIdValue_A = String.valueOf(created_leadId);
        jsonTaskId_A.set(jsonTaskIdKey_A, jsonTaskIdValue_A);
        Response res1_A = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTaskId_A.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        taskId_scheduleProcurement = res1_A.jsonPath().get("data.getAllTasksForLead[0].pk").toString();
        //System.out.println("task id is for schedule Procurement = " + taskId_scheduleProcurement);

        //2 Hitting the mutateScheduleInspectionData API
        Object[] newSlots = SlotFindingForFutureDate.SlotFindingForFutureDate_35DaysAdded();
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/mutateScheduleInspectionData.json"));
        String jsonLeadPk_1 = "variables.leadPk";
        String jsonLeadPkValue_1 = String.valueOf(created_leadId);
        String jsonLeadId_1 = "variables.leadId";
        String jsonLeadIdValue_1 = String.valueOf(created_leadId);
        String jsonStartTime_1 = "variables.scheduledStartTime";
        String jsonStartTimeValueTemp = (String) newSlots[1];
        String jsonStartTimeValue_1 = jsonStartTimeValueTemp + "+05:30";
        jsonTask_1.set(jsonLeadPk_1, jsonLeadPkValue_1);
        jsonTask_1.set(jsonLeadId_1, jsonLeadIdValue_1);
        jsonTask_1.set(jsonStartTime_1, jsonStartTimeValue_1);
        Response res1_B = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_1.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Schedule Procurement is completed - mutateScheduleInspectionData");

        // 3. Hit the updateTaskAndScheduleNextTasks - need to complete this
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/updateTaskAndScheduleNextTasks.json"));
        String jsonTaskId_A1 = "variables.taskId";
        String jsonTaskIdValue_A1 = String.valueOf(taskId_scheduleProcurement);
        jsonTask_2.set(jsonTaskId_A1, jsonTaskIdValue_A1);
        Response res1_C = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_2.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Schedule Procurement is completed - updateTaskAndScheduleNextTasks");
    }

    @And("^Complete the Confirm Procurement Scheduled$")
    public void complete_the_confirm_procurement_scheduled() throws IOException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the taskId -
        DocumentContext jsonTaskId_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/fetchTaskId.json"));
        String jsonTaskIdKey_A = "variables.leadId";
        String jsonTaskIdValue_A = String.valueOf(created_leadId);
        jsonTaskId_A.set(jsonTaskIdKey_A, jsonTaskIdValue_A);
        Response res1_A = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTaskId_A.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        taskId_confirmProcurementScheduled = res1_A.jsonPath().get("data.getAllTasksForLead[0].pk").toString();
        System.out.println("task id is for schedule Procurement = " + taskId_confirmProcurementScheduled);

        // 2. Hit the mutateUpdateTask api
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/mutateUpdateTask.json"));
        String jsontaskId_2 = "variables.taskId";
        String jsontaskIdValue_2 = String.valueOf(taskId_confirmProcurementScheduled);
        jsonTask_2.set(jsontaskId_2, jsontaskIdValue_2);
        Response res_2 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Confirm Procurement Scheduled Completed");
    }

    @And("^Assign this inspection a inspector testUser$")
    public void assign_this_inspection_a_inspector_testuser() throws Throwable {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //Fetch the inspection id for I2 -
        DocumentContext jsonTask = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoad/getLeadInspections.json"));
        String jsonLeadPk1 = "variables.leadPk";
        String jsonLeadPkValue1 = String.valueOf(created_leadId);
        String JsonLeadId1 = "variables.leadId";
        String JsonLeadIdValue1 = String.valueOf(created_leadId);
        jsonTask.set(jsonLeadPk1, jsonLeadPkValue1);
        jsonTask.set(JsonLeadId1, JsonLeadIdValue1);
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();

        int i = 0;
        boolean flag = true;
        while (flag) {
            if (res1.jsonPath().get("data.leadInspections[" + i + "].inspection.taskType").toString().equals("procurement")) {
                inspectionIdForI2 = res1.jsonPath().get("data.leadInspections[" + i + "].inspection.pk").toString();
                flag = false;
            }
            i++;
        }
        System.out.println("Inspection id for I2 = " + inspectionIdForI2);


        // Assign the Inspector - test.user
        DocumentContext jsonTask2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/updateInspectionAssignee.json"));
        String jsonInspectionId = "variables.inspectionId";
        String jsonInspectionIdValue1 = String.valueOf(inspectionIdForI2);
        String jsonInspectorId = "variables.inspectorId";
        String jsonInspectorIdValue1 = String.valueOf(inspectorId);
        jsonTask2.set(jsonInspectionId, jsonInspectionIdValue1);
        jsonTask2.set(jsonInspectorId, jsonInspectorIdValue1);
        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(jsonTask2.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        //res2.prettyPrint();
        System.out.println("Inspector Assigned");
    }

    @And("^User is completing the I2 Inspection$")
    public void user_is_completing_the_i2_inspection() throws IOException {
        baseURI = "https://spntest.myspinny.com/api";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        /*
        //1.  Hit the upload report API - /inspections/{id}/upload_report/
        String endPoint1 = "/inspections/" + inspectionIdForI2 + "/upload_report/";
        Response res1 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(new File("PayLoadForI2/uploadReport.json")).when().put(endPoint1).then().statusCode(200).extract().response();
        */


        //2.  Hit the finish API - inspections/{id}/finish/
        String endPoint2 = "/inspections/" + inspectionIdForI2 + "/finish/";
        DocumentContext Jt1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/finish.json"));
        String date = CurrentDate.currentDate();
        String jsonStartTime = "start_time";
        String jsonStartTimeValue = date + "T21:42:16.665+05:30";
        String jsonEndTime = "finish_time";
        String jsonEndTimeValue = date + "T21:52:51.539+05:30";

        Jt1.set(jsonStartTime, jsonStartTimeValue);
        Jt1.set(jsonEndTime, jsonEndTimeValue);
        Response res2 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt1.jsonString()).when().put(endPoint2).then().statusCode(200).extract().response();

        // //3. Hit the Upload API - /upload/
        // String endPoint3 = "/upload/";
        // Response res3 = given().cookies(cookie).contentType("application/json").accept("application/json").body().when().put(endPoint3).then().statusCode(200).extract().response();

        //4. Hit the profile API - /inspections/{id}/profile/
        String endPoint4 = "/inspections/" + inspectionIdForI2 + "/profile/";
        DocumentContext Jt2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/profile.json"));
        String jsonRegNo = "registration_no";
        String jsonRegNoValue = registration_id;
        Jt2.set(jsonRegNo, jsonRegNoValue);
        Response res3 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt2.jsonString()).when().put(endPoint4).then().statusCode(200).extract().response();

        //5. Hit the Quality Check API - /inspection/{id}/quality_check/
        String endPoint5 = "/inspections/" + inspectionIdForI2 + "/quality_check/";
        DocumentContext Jt3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/qualityCheck.json"));
        Response res4 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt3.jsonString()).when().post(endPoint5).then().statusCode(200).extract().response();


        //6. Hit the Tyre API - /inspections/{id}/tyres/
        String endPoint6 = "/inspections/" + inspectionIdForI2 + "/tyres/";
        DocumentContext Jt4 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/tyre.json"));
        Response res5 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt4.jsonString()).when().post(endPoint6).then().statusCode(200).extract().response();


        //7. Hit the Approval API -  inspections/{id}/approvals/
        String endPoint7 = "/inspections/" + inspectionIdForI2 + "/approvals/";
        DocumentContext Jt5 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/approvals.json"));
        Response res6 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt5.jsonString()).when().post(endPoint7).then().statusCode(200).extract().response();

        /*
        //8. Hit the photos API - /inspections/{id}/photos/
        String endPoint8 = "/inspections/" + inspectionIdForI2 + "/photos/";
        DocumentContext Jt6 = JsonPath.using(configuration).parse(new File("PayLoadForI2/photos.json"));
        Response res7 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt6.jsonString()).when().post(endPoint8).then().statusCode(200).extract().response();
        */

        //9. Hit the photos API - /inspections/{id}/status/
        String endPoint9 = "/inspections/" + inspectionIdForI2 + "/status/";
        DocumentContext Jt7 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForI2/status.json"));
        Response res8 = given().header("Keycloak-Authorization", accessToken).contentType("application/json").accept("application/json").body(Jt7.jsonString()).when().post(endPoint9).then().statusCode(200).extract().response();
        System.out.println("Inspection I2 is completed");

    }

    @And("^User is completing the Form 28 Form 29 Form 30 under Seller RTO$")
    public void user_is_completing_the_form_28_form_29_form_30_under_seller_rto() throws IOException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        // 1. Uploading form 28
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayloadForSellerRTO/addFile-form28.json"));
        String jsonTaskContextId = "variables.contextId";
        String jsonTaskContextId_Value = String.valueOf(created_leadId);
        jsonTask_1.set(jsonTaskContextId, jsonTaskContextId_Value);
        Response res_1 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("multipart/form-data; boundary=------WebKitFormBoundaryBZNBu4xOtR5B4qqs")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"form-28\"}],\"contextId\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png").body(jsonTask_1.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Form-28 is uploaded");


        //2. Uploading form 29
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayloadForSellerRTO/addFile-form29.json"));
        String jsonTaskContextId_2 = "variables.contextId";
        String jsonTaskContextId_Value2 = String.valueOf(created_leadId);
        jsonTask_2.set(jsonTaskContextId_2, jsonTaskContextId_Value2);
        Response res_2 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("multipart/form-data; boundary=------WebKitFormBoundaryBZNBu4xOtR5B4qqs")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"form-29\"}],\"contextId\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png").body(jsonTask_2.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Form-29 is uploaded");


        //3. Uploading form 30
        DocumentContext jsonTask_3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/PayloadForSellerRTO/addFile-form30.json"));
        String jsonTaskContextId_3 = "variables.contextId";
        String jsonTaskContextId_Value3 = String.valueOf(created_leadId);
        jsonTask_3.set(jsonTaskContextId_3, jsonTaskContextId_Value3);
        Response res_3 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("multipart/form-data; boundary=------WebKitFormBoundaryBZNBu4xOtR5B4qqs")
                .multiPart("operations", "{\"operationName\":\"addFile\",\"variables\":{\"contextType\":\"lead\",\"files\":[{\"file\":null,\"label\":\"form-30-front-side\"}],\"contextId\":\"" + created_leadId + "\"},\"query\":\"mutation addFile($files: [FileInput], $contextType: String, $contextId: Int) {\\n  addFile(\\n    fileToUploadAndVerify: {files: $files, contextType: $contextType, contextId: $contextId}\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.files.0.file\"]}")
                .multiPart("1", new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png").body(jsonTask_3.jsonString()).when().post("file_management/graphql")
                .then().statusCode(200).extract().response();
        System.out.println("Form-30 is uploaded");
    }

    @And("^User is marking the hypothecation as NO$")
    public void user_is_marking_the_hypothecation_as_no() throws Throwable {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/Hypothecation_1.json"));
        String jsonTaskId = "variables.leadId";
        String jsonTaskIdValue = String.valueOf(created_leadId);
        jsonTask_1.set(jsonTaskId, jsonTaskIdValue);
        Response res_1 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_1.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Hypothecation is marked as NO");
    }

    @And("^User Enter the Partial Payment$")
    public void user_enter_the_partial_payment() throws IOException {
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //1. hitting the api for addPaymentBreakdownForLead
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/addPaymentBreakdownForLead.json"));
        String jsonTaskIdKey_A = "variables.leadId";
        String jsonTaskIdValue_A = String.valueOf(created_leadId);
        jsonTask_1.set(jsonTaskIdKey_A, jsonTaskIdValue_A);
        Response res_1 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_1.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        String dataHolder = res_1.jsonPath().get("data.addPaymentBreakdownForLead.data").toString();
        String newDataHolder = "[" + dataHolder + "]";
        JSONArray new_array = new JSONArray(newDataHolder);
        for (int i = 0; i < new_array.length(); i++) {
            JSONObject object = new_array.getJSONObject(i);
            paymentBreakdownId = object.getInt("id");
            System.out.println("payment break down id is == " + paymentBreakdownId);
        }
        System.out.println("addPaymentBreakDownForLead is created");

        //2. Adding Trading Partner
        DocumentContext jsonTask_A = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/addProcurementDetails.json"));
        String jsonLeadPk_1 = "variables.leadPk";
        String jsonLeadPkValue_1 = String.valueOf(created_leadId);
        jsonTask_A.set(jsonLeadPk_1, jsonLeadPkValue_1);
        Response res1 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_A.jsonString()).when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("Trading Partner Selected");


        //3. hitting the api for addPaymentRequestForLead -- getting failed over here
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/addPaymentRequestForLead.json"));
        String jsonTask_paymentBreakdownId = "variables.paymentBreakdownId";
        String jsonTask_paymentBreakdownIdValue = String.valueOf(paymentBreakdownId);
        jsonTask_2.set(jsonTask_paymentBreakdownId, jsonTask_paymentBreakdownIdValue);
        Response res_2 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_2.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        String dataHolder_2 = res_2.jsonPath().get("data.addPaymentRequestForLead.data").toString();
        String newDataHolder_2 = "[" + dataHolder_2 + "]";

        JSONArray new_array2 = new JSONArray(newDataHolder_2);
        for (int i = 0; i < new_array2.length(); i++) {
            JSONObject object = new_array2.getJSONObject(i);
            paymentRequestId = object.getInt("id");
        }
        System.out.println("addPaymentRequestForLead is created");

        //4. hitting the api for update - updatePaymentRequestForLead
        DocumentContext jsonTask_3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/updatePaymentRequestForLead.json"));
        String jsonTask_paymentRequestId = "variables.paymentRequestId";
        String jsonTask_paymentRequestIdValue = String.valueOf(paymentRequestId);
        String jsonTask_SellLeadId = "variables.sellLeadId";
        String jsonTask_SellLeadIdValue = String.valueOf(created_leadId);
        jsonTask_3.set(jsonTask_paymentRequestId, jsonTask_paymentRequestIdValue);
        jsonTask_3.set(jsonTask_SellLeadId, jsonTask_SellLeadIdValue);
        Response res_3 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_3.jsonString()).
                when().post("graphql/").then().statusCode(200).extract().response();
        System.out.println("updatePaymentRequestForLead is created");
    }

    @And("^User completed the updatePaymentRequest on finance side$")
    public void user_completed_the_updatepaymentrequest_on_finance_side() throws IOException {
        baseURI = "https://payment-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

        //1. hitting the api for updatePaymentRequest
        DocumentContext jsonTask_1 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/updatePaymentRequest.json"));
        String jsonTask_pk = "variables.pk";
        String jsonTask_pkValue = String.valueOf(paymentRequestId);
        String jsonTask_transactionDateAndTime = "variables.transactionDateTime";
        String date = CurrentDate.currentDate();
        String jsonTask_transactionDateAndTimeValue = date + "T20:35:46+05:30";
        String jsonTask_TransactionNumber = "variables.transactionNumber";
        String jsonTask_TransactionNumberValue = Generator.transaction_id();
        jsonTask_1.set(jsonTask_pk, jsonTask_pkValue);
        jsonTask_1.set(jsonTask_transactionDateAndTime, jsonTask_transactionDateAndTimeValue);
        jsonTask_1.set(jsonTask_TransactionNumber, jsonTask_TransactionNumberValue);
        Response res_1 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("application/json").accept("application/json").body(jsonTask_1.jsonString()).
                when().post("sp_payments/").then().statusCode(200).extract().response();
        System.out.println("updatePaymentRequest is completed from finance side");
    }

    @And("^Now Push the car to Upcoming Section in Refurb$")
    public void now_push_the_car_to_upcoming_section_in_refurb() throws IOException {
        //created_leadId = 150322;
        baseURI = "https://supply-backend-test.spinnyworks.in/api/";
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();


        //2. Mark Stock In - addSupplyToRefurbMovement
        DocumentContext jsonTask_2 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/addSupplyToRefurbMovement.json"));
        String jsonLeadPk_2 = "variables.leadPk";
        String jsonLeadPkValue_2 = String.valueOf(created_leadId);
        String jsonLeadId_2 = "variables.leadId";
        String jsonLeadIdValue_2 = String.valueOf(created_leadId);
        String date = FutureDate.FutureDate_1DaysAdded();
        String jsonStockInDate_2 = "variables.stockInDate";
        String jsonStockInDateValue_2 = date + "T20:35:46+05:30";
        String jsonExpectedRefurbDate_2 = "variables.expectedRefurbDate";
        String jsonExpectedRefurbDateValue_2 = date + "T15:35:46+05:30";
        jsonTask_2.set(jsonLeadPk_2, jsonLeadPkValue_2);
        jsonTask_2.set(jsonLeadId_2, jsonLeadIdValue_2);
        jsonTask_2.set(jsonStockInDate_2, jsonStockInDateValue_2);
        jsonTask_2.set(jsonExpectedRefurbDate_2, jsonExpectedRefurbDateValue_2);
        Response res2 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("multipart/form-data; boundary=------WebKitFormBoundaryBZNBu4xOtR5B4qqs")
                .multiPart("operations", "{\"operationName\":\"addSupplyToRefurbMovement\",\"variables\":{\"leadPk\":\" " + created_leadId + "\",\"leadId\":\"" + created_leadId + "\",\"stockInDate\":\"" + jsonStockInDateValue_2 + "\",\"expectedRefurbLocationId\":\"1\",\"expectedRefurbDate\":\"" + jsonExpectedRefurbDateValue_2 + "\",\"tradingPartner\":\"[object Object]\",\"stockInOdometerPhoto\":null,\"stockInOdometerReading\":\"12344\",\"stockInLocation\":\"Test Address\"},\"query\":\"mutation addSupplyToRefurbMovement($leadId: Int!, $stockInDate: DateTime, $expectedRefurbLocationId: Int, $expectedRefurbDate: DateTime, $commentId: Int, $stockInOdometerReading: Int, $stockInOdometerPhoto: Upload, $stockInLocation: String) {\\n  addSupplyToRefurbMovement(\\n    commentId: $commentId\\n    leadId: $leadId\\n    stockInDate: $stockInDate\\n    expectedRefurbLocationId: $expectedRefurbLocationId\\n    expectedRefurbDate: $expectedRefurbDate\\n    stockInOdometerPhoto: $stockInOdometerPhoto\\n    stockInOdometerReading: $stockInOdometerReading\\n    stockInLocation: $stockInLocation\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.stockInOdometerPhoto\"]}")
                .multiPart("1", new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png").body(jsonTask_2.jsonString()).when().post("graphql/")
                .then().statusCode(200).extract().response();
        //res2.prettyPrint();
        System.out.println("Mark Stock-In is completed");

        //3. Delivery to Refurb - updateSupplyToRefurbMovement
        DocumentContext jsonTask_3 = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayLoadForUpcomingCarsInRefurb/updateSupplyToRefurbMovement.json"));
        String jsonLeadId_3 = "variables.leadId";
        String jsonLeadIdValue_3 = String.valueOf(created_leadId);
        String date_3 = FutureDate.FutureDate_1DaysAdded();
        String jsonRefurbAcceptedAt_3 = "variables.refurbAcceptedAt";
        String jsonRefurbAcceptedAtValue_3 = date_3 + "T20:35:46+05:30";
        jsonTask_3.set(jsonLeadId_3, jsonLeadIdValue_3);
        jsonTask_3.set(jsonRefurbAcceptedAt_3, jsonRefurbAcceptedAtValue_3);
        Response res3 = given().header("Keycloak-Authorization", accessToken).header("client", "supply").contentType("multipart/form-data; boundary=------WebKitFormBoundaryjpCt63APXcdetjpD")
                .multiPart("operations", "{\"operationName\":\"updateSupplyToRefurbMovement\",\"variables\":{\"leadId\":\"" + created_leadId + "\",\"refurbAcceptedAt\":\"" + jsonRefurbAcceptedAtValue_3 + "\",\"refurbLocationId\":\"1\",\"deliveryOdometerReading\":\"12345\",\"carPhoto\":null,\"deliveryOdometerPhoto\":null},\"query\":\"mutation updateSupplyToRefurbMovement($leadId: Int!, $refurbAcceptedAt: DateTime, $refurbLocationId: Int!, $carPhoto: Upload, $deliveryOdometerPhoto: Upload, $deliveryOdometerReading: Int!) {\\n  updateSupplyToRefurbMovement(\\n    leadId: $leadId\\n    refurbAcceptedAt: $refurbAcceptedAt\\n    refurbLocationId: $refurbLocationId\\n    deliveryOdometerReading: $deliveryOdometerReading\\n    deliveryOdometerPhoto: $deliveryOdometerPhoto\\n    carPhoto: $carPhoto\\n  ) {\\n    ok\\n    message\\n    __typename\\n  }\\n}\\n\"}")
                .multiPart("map", "{\"1\":[\"variables.carPhoto\"],\"2\":[\"variables.deliveryOdometerPhoto\"]}")
                .multiPart("1", new File("PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png")
                .multiPart("2", new File("PayLoadForUpcomingCarsInRefurb/testImage.png"), "image/png")
                .body(jsonTask_3.jsonString()).when().post("graphql/")
                .then().statusCode(200).extract().response();
        System.out.println("Delivery to Refurb Completed");
    }


    // to By-pass Refurb Complete the Inspection and reject the joblist, complete the refurb
}