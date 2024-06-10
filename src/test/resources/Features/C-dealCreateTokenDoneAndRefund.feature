 @crm @dev @dcf @hub
Feature: Adding buyer and Requesting Deal and CancelDeal
   
    @Luxury 
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "luxury" is Initiated
    When Lead Created Successfully
    Given Fetch recommended cars from preferences
    And get a car from recommended cars
    #When cancel the deals on the sell lead
   	Then Car added to interest
   	Given Adding a Buyer for the deal
    When Getting Buyer Details
    And Adding Address of the Buyer
    Then Requesting Deal
    And Making token Payment
    Then canceling request for requested deal
    And Approving cancellation request
    When Adding bank details
    And Getting bank buyer details
    Then Initiating refund

  @Bcm
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "bcm" is Initiated
    When Lead Created Successfully
    Given Fetch recommended cars from preferences
    And get a car from recommended cars
    #When cancel the deals on the sell lead
   	Then Car added to interest
   	Given Adding a Buyer for the deal
    When Getting Buyer Details
    And Adding Address of the Buyer
    Then Requesting Deal
    And Making token Payment
    Then canceling request for requested deal
    And Approving cancellation request
    When Adding bank details
    And Getting bank buyer details
    Then Initiating refund
    

  @Assured
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "assured" is Initiated
    When Lead Created Successfully
    Then Contact number unmasked
    Given Fetch recommended cars from preferences
    And get a car from recommended cars
    #When cancel the deals on the sell lead
   	Then Car added to interest
   	Given Adding a Buyer for the deal
    When Getting Buyer Details
    And Adding Address of the Buyer
    Then Requesting Deal
    And Making token Payment
    Then canceling request for requested deal
    And Approving cancellation request
    When Adding bank details
    And Getting bank buyer details
    Then Initiating refund