package jangcho.walking;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DbActivity extends Activity {

   // ListView mListView = null;
    //ListAdapter mAdapter = null;
    RecycleAdapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

//    ArrayList<Data> mData =null;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);



//        mData = new ArrayList<Data>();
        mRealm = Realm.getDefaultInstance();
        mAdapter = new RecycleAdapter(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        /////리스트뷰 생성_start

        RealmResults<UserInfo> userList =  mRealm.where(UserInfo.class).findAll();

        for(int i=0;i<userList.size();i++){

            Data data = new Data();
            data.date = userList.get(i).getDate();
            data.time = userList.get(i).getTime();
            data.distance = userList.get(i).getDistance();
            mAdapter.add(data);

        }
/*
        mAdapter = new ListAdapter(this, mData);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
*/



        /////리스트뷰 생성_end


    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.exit:{
                mRealm.close();
                finish();
                break;
            }


        }




    }

}
