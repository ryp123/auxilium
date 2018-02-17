package ca.usask.auxilium;

import org.joda.time.DateTime;

/**
 * Created by rpiper on 3/3/18.
 */

public class Invitations {

    private String circleId;
    private String email;
    private boolean sentDynamicLink;
    private Long expirationDate;

    public Invitations(String circleId,String email) {
        this.circleId = circleId;
        this.email = email;
        this.sentDynamicLink = false;
        // get expiration date as unix timestamp
        this.expirationDate = DateTime.now().plusMonths(1).getMillis() / 1000L;
    }

    public String getCircleId() {
        return this.circleId;
    }

    public String getEmail() {
        return  this.email;
    }

    public boolean getSentDynamicLink() {
        return this.sentDynamicLink;
    }

    public Long getExpirationDate() {
        return  this.expirationDate;
    }

    public void setCircleId(String newCircleId) {
        this.circleId = newCircleId;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setSentDynamicLink(boolean newSentDynamicLink) {
        this.sentDynamicLink = newSentDynamicLink;
    }

    public void setExpirationDate(Long newExpirationDate) {
        this.expirationDate = newExpirationDate;
    }


}