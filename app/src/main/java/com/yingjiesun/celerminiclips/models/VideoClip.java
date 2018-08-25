package com.yingjiesun.celerminiclips.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Yingjie Sun 8/21/2018
 * This hold all data for a single video clip
 * */
@Entity
public class VideoClip {

    @NonNull
    @PrimaryKey
    int id;

    String imageUrl;
    String videoUrl;
    String image_local = "image_local";
    String video_local = "video_local";

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setId(int id) {
        this.id = id;
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
}
