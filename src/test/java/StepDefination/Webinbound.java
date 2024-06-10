package StepDefination;

import Files.payload;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import io.restassured.path.json.JsonPath;

import java.io.File;

import static com.jayway.jsonpath.JsonPath.using;

public class Webinbound {
    String context_id;
    String phoneNumber;
    Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();



    @Given("Create Webinbound Lead")
    public void createWebinboundLead() throws Throwable {
        if (createLead.phoneNumber == null) {
            phoneNumber = createLead.generatePhoneNumber();
            //System.out.println("Get phone number successfully");
        }

        if (createLead.accessToken == null) {
            createLead.enviroment_should_be_up_and_running();
            //System.out.println("Successfully get access token");
        }

        DocumentContext body = using(configuration).parse(new File("src/test/java/Files/WebInbound/createlead.json"));
        String mobilenumberKey = "contact_details.contact_number";
        body.set(mobilenumberKey, phoneNumber);
        JsonPath output = payload.post(body.jsonString(), "swi/api/enquiry/create_lead/");
        context_id = String.valueOf(output.getString("context_id"));
       // System.out.println("context id =" + context_id);
    }

    @And("Update Lead")
    public void updateLead() throws Throwable{

        DocumentContext body = using(configuration).parse(new File("src/test/java/Files/WebInbound/updatelead.json"));
        String jsoncontextidKey = "context_id";
        body.set(jsoncontextidKey, context_id);
        payload.post(body.jsonString(), "swi/api/enquiry/update_lead/");
        System.out.println("Lead is updated sucessfully");
    }


}
