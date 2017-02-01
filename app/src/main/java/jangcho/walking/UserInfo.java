package jangcho.walking;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 장보현1 on 2017-01-31.
 */

public class UserInfo extends RealmObject {

    private String date;
    private int time;
    private double distance;

    public String getDate(){
        return date;
    }

    public void setDate(){
        long now = System.currentTimeMillis();
        Date date  = new Date(now);
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String strDate = dateFormat.format(date);
        this.date = strDate;
    }

    public int getTime(){
        return this.time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public double getDistance(){
        return this.distance;
    }

    public void setDistance(double distance){
        this.distance = distance;

    }


}
