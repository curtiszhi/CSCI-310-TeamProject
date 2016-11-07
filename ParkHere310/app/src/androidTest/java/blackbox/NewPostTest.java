//package blackbox;
//
//import android.support.test.espresso.NoMatchingViewException;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import com.csci310.ParkHere.ActionActivity;
//import com.csci310.ParkHere.MainActivity;
//import com.csci310.ParkHere.R;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.intent.Intents.intended;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//
///**
// * Created by curtiszhi on 11/6/16.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class NewPostTest
//{
//    @Rule
//    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(
//            MainActivity.class);
//
//    @Before
//    public void initString() {
////        email = "peter@gmail.com";
////        pass = "Password1";
//    }
//
//
//    @Test
//    public void signIn()
//    {
//        // Enter Sign In Info
//
//        try {
//            onView(withId(R.id.emailEditText)).perform(typeText(email), closeSoftKeyboard());
//            onView(withId(R.id.passwordEditText)).perform(typeText(pass), closeSoftKeyboard());
//            onView(withId(R.id.loginButton)).perform(click());
//            intended(hasComponent(ActionActivity.class.getName()));
//        }
//        catch (NoMatchingViewException e)
//        {
//            onView(withId(R.id.locationEditText)).perform(typeText(email), closeSoftKeyboard());
//            onView((withId(R.id.locationEditText))).check(matches(withText(email)));
//        }
//
//
//
//
//    }
//}
