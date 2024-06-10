package Utility;

import StepDefination.Exchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.restassured.http.Cookies;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class SlotFindingForFutureDateforI2 {
    static String startDate;
    static String endDate;
    static JsonArray ListOfAvailableSlotsOnTheGivenDate;
    static Cookies cookie;

    public static Object[] SlotFindingForFutureDateForI2() throws IOException {
//        Process ps = new Process();
        String date = FutureDate.FutureDate_1DaysAdded();
        cookie = given().body(new File("src/test/java/Files/PayLoad/login.json")).when().post("https://supply-backend-test.spinny.com/api/login/").then().extract().response().getDetailedCookies();

        baseURI = "https://supply-backend-test.spinny.com/api/";

        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        DocumentContext json = JsonPath.using(configuration).parse(new File("src/test/java/Files/PayloadforPreI2/getSlotsForClusterV2.json"));
        String JsonStartDate = "variables.startTime";
        String JsonStartDateValue = date + "T08:00:00+05:30";
        String JsonEndDate = "variables.endTime";
        String JsonEndDateValue = date + "T21:00:00+05:30";
        String LeadId = "variables.leadId";
        String LeadIdValue = String.valueOf(Exchange.sell_lead);
        json.set(JsonStartDate, JsonStartDateValue);
        json.set(JsonEndDate, JsonEndDateValue);
        json.set(LeadId, LeadIdValue);

        Response res = given().cookies(cookie).contentType("application/json").body(json.jsonString()).when().post("graphql/").then().extract().response();
        //res.prettyPrint();
        Gson gson = new GsonBuilder().create();
        ListOfAvailableSlotsOnTheGivenDate = gson.toJsonTree(res.jsonPath().get("data.getSlotsForClusterV2.slots")).getAsJsonArray();

        List<Object> ls = new LinkedList<>();
        ls.add(res.jsonPath().get("data.getSlotsForClusterV2.slots"));
        List<Object> ls1 = (List<Object>) ls.get(0);
        Object[] availableSlots = new Object[3];
        System.out.println(ls1.size());
        for (int i = 0; i < ls1.size(); i++) {
            int count = Integer.parseInt(res.jsonPath().get("data.getSlotsForClusterV2.slots[" + i + "].availableSlots").toString());
            if (count > 0) {
                startDate = "";
                endDate = "";
                startDate = res.jsonPath().get("data.getSlotsForClusterV2.slots[" + i + "].startTime").toString();
                endDate = res.jsonPath().get("data.getSlotsForClusterV2.slots[" + i + "].endTime").toString();
                availableSlots[0] = startDate;
                availableSlots[1] = endDate;
                break;
            }
        }
        availableSlots[2] = ListOfAvailableSlotsOnTheGivenDate;
        return availableSlots;
    }
}
