package blackbox;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
        email = "test1234@test.com";
        correctPassword = "Password2";
        incorrectPassword = "incorrect";
    }


    @Test
    public void incorrectSignIn()
    {
        try{
            //not logged in
            onView(withId(R.id.emailEditText)).perform(replaceText(""), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());
        }
        onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText(incorrectPassword), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        try{ Thread.sleep(3000); }catch (Exception _){}
        onView(withText("Sign-In Error")).check(matches(isDisplayed()));

    }

    @Test
    public void correctSignIn()
    {
        try{
            //not logged in
            onView(withId(R.id.emailEditText)).perform(replaceText(""), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());
        }

        onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText(correctPassword), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        try{ Thread.sleep(3000); }catch (Exception _){}
        onView(withId(R.id.loginButton)).check(doesNotExist());
    }

}
