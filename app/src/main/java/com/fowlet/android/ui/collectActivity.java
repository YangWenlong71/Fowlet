package com.fowlet.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fowlet.android.Cache.UserData;
import com.fowlet.android.OwlVar;
import com.fowlet.android.R;
import com.fowlet.android.adapter.CollectA;
import com.fowlet.android.http.RequestCallback;
import com.fowlet.android.http.RequestListener;
import com.fowlet.android.model.CollectBean;
import com.fowlet.android.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class collectActivity extends AppCompatActivity {

    private String TAG = "collectActivity";
    private String cuid;
    private TextView tv_uuid;
    private List<CollectBean> collectBeans = new ArrayList<CollectBean>();
    private CollectBean mlc;
    private ListView lv_list;
    private CollectA collectA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);
        lv_list = findViewById(R.id.lv_list);

        cuid = UserData.getUid(collectActivity.this);
        tv_uuid= findViewById(R.id.tv_uuid);
        tv_uuid.setText(cuid);


        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RequestListener requestListener = new RequestListener();
        requestListener.setPostCollectList(OwlVar.collect_list+"?uid="+cuid+"&pageNum="+1+"&pageSize="+20,new RequestCallback() {
            @Override
            public void success(String result) {
                Log.e(TAG,result);
                //解析json
                JSONObject data_key1 = JSONObject.parseObject(result);
                String dataItem1 = data_key1.getString("list");
                ShowList(dataItem1);
            }
            @Override
            public void error(String error) {
                Log.e(TAG,error);
            }
        });
    }




    private void ShowList(String jsonData) {

        Log.e(TAG,jsonData);
        collectBeans.clear();
        List<CollectBean> subc = JSON.parseArray(jsonData, CollectBean.class);
        for (CollectBean pp : subc) {
            mlc = new CollectBean();
            String MovieId = pp.getMovieId();
            String MovieName = pp.getMovieName();
            String MovieType = pp.getMovieType();
            String MovieUrl = pp.getMovieUrl();

            mlc.setMovieId(MovieId);
            mlc.setMovieName(MovieName);
            mlc.setMovieType(MovieType);
            mlc.setMovieUrl(MovieUrl);
            collectBeans.add(mlc);
        }
        Message msg = new Message();
        msg.what = 1;
        Thandler.sendMessage(msg);//用activity中的handler发送消息
    }
    //ui
    private Handler Thandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    clickData((ArrayList<CollectBean>) collectBeans);
                    break;

            }
        }
    };

    private void clickData(ArrayList<CollectBean> collectBeans) {
        collectA = new CollectA(R.layout.item_collect, collectActivity.this, collectBeans);
        lv_list.setAdapter(collectA);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView tv_movieid = (TextView) view.findViewById(R.id.tv_movieid);
                final TextView tv_moviename = (TextView) view.findViewById(R.id.tv_moviename);
                final TextView tv_movietype = (TextView) view.findViewById(R.id.tv_movietype);
                final TextView tv_movieurl = (TextView) view.findViewById(R.id.tv_movieurl);


                Intent intent=new Intent();
                intent.putExtra("playerurl",tv_movieurl.getText().toString());
                intent.putExtra("player_type",tv_movieid.getText().toString());
                intent.putExtra("player_title",tv_moviename.getText().toString());
                intent.putExtra("player_detail",tv_movietype.getText().toString());
                intent.putExtra("player_episode","来自收藏");
                intent.setClass(collectActivity.this, playerActivity.class);
                collectActivity.this.startActivity(intent);

            }
        });
    }


}