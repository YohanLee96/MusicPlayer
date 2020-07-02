/*
 * Copyright 2020. Pureum Choe
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.massivcode.androidmusicplayer.models.MusicInfo;

import java.util.HashMap;

public class MyMusicFacade {
    private static final String TAG = MyMusicFacade.class.getSimpleName();
    private DbHelper mHelper;
    private Context mContext;


    public static String[] projection
            = new String[]{MyMusicContract._ID,
            MyMusicContract.COLUMN_NAME_MUSIC_ID,
            MyMusicContract.COLUMN_NAME_ARTIST_NAME,
            MyMusicContract.COLUMN_NAME_MUSIC_NAME,
            MyMusicContract.COLUMN_NAME_ALBUM,
            MyMusicContract.COLUMN_NAME_DURATION
    };

    public static String selection_music_id = MyMusicContract.COLUMN_NAME_MUSIC_ID + "=?";
    public static String selection_artist_name = MyMusicContract.COLUMN_NAME_ARTIST_NAME + "=?";
    public static String selection_music_name = MyMusicContract.COLUMN_NAME_MUSIC_NAME + "=?";
    public static String selection_album = MyMusicContract.COLUMN_NAME_ALBUM + "=?";
    public static String selection_durartion = MyMusicContract.COLUMN_NAME_DURATION + "=?";
    public static String selection_search = MyMusicContract.COLUMN_NAME_ARTIST_NAME + "=? OR " + MyMusicContract.COLUMN_NAME_MUSIC_NAME + "=?";

    private static String getAllMusicList_SQL = "select * FROM "+ MyMusicContract.TABLE_NAME;

    public MyMusicFacade(Context context) {
        mHelper = DbHelper.getInstance(context);
        mContext = context;
    }


    //모든 음원 리스트 가져오기
    public Cursor getAllMusicList() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.rawQuery(getAllMusicList_SQL, null);
    }

    /**
     * 음원 리스트 추가
     */
    public void addMusicList(HashMap<Long, MusicInfo> musicInfoHashMap) {
        if(musicInfoHashMap ==null) {
            return;
        }

        SQLiteDatabase db; //DB 초기화
        SQLiteStatement statement;

        if (musicInfoHashMap != null && musicInfoHashMap.size() != 0) {
            db = mHelper.getWritableDatabase(); //음원목록 테이블에 쓰기허용.
            db.beginTransaction(); //트랜잭션 시작.

            //Statment 준비.
            statement = db.compileStatement(
                    "INSERT INTO " + MyMusicContract.TABLE_NAME + " ( " +
                            MyMusicContract.COLUMN_NAME_MUSIC_ID    + " , " +
                            MyMusicContract.COLUMN_NAME_MUSIC_NAME  + " , " +
                            MyMusicContract.COLUMN_NAME_ARTIST_NAME + " , " +
                            MyMusicContract.COLUMN_NAME_ALBUM       + " , " +
                            MyMusicContract.COLUMN_NAME_DURATION    + " ) " +

                            "values(?, ?, ?, ?, ?)"
            );

            //바인딩
            for(MusicInfo musicInfo :  musicInfoHashMap.values()) {
                int column = 1;
                statement.bindLong(column, musicInfo.get_id());
                column++;
                statement.bindString(column, musicInfo.getTitle());
                column++;
                statement.bindString(column, musicInfo.getArtist());
                column++;
                statement.bindString(column, musicInfo.getAlbum());
                column++;
                statement.bindLong(column, musicInfo.getDuration());

                statement.execute();
            }

            statement.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "음원목록에 " + musicInfoHashMap.size() + " 곡이 추가되었습니다.");
        }
    }

}
