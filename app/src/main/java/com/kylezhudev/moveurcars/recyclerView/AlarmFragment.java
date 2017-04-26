package com.kylezhudev.moveurcars.recyclerView;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kylezhudev.moveurcars.R;
import com.kylezhudev.moveurcars.model.DatabaseHelper;
import com.kylezhudev.moveurcars.model.SelectedDates;
import com.kylezhudev.moveurcars.model.Street;

import java.util.ArrayList;
import java.util.List;


public class AlarmFragment extends Fragment {

    //    @BindView(R.id.rv_alarm_list)
    public RecyclerView rvAlarmList;
    private AlarmRVAdapter alarmRVAdapter;
    private View rootView;
    List<SelectedDates> mSelectedDates = new ArrayList<>();
    List<Street> mSelectedStreets = new ArrayList<>();
    //    private static AtomicInteger alarmIds = new AtomicInteger(0);
//    private static String alarmId;
    private TextView tvNoDataFound;
    private String TAG = "Date Creation";
    private final static String ALARM_ID = "spAlarmId";
    private final static String SP_YEAR_KEY = "Year";
    private final static String SP_MONTH_KEY = "Month";
    private final static String SP_DAY_KEY = "Day";
    private final static String SP_HOUR_KEY = "Hour";
    private final static String SP_MINUTE_KEY = "Minute";
    private final static String SP_ALARM_ID = "AlarmId";

    private String streetSP = "streetSP", numSP = "numSP";
    private int numStored;

    private static final String SP_STREET_KEY = "street";
    private static final String SP_SIDE_KEY = "side";
    private static final String SP_STREET_ID_KEY = "side";
    private static boolean mDeleteFlag;
    private DatabaseHelper mDbHelper;


//    private SelectedDates[] dummyDates = {
//            new SelectedDates(2017, 1, 24),
//            new SelectedDates(2017, 2, 7),
//            new SelectedDates(2017, 2, 15),
//            new SelectedDates(2017, 2, 22),
//            new SelectedDates(2017, 3, 1),
//            new SelectedDates(2017, 3, 4),
//            new SelectedDates(2017, 3, 7),
//            new SelectedDates(2017, 3, 9),
//            new SelectedDates(2017, 3, 15),
//            new SelectedDates(2017, 3, 23),
//            new SelectedDates(2017, 3, 30),
//            new SelectedDates(2017, 4, 2),
//            new SelectedDates(2017, 4, 10)};

//    private Street[] dummyStreets = {
//            new Street("2nd Ave, San Francisco"),
//            new Street("3rd Ave, San Francisco"),
//            new Street("4th Ave, San Francisco"),
//            new Street("5th Ave, San Francisco"),
//            new Street("6th Ave, San Francisco"),
//            new Street("7th Ave, San Francisco"),
//            new Street("8th Ave, San Francisco"),
//            new Street("9th Ave, San Francisco"),
//            new Street("10th Ave, San Francisco"),
//            new Street("14tht Ave, San Francisco"),
//            new Street("15th Ave, San Francisco"),
//            new Street("16th Ave, San Francisco"),
//            new Street("17th Ave, San Francisco")};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_saved_alarm_list, container, false);
        tvNoDataFound = (TextView) getActivity().findViewById(R.id.tv_no_data_found);
        rvAlarmList = (RecyclerView) view.findViewById(R.id.rv_alarm_list);
        rootView = getActivity().findViewById(R.id.rootlayout);
        setupSelectedDates();
        setupStreet();

//        if (mSelectedDates == null) {
//            tvNoDataFound = (TextView) getActivity().findViewById(R.id.tv_no_data_found);
//            tvNoDataFound.setVisibility(View.VISIBLE);
//
//        } else {

//        ButterKnife.bind(this, view);
        rvAlarmList = (RecyclerView) view.findViewById(R.id.rv_alarm_list);
//            tvNoDataFound.setVisibility(View.INVISIBLE);
        alarmRVAdapter = new AlarmRVAdapter(getContext(), mSelectedDates, mSelectedStreets);

        alarmRVAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                checkIfEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);

                checkIfEmpty();
            }
        });


        setupRecyclerView(rvAlarmList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(rvAlarmList);


//        }
        return view;

    }

    private List<Street> setupStreet() {
        mDbHelper = DatabaseHelper.getInstance(getContext());
        mDbHelper.getWritableDatabase();
        int itemCounter = mDbHelper.getItemIndex();
        if (itemCounter != -1) {
            for (int i = 0; i < itemCounter; i++) {
                mSelectedStreets.add(i, new Street(
                        mDbHelper.getStreet(i+1),
                        mDbHelper.getSide(i+1)
                ));
            }
            mDbHelper.closeDB();
            mDbHelper.close();
        }

//        SharedPreferences spStreetID = getActivity().getSharedPreferences(numSP, Context.MODE_PRIVATE);
//        int numStreetID = spStreetID.getInt(SP_STREET_ID_KEY, -1);
//        if (numStreetID != -1) {
//            for (int i = 0; i < numStreetID; i++) {
//                SharedPreferences spStreet = getActivity().getSharedPreferences(streetSP + Integer.toString(i + 1), Context.MODE_PRIVATE);
//                mSelectedStreets.add(i, new Street(
//                        spStreet.getString(SP_STREET_KEY, "-1"),
//                        spStreet.getString(SP_SIDE_KEY, "-1")
//                ));
//            }
//        }
        return mSelectedStreets;
    }

    public List<Street> updateStreet() {
        List<Street> updatedStreets = new ArrayList<>();

        SharedPreferences spStreetID = getActivity().getSharedPreferences(numSP, Context.MODE_PRIVATE);
        int numStreetID = spStreetID.getInt(SP_STREET_ID_KEY, -1);
        if (numStreetID != -1) {
            SharedPreferences spStreet = getActivity().getSharedPreferences(streetSP + Integer.toString(numStreetID), Context.MODE_PRIVATE);
            mSelectedStreets.add(numStreetID - 1, new Street(
                    spStreet.getString(SP_STREET_KEY, "-1"),
                    spStreet.getString(SP_SIDE_KEY, "-1")
            ));
//            alarmRVAdapter.notifyDataSetChanged();

        }
        return updatedStreets;
    }

    public void setupRecyclerView(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

//        mSelectedDates = Arrays.asList(dummyDates);
//        setupSelectedDates();
//        mSelectedStreets = Arrays.asList(dummyStreets);
        alarmRVAdapter = new AlarmRVAdapter(getContext(), mSelectedDates, mSelectedStreets);
        recyclerView.setAdapter(alarmRVAdapter);
        checkIfEmpty();


        //TODO notify data changed might be placed in a different location
//        alarmRVAdapter.notifyDataSetChanged();


    }

    //TODO 1/29 6:07 Test the method below
    private List<SelectedDates> setupSelectedDates() {
        mDbHelper = DatabaseHelper.getInstance(getContext());;
//        mDbHelper.getWritableDatabase();
        int itemCounter = mDbHelper.getItemIndex();
        if (itemCounter != -1) {
            for (int i = 0; i < itemCounter; i++) {
                mSelectedDates.add(i, new SelectedDates(
                        mDbHelper.getYear(i+1),
                        mDbHelper.getMonth(i+1),
                        mDbHelper.getDayOfMonth(i+1),
                        mDbHelper.getHour(i+1),
                        mDbHelper.getMinute(i+1)
//                        mDbHelper.getIntData(COL_2, COL_11, i).getInt(0),
//                        mDbHelper.getIntData(COL_3, COL_11, i).getInt(0),
//                        mDbHelper.getIntData(COL_4, COL_11, i).getInt(0),
//                        mDbHelper.getIntData(COL_7, COL_11, i).getInt(0),
//                        mDbHelper.getIntData(COL_8, COL_11, i).getInt(0)
                ));
            }
            mDbHelper.closeDB();
            mDbHelper.close();
        }
        //TODO 4/21 delete the following comment if above works
////        alarmId = Integer.toString(alarmIds.incrementAndGet());
//        SharedPreferences spAlarmId = getActivity().getSharedPreferences(ALARM_ID, Context.MODE_PRIVATE);
//
//        int numAlarm = spAlarmId.getInt(SP_ALARM_ID, -1);
//        if (numAlarm != -1) {
//            for (int i = 0; i < numAlarm; i++) {
//                SharedPreferences sharedPref = getActivity().getSharedPreferences(Integer.toString(i + 1), Context.MODE_PRIVATE);
//                mSelectedDates.add(i, new SelectedDates(
//                        sharedPref.getInt(SP_YEAR_KEY, -1),
//                        sharedPref.getInt(SP_MONTH_KEY, -1),
//                        sharedPref.getInt(SP_DAY_KEY, -1),
//                        sharedPref.getInt(SP_HOUR_KEY, -1),
//                        sharedPref.getInt(SP_MINUTE_KEY, -1)
//                ));
////                Log.i(TAG, "i = " + i + mSelectedDates.get(i).getSelectDates() + "added into list");
////                Toast.makeText(getActivity(), "Retrieved data from SP and added into mSelectedData list", Toast.LENGTH_SHORT).show();
////                rvAlarmList.getAdapter().notifyDataSetChanged();
//
//            }
//        }
        return mSelectedDates;
    }

    public List<SelectedDates> updateSelectedDates() {
        List<SelectedDates> updatedList = new ArrayList<>();
        if (alarmRVAdapter != null) {
            mDbHelper = DatabaseHelper.getInstance(getContext());
            int lastIndex = mDbHelper.getItemIndex();
            Log.i("last_index","Last Index = " + lastIndex);
            mSelectedDates.add(new SelectedDates(
                    mDbHelper.getYear(lastIndex),
                    mDbHelper.getMonth(lastIndex),
                    mDbHelper.getDayOfMonth(lastIndex),
                    mDbHelper.getHour(lastIndex),
                    mDbHelper.getMinute(lastIndex)));


//                    mDbHelper.getIntData(COL_2, COL_11, lastIndex).getInt(0),
//                    mDbHelper.getIntData(COL_3, COL_11, lastIndex).getInt(0),
//                    mDbHelper.getIntData(COL_4, COL_11, lastIndex).getInt(0),
//                    mDbHelper.getIntData(COL_7, COL_11, lastIndex).getInt(0),
//                    mDbHelper.getIntData(COL_8, COL_11, lastIndex).getInt(0)));

            checkIfEmpty();
            mDbHelper.closeDB();
            mDbHelper.close();
            alarmRVAdapter.notifyDataSetChanged();
        }
//        if (alarmRVAdapter != null) {
//            SharedPreferences spAlarmId = getActivity().getSharedPreferences(ALARM_ID, Context.MODE_PRIVATE);
//            int numAlarm = spAlarmId.getInt(SP_ALARM_ID, -1);
//            SharedPreferences sharedPref = getActivity().getSharedPreferences(Integer.toString(numAlarm), Context.MODE_PRIVATE);
//            mSelectedDates.add(numAlarm - 1, new SelectedDates(
//                    sharedPref.getInt(SP_YEAR_KEY, -1),
//                    sharedPref.getInt(SP_MONTH_KEY, -1),
//                    sharedPref.getInt(SP_DAY_KEY, -1),
//                    sharedPref.getInt(SP_HOUR_KEY, -1),
//                    sharedPref.getInt(SP_MINUTE_KEY, -1)
//            ));
//            checkIfEmpty();
//            alarmRVAdapter.notifyDataSetChanged();
//        }
        return updatedList;
    }

    public void checkIfEmpty() {
        if (alarmRVAdapter.getItemCount() == 0) {
            tvNoDataFound.setVisibility(View.VISIBLE);
        } else {
            tvNoDataFound.setVisibility(View.GONE);
        }
    }


    private ItemTouchHelper.Callback createHelperCallBack() {
        final ItemTouchHelper.SimpleCallback simpleItemTouchCallBack =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        ImageView btnDelete = (ImageView) getActivity().findViewById(R.id.btn_delete);
//                        btnDelete.setVisibility(View.VISIBLE);
                        deleteItem(viewHolder.getAdapterPosition(), viewHolder);
                    }
                };
        return simpleItemTouchCallBack;
    }


    private void moveItem(int curPost, int newPost) {
        SelectedDates selectedDate = mSelectedDates.get(curPost);
        mSelectedDates.remove(curPost);
        mSelectedDates.add(newPost, selectedDate);

        Street street = mSelectedStreets.get(curPost);
        mSelectedStreets.remove(curPost);
        mSelectedStreets.add(newPost, street);

        alarmRVAdapter.notifyItemMoved(curPost, newPost);

    }

    private void deleteItem(final int position, final RecyclerView.ViewHolder viewHolder) {
        mDeleteFlag = true;
        final SelectedDates removedDate = mSelectedDates.get(position);
        AlarmRVAdapter.AlarmViewHolder vh = (AlarmRVAdapter.AlarmViewHolder) rvAlarmList.findViewHolderForAdapterPosition(position);
        Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();
//        String dataString = vh.getAlarmDateString();
        String strTag = vh.getTagString();
        mSelectedDates.remove(position);
        alarmRVAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(), "Deleted Alarm ID: " + strTag, Toast.LENGTH_SHORT).show();

        Snackbar.make(rootView, "Item Deleted", 10000)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedDates.add(position, removedDate);
                        alarmRVAdapter.notifyItemChanged(position);
                        mDeleteFlag = false;
                    }
                })
                .show();

        if (mDeleteFlag) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
            dbHelper.deleteItem(position);
            dbHelper.closeDB();
            dbHelper.close();
        }
    }
}


