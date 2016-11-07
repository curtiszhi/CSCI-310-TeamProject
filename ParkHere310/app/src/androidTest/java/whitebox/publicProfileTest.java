package whitebox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.MyRecyclerAdapter;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by yingchen on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class publicProfileTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void publicTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton1 = onView(
                allOf(withId(R.id.action_user), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton1.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.viewHostHistoryButton), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton2.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (MyRecyclerAdapter.feedItemList.size() != 0) {


            ViewInteraction recyclerView = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            recyclerView.perform(actionOnItemAtPosition(0, click()));

            FeedItem spot = MyRecyclerAdapter.feedItemList.get(0);

            ViewInteraction buttonView = onView(
                    allOf(withId(R.id.viewButton),
                            isDisplayed()));
            buttonView.perform(click());



        }

    }

}
