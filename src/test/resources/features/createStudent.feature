Feature: Create student


  Scenario: Create a student teacher and verify status code is 201
    Given I logged Bookit api using "blyst6@si.edu" and "barbabaslyst"
    When I send POST request to "/api/students/student" endpoint with following information
      | first-name      | deniz               |
      | last-name       | demir               |
      | email           | hasan22@yahoo.com   |
      | password        | abc123              |
      | role            | student-team-leader |
      | campus-location | VA                  |
      | batch-number    | 8                   |
      | team-name       | Nukes               |
    Then status code should be 201
#    And I delete previously added student

#  HOMEWORK
#
#  // try to get name,role,batch number, campus, team name from api for one student
#  it will be multiple api request

  @ui
  Scenario: get name,role,batch number, campus, team name from api for one student
    Given I logged Bookit api using "hasan22@yahoo.com" and "abc123"
    When  I get the current users name,role,batch number, campus, team nam information from api
    And   I get the current user information name, role, team, batch and campus from UI
      | email    | hasan22@yahoo.com |
      | password | abc123            |


  @wip
  Scenario: test config
  Given I get env properties




#  //responses returns batch name team name with students information
#  //first make sure your student is inside the respones then get those info
#
#  prepare one list of information about student and compare with ui
#
#  //UI vs API
#
#
#  //verify same information vs
#  //connect db and get the same information which requires joins multiple tables
