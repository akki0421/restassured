@Assured @crm @dev @dcf @hub
  Feature: Validate Multiple source lead creation.
  Scenario Outline: Validating add Lead Functionality
  
    Given Login to the application 
    When Lead Creation for "<source>"
    And Validating Drop Lead Functionality
   
    Examples:
      |source|
      | 7526 |
      | 7587 |
      | 7525 |
      |  15  |
      | 2744 |
      | 9400 |
      |16947 |
      |16928 |
      |16950 |