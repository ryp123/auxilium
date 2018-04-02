package ca.usask.auxilium;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by marvib96 on 2018-04-02.
 * Testing IndexStatus getters and setters
 */
public class IndexStatusGetSetUnitTests {

    IndexStatus mIndexS ;

    String mLastU = "2018/03/30 @ 22:22:46";
    String mLastSB =  "Person";
    String mLastSV = "Facebook";
    String mReportA = "good";


    @Before
    public void setUp(){
        mIndexS = new IndexStatus(mLastU, mLastSB, mLastSV, mReportA);
    }


    @Test
    public void getLastUsedTest() throws Exception {
        String u = mIndexS.getLastUsed();
        assertNotNull(u);
        assertEquals(mLastU, u);
    }

    @Test
    public void setLastUsedTest() throws Exception {
        mLastU = "2018/04/13 @ 9:12:46";
        mIndexS.setLastUsed(mLastU);
        String u = mIndexS.getLastUsed();
        assertNotNull(u);
        assertEquals(mLastU, u);
    }

    @Test
    public void getLastSeenByTest() throws Exception {
        String s = mIndexS.getLastSeenBy();
        assertNotNull(s);
        assertEquals(mLastSB, s);
    }

    @Test
    public void setLastSeenByTest() throws Exception {
        mLastSB = "John";
        mIndexS.setLastSeenBy(mLastSB);
        String s = mIndexS.getLastSeenBy();
        assertNotNull(s);
        assertEquals(mLastSB, s);
    }

    @Test
    public void getLastSeenViaTest() throws Exception {
        String sv = mIndexS.getLastSeenVia();
        assertNotNull(sv);
        assertEquals(mLastSV, sv);
    }

    @Test
    public void setLastSeenViaTest() throws Exception {
        mLastSV = "";
        mIndexS.setLastSeenVia(mLastSV);
        String sv = mIndexS.getLastSeenVia();
        assertNotNull(sv);
        assertEquals(sv, mLastSV);
    }

    @Test
    public void getReportedAssessmentTest() throws Exception {
        String ra = mIndexS.getReportedAssessment();
        assertNotNull(ra);
        assertEquals(mReportA, ra);
    }

    @Test
    public void setReportedAssessmentTest() throws Exception {
        mReportA = "Doing very well";
        mIndexS.setReportedAssessment(mReportA);
        String ra = mIndexS.getReportedAssessment();
        assertNotNull(ra);
        assertEquals(mReportA, ra);
    }


}