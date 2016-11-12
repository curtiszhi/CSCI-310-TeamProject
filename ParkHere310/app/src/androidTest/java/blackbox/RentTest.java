package blackbox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.csci310.ParkHere.ActionActivity;
import com.csci310.ParkHere.MainActivity;
import com.csci310.ParkHere.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.csci310.ParkHere.R.id.locationEditText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Peter on 11/7/16.
 */

@RunWith(JUnit4.class)
public class RentTest {
    private String location;
    private String location1;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initString() {
        location = "1171 W 30th St, Los Angeles, CA 90007, USA";
    }

    @Test
    public void rentCreditCardTest(){
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
        //Search
        try{ Thread.sleep(3000); }catch (Exception _){}
        onView(withId(locationEditText)).perform(replaceText(location), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,15));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16,0));
        onView(withId(android.R.id.button1)).perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());
        try{ Thread.sleep(3000); }catch (Exception _){}

        int y = 0;
        while (y == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Distance"), isDisplayed()));
                appCompatTextView.perform(click());
                y = 1;
            } catch(Exception e){
                y = 0;
            }
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.myList),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        try{ Thread.sleep(3000); }catch (Exception _){}
        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.rentButton), withText("Rent!")));
        appCompatButton7.perform(scrollTo(), click());

        int x = 0;
        while (x == 0){
            try{
                ViewInteraction textView = onView(
                        withText("Pay with Card"));
                textView.perform(scrollTo(), click());
                x = 1;

                try{
                    onView(withText("Deny")).perform(click());
                }
                catch(Exception e1){

                }
                onView(withHint("1234 5678 1234 5678")).perform(typeText("4264 5101 8909 8545 0217 666"));

                ViewInteraction button = onView(
                        allOf(withText("Done"), isDisplayed()));
                button.perform(click());

                x = 0;
                while(x == 0){
                    try{
                        ViewInteraction textView2 = onView(
                                withText("Charge Card"));
                        textView2.perform(scrollTo(), click());
                        x = 1;
                    }
                    catch (Exception e){
                        x = 0;
                    }


                }
            } catch(Exception e){
                x = 0;
                try{
                    ViewInteraction textView = onView(
                            withText("x-2398"));
                    textView.perform(scrollTo(), click());
                    x = 1;

                    x = 0;
                    while(x == 0) {
                        try {
                            ViewInteraction textView2 = onView(
                                    withText("Charge Card"));
                            textView2.perform(scrollTo(), click());
                            x = 1;
                        } catch (Exception e2) {
                            x = 0;
                        }

                    }
                    } catch(Exception e1){
                    x = 0;
                }
            }
        }

/*
        //Check if Spot shows in Rent History
        try{ Thread.sleep(3000); }catch (Exception _){}
        x = 0;
        while (x == 0){
            try {
                ViewInteraction actionMenuItemView = onView(
                        allOf(withId(R.id.action_user), withContentDescription("User"), isDisplayed()));
                actionMenuItemView.perform(click());
                x = 1;
            } catch(Exception e){
                x = 0;
            }
        }

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.viewHostHistoryButton), withText("View History")));
        appCompatButton8.perform(scrollTo(), click());
        try{ Thread.sleep(3000); }catch (Exception _){}
        try{
            ViewInteraction recyclerView2 = onView(
                    allOf(withId(R.id.myList),
                            withParent(allOf(withId(R.id.main_content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            recyclerView2.perform(actionOnItemAtPosition(0, click()));

        }
        catch(Exception e) {
            //Test passes
        }
*/
    }

/*
    @Test
    public void payPalTest(){
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
        try{ Thread.sleep(3000); }catch (Exception _){}
        onView(withId(locationEditText)).perform(replaceText(location1), closeSoftKeyboard());

        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,15));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016,11,16));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.startTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.endTimeText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16,0));
        onView(withId(android.R.id.button1)).perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.searchButton), withText("Search!"), isDisplayed()));
        appCompatButton6.perform(click());

        int y = 0;
        while (y == 0){
            try{
                ViewInteraction appCompatTextView = onView(
                        allOf(withText("Distance"), isDisplayed()));
                appCompatTextView.perform(click());
                y = 1;
            } catch(Exception e){
                y = 0;
            }
        }
        try{ Thread.sleep(3000); }catch (Exception _){}
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.myList),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.rentButton), withText("Rent!")));
        appCompatButton7.perform(scrollTo(), click());
        try{ Thread.sleep(3000); }catch (Exception _){}

        //Paypal
        int x = 0;
        while (x == 0){
            try{
                ViewInteraction textView = onView(
                        withText("Pay with"));
                textView.perform(scrollTo(), click());
                x = 1;
            } catch(Exception e){
                x = 0;
            }
        }

        ViewInteraction editText = onView(
                withContentDescription("Email"));
        editText.perform(scrollTo(), replaceText("parkHere310Test@test.com"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                withContentDescription("Password"));
        editText2.perform(scrollTo(), replaceText("parkHere310"), closeSoftKeyboard());

        ViewInteraction textView2 = onView(
                withHint("Log In"));
        textView2.perform(scrollTo(), click());

        int z = 0;
        while (z == 0){
            try{
                ViewInteraction textView3 = onView(
                        withText("Pay"));
                textView3.perform(scrollTo(), click());
                z = 1;
            }
            catch (Exception e){
                z = 0;
            }
        }
    }*/
}
