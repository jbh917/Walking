package jangcho.walking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 장보현1 on 2017-02-03.
 */

public class ViewHolder extends RecyclerView.ViewHolder{

    public TextView mDistance, mDate, mTime, mCalorie;
    public ViewHolder(View itemView) {
        super(itemView);
        mDate =(TextView)itemView.findViewById(R.id.date);
        mDistance=(TextView)itemView.findViewById(R.id.distance);
        mTime = (TextView)itemView.findViewById(R.id.time);
        mCalorie = (TextView)itemView.findViewById(R.id.calorie);


    }
}
