Feature: Create student

  @wip
  Scenario: Create a student teacher and verify status code is 201
    Given I logged Bookit api using "blyst6@si.edu" and "barbabaslyst"
    When I send POST request to "/api/students/student" endpoint with following information
      | first-name      | basak               |
      | last-name       | mark                |
      | email           | hasan16@yahoo.com    |
      | password        | abc123              |
      | role            | student-team-leader |
      | campus-location | VA                  |
      | batch-number    | 8                   |
      | team-name       | Nukes               |
    Then status code should be 201
    And I delete previously added student

