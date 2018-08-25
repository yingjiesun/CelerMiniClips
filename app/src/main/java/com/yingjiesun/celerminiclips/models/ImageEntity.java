package com.yingjiesun.celerminiclips.models;
/**
 * Yingjie Sun 8/21/2018
 * This is to hold the image URL for the bottom coverflow
 * The values come from VideoClip model
 * */
public class ImageEntity {
    public int id;
    public String imageURL;

    public ImageEntity (int id, String imageURL){
        this.id = id;
        this.imageURL = imageURL;
    }
}
