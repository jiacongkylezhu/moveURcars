package com.kylezhudev.moveurcars.recyclerView;


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

import com.kylezhudev.moveurcars.AlarmTask;
import com.kylezhudev.moveurcars.R;
import com.kylezhudev.moveurcars.model.DatabaseHelper;
import com.kylezhudev.moveurcars.model.SelectedDates;
import com.kylezhudev.moveurcars.model.Street;

import java.util.ArrayList;
import java.util.List;


public class AlarmFragment extends Fragment {


    public RecyclerView rvAlarmList;
    private AlarmRVAdapter alarmRVAdapter;
    private View rootView;
    List<SelectedDates> mSelectedDates = new ArrayList<>();
    List<Street> mSelectedStreets = new ArrayList<>();
    private TextView tvNoDataFound;
    private static int alarmID = 0;
    private static boolean mDeleteFlag;
    private DatabaseHelper mDbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_saved_alarm_list, container, false);
        tvNoDataFound = (TextView) getActivity().findViewById(R.id.tv_no_data_found);
        rvAlarmList = (RecyclerView) view.findViewById(R.id.rv_alarm_list);
        rootView = getActivity().findViewById(R.id.rootlayout);
        setupSelectedDates();
        setupStreet();


        rvAlarmList = (RecyclerView) view.findViewById(R.id.rv_alarm_list);
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


        return mSelectedStreets;
    }

//    public List<Street> updateStreet() {
//        List<Street> updatedStreets = new ArrayList<>();
//
//        SharedPreferences spStreetID = getActivity().getSharedPreferences(numSP, Context.MODE_PRIVATE);
//        int numStreetID = spStreetID.getInt(SP_STREET_ID_KEY, -1);
//        if (numStreetID != -1) {
//            SharedPreferences spStreet = getActivity().getSharedPreferences(streetSP + Integer.toString(numStreetID), Context.MODE_PRIVATE);
//            mSelectedStreets.add(numStreetID - 1, new Street(
//                    spStreet.getString(SP_STREET_KEY, "-1"),
//                    spStreet.getString(SP_SIDE_KEY, "-1")
//            ));
////            alarmRVAdapter.notifyDataSetChanged();
//
//        }
//        return updatedStreets;
//    }

    public void setupRecyclerView(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        alarmRVAdapter = new AlarmRVAdapter(getContext(), mSelectedDates, mSelectedStreets);
        recyclerView.setAdapter(alarmRVAdapter);
        checkIfEmpty();


        //TODO notify data changed might be placed in a different location
//        alarmRVAdapter.notifyDataSetChanged();


    }


    private List<SelectedDates> setupSelectedDates() {
        mDbHelper = DatabaseHelper.getInstance(getContext());;

        int itemCounter = mDbHelper.getItemIndex();
        if (itemCounter != -1) {
            for (int i = 0; i < itemCounter; i++) {
                mSelectedDates.add(i, new SelectedDates(
                        mDbHelper.getYear(i+1),
                        mDbHelper.getMonth(i+1),
                        mDbHelper.getDayOfMonth(i+1),
                        mDbHelper.getHour(i+1),
                        mDbHelper.getMinute(i+1)
                ));
            }
            mDbHelper.closeDB();
            mDbHelper.close();
        }

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

            checkIfEmpty();
            mDbHelper.closeDB();
            mDbHelper.close();
            alarmRVAdapter.notifyDataSetChanged();
        }

        return updatedList;
    }
    public void refreshUI(){
        alarmRVAdapter.notify();
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
        final Street removedStreet = mSelectedStreets.get(position);
        AlarmRVAdapter.AlarmViewHolder vh = (AlarmRVAdapter.AlarmViewHolder) rvAlarmList.findViewHolderForAdapterPosition(position);
        Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();
//        String dataString = vh.getAlarmDateString();
        String strTag = vh.getTagString();
        mSelectedDates.remove(position);
        mSelectedStreets.remove(position);
        alarmRVAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(), "Deleted Alarm ID: " + strTag, Toast.LENGTH_SHORT).show();

        Snackbar.make(rootView, "Item Deleted", 10000)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedDates.add(position, removedDate);
                        mSelectedStreets.add(position,removedStreet);
                        alarmRVAdapter.notifyItemChanged(position);
                        mDeleteFlag = false;
                    }
                })
                .show();

        if (mDeleteFlag) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
            alarmID = dbHelper.getID(position + 1);
            Log.i("deleteID", "Deleting row where id = " + alarmID);
            new AlarmTask(getContext(), alarmID, mDeleteFlag).stop();
            dbHelper.deleteItem(position + 1);
            dbHelper.closeDB();
            dbHelper.close();
        }
    }
}


