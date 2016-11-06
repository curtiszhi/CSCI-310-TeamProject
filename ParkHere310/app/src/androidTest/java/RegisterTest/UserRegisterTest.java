package RegisterTest;

/**
 * Created by seanyuan on 11/6/16.
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.R;
import com.csci310.ParkHere.RegisterActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRegisterTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(
            RegisterActivity.class);

    @Before
    public void initValidString() {
        mStringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.nameEditText))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());

        onView(withText("Wait!")).check(matches(isDisplayed()));

    }
}

