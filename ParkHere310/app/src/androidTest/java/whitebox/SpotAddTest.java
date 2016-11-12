package whitebox;

/**
 * Created by yingchen on 11/6/2016.
 */
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csci310.ParkHere.AddActivity;
import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SpotAddTest {

    private String Address_text;
    private String city_text;
    private String state_text;
    private String zip_text;
    private String price_text;
    private String ealier_current_date;
    private String startDate_text;
    private String startTime_text;
    private String endDate_text;
    private String endTime_text;
    private String wrong_end;
    private String description_text;
    private String error;


    @Rule
    public ActivityTestRule<AddActivity> mActivityRule = new ActivityTestRule<>(
            AddActivity.class);
    public IntentsTestRule mIntentsRule = new IntentsTestRule<>(AddActivity.class);

    @Before
    public void initValidString() {
        Address_text = "1171 W30th st.";
        city_text="los angeles";
        state_text="CA";
        zip_text="90007";
        price_text="3.2";
        ealier_current_date="11-01-2016";
        startDate_text="11-13-2016";
        startTime_text="13:10PM";
        endDate_text="11-20-2016";
        wrong_end="11-10-2016";
        endTime_text="10:30AM";
        description_text="test description";
        error="Please make sure time difference is larger than 1 hour or the starting time is before the current time ";


    }


    @Test
    public void insufficientFields() {
        onView(withId(R.id.Address))
                .perform(typeText(Address_text), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        onView(withText("Please fill all the Text field")).check(matches(isDisplayed()));
    }


    @Test
    public void before_current() {
        onView(withId(R.id.Address))
                .perform(typeText(Address_text), closeSoftKeyboard());

        onView(withId(R.id.city))
                .perform(typeText(city_text), closeSoftKeyboard());

       // onView(withId(R.id.state)).perform(click());
      //  onData(allOf(is(instanceOf(String.class)), is(state_text))).perform(click());

        onView(withId(R.id.postcode))
                .perform(typeText(zip_text), closeSoftKeyboard());

        onView(withId(R.id.price))
                .perform(typeText(price_text), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText))
                .perform(replaceText(ealier_current_date), closeSoftKeyboard());

        onView(withId(R.id.endDateEditText))
                .perform(replaceText(endDate_text), closeSoftKeyboard());

        onView(withId(R.id.startTimeEditText))
                .perform(replaceText(startTime_text), closeSoftKeyboard());

        onView(withId(R.id.endTimeEditText))
                .perform(replaceText(endTime_text), closeSoftKeyboard());



        onView(withId(R.id.description))
                .perform(typeText(description_text), closeSoftKeyboard());

        //onView(withId(R.id.state)).check(matches(withSpinnerText(containsString(state_text))));



        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        onView(withText(error)).check(matches(isDisplayed()));

    }


    @Test
    public void end_before_start() {
        onView(withId(R.id.Address))
                .perform(typeText(Address_text), closeSoftKeyboard());

        onView(withId(R.id.city))
                .perform(typeText(city_text), closeSoftKeyboard());

        //onView(withId(R.id.state)).perform(scrollTo(),click());
        //onData(allOf(is(instanceOf(String.class)), is(state_text))).perform(click());

        onView(withId(R.id.postcode))
                .perform(typeText(zip_text), closeSoftKeyboard());

        onView(withId(R.id.price))
                .perform(typeText(price_text), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText))
                .perform(replaceText(ealier_current_date), closeSoftKeyboard());

        onView(withId(R.id.endDateEditText))
                .perform(replaceText(wrong_end), closeSoftKeyboard());

        onView(withId(R.id.startTimeEditText))
                .perform(replaceText(startTime_text), closeSoftKeyboard());

        onView(withId(R.id.endTimeEditText))
                .perform(replaceText(endTime_text), closeSoftKeyboard());

      /*  onView(withId(R.id.mySpinner1))
                .perform(scrollTo(),click());

        onData(allOf(is(instanceOf(String.class)), is("handicap"))).perform(click());

        onData(allOf(is(instanceOf(String.class)), is("compact"))).perform(click());
*/



        onView(withId(R.id.description))
                .perform(typeText(description_text), closeSoftKeyboard());

       // onView(withId(R.id.state)).check(matches(withSpinnerText(containsString(state_text))));



        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        onView(withText("Please make sure time difference is larger than 1 hour or the starting time is before the current time ")).inRoot(isDialog()).check(matches(isDisplayed()));

    }


}