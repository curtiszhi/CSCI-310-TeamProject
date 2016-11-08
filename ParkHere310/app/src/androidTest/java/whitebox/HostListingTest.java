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

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by yingchen on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HostListingTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void HostListTest() {
        /*ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton.perform(click());*/

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

        ViewInteraction AppCompatTextView = onView(
                allOf(withText("Hosting"), isDisplayed()));
        AppCompatTextView.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        if(MyRecyclerAdapter.feedItemList.size()!=0) {
            ViewInteraction recyclerView1 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));

            assertThat(MyRecyclerAdapter.feedItemList.size(), is(1));
            recyclerView1.perform(actionOnItemAtPosition(0, click()));

            FeedItem spot1 = MyRecyclerAdapter.feedItemList.get(0);

            assertThat(MyRecyclerAdapter.feedItemList.size(), is(1));

            onView(withId(R.id.address))
                    .check(matches(withText(spot1.getAddress())));
            onView(withId(R.id.price))
                    .check(matches(withText("$" + spot1.getPrice())));

            String filt1 = "";
            if (spot1.getFilter() != null) {
                for (int i = 0; i < spot1.getFilter().size(); i++) {
                    if (i != spot1.getFilter().size() - 1) {
                        filt1 = filt1 + spot1.getFilter().get(i) + ", ";
                    } else {
                        filt1 = filt1 + spot1.getFilter().get(i);
                    }
                }
            }
            onView(withId(R.id.filters))
                    .check(matches(withText(filt1)));
            onView(withId(R.id.description))
                    .check(matches(withText(spot1.getDescription())));

            if (spot1.getRentedTime().size() != 0) {
                String time_frame="";
                for (HashMap.Entry<String, ArrayList<String>> innerEntry : spot1.getRentedTime().entrySet()) {
                    String key = innerEntry.getKey();
                    ArrayList<String> value = innerEntry.getValue();
                    time_frame = value.get(0) + " to " + value.get(1);}
                onView(withId(R.id.time))
                        .check(matches(withText(time_frame)));
            }

            onView(withId(R.id.cancel))
                    .check(matches(withText(spot1.getCancel())));


            onView(withId(R.id.confirmButton))
                    .check(matches(not(isEnabled())));
            onView(withId(R.id.cancelButton))
                    .check(matches(isClickable()));
            onView(withId(R.id.editButton))
                    .check(matches(isEnabled()));

        }
    }
}
