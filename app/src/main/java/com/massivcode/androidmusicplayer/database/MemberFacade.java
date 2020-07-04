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

import com.massivcode.androidmusicplayer.models.Member;

public class MemberFacade {
    private static final String TAG = MyMusicFacade.class.getSimpleName();

    private DbHelper mHelper;
    private Context mContext;

    public MemberFacade(Context context) {
        mHelper = DbHelper.getInstance(context);
        mContext  = context;
    }
    private static final String SELECTION_DEPARTMENT_NO = "AND "+MemberContract.COLUMN_NAME_DEPARTMENT_NO + "=?";
    private static final String SELECTION_PASSWORD = "AND "+MemberContract.COLUMN_NAME_PASSWORD + "=?";

    private static final String LOGIN_VALID_SQL = "SELECT * FROM "
            +MemberContract.TABLE_NAME
            +" WHERE 1=1 "
            +SELECTION_DEPARTMENT_NO
            +SELECTION_PASSWORD;
    private static final String DUPLICATE_CHECK_SQL = "SELECT * FROM "
            + MemberContract.TABLE_NAME+" WHERE 1=1 "
            + SELECTION_DEPARTMENT_NO;


    public boolean validLogin(String id, String password) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(LOGIN_VALID_SQL, new String[]{id, password});
        int existCount = cursor.getCount();
        cursor.close();
        return existCount != 0;
    }

    public boolean duplicateCheck(String departmentNo) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(DUPLICATE_CHECK_SQL, new String[]{departmentNo});
        int existCount =  cursor.getCount();
        cursor.close();
        return existCount != 0;
    }

    public void addMember(Member member) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        SQLiteStatement statement;

        db.beginTransaction();

        statement = db.compileStatement(
                "INSERT INTO " + MemberContract.TABLE_NAME + " ( " +
                        MemberContract.COLUMN_NAME_DEPARTMENT_NO    + " , " +
                        MemberContract.COLUMN_NAME_DEPARTMENT  + " , " +
                        MemberContract.COLUMN_NAME_NAME + " , " +
                        MemberContract.COLUMN_NAME_PASSWORD    + " ) " +

                        "values(?, ?, ?, ?)"
        );

        int column = 1;
        statement.bindString(column, member.getDepartmentNo());
        column++;
        statement.bindString(column, member.getDepartment());
        column++;
        statement.bindString(column, member.getName());
        column++;
        statement.bindString(column, member.getPassword());

        statement.execute();

        statement.close();

        db.setTransactionSuccessful();
        db.endTransaction();

        Log.d(TAG, member.getName()+"님이 회원가입에 성공하였습니다.");

    }
}
