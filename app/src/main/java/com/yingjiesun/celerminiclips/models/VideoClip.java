package com.yingjiesun.celerminiclips.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Yingjie Sun 8/21/2018
 * This hold all data for a single video clip
 * */
@Entity
public class VideoClip {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;


    @SerializedName("id")
    Integer id;
    @SerializedName("imageUrl")
    String imageUrl;
    @SerializedName("videoUrl")
    String videoUrl;
    String image_local = "image_local"; //default value, will be updated when saving to internal storage
    String video_local = "video_local"; //default value, will be updated when saving to internal storage
 @Ignore
  public VideoClip( ){}

    public VideoClip( Integer id, String imageUrl, String videoUrl){
        this.id = id;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImage_local() {
        return image_local;
    }

    public String getVideo_local() {
        return video_local;
    }

    public void setImage_local(String image_local) {
        this.image_local = image_local;
    }

    public void setVideo_local(String video_local) {
        this.video_local = video_local;
    }

    @NonNull
    public int getUid() {
        return uid;
    }

    public void setUid(@NonNull int uid) {
        this.uid = uid;
    }

}
