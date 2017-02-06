package jangcho.walking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 장보현1 on 2017-01-31.
 */

public class ExitDialog extends Activity {

    int time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.dialog_exit);


    }


    public void onClick(View v){
        switch(v.getId()){


            case R.id.yes:


            {

                finish();
                MainActivity.main_activty.finish();

                break;
            }
            case R.id.no:{
                finish();
                break;
            }


        }



    }

}