package blackbox;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
public class hostCancelSpot {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void HostCancelTest() {
        try{
            //logged in
            onView(withId(R.id.locationEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //not logged in, then log in
            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailEditText), isDisplayed()));
            appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.passwordEditText), isDisplayed()));
            appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
            appCompatButton.perform(click());
        }



        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton1 = onView(
                allOf(withId(R.id.action_user), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton1.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.viewHostHistoryButton), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction AppCompatTextView = onView(
                allOf(withText("Hosting"), isDisplayed()));
        AppCompatTextView.perform(click());


        if (MyRecyclerAdapter.feedItemList.size() != 0) {
            ViewInteraction recyclerView1 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            recyclerView1.perform(actionOnItemAtPosition(0, click()));

            FeedItem spot1 = MyRecyclerAdapter.feedItemList.get(0);

            onView(withId(R.id.cancelButton))
                    .perform(scrollTo(), click());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ViewInteraction appCompatButton3 = onView(
                    allOf(withId(R.id.viewHostHistoryButton), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
            appCompatButton3.perform(click());

            if (MyRecyclerAdapter.feedItemList.size() != 0) {
                ViewInteraction recyclerView2 = onView(
                        allOf(withId(R.id.myList),
                                withParent(allOf(withId(R.id.main_content),
                                        withParent(withId(android.R.id.content)))),
                                isDisplayed()));
                recyclerView2.perform(actionOnItemAtPosition(0, click()));

                FeedItem spot2 = MyRecyclerAdapter.feedItemList.get(0);

                assertThat(spot2.getIdentifier(), not((spot1.getIdentifier())));
            }



        }
    }
}
