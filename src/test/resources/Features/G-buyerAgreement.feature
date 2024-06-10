@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Downloading and Uploading Buyer Agreement

  Scenario: Downloading and Uploading Buyer Agreement
    Given Download Buyer Agreement
    Then Upload and Verify Buyer Agreement

  Scenario: Updating Buyer Detail and Validating the new Agreement
    Given Delete Old Agreement
     And Edit Buyer Details with PAN "AKUPC2694J"
    Given Download Buyer Agreement
    Then Upload and Verify Buyer Agreement
    And Creating a New Buyer to Switch
    Then Setting Same state RTO to TRUE
    And Setting insurance Details
    
    
 # Scenario: Cancelling the current deal
 # 	When cancel the deals on the sell lead
