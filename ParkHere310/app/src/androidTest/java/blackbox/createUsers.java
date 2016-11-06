package blackbox;

import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;

/**
 * Created by Peter on 11/3/16.
 */

@RunWith(JUnit4.class)
public class CreateUsers {
    private String email, pass;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initString() {
        email = "peter@gmail.com";
        pass = "Password1";
    }


    @Test
    public void signIn()
    {
        // Enter Sign In Info

        onView(withId(R.id.locationEditText)).perform(typeText(email), closeSoftKeyboard());
        onView((withId(R.id.locationEditText))).check(matches(withText(email)));
    }


}
