package jangcho.walking;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Realm mRealm;

    int sex;         //성별 (0==성별, 1==남, 2==여)
    int tall;          //키
    int weight;        //무게
    double distance;
    int timer_sec;


    private TimerTask second;
    private final Handler handler = new Handler();

    Spinner spinner_sex;
    EditText edit_tall;
    EditText edit_weight;
    TextView text_timer;
    TextView text_distance;
    TextView text_calorie;
    Button button_start;
    Button button_exit;


    private GpsInfo gps;

    LocationManager lm;
    String provider;
    Location location1;
    int count;
    double templat;
    double templon;

    WalkingService mWalkingService = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWalkingService = ((WalkingService.LocalBinder) service).getCountService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /////////////////초기화 부분_start
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getDefaultInstance();


        spinner_sex = (Spinner) findViewById(R.id.sex);
        edit_tall = (EditText) findViewById(R.id.tall);
        edit_weight = (EditText) findViewById(R.id.weight);
        text_timer = (TextView) findViewById(R.id.timer);
        text_distance = (TextView) findViewById(R.id.distance);
        text_calorie = (TextView) findViewById(R.id.calorie);
        button_start = (Button) findViewById(R.id.start);
        button_exit = (Button) findViewById(R.id.exit);


        sex = (int) MyProfile.getValue(this, "SEX");
        tall = (int) MyProfile.getValue(this, "TALL");
        weight = (int) MyProfile.getValue(this, "WEIGHT");
        distance = 0;
        //gps = new GpsInfo(MainActivity.this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = lm.getBestProvider(new Criteria(), true);

        if (sex == 1) {
            spinner_sex.setSelection(1);
        } else if (sex == 2) {
            spinner_sex.setSelection(2);
        } else if (sex == 0) {
            spinner_sex.setSelection(0);
        }
        edit_tall.setText("" + tall);
        edit_weight.setText("" + weight);

        /////////////////초기화 부분_end


        ////////////////////////////////이벤트 처리_start
        // 1. 스피너 클릭시 sharedpref에 저장

        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                MyProfile.setSex(getApplicationContext(), position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //2. editText 포커스 이벤트

        edit_weight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MyProfile.setWeight(getApplicationContext(), Integer.valueOf(edit_weight.getText().toString()));
                    weight = Integer.valueOf(edit_weight.getText().toString());
                    text_calorie.setText("" + Double.parseDouble(String.format("%.2f", cal_cal())) + "Kcal");


                }
                return false;
            }
        });

        edit_tall.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MyProfile.setTall(getApplicationContext(), Integer.valueOf(edit_tall.getText().toString()));

                }
                return false;
            }
        });

/*
        edit_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false) {
                    MyProfile.setWeight(getApplicationContext(), Integer.valueOf(edit_weight.getText().toString()));
                    weight = Integer.valueOf(edit_weight.getText().toString());
                    text_calorie.setText(""+cal_cal());
                }

            }
        });

        edit_tall.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false) {
                    MyProfile.setTall(getApplicationContext(), Integer.valueOf(edit_tall.getText().toString()));

                }

            }
        });
*/
        ////////////////////////////////이벤트 처리_end


    }


    /////////////////////////////버튼 클릭 동작_ start
    // go_db : db버튼을 눌렀을 때 dbActivity로 이동


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.go_db: {
                Intent intent = new Intent(getApplicationContext(), DbActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.start: {
                /*
                ////service_start
                Intent serviceIntent = new Intent("jangcho.service.WalkingService");
                startService(serviceIntent);

                ////service_end
                */

                if (spinner_sex.getSelectedItem().toString().equals("성별") || Integer.valueOf(edit_weight.getText().toString()) == 0 || Integer.valueOf(edit_tall.getText().toString()) == 0) {
                    Toast toast = Toast.makeText(this, "성별/몸무게/키를 입력해주세요", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                } else {
                    button_start.setVisibility(View.GONE);
                    button_exit.setVisibility(View.VISIBLE);
                    text_timer.setVisibility(View.VISIBLE);
                    text_distance.setVisibility(View.VISIBLE);
                    text_calorie.setVisibility(View.VISIBLE);

                    timer_sec = 0;
                    ////service end_start
                    if (isServiceRunningCheck()) {
                        timer_sec = mWalkingService.getCurCountNumber();
                        unbindService(mConnection);
                    }

                    ////service end_end


                    timerStart();       //타이머시작


                    //////////////gps_start
                    count = 0;
                    lm.requestLocationUpdates(provider, 1000, 0, gpsListener);
                    //////////////gps_end

                }


                break;
            }

            case R.id.exit: {

                /*
                ////service_exit_start
                Intent serviceIntent = new Intent("jangcho.service.WalkingService");
                stopService(serviceIntent);

                ////service_exit_end
                */
                button_start.setVisibility(View.VISIBLE);
                button_exit.setVisibility(View.GONE);

                second.cancel();//타이머 종료

                /////////DB_start

                mRealm.beginTransaction();
                UserInfo userinfo = mRealm.createObject(UserInfo.class);
                userinfo.setDate();
                userinfo.setTime(timer_sec);
                userinfo.setDistance(distance);
                mRealm.commitTransaction();
                ////////DB_end


                //lm.removeUpdates(mLocationListener);
                distance = 0;


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

                break;
            }


        }


    }

    /////////////////////////////버튼 클릭 동작_end


    /////////////////////////////타이머_start
    public void timerStart() {

        second = new TimerTask() {


            @Override
            public void run() {
                Update();
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                int hour;
                int minute;
                int second;

                hour = timer_sec / 3600;
                minute = (timer_sec - (hour * 3600)) / 60;
                second = (timer_sec - (hour * 3600) - (minute * 60));
                String time = String.format("%02d:%02d:%02d", hour, minute, second);

                text_timer.setText(time);

                text_calorie.setText(""+Double.parseDouble(String.format("%.2f",cal_cal()))+"Kcal");
            }
        };
        handler.post(updater);
    }
    /////////////////////////////타이머_end

    /////////////////////////////gps_start

    public void onResume() {
        super.onResume();


    }

    public void onPause() {
        super.onPause();

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
                templat=location.getLatitude();
                templon =location.getLongitude();
                String temp = String.format("수신회수:%d\nProvider:%s\n위도:%f\n경도:%f\n거리:%f", count,location.getProvider(),location.getLatitude(), location.getLongitude(),distance);
                text_distance.setText(temp);

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



/////////////////////////////gps_end

 protected  void onDestroy(){
     super.onDestroy();
     mRealm.close();
 }

    ////칼로리 구하는 식_start

    public double cal_cal(){



        return 3.3*(3.5*weight*timer_sec/60)*5/1000;
    }

    ////칼로리 구하는 식_end

    /////뒤로 가는 버튼_start
    public void onBackPressed() {

        final Intent intent = new Intent(getApplicationContext(), ExitDialog.class);
        intent.putExtra("time",timer_sec);
        startActivity(intent);
    }

    /////뒤로 가는 버튼_end

    /////서비스 확인_start

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("WalkingService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /////서비스 확인_end

}
