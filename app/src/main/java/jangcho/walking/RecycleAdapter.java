package jangcho.walking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 장보현1 on 2017-02-03.
 */

public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder> {


    List<Data> items = new ArrayList<>();
    Context context;
    int weight;

    public RecycleAdapter(Context context){
        this.context = context;
        this.weight = (int) MyProfile.getValue(context, "WEIGHT");
    }

    public void add(Data data){
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



        Data item =items.get(position);

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
