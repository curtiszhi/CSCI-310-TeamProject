package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.MyRecyclerAdapter;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UpdateSpotInfo {
    private String description;
    private double price;
    private int index;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initString()
    {
        description = "New description for update spot info test";
        price = 99999;
    }

    @Test
    public void updateSpotInfo()
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
        //Open a spot detail page:
        try{ Thread.sleep(3000); }catch (Exception _){}
        ViewInteraction appCompatButton1 = onView(
                allOf(withId(R.id.action_user), /*withParent(allOf(withId(R.id.toolbar)))*/isDisplayed()));
        appCompatButton1.perform(click());
        onView(withId(R.id.viewHostHistoryButton)).perform(click());
        onView(withText("Hosting")).perform(click());
        try{ Thread.sleep(2000); }catch (Exception _){}

        if(MyRecyclerAdapter.feedItemList.size()!=0) {
            ViewInteraction recyclerView1 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));


            recyclerView1.perform(actionOnItemAtPosition(0, click()));


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

        if(MyRecyclerAdapter.feedItemList.size()!=0) {
            ViewInteraction recyclerView1 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));


            recyclerView1.perform(actionOnItemAtPosition(0, click()));


            onView(withId(R.id.price)).check(matches(withText("$" + Double.toString(price))));
        onView(withId(R.id.description)).check(matches(withText(description)));}
    }
}
