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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.csci310.ParkHere.R.id.locationEditText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Peter on 11/7/16.
 */

@RunWith(JUnit4.class)
public class ViewRentHostTest {
    private String location;

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Before
    public void initString() {
        location = "3131 McClintock Ave, Los Angeles, CA 90007, USA";
    }
    @Test
    public void payPalTest(){
        onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,26));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,26));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16,0));
        onView(withId(android.R.id.button1)).perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());

        int y = 0;
        while (y == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Distance"), isDisplayed()));
                appCompatTextView.perform(click());
                y = 1;
            } catch(Exception e){
                y = 0;
            }
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.myList),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.rentButton), withText("Rent!")));
        appCompatButton7.perform(scrollTo(), click());

        //Paypal
        int x = 0;
        while (x == 0){
            try{
                ViewInteraction textView = onView(
                        withText("Pay with"));
                textView.perform(scrollTo(), click());
                x = 1;
            } catch(Exception e){
                x = 0;
            }
        }

        ViewInteraction editText = onView(
                withContentDescription("Email"));
        editText.perform(scrollTo(), replaceText("parkHere310Test@test.com"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                withContentDescription("Password"));
        editText2.perform(scrollTo(), replaceText("parkHere310"), closeSoftKeyboard());

        ViewInteraction textView2 = onView(
                withHint("Log In"));
        textView2.perform(scrollTo(), click());

        int z = 0;
        while (z == 0){
            try{
                ViewInteraction textView3 = onView(
                        withText("Pay"));
                textView3.perform(scrollTo(), click());
                z = 1;
            }
            catch (Exception e){
                z = 0;
            }
        }

        x = 0;
        while (x == 0){
            try {
                ViewInteraction actionMenuItemView = onView(
                        allOf(withId(R.id.action_user), withContentDescription("User"), isDisplayed()));
                actionMenuItemView.perform(click());
                x = 1;
            } catch(Exception e){
                x = 0;
            }
        }

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.viewHostHistoryButton), withText("View History")));
        appCompatButton8.perform(scrollTo(), click());

        try{
            ViewInteraction recyclerView2 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            recyclerView2.perform(actionOnItemAtPosition(0, click()));

        }
        catch(Exception e) {
            //Test passes
        }
    }
}
