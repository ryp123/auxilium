package ca.usask.auxilium;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jadenball on 2018-03-28.
 */

public class IndexListItem {

    private String msg;
    private ArrayList<String> users;
    private ArrayList<String> fusers;
    private int count;
    public boolean expanded;
    public boolean respondedTo;

    public IndexListItem(String msg, String user, String fuser){
        this.msg = msg;
        this.users = new ArrayList<>();
        this.fusers = new ArrayList<>();
        this.users.add(user);
        this.fusers.add(fuser);
        this.count = 1;
        this.expanded = false;
        respondedTo = false;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void addCount(String user, String fuser){
        if(!this.fusers.contains(fuser)) {
            count += 1;
            addUser(user);
            addFUser(fuser);
        }
    }

    public void addUser(String user){
        this.users.add(user);
    }

    public void addFUser(String fuser){
        this.fusers.add(fuser);
    }

    public String getUsers(){
        StringBuilder allUsers = new StringBuilder();
        for(String u : this.users){
            allUsers.append("\n");
            allUsers.append(u);
        }
        return allUsers.toString();
    }
}
