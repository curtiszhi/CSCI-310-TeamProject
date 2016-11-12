package whitebox;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchFieldTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void invalidDateTest() {
        try{
            //logged in
            onView(withId(R.id.locationEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //not logged in, then log in
            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailEditText), isDisplayed()));
            appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.passwordEditText), isDisplayed()));
            appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
            appCompatButton.perform(click());
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(ViewMatchers.withId(R.id.startTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("22:56PM"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.endTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("22:56PM"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.startDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("10-04-16"));


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.endDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("10-06-16"));


        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("1306 W. 29th Street Los Angeles CA 90007"), closeSoftKeyboard());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Filters"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.coverBox), withText("Covered Parking"),
                        withParent(allOf(withId(R.id.stepFourLayout),
                                withParent(withId(R.id.filters)))),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        onView(withText("Wait!")).check(matches(isDisplayed()));


    }

    @Test
    public void emptyAddressTest() {
        try{
            //logged in
            onView(withId(R.id.locationEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //not logged in, then log in
            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailEditText), isDisplayed()));
            appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.passwordEditText), isDisplayed()));
            appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
            appCompatButton.perform(click());
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.startTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("22:56"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.endTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("22:56"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.startDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("11-21-16"));


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.endDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("11-22-16"));


        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Filters"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.coverBox), withText("Covered Parking"),
                        withParent(allOf(withId(R.id.stepFourLayout),
                                withParent(withId(R.id.filters)))),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());
        onView(withText("Wait!")).check(matches(isDisplayed()));


    }
}
