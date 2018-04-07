package ca.usask.auxilium;

import android.util.Patterns;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by gongcheng on 2018-02-14.
 */

public class User {


    private String mStatus;
    private String firstName;
    private String lastName;
    private String preferredName;
    private String age;
    private String gender;
    private String diagnosis;
    private String emergencyContact;
    private HashMap<String, String> validationErrors;


    public User(){
        this.mStatus = "";
        this.firstName = "";
        this.lastName = "";
        this.preferredName = "";
        this.age = "";
        this.gender = "";
        this.diagnosis = "";
        this.emergencyContact = "";
        this.validationErrors = new HashMap<>();
    }

    public User(String firstName, String lastName, String preferredName, String age, String gender, String diagnosis, String emergencyContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
        this.emergencyContact = emergencyContact;
        this.validationErrors = new HashMap<>();
    }

    public boolean isValid(){
        return this.validationErrors.isEmpty();
    }


    public void validate() {
        this.validationErrors.clear();
        areRequiredFieldsSet();
        validateString("firstName", this.firstName);
        validateAlpha("firstName", this.firstName);
        validateString("lastName", this.lastName);
        validateAlpha("lastName", this.lastName);
        validateString("preferredName", this.preferredName);
        if (!this.preferredName.isEmpty()) {
            validateAlpha("preferredName", this.preferredName);
        }
        validateAge();
        validateString("gender", this.gender);
        validateAlpha("gender", this.gender);
        validateString("diagnosis", this.diagnosis);
        if (!this.diagnosis.isEmpty()) {
            validateAlpha("diagnosis", this.diagnosis);
        }
        validateEmergencyContact();
    }

    private void validateString(String key, String value) {
        if (value.contains("\\n") || value.contains("\\r")) {
            if (!this.validationErrors.containsKey(key)) {
                this.validationErrors.put(key, "Cannot contain new lines.");
            }
            return;
        } else if (value.contains("\\t")) {
            if (!this.validationErrors.containsKey(key)) {
                this.validationErrors.put(key, "Cannot contain tabs.");
            }
        } else if (value.length() > 50) {
            if (!this.validationErrors.containsKey(key)) {
                this.validationErrors.put(key, "Cannot be over 50 characters.");
            }
            return;
        } else {
            return;
        }
    }

    private void validateAlpha(String key, String value) {
        // regular expression from https://stackoverflow.com/a/22483933
        Pattern namePattern = Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
        if (!namePattern.matcher(value).matches()) {
            if (!this.validationErrors.containsKey(key)) {
                this.validationErrors.put(key, "Invalid characters found in name");
            }
        }
    }

    private void areRequiredFieldsSet() {
        String errorMessage = "Required field.";
        if (this.firstName.isEmpty()) {
            this.validationErrors.put("firstName", errorMessage);
        }
        if (this.lastName.isEmpty()) {
            this.validationErrors.put("lastName", errorMessage);
        }
        if (this.age.isEmpty()) {
            this.validationErrors.put("age", errorMessage);
        }
        if (this.gender.isEmpty()) {
            this.validationErrors.put("gender", errorMessage);
        }
        return;
    }


    private void validateAge() {
        try {
            if (this.age.length() > 3) {
                if (!this.validationErrors.containsKey("age")) {
                    this.validationErrors.put("age", "Invalid age value should be numeric and no greater than 200.");
                }
                return;
            }
            Pattern numPattern = Pattern.compile("^-?\\d+$");
            if (!numPattern.matcher(this.age).matches()) {
                if (!this.validationErrors.containsKey("age")) {
                    this.validationErrors.put("age", "Age must be a number.");
                }
            }
            Integer currentAge = Integer.parseInt(this.age);
            if (currentAge > 200 || currentAge < 1) {
                if (!this.validationErrors.containsKey("age")) {
                    this.validationErrors.put("age", "Age must be between 1 and 200");
                }
            }
        } catch(NumberFormatException _notUsed) {
            if (!this.validationErrors.containsKey("age")) {
                this.validationErrors.put("age", "Invalid age value");
            }
        }
        return;
    }

    private void validateEmergencyContact() {
        if (this.emergencyContact.isEmpty()) {
            return;
        }
        if (Patterns.PHONE.matcher(this.emergencyContact).matches()) {
            if (!this.validationErrors.containsKey("emergencyContact")) {
                this.validationErrors.put("emergencyContact", "Must be a valid phone number");
            }
            return;
        }
    }

    public HashMap<String, String> getValidationErrors() {
        return  this.validationErrors;
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
