package ca.usask.auxilium;

/**
 * Created by Kocur on 2018-03-29.
 */

import android.util.Patterns;
import java.util.HashMap;
import java.util.regex.Pattern;

public class IndexStatus {

    private String lastUsed;
    private String lastSeenBy;
    private String lastSeenVia;
    private String reportedAssessment;
    private HashMap<String, String> validationErrors;
    final private int MAX_CHAR_LENGTH = 25;


    public IndexStatus(){
        this.lastUsed = "";
        this.lastSeenBy = "";
        this.lastSeenVia = "";
        this.reportedAssessment = "";
        this.validationErrors = new HashMap<>();


    }
    public IndexStatus(String lu, String lsb, String lsv, String ra){
        this.lastUsed = lu;
        this.lastSeenBy = lsb;
        this.lastSeenVia = lsv;
        this.reportedAssessment = ra;
        this.validationErrors = new HashMap<>();


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
        } else if (value.length() > MAX_CHAR_LENGTH) {
            if (!this.validationErrors.containsKey(key)) {
                this.validationErrors.put(key, "Cannot be over 25 characters.");
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
                this.validationErrors.put(key, "Invalid characters");
            }
        }
    }
    public boolean isValid(){
        return this.validationErrors.isEmpty();
    }

    public void validate() {
        this.validationErrors.clear();
        areRequiredFieldsSet();
        validateString("lastSeenBy", this.lastSeenBy);
        validateAlpha("lastSeenBy", this.lastSeenBy);
        validateString("lastSeenVia", this.lastSeenVia);
        validateAlpha("lastSeenVia", this.lastSeenVia);
        validateString("reportedAssessment", this.reportedAssessment);
        validateAlpha("reportedAssessment", this.reportedAssessment);


    }
    private void areRequiredFieldsSet() {
        String errorMessage = "Required field.";
        if (this.lastSeenBy.isEmpty()) {
            this.validationErrors.put("lastSeenBy", errorMessage);
        }
        if (this.lastSeenVia.isEmpty()) {
            this.validationErrors.put("lastSeenVia", errorMessage);
        }
        if (this.reportedAssessment.isEmpty()) {
            this.validationErrors.put("reportedAssessment", errorMessage);
        }
        return;
    }



    public HashMap<String, String> getValidationErrors() {
        return  this.validationErrors;
    }


    public String getLastUsed(){
        return this.lastUsed;
    }

    public void setLastUsed(String last){
        this.lastUsed = last;
    }
    public String getLastSeenBy(){
        return this.lastSeenBy;
    }
    public void setLastSeenBy(String lastSeen){
        this.lastSeenBy = lastSeen;
    }
    public String getLastSeenVia(){
        return this.lastSeenVia;
    }
    public void setLastSeenVia(String lastSeenVia){
        this.lastSeenVia = lastSeenVia;
    }
    public String getReportedAssessment(){
        return this.reportedAssessment;
    }
    public void setReportedAssessment(String reportedAssessment){
        this.reportedAssessment = reportedAssessment;
    }

}