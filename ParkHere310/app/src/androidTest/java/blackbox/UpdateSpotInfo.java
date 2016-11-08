package blackbox;

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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Peter on 11/6/16.
 */

@RunWith(JUnit4.class)
public class UpdateSpotInfo {
    private String description;

    @Rule
    public ActivityTestRule<ActionActivity> myActivityRule = new ActivityTestRule<>(
            ActionActivity.class);

    @Before
    public void initString() {
        description = "New Description";
    }

    @Test
    public void updateSpotInfo()
    {
        //Open a spot detail page:
        onView(withId(R.id.action_user)).perform(click());
        onView(withId(R.id.viewHostHistoryButton)).perform(click());
        onView(withText("Hosting")).perform(click());
        onData(anything()).inAdapterView(allOf(withId(R.id.list))


    }
}
