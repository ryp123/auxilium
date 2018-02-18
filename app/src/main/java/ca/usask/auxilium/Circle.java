package ca.usask.auxilium;

/**
 * Created by gongcheng on 2018-02-14.
 */

public class Circle {
    private String CircleName;

    private String ailment;

    private String circleInfo;

    public void setCircleName(String circle_name){
        CircleName = circle_name;

    }

    public String getCircleName(){
        return CircleName;
    }

    public void setAilment(String ail){
       ailment = ail;
    }

    public String getAilment() { return ailment; }

    public void setCircleInfo(String info) {
        circleInfo = info;
    }

    public String getCircleInfo( ){ return circleInfo;}

}
