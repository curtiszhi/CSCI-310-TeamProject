import android.content.Context;

import com.csci310.ParkHere.FeedItem;
import com.csci310.ParkHere.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        spot.setStartTime("13:10PM");
        spot.setEndDates("11-20-2016");
        spot.setEndTime("10:30AM");
        spot.setIdentifier("asdfghjklkjhgf");
        spot.setPrice(2.3);

        assertThat(spot.getAddress(), is("1171 W30th Street,Los Angeles,CA90007"));
        assertThat(spot.getHost(), is("abcdefg"));
        assertThat(spot.getFilter(), is(filters));
        assertThat(spot.getDescription(), is("test description"));
        assertThat(spot.getActivity(), is(true));
        assertThat(spot.getCancel(), is("No refund"));
        assertThat(spot.getCurrentRenter(), is("ABCDEFG"));
        assertThat(spot.getSpotID(), is("23456"));
        assertThat(spot.getStartDates(), is("11-10-2016"));
        assertThat(spot.getStartTime(), is("13:10PM"));
        assertThat(spot.getEndDates(), is("11-20-2016"));
        assertThat(spot.getEndTime(), is("10:30AM"));
        assertThat(spot.getIdentifier(), is("asdfghjklkjhgf"));
        assertThat(spot.getPrice(), is(2.3));

    }

    @Test
    public void UserAdvancedFunctions(){
        FeedItem spot=new FeedItem();
        assertThat(spot.getReview(), is(notNullValue()));
        assertThat(spot.getRentedTime(), is(notNullValue()));
        assertThat(spot.getRating(), is(notNullValue()));
        assertThat(spot.getLatitude(), is(0.0));
        assertThat(spot.getLongitude(), is(0.0));
        assertThat(spot.getPhotos(), is(notNullValue()));

        Vector<String> testReviews = new Vector<String>();
        testReviews.add("Test review1");
        testReviews.add("Test review2");
        testReviews.add("Test review3");
        testReviews.add(" ");
        spot.setReview(testReviews);
        assertThat(spot.getReview().size(), is(4));
        assertThat(spot.getReview().toString(), is("[Test review1, Test review2, Test review3,  ]"));

        Vector<Integer> testRatings = new Vector<Integer>();
        testRatings.add(3);
        testRatings.add(5);
        testRatings.add(1);
        testRatings.add(2);
        spot.setRating(testRatings);
        assertThat(spot.calculateRate(), is((float)2.75));

        Map<String,ArrayList<String>> testRented=new HashMap<String,ArrayList<String>>();
        ArrayList<String> rented = new ArrayList<>();
        rented.add("11-10-2016 13:10PM");
        rented.add("11-20-2016 10:30AM");
        testRented.put("234567",rented);
        spot.setRentedTime(testRented);
        assertThat(spot.getRentedTime().size(), is(1));
        assertThat(spot.getRentedTime(), is(testRented));

    }
}