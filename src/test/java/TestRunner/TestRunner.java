package TestRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
//import io.cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions
(
		features = {"src/test/resources/Features/2createLead.feature",
                  	//"src/test/resources/Features/multipleSourceLeadCreation.feature" ,
					"src/test/resources/Features/1defineEnv.feature",
					"src/test/resources/Features/B-Visits.feature",
					//"src/test/resources/Features/opsdashboardfilters.feature",
					//"src/test/resources/Features/LeadListingPageFilters.feature",
        			"src/test/resources/Features/3addCars.feature",
        			//"src/test/resources/Features/Y-Removecar.feature",
        			//"src/test/resources/Features/DealCreate.feature",
					//"src/test/resources/Features/DealLoan.feature",
        			//"src/test/resources/Features/DealPayment.feature",
					//"src/test/resources/Features/updatestatus.feature",
        			/*"src/test/resources/Features/E-dealDetails.feature",
        			"src/test/resources/Features/F-buyerDocuments.feature",
        			"src/test/resources/Features/G-buyerAgreement.feature",
					//"src/test/resources/Features/K-Camunda.feature",
					//"src/test/resources/Features/Z-dealCancellation.feature",
					//"src/test/resources/Features/consumerlogin.feature",
        			"src/test/resources/Features/Exchange.feature",
					"src/test/resources/Features/ExchangeProcess.feature",
					"src/test/resources/Features/I-DeliveryVisit.feature",
					"src/test/resources/Features/J-DeliveryDone.feature",
					"src/test/resources/Features/ZZ-ReturnCar.feature",
					"src/test/resources/Features/Webinbound.feature",
					"src/test/resources/Features/ZZZ-DealCreateAndCancel.feature",*/
					
		},


       glue =	{"StepDefination"},stepNotifications=true,
        plugin = {"pretty","html:target/cucumber-reports.html/",
        		"json:target/Cucumber.json","junit:target/cucumber.xml"},
        

        		  monochrome = true,
        	        publish = true,  
      
    tags = ("@Assured and @dev")
        
)

public class TestRunner{
	
}