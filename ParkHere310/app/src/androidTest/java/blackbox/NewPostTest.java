package blackbox;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.csci310.ParkHere.AddActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by curtiszhi on 11/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class NewPostTest
{

    @Rule
    public ActivityTestRule<AddActivity> myActivityRule = new ActivityTestRule<>(AddActivity.class);

    @Before
    public void initString() {
//        email = "peter@gmail.com";
//        pass = "Password1";
    }


    @Test
    public void NewPostWithValidEntries()
    {
        //Enter the address:
        onView(withId(R.id.Address)).perform(typeText("1209 W 27TH ST"), closeSoftKeyboard());

        //Enter the city:
        onView(withId(R.id.city)).perform(typeText("Los Angeles"), closeSoftKeyboard());

        //Select the state:
        onView(withId(R.id.state)).check(matches(allOf(isEnabled(),isClickable()))).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isEnabled(); // no constraints, they are checked above
            }

            @Override
            public String getDescription() {
                return "click plus button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        });
        onData(allOf(is(instanceOf(String.class)), is("CA"))).perform(click());

        //Enter zip code:
        onView(withId(R.id.postcode)).perform(typeText("90007"), closeSoftKeyboard());

        //Enter the price:
        onView(withId(R.id.price)).perform(typeText("10"), closeSoftKeyboard());

        //Enter dates and times:
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,8));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,18));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(3,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(3,0));
        onView(withId(android.R.id.button1)).perform(click());

        //Enter description:
        onView(withId(R.id.description)).perform(typeText("New post test"), closeSoftKeyboard());

        //Select cancel policy:
        onView(withId(R.id.radio_norefund)).perform(scrollTo(), click());


        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        try{ Thread.sleep(1000); }catch (Exception _){}

        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
    }

    @Test
    public void NewPostWithEmptyEntries()
    {
        onView(withId(R.id.Address)).perform(typeText("1209 W 27TH ST"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(scrollTo(), click());
        onView(withText("Alert")).check(matches(isDisplayed()));
    }

    @Test
    public void NewPostWithInvalidTimes()
    {
        //Enter the address:
        onView(withId(R.id.Address)).perform(typeText("1209 W 27TH ST"), closeSoftKeyboard());

        //Enter the city:
        onView(withId(R.id.city)).perform(typeText("Los Angeles"), closeSoftKeyboard());

        //Select the state:
        onView(withId(R.id.state)).check(matches(allOf(isEnabled(),isClickable()))).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isEnabled(); // no constraints, they are checked above
            }

            @Override
            public String getDescription() {
                return "click plus button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        });
        onData(allOf(is(instanceOf(String.class)), is("CA"))).perform(click());

        //Enter zip code:
        onView(withId(R.id.postcode)).perform(typeText("90007"), closeSoftKeyboard());

        //Enter the price:
        onView(withId(R.id.price)).perform(typeText("10"), closeSoftKeyboard());

        //Enter dates and times:
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,28));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,18));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(3,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(3,0));
        onView(withId(android.R.id.button1)).perform(click());

        //Enter description:
        onView(withId(R.id.description)).perform(typeText("New post test"), closeSoftKeyboard());

        //Select cancel policy:
        onView(withId(R.id.radio_norefund)).perform(scrollTo(), click());


        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        try{ Thread.sleep(1000); }catch (Exception _){}

        onView(withText("Alert")).check(matches(isDisplayed()));
    }
}

