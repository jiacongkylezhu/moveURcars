package com.kylezhudev.moveurcars;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.kylezhudev.moveurcars.recyclerView.AlarmFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ViewPager vpList;
    private static final int REQ_ALARM_INFO = 101;
    private static AlarmFragment alarmFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        vpList = (ViewPager) findViewById(R.id.vp_list);

//                ButterKnife.bind(this);
        setupViewPager(vpList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    getWindow().setExitTransition(new Explode());
                    startActivity(new Intent(MainActivity.this, StreetSetup.class),
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                }else{
                    startActivity(new Intent(MainActivity.this, StreetSetup.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO 5.5.2017 work on the resume method to refreshUI
        getAlarmFragment().refreshUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        alarmFragment = new AlarmFragment();
        vpAdapter.addFrag(alarmFragment, "Coordinator Layout");
        viewPager.setAdapter(vpAdapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    public static AlarmFragment getAlarmFragment() {
        return alarmFragment;
    }
}
