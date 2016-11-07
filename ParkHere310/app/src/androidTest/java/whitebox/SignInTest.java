package whitebox;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignInTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void invaidPasswordSignInTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("hi"), closeSoftKeyboard());


        ViewInteraction appCompatButton = onView(allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton.perform(click());
    }

    @Test
    public void invaidEmailSignInTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("notaregisteredemail@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());


        ViewInteraction appCompatButton = onView(allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton.perform(click());
    }

    @Test
    public void emptyFieldSignInTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText(""), closeSoftKeyboard());


        ViewInteraction appCompatButton = onView(allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton.perform(click());
    }
}