package com.yingjiesun.celerminiclips.fragments;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.yingjiesun.celerminiclips.utilities.StringUtil;


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
        String uriPath;
        try {
            uriPath = StringUtil.checkNull(thisClip.getVideoUrl());

        }catch(Exception e){
            Log.i("tag", "*** Mp4 Fragment Exception" + e);
            uriPath = "";
        }
        Log.i("tag", "*** Clip URL: " + uriPath);
        vv.setVideoURI(Uri.parse(uriPath));
       // vv.setMediaController(mediacontroller);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
               // progressBar.setVisibility(View.GONE);
                vv.start();
            }
        });
        return rootView;
    }
}
