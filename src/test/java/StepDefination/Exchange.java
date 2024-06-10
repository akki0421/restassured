package StepDefination;


import Files.payload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;


public class Exchange {

    public static String sell_lead ;
    @Given("^Requested exchange lead$")
    public void requestedExchangeLead() {
        String body = "{\"id\":\""+ createLead.leadId +"\",\"contact_details\":{\"name\":\"Manisha\",\"email\":null,\"contact_number\":"+ createLead.phoneNumber +"},\"car_data\":{\"model\":{\"id\":375,\"display_name\":\"A3\"},\"make\":{\"id\":11,\"display_name\":\"Audi\"},\"make_year\":2014,\"variant\":{\"id\":5615,\"display_name\":\"35 TDI Attraction\"},\"expected_budget\":500000,\"mileage\":27,\"no_of_owners\":1}}";
        //System.out.println(body);
        JsonPath post = payload.post(body, "api/buy_leads/"+createLead.leadId+"/exchange_interest/sell_orders/");
        sell_lead = String.valueOf(post.getInt("sell_lead.id"));
        System.out.println(sell_lead);
    }

    @And("^Opt-in exchange lead$")
    public void optInExchangeLead() {
        String body = "{\"sell_lead_id\":"+sell_lead+",\"opt_in\":true,\"plan_of_buying\":\"immediate\",\"plan_of_selling\":\"immediate\"}";
       // System.out.println(body);
        payload.post(body, "api/buy_leads/"+createLead.leadId+"/exchange_interest/").prettyPrint();
    }

    @Then("^Apply exchange to the deal$")
    public void applyExchangeToTheDeal() {
        String body = "{\"sell_id\": "+sell_lead+", \"intent\": true, \"reason\": null}";
        //System.out.println(body);
        payload.post(body, "api/carpurchases/"+DealCreate.dealId+"/exchange_program/").prettyPrint();

    }

    @And("^Remove exchange request$")
    public void removeExchangeRequest() {
        String body = "{\"sell_id\": "+sell_lead+", \"intent\":false,\"reason\":\"Still thinking it over\"}";
        //System.out.println(body);
        payload.post(body, "api/buy_leads/"+createLead.leadId+"/exchange_interest/").prettyPrint();
        
    }

    @And("^Change car in exchange$")
    public void changeCarInExchange() {
        payload.get("api/buy_leads/322693/exchange_interest/sell_orders/");
        
    }

    @And("^Opt-out exchange lead$")
    public void optOutExchangeLead() {
        String body = "{\"sell_id\": "+sell_lead+", \"opt_in\":false,\"category\":\"sell\",\"comments\":\"qwerty\",\"reason\":\"Customer plans to sell it on his own\"}";
        //System.out.println(body);
        payload.post(body, "api/buy_leads/"+createLead.leadId+"/exchange_interest/").prettyPrint();
    }
}
