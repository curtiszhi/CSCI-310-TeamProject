package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;


@RunWith(JUnit4.class)
public class ViewPublicProfileTest {

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void viewPublicProfile()
    {
        try{
            //not logged in
            onView(withId(R.id.locationEditText)).perform(replaceText(""), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out


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
        //Open and click the renting tab:
        try{ Thread.sleep(1000); }catch (Exception _){}
        onView(withId(R.id.action_user)).perform(click());
        onView(withId(R.id.viewHostHistoryButton)).perform(click());
        onView(withText("Hosting")).perform(click());
        try{ Thread.sleep(1000); }catch (Exception _){}
        onView(withText("Renting")).perform(click());
        try{ Thread.sleep(1000); }catch (Exception _){}

        //Open the first item and check the public profile of the host:
        onView(withId(R.id.myList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.viewButton)).perform(click());
        try{ Thread.sleep(1000); }catch (Exception _){}
        onView(withId(R.id.ratingTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.reviewText)).check(matches(isDisplayed()));
        onView(withId(R.id.public_name)).check(matches(isDisplayed()));
        onView(withId(R.id.editButton)).check(doesNotExist());
    }
}
