package blackbox;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
        name = "NewTest1234";
        phone = "1234567891";
        email = "";
    }

    @Test
    public void updateUser(){
        try{
            //not logged in
            onView(withId(R.id.locationEditText)).perform(replaceText(""), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            /*openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());*/

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailEditText), isDisplayed()));
            appCompatEditText.perform(replaceText("test1234@test.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.passwordEditText), isDisplayed()));
            appCompatEditText2.perform(replaceText("Password2"), closeSoftKeyboard());


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
            appCompatButton.perform(click());
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.action_user)).perform(click());

        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());

        onView(withId(R.id.nameEditText)).perform(replaceText(name));
        onView(withId(R.id.phoneEditText)).perform(replaceText(phone));
        onView(withId(R.id.editUserDetailsToggleButton)).perform(click());


        onView((withId(R.id.nameEditText))).check(matches(withText(name)));
        onView((withId(R.id.phoneEditText))).check(matches(withText(phone)));
        //Check if updated info is saved
        pressBack();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.action_user)).perform(click());

        onView((withId(R.id.nameEditText))).check(matches(withText(name)));
        onView((withId(R.id.phoneEditText))).check(matches(withText(phone)));
    }

}
