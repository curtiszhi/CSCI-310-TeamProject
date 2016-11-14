package blackbox;

import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Peter on 11/6/16.
 */


@RunWith(JUnit4.class)
public class RegisterTest {

    private String name, email, pass, phone, existingEmail;

    @Rule
    public ActivityTestRule<RegisterActivity> myActivityRule = new ActivityTestRule<>(
            RegisterActivity.class);

    @Before
    public void initString() {
        name = "Test123";
        email = "test123@test.com";
        pass = "Password1";
        phone = "1231231234";

        existingEmail = "peter@gmail.com";

    }


    @Test
    public void registerUser(){
            onView(withId(R.id.nameEditText)).perform(typeText(name), closeSoftKeyboard());
            onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.passwordEditText)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.confirmPasswordEditText)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.phoneEditText)).perform(typeText(phone), closeSoftKeyboard());
            onView(withId(R.id.registerButton)).perform(click());

            onView(withId(R.id.locationEditText)).perform(typeText(email), closeSoftKeyboard());
            onView((withId(R.id.locationEditText))).check(matches(withText(email)));
    }


    @Test
    public void registerUserFail(){
            onView(withId(R.id.nameEditText)).perform(typeText(name), closeSoftKeyboard());
            onView(withId(R.id.emailEditText)).perform(typeText(existingEmail), closeSoftKeyboard());
            onView(withId(R.id.passwordEditText)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.confirmPasswordEditText)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.phoneEditText)).perform(typeText(phone), closeSoftKeyboard());
            onView(withId(R.id.registerButton)).perform(click());

            onView((withId(R.id.locationEditText))).check(doesNotExist());

    }

}
