package whitebox;

/**
 * Created by seanyuan on 11/6/16.
 */

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SortResultTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchAddressTest() {
        /*ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.emailEditText), isDisplayed()));
        appCompatEditText.perform(replaceText("seanyuan@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordEditText), isDisplayed()));
        appCompatEditText2.perform(replaceText("Testing1"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.loginButton), withText("login"), isDisplayed()));
        appCompatButton.perform(click());*/

        ViewInteraction appCompatEditText3 = onView(
                allOf(ViewMatchers.withId(R.id.startTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("22:56"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.endTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("22:56"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.startDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("12-8-16"));


        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.endDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("12-9-16"));


        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("1306 W. 29th Street, Los Angeles, CA 90007"), closeSoftKeyboard());

        ViewInteraction appCompatTextViewqq = onView(
                allOf(withId(android.R.id.title), withText("Filters"), isDisplayed()));
        appCompatTextViewqq.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.coverBox), withText("Covered Parking"),
                        withParent(allOf(withId(R.id.stepFourLayout),
                                withParent(withId(R.id.filters)))),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("Price"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.price), withText("$999.0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                3),
                        isDisplayed()));
        textView.check(matches(withText("$999.0")));

        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("Distance"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.house), withText("1306 W 29th St, Los Angeles, CA 90007, USA"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("1306 W 29th St, Los Angeles, CA 90007, USA")));

        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Rating: Spot"), isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.house), withText("1306 W 29th St, Los Angeles, CA 90007, USA"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("1306 W 29th St, Los Angeles, CA 90007, USA")));



    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
