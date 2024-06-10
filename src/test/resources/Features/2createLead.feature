@crm @dev @dcf @hub
Feature: Validating add Lead Functionality


  @Luxury 
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "luxury" is Initiated
    When Lead Created Successfully
    

  @Bcm
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "bcm" is Initiated
    When Lead Created Successfully
    

  @Assured
  Scenario: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "assured" is Initiated
    When Lead Created Successfully
    Then Contact number unmasked
  @Regression
  Scenario Outline: Validating add Lead Functionality
    Given Login to the application
    And Mobile Number Generated
    When Lead Creation for "<category>" is Initiated
    When Lead Created Successfully
    
    Examples:
      | category |
      | assured  |
      | bcm      |
      | luxury   |
