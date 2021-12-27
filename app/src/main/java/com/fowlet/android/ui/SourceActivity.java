package com.fowlet.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fowlet.android.APPAplication;
import com.fowlet.android.OwlVar;
import com.fowlet.android.R;
import com.fowlet.android.adapter.sourceA;
import com.fowlet.android.model.source;
import com.fowlet.android.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SourceActivity extends AppCompatActivity {

    private ArrayList<source> sourceArrayList = new ArrayList<source>();

    private GridView gv_list;

    private APPAplication app;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        app = (APPAplication) getApplication();
        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        gv_list = findViewById(R.id.gv_list);
        tv_title= findViewById(R.id.tv_title);
        tv_title.setText(app.getPlayerName());
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //请求一个接口，获取所有表
        //表添加到实体类
        //实体类到适配器显示
        RequestHttp();
    }


    private void RequestHttp(){
        //get请求
        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(OwlVar.source_URL)
                .build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() >= 200 && response.code() < 300) {
                    String result = response.body().string();
                   //拿到请求的数据
                    //解析
                    Message msg = new Message();
                    msg.what = 16;
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result);//往Bundle中存放数据
                    msg.setData(bundle);
                    Thandler.sendMessage(msg);
                }
            }
        });
    }

   private source sourceList;
    private sourceA sAA;
    private Handler Thandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16:
                    String result = msg.getData().getString("result");//接受msg传递过来的参数
                    //解析结果
                    JSONObject data_key1 = JSONObject.parseObject(result);
                    int code = data_key1.getInteger("code");
                    if(code==0) {
                        //写到实体类当中
                        String dataItem1 = data_key1.getString("data");
                        List<source> ulc = JSON.parseArray(dataItem1, source.class);
                        for (source pp : ulc) {
                            sourceList = new source();
                            String player_name = pp.getPlayerName();
                            String player_type = pp.getPlayerType();
                            String player_search = pp.getPlayerSearch();
                            String player_detail = pp.getPlayerDetail();

                            sourceList.setPlayerName(player_name);
                            sourceList.setPlayerType(player_type);
                            sourceList.setPlayerSearch(player_search);
                            sourceList.setPlayerDetail(player_detail);
                            sourceArrayList.add(sourceList);
                        }

                        sAA = new sourceA(R.layout.item_source, SourceActivity.this, sourceArrayList);
                        gv_list.setAdapter(sAA);
                        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final TextView tv_player_name = (TextView) view.findViewById(R.id.tv_player_name);
                                final TextView tv_player_search = (TextView) view.findViewById(R.id.tv_player_search);
                                final TextView tv_player_detail = (TextView) view.findViewById(R.id.tv_player_detail);
                                app.setPlayerSearch(tv_player_search.getText().toString());
                                app.setPlayerDetail(tv_player_detail.getText().toString());
                                app.setPlayerName(tv_player_name.getText().toString());
                                Toast.makeText(SourceActivity.this, "已切换为："+tv_player_name.getText().toString(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                    break;
            }
        }
    };






}