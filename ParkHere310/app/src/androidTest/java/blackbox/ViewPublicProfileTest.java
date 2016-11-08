package blackbox;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;


@RunWith(JUnit4.class)
public class ViewPublicProfileTest {

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Test
    public void viewPublicProfile()
    {
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
    }
}
