package jangcho.walking;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 장보현1 on 2017-02-01.
 */

public class WalkingService extends Service {

    private int timer_sec = 0;
    private double distance = 0;
    private String debug = "";
    private TimerTask second;
    private Timer timer;

    LocationManager lm;
    String provider;
    int count = 0;
    double templat;
    double templon;


    IWalkingService.Stub mBinder = new IWalkingService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }


        @Override
        public int getTime() throws RemoteException {


            Log.i("getTime", "getTime");

            return timer_sec;
        }

        @Override
        public double getDisatance() throws RemoteException {
            return distance;
        }

        public String getDebug() throws RemoteException {
            return debug;
        }
    };


    public void onCreate() {
        super.onCreate();
        //////notification_start
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Walking Service")
                .setContentText("측정 중입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        startForeground(1230, noti);

        //////notification_end
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = lm.getBestProvider(new Criteria(), true);


        Log.i("on create", "on create");

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (second != null) {
            lm.removeUpdates(gpsListener);
            timer_sec = 0;
            distance = 0;
            debug="";
            second.cancel();
            timer.cancel();
            count = 0;
        }

        timer_sec = 0;
        timerStart();

        //////////////gps_start
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        lm.requestLocationUpdates(provider, 1000, 0, gpsListener);
        //////////////gps_end

        Log.i("on StartCommand", "onStartcommand");


        return START_NOT_STICKY;
    }


    public void timerStart() {

        second = new TimerTask() {


            @Override
            public void run() {
                timer_sec++;
            }
        };
        timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    public void onDestroy() {
        if(second!=null){
            second.cancel();
            timer.cancel();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(gpsListener);
        super.onDestroy();

        Log.i("on Destroy","on Destroy");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i("on bind","on bind");

        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

    LocationListener gpsListener = new LocationListener()   // GPS가 정보가 변경 될때 호출 되는 리스너
    {
        @Override
        public void onLocationChanged(Location location)    // 위에서 설정한 위치 변경시 호출
        {


            count++;
            if(count==1){

                templat = location.getLatitude();
                templon = location.getLongitude();
            }else{

                distance += distance(templat,templon,location.getLatitude(),location.getLongitude());
                debug = String.format("수신회수:%d\nProvider:%s\n위도:%f\n경도:%f\n거리:%f", count,location.getProvider(),location.getLatitude(), location.getLongitude(),distance);

                templat=location.getLatitude();
                templon =location.getLongitude();

            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) // 상태 변경시 호출
        {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }

    };

    public double distance(double lat1, double lon1,
                           double lat2, double lon2) {

        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;

        EARTH_R = 6371000.0;
        Rad = Math.PI/180;
        radLat1 = Rad * lat1;
        radLat2 = Rad * lat2;
        radDist = Rad * (lon1 - lon2);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        double rslt = Math.round(Math.round(ret));

        return rslt;



    }

}
