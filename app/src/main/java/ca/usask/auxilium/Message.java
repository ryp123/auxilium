package ca.usask.auxilium;

/**
 * Created by Kocur on 2018-02-22.
 */

public class Message {

    private String id;
    private String sender;
    private String senderFUid;
    private String message;

    public Message() { }

    public Message(String Sender, String senderFUid, String Message)
    {
        this.sender = Sender;
        this.senderFUid = senderFUid;
        this.message = Message;
    }

    public Message(String Sender, String Message)
    {
        this.sender = Sender;
        this.message = Message;
    }

    public String getID()
    {
        return this.id;
    }

    public void setID(String newID) { this.id = newID; }

    public String getSender()
    {
        return this.sender;
    }

    public void setSender(String newSender)
    {
        this.sender = newSender;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String newMessage)
    {
        this.message = newMessage;
    }

    public String getSenderFUid() {
        return senderFUid;
    }

    public void setSenderFUid(String senderFUid) {
        this.senderFUid = senderFUid;
    }

}
