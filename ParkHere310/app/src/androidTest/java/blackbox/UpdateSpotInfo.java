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
public class UpdateSpotInfo {
    private String description;
    private double price;
    private int index;

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Before
    public void initString()
    {
        description = "New description for update spot info test";
        price = 99999;
        index = 1;
    }

    @Test
    public void updateSpotInfo()
    {
        //Open a spot detail page:
        try{ Thread.sleep(1000); }catch (Exception _){}
        onView(withId(R.id.action_user)).perform(click());
        onView(withId(R.id.viewHostHistoryButton)).perform(click());
        onView(withText("Hosting")).perform(click());
        try{ Thread.sleep(2000); }catch (Exception _){}
        onView(withId(R.id.myList)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));

        //Edit the info of that spot:
        onView(withId(R.id.editButton)).perform(scrollTo(), click());
        onView(withId(R.id.price)).perform(clearText());
        onView(withId(R.id.price)).perform(typeText(Double.toString(price)), closeSoftKeyboard());
        onView(withId(R.id.description)).perform(clearText());
        onView(withId(R.id.description)).perform(typeText(description), closeSoftKeyboard());

        onView(withId(R.id.postButton)).perform(scrollTo(), click());

        //Nothing should be prompted:
        onView(withId(R.id.viewHostHistoryButton)).check(matches(isDisplayed()));


    }

    @Test
    public void viewUpdatedSpotInfo()
    {
        //Open again and check the updated info:
        try{ Thread.sleep(1000); }catch (Exception _){}
        onView(withId(R.id.action_user)).perform(click());
        onView(withId(R.id.viewHostHistoryButton)).perform(click());
        onView(withText("Hosting")).perform(click());
        try{ Thread.sleep(2000); }catch (Exception _){}
        onView(withId(R.id.myList)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));

        onView(withId(R.id.price)).check(matches(withText("$" + Double.toString(price))));
        onView(withId(R.id.description)).check(matches(withText(description)));
    }
}
