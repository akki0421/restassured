@Assured @Bcm @Luxury @crm @dev @dcf @hub

Feature: Deal cancellation flow

 Scenario: Deal cancellation flow
    Given Raise cancellation request
    When Rejecting cancellation request
    Then Approving cancellation request
