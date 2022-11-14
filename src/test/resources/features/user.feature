@smoke
Feature: User Verification



  Scenario: verify information about logged user
    Given I logged Bookit api using "wcanadinea@ihg.com" and "waverleycanadine"
    When I get the current user information from api
    Then status code should be 200


  @wip
  Scenario: verify user information from api and database
    Given I logged Bookit api using "wcanadinea@ihg.com" and "waverleycanadine"
    When I get the current user information from api
    Then user information from api and database should match