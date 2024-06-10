@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Create delivery visit

  Scenario: Create delivery visit
    Given To convert buylead Id into base
    And To convert dealId into base
    And Login to the Hub App
    When Visit Created is initiated
   	Then Visit Created Successfully