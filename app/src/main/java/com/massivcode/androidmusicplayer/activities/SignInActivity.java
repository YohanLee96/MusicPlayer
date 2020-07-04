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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.massivcode.androidmusicplayer.R;
import com.massivcode.androidmusicplayer.database.MemberFacade;
import com.massivcode.androidmusicplayer.models.Member;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private MemberFacade memberFacade;

    private Spinner department;
    private EditText departmentNo;
    private EditText password;
    private EditText passwordConfirm;
    private EditText name;

    private Button duplicateCheckBtn;
    private Button confirmBtn;
    private Button backBtn;

    private Member member;

    //학번 중복 체크
    private static boolean isDuplicateCheck = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_main);

        initView();

        memberFacade = new MemberFacade(getApplicationContext());

        //중복체크
        duplicateCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departmentNoStr = departmentNo.getText().toString();

                if(TextUtils.isEmpty(departmentNoStr)) {
                    showMessage("학번을 입력해주세요.");
                    return;
                }

                if(memberFacade.duplicateCheck(departmentNoStr)) {
                    showMessage("이미 가입된 학번입니다.");
                    return;
                }

                showMessage("가입 가능한 학번입니다.");
                isDuplicateCheck =  true;
            }
        });

        //회원가입
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validFieldAndInitMember()){
                    return;
                }

                memberFacade.addMember(member);
                showMessage("회원가입 성공");

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

        //뒤로가기 버튼
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initView() {
        //필드
        department = (Spinner)findViewById(R.id.spinner_department);
        departmentNo = (EditText)findViewById(R.id.edit_department_no);
        password = (EditText)findViewById(R.id.edit_password);
        passwordConfirm = (EditText)findViewById(R.id.edit_password_confirm);
        name = (EditText)findViewById(R.id.edit_name);
        //버튼
        duplicateCheckBtn = (Button)findViewById(R.id.btn_duplicate_check);
        confirmBtn = (Button)findViewById(R.id.btn_confirm);
        backBtn = (Button)findViewById(R.id.btn_back);

    }

    private boolean validFieldAndInitMember() {
        String passwordStr = toStr(password);
        String passwordConfirmStr = toStr(passwordConfirm);
        String departmentNoStr = toStr(departmentNo);
        String nameStr = toStr(name);

        if(isEmpty(passwordStr)) { showMessage("패스워드을 입력해주세요.");return false; }
        if(isEmpty(departmentNoStr)) { showMessage("학번을 입력해주세요.");return false; }
        if(isEmpty(nameStr)) { showMessage("이름을 입력해주세요.");return false; }

        if(!isDuplicateCheck) {
            showMessage("학번 중복체크를 먼저 진행해주세요.");
            return false;
        }

        if(passwordStr.length() < 4) {
            showMessage("패스워드는 4글자 이상이어야 합니다.");
        }

        if(!passwordStr.equals(passwordConfirmStr)) {
            showMessage("비밀번호란과 비밀번호 확인란이 일치하지 않습니다.");
            return false;
        }

        //Validation이 모두 통과하면, Member 초기화
        this.member = new Member(departmentNoStr, department.getSelectedItem().toString(), passwordStr, nameStr);

        return true;
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(SignInActivity.this, message,Toast.LENGTH_SHORT);
        toast.show();
    }

    private String toStr(EditText editText) {
        return editText.getText().toString();
}

    private boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

}
