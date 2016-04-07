package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.mobilesafe.R;
public class Setup3Activity extends BaseSetupActivity {
    private Button selectedPeopleBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        selectedPeopleBtn= (Button) findViewById(R.id.btn_selected_people);
        selectedPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入联系人列表
            }
        });
    }

    @Override
    public void showNext() {
        Intent intent=new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);
    }

    @Override
    public void showPre() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }
}
