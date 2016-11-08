package whitebox;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SortResultTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void detailedViewTest() {
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

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.locationEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("1306 W. 29th Street Los Angeles CA 90007"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.startDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.endDateEditText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.startTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.endTimeText),
                        withParent(allOf(withId(R.id.basics),
                                withParent(withId(android.R.id.tabcontent)))),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());

        pressBack();

        /*ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.startDateEditText), withText("11-14-2016 "),
withParent(allOf(withId(R.id.basics),
withParent(withId(android.R.id.tabcontent)))),
isDisplayed()));
        appCompatEditText8.perform(click());*/

        /*ViewInteraction appCompatImageButton = onView(
allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Next month"),
withParent(allOf(withClassName(is("android.widget.DayPickerView")),
withParent(withClassName(is("com.android.internal.widget.DialogViewAnimator"))))),
isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton7 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
withParent(withClassName(is("android.widget.LinearLayout"))))),
isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatEditText9 = onView(
allOf(withId(R.id.endDateEditText), withText("11-16-2016 "),
withParent(allOf(withId(R.id.basics),
withParent(withId(android.R.id.tabcontent)))),
isDisplayed()));
        appCompatEditText9.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Next month"),
withParent(allOf(withClassName(is("android.widget.DayPickerView")),
withParent(withClassName(is("com.android.internal.widget.DialogViewAnimator"))))),
isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton8 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
withParent(withClassName(is("android.widget.LinearLayout"))))),
isDisplayed()));
        appCompatButton8.perform(click());*/

        /*ViewInteraction appCompatButton9 = onView(
allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton9.perform(click());*/

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Distance"), isDisplayed()));
        appCompatTextView.perform(click());

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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
}
