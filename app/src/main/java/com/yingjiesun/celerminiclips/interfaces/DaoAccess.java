package com.yingjiesun.celerminiclips.interfaces;
/**
 * Yingjie Sun 8/21/2018
 * ROOM Database Access
 * */

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yingjiesun.celerminiclips.models.VideoClip;

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertOnlySingleMovie (VideoClip videoClip);

    @Insert
    void insertMultipleMovies (List<VideoClip> videoClipsList);

    @Query("SELECT * FROM VideoClip WHERE id = :movieId")
    VideoClip fetchOneMoviesbyMovieId (int movieId);

    @Query("SELECT * FROM VideoClip" )
    List<VideoClip> fetchAllMovies();

    @Update
    void updateVideoClip (VideoClip videoClip);

    @Delete
    void deleteVideoClip (VideoClip videoClip);

}
