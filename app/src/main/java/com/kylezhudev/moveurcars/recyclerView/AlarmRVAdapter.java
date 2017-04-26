package com.kylezhudev.moveurcars.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylezhudev.moveurcars.R;
import com.kylezhudev.moveurcars.model.SelectedDates;
import com.kylezhudev.moveurcars.model.Street;

import java.text.SimpleDateFormat;
import java.util.List;




public class AlarmRVAdapter extends RecyclerView.Adapter<AlarmRVAdapter.AlarmViewHolder> {

    private Context mContext;
    private List<SelectedDates> mSelectedDates;
    private List<Street> mStreet;



    //TODO 1/24/2017 8PM: Modify constructor adding mStreet and set up dummyData in AlarmFragment. Then, make RecyclerView work on Home.
    public AlarmRVAdapter(Context context, List<SelectedDates> selectedDates, List<Street> selectedStreet) {
        this.mContext = context;
       this.mSelectedDates = selectedDates;
        this.mStreet = selectedStreet;

    }

    public Context getmContext() {
        return mContext;
    }

    //TODO(5) Modify the followings:

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_alarm_date)
        private TextView mTvAlarmDate;
//        @BindView(R.id.tv_street_name)
        private TextView mTvDayOfWeek;
        private TextView mTvStreet;
        private TextView mTvTag;
        private ImageView mImageView;


        public AlarmViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mTvAlarmDate = (TextView) itemView.findViewById(R.id.tv_alarm_date);
            mTvDayOfWeek = (TextView) itemView.findViewById(R.id.tv_day_of_week);
            mTvStreet =  (TextView) itemView.findViewById(R.id.tv_street_name);
            mTvTag= (TextView) itemView.findViewById(R.id.tv_tag);
            mImageView = (ImageView) itemView.findViewById(R.id.img_side_icon);
        }

        public String getAlarmDateString(){
            return mTvAlarmDate.getText().toString();
        }

        public String getTagString(){
            return mTvTag.getText().toString();
        }

    }



    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alarmView = inflater.inflate(R.layout.alarm_list_item, parent, false);
        //TODO(6) Check if the ButterKnife works above or modify the one below
//       ButterKnife.bind(this, itemView);

        return new AlarmViewHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        SelectedDates selectedDates = mSelectedDates.get(position);
        String strDate = new SimpleDateFormat().format(selectedDates.getSelectedDate());
        TextView tvAlarmDate = holder.mTvAlarmDate;
        tvAlarmDate.setText(strDate);
        String dayOfWeek = everyDayOfWeek(selectedDates);
        TextView tvDayOfWeek = holder.mTvDayOfWeek;
        tvDayOfWeek.setText(dayOfWeek);
        TextView tvTag = holder.mTvTag;

        //check if position + 1 == alarmId / yes
        String strTag = Integer.toString(position + 1);
        tvTag.setText(strTag);




        Street street = mStreet.get(position);
        //TODO(12) Need to delete dummy
        TextView tvStreet = holder.mTvStreet;
//      tvStreet.setText("Street: " + street.getStreetName() + ", " + street.getCity());
        tvStreet.setText(street.getStreetName());
        ImageView ivSide = holder.mImageView;
        switch (street.getSideOfStreet()){
            case "A": ivSide.setBackgroundResource(R.drawable.ic_side_a);
                break;
            case "B": ivSide.setBackgroundResource(R.drawable.ic_side_b);
                break;
        }

    }

    //TODO(7) Make sure it only gets one list's size and displays all list items including streets
    @Override
    public int getItemCount() {
        if(mSelectedDates != null) {
            return mSelectedDates.size();
        }else{
            return 0;
        }
    }



    private String everyDayOfWeek(SelectedDates selectedDates){
        String resultString, strDayOfWeek = "", strDayOfWeekInMon = "";
        int dayOfWeek, dayOfWeekInMon;
        dayOfWeekInMon = selectedDates.getDateOfWeekInMonth();
        dayOfWeek = selectedDates.getDayOfWeek();

        switch (dayOfWeekInMon){
            case 1: strDayOfWeekInMon = "1st";
                break;
            case 2: strDayOfWeekInMon = "2nd";
                break;
            case 3: strDayOfWeekInMon = "3rd";
                break;
            case 4: strDayOfWeekInMon = "4th";
                break;
        }

        switch (dayOfWeek){
            case 1: strDayOfWeek = "Sunday";
                break;
            case 2: strDayOfWeek = "Monday";
                break;
            case 3: strDayOfWeek = "Tuesday";
                break;
            case 4: strDayOfWeek = "Wednesday";
                break;
            case 5: strDayOfWeek = "Thursday";
                break;
            case 6: strDayOfWeek = "Friday";
                break;
            case 7: strDayOfWeek = "Saturday";
                break;
        }



        resultString = strDayOfWeekInMon + " " + strDayOfWeek + " of Every Month";

        return resultString;
    }






}
