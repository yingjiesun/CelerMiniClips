package com.yingjiesun.celerminiclips.activities;
/**
 * Yingjie Sun 8/21/2018
 * */
import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.yingjiesun.celerminiclips.R;
import com.yingjiesun.celerminiclips.adapter.CoverFlowAdapter;
import com.yingjiesun.celerminiclips.adapter.ViewPagerAdapter;
import com.yingjiesun.celerminiclips.interfaces.GetDataService;
import com.yingjiesun.celerminiclips.network.RetrofitClientInstance;
import com.yingjiesun.celerminiclips.room.MiniClipDatabase;
import com.yingjiesun.celerminiclips.fragments.Mp4;
import com.yingjiesun.celerminiclips.models.ImageEntity;
import com.yingjiesun.celerminiclips.models.VideoClip;

import com.yingjiesun.celerminiclips.utilities.JsonUtil;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity {

    private final String DATABASE_NAME = "videoclip_db";
    private MiniClipDatabase miniClipDatabase;
    ViewPager viewPager;
    public static List<VideoClip> videoClips;
    FeatureCoverFlow mCoverFlow;
    CoverFlowAdapter mAdapter;
    private ArrayList<ImageEntity> mData = new ArrayList<>(0);
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        miniClipDatabase = Room.databaseBuilder(getApplicationContext(),MiniClipDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        initialize_components();
    }

    private void initialize_components() {
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<VideoClip>> call = service.getAllClips();
        call.enqueue(new Callback<List<VideoClip>>() {
            @Override
            public void onResponse(Call<List<VideoClip>> call, Response<List<VideoClip>> response) {
                try {
                    videoClips = response.body();
                    Log.i("tag", "*** From Retrofit, videoClips size: " + videoClips.size());
                }catch(Exception e){}
            }
            @Override
            public void onFailure(Call<List<VideoClip>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        if ( videoClips == null || videoClips.size() == 0 ) {
            videoClips = JsonUtil.getClipsFromJsonArray(getJsonArr());
        }

        if (videoClips != null && videoClips.size() > 0 ){
            setVideoClipsLocalFilesPath();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        miniClipDatabase.daoAccess().insertMultipleMovies(videoClips);
                        Log.i("tag", "*** insertMultipleMovies to DB Done " );
                    }catch (Exception e){
                        Log.i("tag", "*** insertMultipleMovies Exception: " + e);
                    }
                }
            }) .start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        videoClips = miniClipDatabase.daoAccess().fetchAllMovies();
                        Log.i("tag", "*** fetchAllMovies from DB done " );
                        setVideoClipsToLocalPath();
                    }catch (Exception e){
                        Log.i("tag", "*** fetchAllMovies Exception: " + e);
                    }
                }
            }) .start();
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        for (int i=0;i<videoClips.size();i++){
            final Fragment myFrag = Mp4.newInstance(videoClips.get(i).getId());
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
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(viewPagerAdapter);

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

    //default data, load this if all others fail
    public static JSONArray getJsonArr(){
        JSONArray jsonArr;
        String json_str = "[{\"id\":1,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_1.jpg\",\"videoUrl\":\"https://media.giphy.com/media/l0ExncehJzexFpRHq/giphy.mp4\"},{\"id\":2,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_2.jpg\",\"videoUrl\":\"https://media.giphy.com/media/26gsqQxPQXHBiBEUU/giphy.mp4\"},{\"id\":3,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_3.jpg\",\"videoUrl\":\"https://media.giphy.com/media/oqLgjAahmDPvG/giphy.mp4\"},{\"id\":4,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_4.jpg\",\"videoUrl\":\"https://media.giphy.com/media/d1E1szXDsHUs3WvK/giphy.mp4\"},{\"id\":5,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_5.jpg\",\"videoUrl\":\"https://media.giphy.com/media/OiJjUsdAb11aE/giphy.mp4\"},{\"id\":6,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_6.jpg\",\"videoUrl\":\"https://media.giphy.com/media/4My4Bdf4cakLu/giphy.mp4\"}]";
        try {
            jsonArr = new JSONArray(json_str);
            return jsonArr;
        } catch (Exception e) {
        }
        return null;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //Save video and image to default download folder, and update model data
    private void setVideoClipsLocalFilesPath(){
        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (videoClips.size() > 0 ) {
            for (int i=0;i<videoClips.size();i++){
                videoClips.get(i).setImage_local(filepath + "/MiniClips_" + videoClips.get(i).getId() + ".jpg");
                videoClips.get(i).setVideo_local(filepath + "/MiniClips_" + videoClips.get(i).getId() + ".mp4");
                Log.i("tag", "*** VideoClips getImage_local: " +  videoClips.get(i).getImage_local());
                Log.i("tag", "*** VideoClips getVideo_local: " +  videoClips.get(i).getVideo_local());
            }
        }
    }

    //if network fail, use local videos and images
    private void setVideoClipsToLocalPath(){
        Log.i("tag", "*** setVideoClipsToLocalPath Called " );
        if (videoClips.size() > 0 ) {
            for (int i=0;i<videoClips.size();i++){
                videoClips.get(i).setImageUrl(videoClips.get(i).getImage_local());
                videoClips.get(i).setVideoUrl(videoClips.get(i).getVideo_local());
            }
        }
    }

}
