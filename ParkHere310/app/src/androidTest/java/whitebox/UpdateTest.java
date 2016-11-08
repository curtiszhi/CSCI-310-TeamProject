package whitebox;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.MyRecyclerAdapter;
import com.csci310.ParkHere.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by yingchen on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void privateUpdateTest() {
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

        String email=getText(withId(R.id.emailEditText));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.editUserDetailsToggleButton), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton2.perform(click());

        onView(withId(R.id.emailEditText))
                .check(matches(withText(email)));

        onView(withId(R.id.nameEditText))
                .check(matches(isFocusable()));
        onView(withId(R.id.phoneEditText))
                .check(matches(isFocusable()));

        onView(withId(R.id.profilePicImageView))
                .check(matches(isClickable()));


    }
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    @Test
    public void spotUpdateTest() {
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


        if(MyRecyclerAdapter.feedItemList.size()!=0) {
            ViewInteraction recyclerView1 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            recyclerView1.perform(actionOnItemAtPosition(0, click()));

            FeedItem spot1 = MyRecyclerAdapter.feedItemList.get(0);


            onView(withId(R.id.editButton))
                    .perform(scrollTo(), click());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            onView(withId(R.id.Address))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.city))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.state))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.postcode))
                    .check(matches(not(isDisplayed())));

            onView(withId(R.id.price))
                    .check(matches(withText(spot1.getPrice()+"")));

            onView(withId(R.id.startDateEditText))
                    .check(matches(withText(spot1.getStartDates()+"")));

            onView(withId(R.id.endDateEditText))
                    .check(matches(withText(spot1.getEndDates()+"")));

            onView(withId(R.id.startTimeEditText))
                    .check(matches(withText(spot1.getStartTime()+"")));

            onView(withId(R.id.endTimeEditText))
                    .check(matches(withText(spot1.getEndTime()+"")));

            onView(withId(R.id.description))
                    .check(matches(withText(spot1.getDescription()+"")));

            String filters="";
            for(int i=0;i<spot1.getFilter().size();i++){
                filters+=spot1.getFilter().get(i);
            }
            onView(withId(R.id.mySpinner1))
                    .check(matches(withSpinnerText(containsString(filters))));

        }

    }
}
