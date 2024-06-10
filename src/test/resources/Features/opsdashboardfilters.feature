@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Validating filters for ops dashboard

  Scenario: Validating filter Functionality
    Given Creat acces token
    When Fetch sell-lead id for unlisted-cars
    And Fetch sell-lead id for unpublished-cars
    And Fetch sell-lead id for published-cars
    And Fetch sell-lead id for in-progress-cars
    Then Fetch sell-lead id for all-cars
