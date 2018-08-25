package com.yingjiesun.celerminiclips.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.yingjiesun.celerminiclips.interfaces.DaoAccess;
import com.yingjiesun.celerminiclips.models.VideoClip;

@Database (entities = {VideoClip.class}, version = 1, exportSchema = false)
public abstract class MiniClipDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
