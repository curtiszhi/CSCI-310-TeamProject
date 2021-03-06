package whitebox;


import android.graphics.drawable.Drawable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.csci310.ParkHere.R.id.locationEditText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by seanyuan on 11/7/16.
 */

public class ProgressBarTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void detailedViewTest() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            //not logged in
            onView(withId(R.id.emailEditText)).perform(replaceText("hi"), closeSoftKeyboard());
        }

        catch (Exception e){
            //logged in, log out
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Sign-out"), isDisplayed()));
            appCompatTextView3.perform(click());
        }

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        //onView(withId(R.id.signInProgress)).check(matches(isDisplayed()));
        appCompatButton.perform(click());
        //Drawable notAnimatedDrawable = ContextCompat.getDrawable(mActivityTestRule.getActivity(), R.drawable.login);
        //((ProgressBar) mActivityTestRule.getActivity().findViewById(R.id.signInProgress)).setIndeterminateDrawable(notAnimatedDrawable);;


        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("1306 W. 29th Street Los Angeles CA 90007"), closeSoftKeyboard());
    }
}
