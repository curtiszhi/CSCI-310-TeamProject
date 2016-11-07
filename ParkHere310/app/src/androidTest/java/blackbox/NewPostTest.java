package blackbox;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csci310.ParkHere.AddActivity;
import com.csci310.ParkHere.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by curtiszhi on 11/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class NewPostTest
{
    @Rule
    public ActivityTestRule<AddActivity> myActivityRule = new ActivityTestRule<>(AddActivity.class);

    @Before
    public void initString() {
//        email = "peter@gmail.com";
//        pass = "Password1";
    }


    @Test
    public void NewPost()
    {
        onView(withId(R.id.Address)).perform(typeText("Test address"));




    }
}
