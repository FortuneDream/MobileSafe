package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
    private Button selectedPeopleBtn;
    private EditText numberEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        String number = sharedPreferences.getString("number", "");//sp取出数据一般在oncreate方法中执行
        selectedPeopleBtn = (Button) findViewById(R.id.btn_selected_people);
        numberEdt = (EditText) findViewById(R.id.edt_number);
        assert numberEdt != null;
        numberEdt.setText(number);
        selectedPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入联系人列表
                Intent intent = new Intent(Setup3Activity.this, SelectContactActivity.class);
                startActivityForResult(intent, 0);//需要新Acitivity返回数据的时候使用
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String number = data.getStringExtra("number").replace("-", "");//getStringExtra自带判空条件语句
        numberEdt.setText(number);//若第一次点击选择了一个联系人，第二次没有点直接返回，那么显示的还是第一个号码
    }

    @Override
    public void showNext() {
        String number = numberEdt.getText().toString().trim();
        //必须设置了安全号码才可以进入下一页面
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "请设置安全号码", Toast.LENGTH_SHORT).show();
            return;//使用return可以不进入下一个页面
        }
        //保存号码
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number", number);
        editor.apply();
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);

    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }
}
