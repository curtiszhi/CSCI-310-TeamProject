package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
        name = "Test1234";
        email = "test1234@test.com";
        pass = "Password2";
        phone = "1231231235";

        existingEmail = "test1234@test.com";

    }

    @Test
    public void registerUserTwo(){

    }


    @Test
    public void registerUser(){
        try{
            //not logged in
            onView(withId(R.id.emailEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());
        }
        onView(withId(R.id.nameEditText)).perform(replaceText(name), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(replaceText(pass), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(replaceText(pass), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(replaceText(phone), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());

        onView(withId(R.id.emailEditText)).check(doesNotExist());


        try{
            //not logged in
            onView(withId(R.id.emailEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());
        }
            onView(withId(R.id.registerButton)).perform(click());
            onView(withId(R.id.nameEditText)).perform(replaceText(name), closeSoftKeyboard());
            onView(withId(R.id.emailEditText)).perform(replaceText(existingEmail), closeSoftKeyboard());
            onView(withId(R.id.passwordEditText)).perform(replaceText(pass), closeSoftKeyboard());
            onView(withId(R.id.confirmPasswordEditText)).perform(replaceText(pass), closeSoftKeyboard());
            onView(withId(R.id.phoneEditText)).perform(replaceText(phone), closeSoftKeyboard());
            onView(withId(R.id.registerButton)).perform(click());

            onView((withId(R.id.locationEditText))).check(doesNotExist());

    }

}
