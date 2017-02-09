package jangcho.walking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 장보현1 on 2017-02-03.
 */

public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperListener{


    List<Data> items = new ArrayList<>();
    Context context;
    int weight;
    Realm mRealm;

    public RecycleAdapter(Context context){
        this.context = context;
        this.weight = (int) MyProfile.getValue(context, "WEIGHT");
        mRealm = Realm.getDefaultInstance();
    }

    public void add(Data data){
        Log.i("add","add");
        items.add(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final Data item =items.get(position);

        int hour;
        int minute;
        int second;

        hour = item.getTime() / 3600;
        minute = (item.getTime() - (hour * 3600)) / 60;
        second = (item.getTime() - (hour * 3600) - (minute * 60));
        String time = String.format("%02d:%02d:%02d", hour, minute, second);


        holder.mTime.setText(time);
        holder.mDistance.setText(""+item.getDistance()+"m");
        holder.mDate.setText(item.getDate());
        holder.mCalorie.setText(""+Double.parseDouble(String.format("%.2f",3.3*(3.5*weight*item.getTime()/60)*5/1000))+"Kcal");
/*
        holder.lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("" + item.getDate() + "의 일기내용을 정말로 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final RealmResults<UserInfo> result = mRealm.where(UserInfo.class)
                                        .equalTo("date",item.getDate())
                                        .equalTo("distance",item.getDistance())
                                        .equalTo("time",item.getTime())
                                        .findAll();
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        result.deleteAllFromRealm();

                                    }
                                });

                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();



                return false;
            }
        });

*/

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemRemove(int position) {

        final RealmResults<UserInfo> result = mRealm.where(UserInfo.class)
                .equalTo("date",items.get(position).getDate())
                .equalTo("distance",items.get(position).getDistance())
                .equalTo("time",items.get(position).getTime())
                .findAll();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();

            }
        });

        items.remove(position);
        notifyItemRemoved(position);
    }
}
