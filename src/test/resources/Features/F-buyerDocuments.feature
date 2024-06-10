@Assured @Luxury @Bcm @crm @dev @dcf @hub
Feature: Updating and Verifying Buyer Docs

@Reject
Scenario: Update and Reject Buyer Docs
    Given Upload and "Reject" Adhaar Back 
    When Upload and "Reject" pre delivery docs
    When Upload and "Reject" Adhaar Front 
 		And Upload and "Reject" PAN
    Then Setting Same state RTO to TRUE



@Delete
Scenario: Upload and Delete Buyer Docs
    Given Upload and "Delete" Adhaar Back 
    When Upload and "Delete" pre delivery docs
    When Upload and "Delete" Adhaar Front 
 		And Upload and "Delete" PAN
 		
    Then Setting Same state RTO to TRUE



@Verify @Assured
  Scenario: Update and Verify Buyer Docs
    Given Upload and "Verify" Adhaar Back
    When Upload and "Verify" Adhaar Front 
    When Upload and "Verify" pre delivery docs 
 		And Upload and "Verify" PAN
 		
    Then Setting Same state RTO to TRUE
   




    