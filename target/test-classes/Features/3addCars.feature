@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Validating add Cars Functionality

  Scenario Outline: Validating add Cars Functionality
    Given Fetch recommended cars from preferences
   	And get a car from recommended cars
    And Search cars from inventory and save
    #When cancel the deals on the sell lead
   	Then Car added to interest
   	