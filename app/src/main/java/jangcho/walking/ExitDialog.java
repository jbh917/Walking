package jangcho.walking;

import android.app.Activity;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 장보현1 on 2017-01-31.
 */

public class ExitDialog extends Activity {

    int time;


    WalkingService mWalkingService =null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWalkingService = ((WalkingService.LocalBinder)service).getCountService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.dialog_exit);

        Intent intent = getIntent();
        time = intent.getExtras().getInt("time");

    }

    public void onClick(View v){
        switch(v.getId()){


            case R.id.yes:{
                Intent serviceIntent = new Intent("jangcho.service.WalkingService");
                serviceIntent.putExtra("time",time);
                startService(serviceIntent);

                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());

                break;
            }
            case R.id.no:{
                finish();
                break;
            }


        }



    }

}