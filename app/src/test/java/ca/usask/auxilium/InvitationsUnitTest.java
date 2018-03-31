package ca.usask.auxilium;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sherry on 2018-03-04.
 */

/**  tests all setters and getters in Invitations Class for functionality
 *   does not test for valid/non-valid inputs
 */
public class InvitationsUnitTest {

    String testCircleID = "1";
    String testEmail = "test@test.com";
    String currentUserEmail = "fake@gmail.com";
    Long testLong = 123L;

    Invitations i = new Invitations(testCircleID, testEmail, currentUserEmail);

    @Test
    public void setCircleId_and_getCircleId_test() throws Exception {
        assertSame("1", i.getCircleId());
        i.setCircleId("2");
        assertNotNull(i.getCircleId());
        assertSame("2", i.getCircleId());
    }

    @Test
    public void setEmail_and_getEmail_test() throws Exception {
        assertSame("test@test.com", i.getEmail());
        i.setEmail("testEmail");
        assertNotNull(i.getCircleId());
        assertSame("testEmail", i.getEmail());
    }

    @Test
    public void setCurrentUserEmail_and_getCurrentUserEmail_test() throws Exception {
        assertSame("fake@gmail.com", i.getSenderEmail());
        i.setSenderEmail("testEmail");
        assertNotNull(i.getCircleId());
        assertSame("testEmail", i.getSenderEmail());
    }

    @Test
    public void setSentDynamicLink_and_getSentDynamicLink_test() throws Exception {
        assertFalse(i.getSentDynamicLink());
        i.setSentDynamicLink(true);
        assertTrue(i.getSentDynamicLink());
    }

    @Test
    public void setExpirationDate_and_getExpirationDate_test() throws Exception {
        i.setExpirationDate(testLong);
        assertEquals(testLong, i.getExpirationDate());
    }

}