@Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Making and Approving Payments

 Scenario: Making token payment
     Given Making token Payment


  Scenario Outline: Making and Approving Partial Payments
   
    And Making Partial Payment with payment type "<type>"
    And Open deal in payment
    And Approving the payment type "<type>"

    	Examples: 
     | type   |
     | NEFT   |
     | IMPS   |
     | RTGS   |
     | UPI    |
     | DD     |

  Scenario: Making and Approving full payment
    And Getting Balance Amount of the deal
    When Making Full Payment
    And Open deal in payment
    Then Approving full payment
