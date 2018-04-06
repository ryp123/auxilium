package ca.usask.auxilium;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by marvib96 on 2018-04-06.
 */

public class IndexListItemUnitTests {

    private String mMsg;


    String aUser;
    String fUser;

    IndexListItem mIndexL;

    @Before
    public void setUp(){
        mMsg = "A message";
        aUser = "user1";
        fUser = "Wg7V5nyS7RPFFCbSUFiZAOyOGaj2";
        mIndexL = new IndexListItem(mMsg, aUser, fUser);
    }


    @Test
    public void getAndSetMsgTest(){

        String msg = mIndexL.getMsg();
        assertEquals(msg, mMsg);

        msg = "a message 2.71625376891273928";
        mIndexL.setMsg(msg);
        mMsg = mIndexL.getMsg();
        assertEquals(msg, mMsg);
    }

    @Test
    public void getSetGetCount(){

        int count = mIndexL.getCount();
        assertEquals(1, count);
        mIndexL.setCount(7);
        assertEquals(7, mIndexL.getCount());
        mIndexL.setCount(1);
    }

    @Test
    public void addCountTest(){
        int countBefore = mIndexL.getCount();
        mIndexL.addCount(aUser, fUser);
        int countAfter = mIndexL.getCount();
        assertEquals(countBefore, countAfter );

        countBefore = countAfter;
        aUser = "John";
        fUser = "testID";
        mIndexL.addCount(aUser, fUser);
        countAfter = mIndexL.getCount();
        assertEquals(countBefore+1, countAfter);
    }


    @Test
    public void addAndGetUsersTest(){
        String fUser2 = "TestID2";
        String aUser2 = "Stacy";
        mIndexL.addFUser(fUser2);
        mIndexL.addUser(aUser2);

        String uList= mIndexL.getUsers();
        assertNotNull(uList);
        assert(uList.contains(aUser) && uList.contains(aUser2));

    }



}
