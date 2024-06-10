@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Mark delivery done

  Scenario: Mark delivery done
    Given Generating the OTP
    And To convert hubvisitId into base
    And Get car mileage
    And Validating the Delivery
    When Final Hub Delivery
    Then Hub Delivery is done Successfully
