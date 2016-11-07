package blackbox;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Peter on 11/3/16.
 */


@RunWith(JUnit4.class)
public class SignInTest {
    private String email, correctPassword, incorrectPassword;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initString() {
        email = "peter@gmail.com";
        correctPassword = "Password1";
        incorrectPassword = "Password2";
    }


    @Test
    public void incorrectSignIn()
    {
//            //Incorrect signin:
//            onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
//            onView(withId(R.id.passwordEditText)).perform(typeText(incorrectPassword), closeSoftKeyboard());
//            onView(withId(R.id.loginButton)).perform(click());
//            System.out.println("Here1234");
//            long waitingTime = 5000L;
//            IdlingPolicies.setMasterPolicyTimeout(
//                    waitingTime * 2, TimeUnit.MILLISECONDS);
//            IdlingPolicies.setIdlingResourceTimeout(
//                    waitingTime * 2, TimeUnit.MILLISECONDS);
//
//            // Now we wait
//            IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
//            Espresso.registerIdlingResources(idlingResource);
//
//            onView(withId(R.id.searchButton)).check(doesNotExist());
//
//
//
//
//
//            //Click on back button:
//            onView(withText("Sign-In Error")).perform(pressBack());

            //Correct signin:

    }

    @Test
    public void correctSignIn()
    {
        onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText(correctPassword), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginButton)).check(doesNotExist());
    }

}
