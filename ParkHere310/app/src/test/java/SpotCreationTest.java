import android.content.Context;

import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by yingchen on 11/6/2016.
 */


@RunWith(MockitoJUnitRunner.class)
public class SpotCreationTest {

    @Mock
    Context mMockContext;

    @Test
    public void UserBasicFunctions() {
        FeedItem spot=new FeedItem();
        spot.setAddress("1171 W30th Street,Los Angeles,CA90007");
        spot.setHost("abcdefg");
        List<String> filters=new ArrayList<String>();
        filters.add("compact");
        spot.setFilter(filters);
        spot.setDescription("test description");
        spot.setActivity(true);
        spot.setCancel("No refund");
        spot.setCurrentRenter("ABCDEFG");//rent page didn't add current
        spot.setSpotID("23456");
        spot.setStartDates("11-10-2016");
        spot

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