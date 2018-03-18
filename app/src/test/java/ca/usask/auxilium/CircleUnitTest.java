package ca.usask.auxilium;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sherry on 2018-03-18.
 */
public class CircleUnitTest {
    Circle c = new Circle();

    @Test
    public void setCircleName_and_getCircleName_test() throws Exception {
        Assert.assertNull(c.getCircleName());
        c.setCircleName("Test");
        Assert.assertNotNull(c.getCircleName());
        Assert.assertEquals("Test", c.getCircleName());

        c.setCircleName("Test with 1234");
        Assert.assertNotNull(c.getCircleName());
        Assert.assertEquals("Test with 1234", c.getCircleName());

        c.setCircleName("Test with 1234!!");
        Assert.assertNotNull(c.getCircleName());
        Assert.assertEquals("Test with 1234!!", c.getCircleName());
    }

    @Test
    public void setAilment_and_getAilment_test() throws Exception {
        Assert.assertNull(c.getAilment());
        c.setAilment("Test");
        Assert.assertNotNull(c.getAilment());
        Assert.assertEquals("Test", c.getAilment());

        c.setAilment("Test with 1234");
        Assert.assertNotNull(c.getAilment());
        Assert.assertEquals("Test with 1234", c.getAilment());

        c.setAilment("Test with 1234!!");
        Assert.assertNotNull(c.getAilment());
        Assert.assertEquals("Test with 1234!!", c.getAilment());
    }

    @Test
    public void setCircleInfo_and_getCircleInfo_test() throws Exception {
        Assert.assertNull(c.getCircleInfo());
        c.setCircleInfo("Test");
        Assert.assertNotNull(c.getCircleInfo());
        Assert.assertEquals("Test", c.getCircleInfo());

        c.setCircleInfo("Test with 1234");
        Assert.assertNotNull(c.getCircleInfo());
        Assert.assertEquals("Test with 1234", c.getCircleInfo());

        c.setCircleInfo("Test with 1234!!");
        Assert.assertNotNull(c.getCircleInfo());
        Assert.assertEquals("Test with 1234!!", c.getCircleInfo());
    }

}