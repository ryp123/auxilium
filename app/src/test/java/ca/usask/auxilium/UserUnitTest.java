package ca.usask.auxilium;

import org.junit.Test;

import static org.junit.Assert.*;

/** tests all setters and getters in User Class for functionality
*   does not test for valid/non-valid inputs
*/
 public class UserUnitTest {

    private User u = new User();

    @Test
    public void setStatus_and_getStatus_test() throws Exception {
        u.setStatus("testStatus");
        assertNotNull(u.getStatus());
        assertSame("testStatus", u.getStatus());
        u.setStatus("testStatusWithNumbers90");
        assertNotNull(u.getStatus());
        assertSame("testStatusWithNumbers90", u.getStatus());
    }

    @Test
    public void setFirstName_and_getFirstName_test() throws Exception {
        u.setFirstName("testFirstName");
        assertNotNull(u.getFirstName());
        assertSame("testFirstName", u.getFirstName());
        u.setFirstName("testFirstNameWithNumbers90");
        assertNotNull(u.getFirstName());
        assertSame("testFirstNameWithNumbers90", u.getFirstName());
    }

    @Test
    public void setLastName_and_getLastName_test() throws Exception {
        u.setLastName("testLastName");
        assertNotNull(u.getLastName());
        assertSame("testLastName", u.getLastName());
        u.setLastName("testLastNameWithNumbers90");
        assertNotNull(u.getLastName());
        assertSame("testLastNameWithNumbers90", u.getLastName());
    }

    @Test
    public void setPreferredName_and_getPreferredName_test() throws Exception {
        u.setPreferredName("testPrefName");
        assertNotNull(u.getPreferredName());
        assertSame("testPrefName", u.getPreferredName());
        u.setPreferredName("testPrefNameWithNumbers90");
        assertNotNull(u.getPreferredName());
        assertSame("testPrefNameWithNumbers90", u.getPreferredName());
    }

    @Test
    public void setAge_and_getAge_test() throws Exception {
        u.setAge("20");
        assertNotNull(u.getAge());
        assertSame("20", u.getAge());
        u.setAge("forty");
        assertNotNull(u.getAge());
        assertSame("forty", u.getAge());
    }

    @Test
    public void setGender_and_getGender_test() throws Exception {
        u.setGender("M");
        assertNotNull(u.getGender());
        assertSame("M", u.getGender());
        u.setGender("1234567");
        assertNotNull(u.getGender());
        assertSame("1234567", u.getGender());
    }

    @Test
    public void setDiagnosis_and_getDiagnosis_test() throws Exception {
        u.setDiagnosis("Alcoholic");
        assertNotNull(u.getDiagnosis());
        assertSame("Alcoholic", u.getDiagnosis());
        u.setDiagnosis("Diagnosis90");
        assertNotNull(u.getDiagnosis());
        assertSame("Diagnosis90", u.getDiagnosis());
    }

    @Test
    public void setEmergencyContact_and_getEmergencyContact_test() throws Exception {
        u.setEmergencyContact("12341234567");
        assertNotNull(u.getEmergencyContact());
        assertSame("12341234567", u.getEmergencyContact());
        u.setEmergencyContact("1-234-123-4567");
        assertNotNull(u.getEmergencyContact());
        assertSame("1-234-123-4567", u.getEmergencyContact());
        u.setEmergencyContact("someNumber");
        assertNotNull(u.getEmergencyContact());
        assertSame("someNumber", u.getEmergencyContact());
    }

}