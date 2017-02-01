package jangcho.walking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 장보현1 on 2017-01-31.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Data> mData =null;
    int weight;
    LayoutInflater layoutInflater;


    public ListAdapter(Context context, ArrayList<Data> data){
        this.context = context;
        mData = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.weight=(int) MyProfile.getValue(context, "WEIGHT");;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_list,parent,false);
        }

        int hour;
        int minute;
        int second;

        hour = mData.get(position).time / 3600;
        minute = (mData.get(position).time - (hour * 3600)) / 60;
        second = (mData.get(position).time - (hour * 3600) - (minute * 60));
        String time = String.format("%02d:%02d:%02d", hour, minute, second);


        TextView content_date = (TextView) convertView.findViewById(R.id.date);
        TextView content_time = (TextView) convertView.findViewById(R.id.time);
        TextView content_distance = (TextView) convertView.findViewById(R.id.distance);
        TextView content_calorie = (TextView) convertView.findViewById(R.id.calorie);

        content_date.setText(""+mData.get(position).date);
        content_time.setText(time);
        content_distance.setText(""+mData.get(position).distance+"m");
        content_calorie.setText(""+Double.parseDouble(String.format("%.2f",3.3*(3.5*weight*mData.get(position).time/60)*5/1000))+"Kcal");


        return convertView;
    }
}
