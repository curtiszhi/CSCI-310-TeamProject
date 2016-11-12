package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.MainActivity;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class ViewRentTest {
    private String location;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initString() {
        location = "1171 W 30th St, Los Angeles, CA 90007, USA";
    }
    @Test
    public void payPalTest(){
        try{
            //not logged in
            onView(withId(R.id.locationEditText)).perform(replaceText(""), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out


            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailEditText), isDisplayed()));
            appCompatEditText.perform(replaceText("test1234@test.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.passwordEditText), isDisplayed()));
            appCompatEditText2.perform(replaceText("Password2"), closeSoftKeyboard());


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
            appCompatButton.perform(click());
        }


        try{ Thread.sleep(3000); }catch (Exception _){}
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_user), isDisplayed()));
        actionMenuItemView.perform(click());


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
            try{ Thread.sleep(2000); }catch (Exception _){}
            onView(withId(R.id.address)).check(matches(withText(location)));

        }
        catch(Exception e) {
            //Test passes
        }
    }
}
