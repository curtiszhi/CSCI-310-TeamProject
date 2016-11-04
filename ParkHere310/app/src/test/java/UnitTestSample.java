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

@RunWith(MockitoJUnitRunner.class)
public class UnitTestSample {

    @Mock
    Context mMockContext;

    @Test
    public void readStringFromContext_LocalizedString() {
        User test_user = new User();
        test_user.setEmail("testemail@gmail.com");
        test_user.setHost(true);
        test_user.setPhone("2222222222");
        test_user.setUserName("Billy Test");

        // ...when the string is returned from the object under test...
        String result = test_user.getEmail();

        // ...then the result should be the expected one.
        assertThat(result, is("testemail@gmail.com"));
    }
}