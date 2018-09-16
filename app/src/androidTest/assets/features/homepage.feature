Feature: Edit Switch Fixture Feature
    As a user toggle the switch button, corresponding device fixture will be turned on/off

    Background:
        Given I open the app
        And I am on "Home Page"

    Scenario Outline: <Room> Switch Turning On and Off
        When I turn on a "<Room>" switch
        Then a hint message will be shown on screen

    Examples:
        | Room |
        | Bedroom |
        | Living  |