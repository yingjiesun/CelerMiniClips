package com.yingjiesun.celerminiclips.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import com.yingjiesun.celerminiclips.R;
import com.yingjiesun.celerminiclips.activities.MainActivity;
import com.yingjiesun.celerminiclips.models.VideoClip;
import com.yingjiesun.celerminiclips.persistence.PersistenceHelper;
import com.yingjiesun.celerminiclips.utilities.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class Mp4 extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private VideoClip thisClip;
    private VideoView vv;
    private MediaController mediacontroller;
    private Uri uri;
    private ProgressBar progressBar;

    public Mp4() {
    }

    public static Mp4 newInstance(int param1) {
        Mp4 fragment = new Mp4();
        Bundle args = new Bundle();
       args.putInt(ARG_ITEM_ID, param1);
       // args.putString(ARG_ITEM_ID, "" + param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            try{
                thisClip = MainActivity.videoClips.get(getArguments().getInt(ARG_ITEM_ID) - 1);
            } catch (Exception e){
                Log.i("tag", "*** Mp4 Fragment videoClips Exception" + e);
            }
            Activity activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mp4, container, false);
        vv = (VideoView) rootView.findViewById(R.id.VideoView);
        mediacontroller = new MediaController(getActivity());
        final String uriPath_video, uriPath_image;

        try {
            uriPath_video = StringUtil.checkNull(thisClip.getVideoUrl());
            uriPath_image  = StringUtil.checkNull(thisClip.getImageUrl());

            if (uriPath_video.contains("MiniClips_")){
                File path = new File(uriPath_video);
                vv.setVideoPath(path.getAbsolutePath());
            } else {
                vv.setVideoURI(Uri.parse(uriPath_video));
            }
            // vv.setMediaController(mediacontroller);
            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    // progressBar.setVisibility(View.GONE);

                    try {
                        File videoFile, imageFile;
                        //File filepath = Environment.getExternalStorageDirectory();
                        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File dir = new File(filepath.getAbsolutePath());
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        videoFile = new File(dir, "MiniClips_" + thisClip.getId() +".mp4");
                        imageFile = new File(dir, "MiniClips_" + thisClip.getId() +".jpg");
                        PersistenceHelper.downloadFile(getContext(), uriPath_video, videoFile );
                        PersistenceHelper.downloadFile(getContext(), uriPath_image, imageFile );
                        
                    } catch (Exception e) {
                    }
                    vv.start();
                }
            });

        }catch(Exception e){
            Log.i("tag", "*** Mp4 Fragment Exception" + e);

        }


        return rootView;
    }
}
