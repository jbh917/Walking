package jangcho.walking;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


/**
 * Created by 장보현1 on 2017-02-01.
 */

public class WalkingService extends Service {

    private int timer_sec=0;

    private Thread mCountThread;

    public int getCurCountNumber(){
        return timer_sec;
    }

    public class LocalBinder extends Binder {
        WalkingService getCountService(){
            return WalkingService.this;
        }
    }

    private final Binder mBinder = new LocalBinder();

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

        return START_NOT_STICKY;
    }

    public void onDestroy(){

        super.onDestroy();

    }

    @Nullable
    @Override
    public Binder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

}
