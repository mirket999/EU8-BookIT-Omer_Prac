@smoke
Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api using "wcanadinea@ihg.com" and "waverleycanadine"
    When I get the current user information from api
    Then status code should be 200


