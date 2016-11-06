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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.AddActivity;
import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SpotAddTest {

    private String Address_text;
    private String city_text;
    private String state_text;
    private String zip_text;
    private String price_text;
    private String startDate_text;
    private String startTime_text;
    private String endDate_text;
    private String endTime_text;
    private String description_text;


    @Rule
    public ActivityTestRule<AddActivity> mActivityRule = new ActivityTestRule<>(
            AddActivity.class);

    @Before
    public void initValidString() {
        Address_text = "1171 W30th st.";
        city_text="los angeles";
        state_text="CA";
        zip_text="90007";
        price_text="3.2";
        startDate_text="11-10-2016";
        startTime_text="13:10PM";
        endDate_text="11-20-2016";
        endTime_text="10:30AM";
        description_text="test description";



    }

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.Address))
                .perform(typeText(Address_text), closeSoftKeyboard());

        onView(withId(R.id.city))
                .perform(typeText(city_text), closeSoftKeyboard());

        onView(withId(R.id.state)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(state_text))).perform(click());

        onView(withId(R.id.postcode))
                .perform(typeText(zip_text), closeSoftKeyboard());

        onView(withId(R.id.price))
                .perform(typeText(price_text), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText))
                .perform(typeText(startDate_text), closeSoftKeyboard());

        onView(withId(R.id.endDateEditText))
                .perform(typeText(endDate_text), closeSoftKeyboard());

        onView(withId(R.id.startTimeEditText))
                .perform(typeText(startTime_text), closeSoftKeyboard());

        onView(withId(R.id.endTimeEditText))
                .perform(typeText(endTime_text), closeSoftKeyboard());

        onView(withId(R.id.mySpinner1))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("handicap"))).perform(click());

        onData(allOf(is(instanceOf(String.class)), is("compact"))).perform(click());









        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.state)).check(matches(withSpinnerText(containsString(state_text))));

        onView(withText("Wait!")).check(matches(isDisplayed()));

    }

    @Before
    public void stubCameraIntent() {
        Instrumentation.ActivityResult result = createImageCaptureActivityResultStub();

        // Stub the Intent.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }

    @Test
    public void takePhoto_drawableIsApplied() {

        // Click on the button that will trigger the stubbed intent.
        onView(withId(R.id.photo)).perform(click());

    }

    private Instrumentation.ActivityResult createImageCaptureActivityResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddActivity.KEY_IMAGE_DATA, BitmapFactory.decodeResource(
                mActivityRule.getActivity().getResources(), R.drawable.ic_launcher));

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }
}