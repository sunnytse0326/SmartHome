package com.smarthome

import android.support.test.rule.ActivityTestRule
import android.test.ActivityInstrumentationTestCase2
import com.smarthome.activity.HomeActivity
import com.smarthome.model.Page
import com.smarthome.testcase.HomePage
import cucumber.api.CucumberOptions
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule


@CucumberOptions(features = arrayOf("features"))
class HomePageTest : ActivityInstrumentationTestCase2<HomeActivity>(HomeActivity::class.java) {
    private var page: Page? = null

    @Rule
    var activityTestRule: ActivityTestRule<HomeActivity>? = ActivityTestRule(HomeActivity::class.java, false, false)


    @Before
    fun setup() {
        activityTestRule?.launchActivity(null)
        activity = activityTestRule?.getActivity()
    }

    @After
    public override fun tearDown() {
        activityTestRule?.finishActivity()
    }

    @Given("^I open the app$")
    fun i_open_the_app() {
        page = Page ()
        page?.startApp()
    }

    @Given("^I am on \"([^\"]*)\"$")
    fun i_am_on(arg1: String) {

    }

    @When("^I turn on a \"([^\"]*)\" switch$")
    fun i_turn_on_a_switch(arg1: String) {

    }

    @Then("^a hint message will be shown on screen")
    fun a_hint_message_will_be_shown_on_screen() {

    }
}