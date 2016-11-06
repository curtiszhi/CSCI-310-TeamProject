package whitebox;

/**
 * Created by seanyuan on 11/6/16.
 */

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.DatePicker;
import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.ListingResultActivity;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;

import static android.app.Activity.RESULT_OK;
import static android.nfc.NfcAdapter.ACTION_NDEF_DISCOVERED;
import static android.nfc.NfcAdapter.EXTRA_NDEF_MESSAGES;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RentViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {

    }

    @Test
    public void insufficientFields() {
        /*onView(withId(R.id.emailEditText))
                .perform(typeText("seanyuan@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("Testing1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());*/
        onView(withId(R.id.locationEditText))
                .perform(typeText("1306 W. 29th Street, Los Angeles, CA 90007"), closeSoftKeyboard());
        onView(withId(R.id.startDateEditText))
                .perform(click());
        onView(withText("2016"))
                .perform(PickerActions.setDate(2016, 10  + 1, 23));
    }
}

