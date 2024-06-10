@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Visits

  Scenario Outline: Creating Payment and Misc Visits
    Given Creating "<visit>" type visit

    Examples: 
      | visit   |
      | payment |
      | misc    |

    Scenario: Creating STD
    When Schedule "showroom" test drive

  Scenario: Creating HTD
    And Mobile Number Generated
    When Lead Creation for "assured" is Initiated
    When Schedule "home" test drive
    
    Scenario: Visit hotness for Hub Admin
  	When Visit hotness for Hub Admin