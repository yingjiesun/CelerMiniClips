package com.yingjiesun.celerminiclips.activities;
/**
 * Yingjie Sun 8/21/2018
 * */
import android.arch.persistence.room.Room;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.yingjiesun.celerminiclips.R;
import com.yingjiesun.celerminiclips.adapter.CoverFlowAdapter;
import com.yingjiesun.celerminiclips.adapter.ViewPagerAdapter;
import com.yingjiesun.celerminiclips.room.MiniClipDatabase;
import com.yingjiesun.celerminiclips.fragments.Mp4;
import com.yingjiesun.celerminiclips.models.ImageEntity;
import com.yingjiesun.celerminiclips.models.VideoClip;
import com.yingjiesun.celerminiclips.network.HttpGet;
import com.yingjiesun.celerminiclips.utilities.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends FragmentActivity {

    private final String DATABASE_NAME = "videoclip_db";
    private MiniClipDatabase miniClipDatabase;
    ViewPager viewPager;
    TabLayout tabLayout;
    public static List<VideoClip> videoClips;
    FeatureCoverFlow mCoverFlow;
    CoverFlowAdapter mAdapter;
    private ArrayList<ImageEntity> mData = new ArrayList<>(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize_components();
    }

    private void initialize_components() {
        miniClipDatabase = Room.databaseBuilder(getApplicationContext(),MiniClipDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        videoClips =  JsonUtil.getClipsFromJsonArray(HttpGet.getJsonArr());

        Log.i("tag", "*** videoClips size: " + videoClips.size());

        if (videoClips.size() > 0 ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        miniClipDatabase.daoAccess().insertMultipleMovies(videoClips);
                    }catch (Exception e){
                        Log.i("tag", "*** insertMultipleMovies Exception: " + e);
                    }
                }
            }) .start();
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        for (int i=0;i<videoClips.size();i++){

            final Fragment myFrag = Mp4.newInstance(videoClips.get(i).getId());
            Log.i("tag", "*** videoClip id: " + videoClips.get(i).getId());
            Log.i("tag", "*** videoClip URL: " + videoClips.get(i).getVideoUrl());
           // viewPagerAdapter.addFragment(myFrag, videoClips.get(i).getImageUrl()+"");
            viewPagerAdapter.addFragment(myFrag);
        }

        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getBaseContext(), "Current position: " + position, Toast.LENGTH_SHORT).show();
                try {
                   // mCoverFlow.setSeletedItemPosition(position);
                    mCoverFlow.scrollToPosition(position);
                   // Toast.makeText(getBaseContext(), "Current position: " + position, Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.i("tag", "*** ViewPager.OnPageChangeListener: " + e);
                }

                if (position == 0) {

                }

                if (position == 3) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(viewPagerAdapter);
/*
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
*/

        for (int i=0;i<videoClips.size();i++) {
            mData.add(new ImageEntity(videoClips.get(i).getId(), videoClips.get(i).getImageUrl()));
        }
        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO CoverFlow item clicked
                viewPager.setCurrentItem(position);
            }
        });



        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //TODO CoverFlow stopped to position
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onScrolling() {
                //TODO CoverFlow began scrolling
            }
        });

    }
}
