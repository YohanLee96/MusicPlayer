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


import android.provider.BaseColumns;

public class MemberContract implements BaseColumns {
    public static final String TABLE_NAME = "Member";
    public static final String COLUMN_NAME_NAME = "name"; //이름
    public static final String COLUMN_NAME_PASSWORD = "password"; //비밀번호
    public static final String COLUMN_NAME_DEPARTMENT = "department"; //학과
    public static final String COLUMN_NAME_DEPARTMENT_NO = "department_no"; //학번

}
