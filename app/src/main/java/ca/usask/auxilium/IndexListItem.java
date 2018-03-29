package ca.usask.auxilium;

/**
 * Created by jadenball on 2018-03-28.
 */

public class IndexListItem {

    private String msg;
    private int count;

    public IndexListItem(String msg){
        this.msg = msg;
        count = 1;
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

    public void addCount(){
        count += 1;
    }
}
