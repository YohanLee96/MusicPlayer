/*
 * Copyright 2015. Pureum Choe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.massivcode.androidmusicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EasyMusic.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_MEMBER =
            "CREATE TABLE IF NOT EXISTS " + MemberContract.TABLE_NAME + " (" +
                    MemberContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MemberContract.COLUMN_NAME_DEPARTMENT_NO + " INTEGER NOT NULL, " +
                    MemberContract.COLUMN_NAME_DEPARTMENT + " TEXT NOT NULL , " +
                    MemberContract.COLUMN_NAME_NAME  + " TEXT NOT NULL ," +
                    MemberContract.COLUMN_NAME_PASSWORD  + " TEXT NOT NULL " +
                    ");";

    //음원목록
    private static final String SQL_CREATE_MY_MUSIC =
            "CREATE TABLE IF NOT EXISTS " + MyMusicContract.TABLE_NAME + " (" +
                  //  MyMusicContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MyMusicContract._ID + " INTEGER NOT NULL, " +
                    MyMusicContract.COLUMN_NAME_ARTIST_NAME + " TEXT NOT NULL , " +
                    MyMusicContract.COLUMN_NAME_MUSIC_NAME  + " TEXT NOT NULL , " +
                    MyMusicContract.COLUMN_NAME_ALBUM  + " TEXT NOT NULL , " +
                    MyMusicContract.COLUMN_NAME_DURATION  + " INTEGER NOT NULL " +
                    ");";

    //플레이리스트
    private static final String SQL_CREATE_MY_PLAYLIST =
            "CREATE TABLE IF NOT EXISTS " + MyPlaylistContract.MyPlaylistEntry.TABLE_NAME + " (" +
                    MyPlaylistContract.MyPlaylistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MyPlaylistContract.MyPlaylistEntry.COLUMN_NAME_PLAYLIST + " TEXT NOT NULL , " +
                    MyPlaylistContract.MyPlaylistEntry.COLUMN_NAME_MUSIC_ID + " INTEGER NOT NULL, " +
                    MyPlaylistContract.MyPlaylistEntry.COLUMN_NAME_PLAYLIST_TYPE + " TEXT NOT NULL " +
                    ");";

    private static DbHelper sSingleton = null;

    public static synchronized  DbHelper getInstance(Context context) {
        if(sSingleton == null) {
            sSingleton = new DbHelper(context);
        }
        return sSingleton;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DB Create
        db.execSQL(SQL_CREATE_MEMBER);
        db.execSQL(SQL_CREATE_MY_MUSIC);
        db.execSQL(SQL_CREATE_MY_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
