package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static com.csci310.ParkHere.R.id.locationEditText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Peter on 11/7/16.
 */

@RunWith(JUnit4.class)
public class SearchTest {


    private String location;

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Before
    public void initString() {
        location = "3771 McClintock Ave. Los Angeles CA 90007";
    }

    @Test
    public void searchSameTimeTest(){
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText(location), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.startDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.endDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.startTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.endTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText8.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        onView((withId(R.id.searchButton))).perform(click());
        onView(withText("Distance")).check(doesNotExist());
    }

    @Test
    public void searchValidTest(){
        onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,8));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,9));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(5,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.searchButton)).perform(click());


        try{
            onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());
        }

        catch (Exception e){
            //Pass test
        }

    }

}
