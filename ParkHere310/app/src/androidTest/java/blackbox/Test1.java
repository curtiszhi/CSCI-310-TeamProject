package blackbox;

import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.*;
import android.support.test.espresso.ViewFinder;

/**
 * Created by Peter on 11/3/16.
 */


@RunWith(JUnit4.class)
public class Test1 {
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
    public void signIn(){
        // Enter Sign In Info
//        onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
//        onView(withId(R.id.passwordEditText)).perform(typeText(pass), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        intended(hasComponent(ActionActivity.class.getName()));
//        onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
//        onView(withId(R.id.emailEditText)).check(m
        //onView(withId);
    }

}
