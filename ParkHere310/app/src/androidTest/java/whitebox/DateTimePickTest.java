package whitebox;

/**
 * Created by seanyuan on 11/7/16.
 */

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.csci310.ParkHere.AddActivity;
import com.csci310.ParkHere.R;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DateTimePickTest {

    @Rule
    public ActivityTestRule<AddActivity> mActivityRule = new ActivityTestRule<>(
            AddActivity.class);

    @Before
    public void initValidString() {

    }


    @Test
    public void datePickerTest() {
        pressBack();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,8));
        onView(withId(android.R.id.button1)).perform(click());

        //onView(withId(R.id.startDateEditText))
        //.check(matches(withText("11-08-2016")));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("11-08-2016 ")).check(matches(isDisplayed()));
    }

    @Test
    public void timePickerTest() {
        pressBack();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.startTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(5,0));
        onView(withId(android.R.id.button1)).perform(click());

        //onView(withId(R.id.startTimeEditText))
          //      .check(matches(withText("05:00AM")));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("05:00AM")).check(matches(isDisplayed()));
    }
}