package whitebox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by yingchen on 11/6/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HostButtonTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void HostTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("louisarg@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("T1234567"), closeSoftKeyboard());

        pressBack();
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.action_user), withParent(allOf(withId(R.id.toolbar))),isDisplayed()));
        appCompatButton4.perform(click());
    }

    }
