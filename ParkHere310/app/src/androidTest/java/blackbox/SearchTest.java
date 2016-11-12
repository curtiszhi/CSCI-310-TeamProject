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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.csci310.ParkHere.R.id.locationEditText;
import static org.hamcrest.Matchers.allOf;

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
        location = "1209 W 27TH ST Los Angeles CA 90007";
    }
    @Test
    public void searchValidTest(){
        onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,15));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.searchButton)).perform(click());

        int x = 0;
        while (x == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Distance"), isDisplayed()));
                appCompatTextView.perform(click());
                x = 1;
            }
            catch (Exception e){
                x = 0;
            }
        }


    }
    @Test
    public void searchSameTimeTest(){
        onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.searchButton)).perform(click());

        int x = 0;
        while (x == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Wait!"), isDisplayed()));
                //appCompatTextView.perform(click());
                x = 1;
            }
            catch (Exception e){
                x = 0;
            }
        }
    }

    @Test
    public void noAddressTest(){
        onView(withId(locationEditText)).perform(replaceText(""), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.searchButton)).perform(click());

        int x = 0;
        while (x == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Wait!"), isDisplayed()));
                //appCompatTextView.perform(click());
                x = 1;
            }
            catch (Exception e){
                x = 0;
            }
        }
    }


}
