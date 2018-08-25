package com.yingjiesun.celerminiclips.utilities;

import android.util.Log;
import com.yingjiesun.celerminiclips.models.VideoClip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtil {
    public static VideoClip getVideoClipFromJson(JSONObject input){
        VideoClip returnObj = new VideoClip();
        if (input != null){
            try {
                returnObj.setId(input.getInt("id"));
                returnObj.setImageUrl(input.getString("imageUrl"));
                returnObj.setVideoUrl(input.getString("videoUrl"));
            }catch(Exception e){
                Log.i("tag", "*** getVideoClipFromJson Exception: " + e);
            }
            return returnObj;
        }
        return null;
    }

    public static ArrayList<VideoClip> getClipsFromJsonArray(JSONArray input){
        ArrayList<VideoClip> returnObj = new ArrayList<VideoClip>();
        if (input != null){
            for (int i=0;i<input.length();i++) {
                try {
                    returnObj.add(getVideoClipFromJson(input.getJSONObject(i)));
                } catch (Exception e) {
                    Log.i("tag", "*** getClipsFromJsonArray Exception: " + e);
                }
            }
            return returnObj;
        }
        return null;
    }
}
