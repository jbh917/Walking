package jangcho.walking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 장보현1 on 2017-02-09.
 */

public class CustomNotiBroadCast extends BroadcastReceiver {
    int mode;
    @Override
    public void onReceive(Context context, Intent intent) {


        switch(intent.getIntExtra("value",0)){      //1은 시작 2는 DB 3은 Exit

            case 1:{
                if(MainActivity.mode==1){

                    MainActivity.button_start.performClick();

                }else{
                    Toast.makeText( context, "측정중입니다.", Toast.LENGTH_SHORT).show();

                }
                
               
                break;
            }
            case 2:{
                MainActivity.button_db.performClick();
                break;
            }
            
            case 3:{
                if(MainActivity.mode==1){
                    Toast.makeText( context, "시작 버튼을 먼저 눌러주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    MainActivity.button_exit.performClick();
                }
                
                break;
            }

        }





    }
}
