@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Remove add Cars Functionality
  Scenario: Remove add Cars Functionality
    When Schedule "showroom" test drive
    And Cancelled visit for car
    And Pin added car
    Then Remove car from car interest
