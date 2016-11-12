package whitebox;

/**
 * Created by seanyuan on 11/6/16.
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRegisterTest {

    private String testname;
    private String invalidPasswordError;

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(
            RegisterActivity.class);

    @Before
    public void initValidString() {
        testname = "Blah";
        invalidPasswordError = "Passwords must be at least 6 characters long and contain at least: 1-number 1-uppercase letter";
    }

    @Test
    public void insufficientFields() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Please fill all fields")).check(matches(isDisplayed()));
    }
    @Test
    public void invalidEmail() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.emailEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("Testing1"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText("Testing1"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText))
                .perform(typeText("2222222222"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Please enter valid email")).check(matches(isDisplayed()));
    }
    @Test
    public void invalidPasswordOne() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.emailEditText))
                .perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("Hi"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText("Hi"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText))
                .perform(typeText("2222222222"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText(invalidPasswordError)).check(matches(isDisplayed()));
        //onView(withText(invalidPasswordError)).perform(click());
    }
    @Test
    public void invalidPasswordTwo() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.emailEditText))
                .perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText("hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText))
                .perform(typeText("2222222222"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText(invalidPasswordError)).check(matches(isDisplayed()));
        //onView(withText(invalidPasswordError)).perform(click());
    }
    @Test
    public void invalidPasswordThree() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.emailEditText))
                .perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText))
                .perform(typeText("2222222222"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("Hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText("Hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText(invalidPasswordError)).check(matches(isDisplayed()));
        //onView(withText(invalidPasswordError)).perform(click());
    }
    @Test
    public void invalidPasswordFour() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(testname), closeSoftKeyboard());
        onView(withId(R.id.emailEditText))
                .perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("7hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText("7hihihihi"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText))
                .perform(typeText("2222222222"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Passwords must be at least 6 characters long and contain at least: 1-number 1-uppercase letter")).check(matches(isDisplayed()));
        //onView(withText(invalidPasswordError)).perform(click());
    }
}

