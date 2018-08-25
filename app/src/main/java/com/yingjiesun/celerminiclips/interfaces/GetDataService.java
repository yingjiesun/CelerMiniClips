package com.yingjiesun.celerminiclips.interfaces;

import com.yingjiesun.celerminiclips.models.VideoClip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/pictures")
    Call<List<VideoClip>> getAllClips();

}
