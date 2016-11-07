package blackbox;

import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.UserActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by Peter on 11/6/16.
 */

@RunWith(JUnit4.class)
public class UpdateUserInfoTest {


    private String name, phone, email;

    @Rule
    public ActivityTestRule<UserActivity> myActivityRule = new ActivityTestRule<>(
            UserActivity.class);

    @Before
    public void initString() {
        name = "Testing Name";
        phone = "1112223333";
        email = "shouldntchange@test.com";
    }

    @Test
    public void registerUser(){
        /*
        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());

        onView(withId(R.id.nameEditText)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(typeText(phone), closeSoftKeyboard());
        //onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());

        onView((withId(R.id.nameEditText))).check(matches(withText(name)));
        onView((withId(R.id.phoneEditText))).check(matches(withText(phone)));
        //onView((withId(R.id.emailEditText))).check(matches(withText(email)));
*/
    }

}
