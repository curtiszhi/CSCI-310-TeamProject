package blackbox;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.*;

/**
 * Created by Peter on 11/3/16.
 */


@RunWith(JUnit4.class)
public class SignInTest {
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
        try {
            onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.passwordEditText)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(doesNotExist());
        }
        catch (NoMatchingViewException e)
        {
            e.printStackTrace();
            System.out.println("Already signed in! Please sign out first to initiate this test!");
        }
    }


}
