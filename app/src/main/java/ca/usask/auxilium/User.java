package ca.usask.auxilium;

/**
 * Created by gongcheng on 2018-02-14.
 */

public class User {


    private String mUserName;
    private String mStatus;
    private String firstName;
    private String lastName;
    private String preferredName;
    private String age;
    private String gender;
    private String diagnosis;
    private String emergencyContact;


    public User(){
        this.mUserName = "";
        this.mStatus = "";
        this.firstName = "";
        this.lastName = "";
        this.preferredName = "";
        this.age = "";
        this.gender = "";
        this.diagnosis = "";
        this.emergencyContact = "";
    }

    public User(String firstName, String lastName, String preferredName, String age, String gender, String diagnosis, String emergencyContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
        this.emergencyContact = emergencyContact;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }



    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
