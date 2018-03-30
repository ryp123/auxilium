package ca.usask.auxilium;

/**
 * Created by Kocur on 2018-03-29.
 */

public class IndexStatus {

    private String lastUsed;
    private String lastSeenBy;
    private String lastSeenVia;
    private String reportedAssessment;

    public IndexStatus(){
        this.lastUsed = "";
        this.lastSeenBy = "";
        this.lastSeenVia = "";
        this.reportedAssessment = "";
    }
    public IndexStatus(String lu, String lsb, String lsv, String ra){
        this.lastUsed = lu;
        this.lastSeenBy = lsb;
        this.lastSeenVia = lsv;
        this.reportedAssessment = ra;
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
