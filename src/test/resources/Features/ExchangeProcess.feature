@ParallerTestingWithCucumber @Assured @Bcm @Luxury @crm @dev @dcf @hub
Feature: Automating the process for sell lead (supply), Creating I1, I2

  @I2
  Scenario: Creating I2
    Given User has logged in
#    And User has created one lead in supply
    When User has booked the Inspection Slot
    Then Assignee Test user inspector to this Inspection
    And User is completing I1 Inspection
    And User is completing the post inspection pricing
    And User is completing the post inspection Negotiation
    And Complete the Seller KYC
    And Complete the Schedule Procurement
    And Complete the Confirm Procurement Scheduled
    And Assign this inspection a inspector testUser
    And User is completing the I2 Inspection
    And User is completing the Form 28 Form 29 Form 30 under Seller RTO
    And User is marking the hypothecation as NO
    And User Enter the Partial Payment
    And User completed the updatePaymentRequest on finance side



