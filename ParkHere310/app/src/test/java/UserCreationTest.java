/**
 * Created by seanyuan on 11/3/16.
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.Context;
import com.csci310.ParkHere.User;

import java.util.Vector;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationTest {

    @Mock
    Context mMockContext;

    @Test
    public void UserBasicFunctions() {
        User test_user = new User();
        test_user.setEmail("testemail@gmail.com");
        test_user.setHost(true);
        test_user.setPhone("2222222222");
        test_user.setUserName("Billy Test");
        String email = test_user.getEmail();
        assertThat(email, is("testemail@gmail.com"));
        Boolean isHost = test_user.getHost();
        assertThat(isHost, is(true));
        String phone = test_user.getPhone();
        assertThat(phone, is("2222222222"));
        String username = test_user.getUserName();
        assertThat(username, is("Billy Test"));
    }

    @Test
    public void UserAdvancedFunctions(){
        User test_user = new User();
        assertThat(test_user.getReview(), is(notNullValue()));
        assertThat(test_user.getRateList(), is(notNullValue()));
        assertThat(test_user.getRating(), is(notNullValue()));
        assertThat(test_user.getRenting(), is(notNullValue()));
        assertThat(test_user.getHosting(), is(notNullValue()));
        assertThat(test_user.getPhoto(), is(nullValue()));

        Vector<String> testReviews = new Vector<String>();
        testReviews.add("Test review1");
        testReviews.add("Test review2");
        testReviews.add("Test review3");
        testReviews.add(" ");
        test_user.setReview(testReviews);
        assertThat(test_user.getReview().size(), is(4));
        assertThat(test_user.getReview().toString(), is("[Test review1, Test review2, Test review3,  ]"));

        Vector<Integer> testRatings = new Vector<Integer>();
        testRatings.add(3);
        testRatings.add(5);
        testRatings.add(1);
        testRatings.add(2);
        test_user.setRating(testRatings);
        assertThat(test_user.getRating(), is((float)2.75));


    }
}