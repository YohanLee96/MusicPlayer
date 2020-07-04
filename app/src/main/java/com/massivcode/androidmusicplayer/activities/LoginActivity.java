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

package com.massivcode.androidmusicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.massivcode.androidmusicplayer.R;
import com.massivcode.androidmusicplayer.database.MemberFacade;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private MemberFacade memberFacade;

    EditText id_txt;
    EditText pw_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        id_txt = (EditText) findViewById(R.id.member_id);
        pw_txt = (EditText) findViewById(R.id.member_password);

        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button signinBtn = (Button) findViewById(R.id.btn_sign_in);

        //회원서비스 Facade 초기화
        memberFacade = new MemberFacade(getApplicationContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_str = id_txt.getText().toString();
                String pw_str = pw_txt.getText().toString();


                if(!memberFacade.validLogin(id_str, pw_str)) {
                    Toast toast = Toast.makeText(LoginActivity.this, "아이디/비밀번호가 정확하지 않습니다.",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
