package jangcho.walking;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 장보현1 on 2017-02-01.
 */

public class WalkingService extends Service {

    private int timer_sec=0;
    private double distance=0;
    private TimerTask second;

    private Thread mCountThread;
    IWalkingService.Stub mBinder = new IWalkingService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getTime() throws RemoteException {

            return timer_sec;
        }

        @Override
        public double getDisatance() throws RemoteException {
            return distance;
        }
    };


    public void onCreate(){
        super.onCreate();
        //////notification_start
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Walking Service")
                .setContentText("측정 중입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        startForeground(1230,noti);

        //////notification_end




    }

    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);


        timerStart();


        return START_NOT_STICKY;
    }

    public void timerStart() {

        second = new TimerTask() {


            @Override
            public void run() {
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    public void onDestroy(){
        second.cancel();
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

}
