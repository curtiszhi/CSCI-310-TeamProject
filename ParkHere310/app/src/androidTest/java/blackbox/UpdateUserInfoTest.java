package blackbox;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Peter on 11/6/16.
 */

@RunWith(JUnit4.class)
public class UpdateUserInfoTest {


    private String name, phone, email;

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Before
    public void initString() {
        name = "Testing Name";
        phone = "1112223333";
        email = "shouldntchange@test.com";
    }

    @Test
    public void updateUser(){
        onView(withId(R.id.action_user)).perform(click());

        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());

        onView(withId(R.id.nameEditText)).perform(replaceText(name));
        onView(withId(R.id.phoneEditText)).perform(replaceText(phone));

        try{
            onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
        } catch (PerformException e){

        }

        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());

        onView((withId(R.id.nameEditText))).check(matches(withText(name)));
        onView((withId(R.id.phoneEditText))).check(matches(withText(phone)));
        onView(withId(R.id.emailEditText)).check(matches(withText("peter@gmail.com")));

        //Check if updated info is saved
        Espresso.pressBack();

        onView(withId(R.id.action_user)).perform(click());

        onView((withId(R.id.nameEditText))).check(matches(withText(name)));
        onView((withId(R.id.phoneEditText))).check(matches(withText(phone)));
    }

}
