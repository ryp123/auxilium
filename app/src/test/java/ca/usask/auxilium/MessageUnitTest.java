package ca.usask.auxilium;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sherry on 2018-03-04.
 */

/**  tests all setters and getters in Message Class for functionality
 *   does not test for valid/non-valid inputs
 */
public class MessageUnitTest {

    Message m = new Message();

    @Test
    public void setID_and_getID_test() throws Exception {
        assertNull(m.getID());
        m.setID("testID");
        assertNotNull(m.getID());
        assertSame("testID", m.getID());
        m.setID("90test90");
        assertNotNull(m.getID());
        assertSame("90test90", m.getID());
    }

    @Test
    public void setSender_and_getSender_test() throws Exception {
        assertNull(m.getSender());
        m.setSender("testSender");
        assertNotNull(m.getSender());
        assertSame("testSender", m.getSender());
        m.setSender("90test90");
        assertNotNull(m.getSender());
        assertSame("90test90", m.getSender());
    }

    @Test
    public void setMessage_and_getMessage_test() throws Exception {
        assertNull(m.getMessage());
        m.setMessage("testMessage");
        assertNotNull(m.getMessage());
        assertSame("testMessage", m.getMessage());
        m.setMessage("90test90");
        assertNotNull(m.getMessage());
        assertSame("90test90", m.getMessage());
    }

}