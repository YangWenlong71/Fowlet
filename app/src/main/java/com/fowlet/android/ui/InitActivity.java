package com.fowlet.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fowlet.android.Cache.UserData;
import com.fowlet.android.R;
import com.fowlet.android.base.BaseActivity;
import com.fowlet.android.utils.SystemUtil;

public class InitActivity extends BaseActivity {


    private EditText ed_uid;
    private Button btn_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);


        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        //这里面没什么逻辑，就很简单，先判断是否有uuid
        //如果有，就直接进
        //如果没有就停在这，等待用户创建
        //创建成功，进入程序

        ed_uid = findViewById(R.id.et_uuid);
        btn_uid = findViewById(R.id.btn_uuid);

        String cuid = UserData.getUid(InitActivity.this);
        if(cuid==null){
            Toast.makeText(InitActivity.this, "缓存ID为空", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.setClass(InitActivity.this, SearchActivity.class);
            InitActivity.this.startActivity(intent);
        }


        btn_uid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid = ed_uid.getText().toString();


                if(isNumericZidai(uuid)&!uuid.contains(" ")){
                    if(uuid.length()>=5&uuid.length()<=19){
                        //写入缓存
                        UserData.setUid(InitActivity.this,uuid);
                        //跳转
                        Intent intent = new Intent();
                        intent.setClass(InitActivity.this, SearchActivity.class);
                        InitActivity.this.startActivity(intent);
                    }else{
                        Toast.makeText(InitActivity.this, "建议6-20个字符", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(InitActivity.this, "请输入纯数字，且不要留有空格", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public static boolean isNumericZidai(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}