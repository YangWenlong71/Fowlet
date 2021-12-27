package com.fowlet.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.fowlet.android.APPAplication;
import com.fowlet.android.R;
import com.fowlet.android.adapter.SeriesA;
import com.fowlet.android.adapter.dramaSeries_A;
import com.fowlet.android.model.Series;
import com.fowlet.android.model.dramaSeries_C;
import com.fowlet.android.utils.GlideRoundTransform;
import com.fowlet.android.utils.SystemUtil;
import com.fowlet.android.utils.XmlToJson;
import com.fowlet.android.view.HorizontalListView;
import com.fowlet.android.view.NoScrollGridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {


    private TextView tv_last,tv_name,tv_des,tv_actor,tv_vamp,tv_type,tv_director;
    private ImageView iv_pic;
    private HorizontalListView hl_list;


    private Series mtv;
    private List<Series> SeriesList= new ArrayList<Series>();

    private NoScrollGridView gv_series;
    private dramaSeries_C dramaSeries_c;
    private List<dramaSeries_C> dramaSeries_cs= new ArrayList<dramaSeries_C>();
    private dramaSeries_A dramaSeries_a;
    private APPAplication app;

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        app = (APPAplication) getApplication();
        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        initView();

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
        * 拿到传递过来的数据
        * */
        Intent intent=getIntent();
        String movie_id=intent.getStringExtra("movie_id");

        RequestHttp(movie_id);

    }


    /*
    * 初始化控件
    * */
    private void initView(){
        tv_name=findViewById(R.id.tv_name);
        tv_last=findViewById(R.id.tv_last);
        tv_des=findViewById(R.id.tv_des);
        tv_actor=findViewById(R.id.tv_actor);
        tv_vamp=findViewById(R.id.tv_vamp);
        tv_type=findViewById(R.id.tv_type);
        tv_director=findViewById(R.id.tv_director);

        iv_pic=findViewById(R.id.iv_pic);
        hl_list =findViewById(R.id.hl_list);
        gv_series =findViewById(R.id.gv_series);


    }




    private void RequestHttp(String str){

        //get请求
        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(app.getPlayerDetail()+str)
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

                    XmlToJson xmlToJson = new XmlToJson.Builder(result).build();
                    System.out.println("CorePlayer::"+result);

                    Message msg = new Message();
                    msg.what = 16;
                    Bundle bundle = new Bundle();
                    bundle.putString("result", xmlToJson.toString());//往Bundle中存放数据
                    msg.setData(bundle);
                    Thandler.sendMessage(msg);

                }
            }
        });
    }

    private Handler Thandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16:
                    String result = msg.getData().getString("result");//接受msg传递过来的参数
                    /*
                     * 解析
                     * */
                    String rss = JSONObject.parseObject(result).getString("rss");
                    String list = JSONObject.parseObject(rss).getString("list");
                    String video = JSONObject.parseObject(list).getString("video");
                    /*
                    * 显示控件
                    * */
                    showTv(video);
                    break;
                case 21:
                    clickData((ArrayList<Series>) SeriesList);
                    break;

            }
        }
    };


    private void showTv(String video){

        /*
        * 电影名称
        * */
        String name = JSONObject.parseObject(video).getString("name");
        tv_name.setText(name);

        /*
         * 导演
         * */
        String director = JSONObject.parseObject(video).getString("director");
        tv_director.setText("导演:"+director);

        /*
         * 类型
         * */
        type = JSONObject.parseObject(video).getString("type");
        tv_type.setText("类型:"+type);

        /*
         * 地区，上映时间，语言
         * */
        String area = JSONObject.parseObject(video).getString("area");
        String year = JSONObject.parseObject(video).getString("year");
        String lang = JSONObject.parseObject(video).getString("lang");
        tv_vamp.setText(" 地区:"+area+" 年份:"+year+" 语言:"+lang);

        /*
         * 更新时间
         * */
        String last = JSONObject.parseObject(video).getString("last");
        tv_last.setText("更新时间:"+last);

        /*
         * 主演
         * */
        String actor = JSONObject.parseObject(video).getString("actor");
        tv_actor.setText("主演:"+actor);

        /*
         * 简介
         * */
        String des = JSONObject.parseObject(video).getString("des");
        tv_des.setText("简介:"+des);

        /*
         * 封面
         * */
        String pic = JSONObject.parseObject(video).getString("pic");

        //第一个是上下文，第二个是圆角的弧度
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(20));
        Glide.with(DetailActivity.this)
                .load(pic)
                .apply(options)
                .error(R.drawable.ic_404)
                .into(iv_pic);
        /*
         * 剧集
         * */
        String dl = JSONObject.parseObject(video).getString("dl");
        String dd = JSONObject.parseObject(dl).getString("dd");
        System.out.println("Handler::::"+dd);


        getList(dd);
    }
    /*
     * 读取列表
     * */
    private void  getList(String jsonData){

        try {
            List<Series> ser = JSON.parseArray(jsonData, Series.class);
            //avatar,job,nickname,status,user_id;
            for (Series pp : ser) {
                mtv = new Series();
                String flag = pp.getFlag();
                String content=pp.getContent();
                mtv.setFlag(flag);
                mtv.setContent(content);
                SeriesList.add(mtv);
            }

        }catch(Exception e){
            /*
            * 有意外情况，只会有一个播放源
            * */
            String fjson = jsonData.replace("[","").replace("]","");
            String flag = JSONObject.parseObject(fjson).getString("flag");
            String content = JSONObject.parseObject(fjson).getString("content");
            System.out.println("fjson::"+flag);
            mtv = new Series();
            mtv.setFlag(flag);
            mtv.setContent(content);
            SeriesList.add(mtv);
        }
        Message msg = new Message();
        msg.what = 21;
        Thandler.sendMessage(msg);//用activity中的handler发送消息
    }


/*
* 点击事件
* */
    private void clickData(ArrayList<Series> SeriesList) {

        SeriesA seriesa = new SeriesA(R.layout.item_series, DetailActivity.this, SeriesList);
        hl_list.setAdapter(seriesa);
        hl_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView tv_str = (TextView) view.findViewById(R.id.tv_str);
                final TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
                /*
                * 点击读取，显示剧集
                * */
                SeriesSplit(tv_content.getText().toString());

            }
        });
    }

/*
* 剧集拆分
* */
    private void SeriesSplit(String str){
        /*
        * 清空
        * */
        dramaSeries_cs.clear();
       String s =  str.substring(0,str.length()-1);

        if(s.contains("#")){
            System.out.println("SeriesSplit::::"+"包含"+str);
            String[] ss = str.split("#");
            for(int i=0;i<ss.length;i++){
                trycatch(ss[i]);
            }
            SeriesClick((ArrayList<dramaSeries_C>) dramaSeries_cs);
        }else {
            System.out.println("SeriesSplit::::"+"不包含");
            trycatch(str);
        }

    }



    private void trycatch(String str){
        try {
            /*
             * 不是连续剧，就直接按$拆分
             * */
            String std[] = str.split("\\$");
            dramaSeries_c = new dramaSeries_C();
            dramaSeries_c.setEpisode(std[0]);
            dramaSeries_c.setPlayerurl(std[1]);
            dramaSeries_cs.add(dramaSeries_c);
            SeriesClick((ArrayList<dramaSeries_C>) dramaSeries_cs);
        }catch(Exception e){
            String s = null;
            if(str.contains("#")){
                s =  str.substring(0,str.length()-1);
            }
            else {
                s = str;
            }

            dramaSeries_c = new dramaSeries_C();
            dramaSeries_c.setEpisode("m3u8");
            dramaSeries_c.setPlayerurl(s);
            dramaSeries_cs.add(dramaSeries_c);
            SeriesClick((ArrayList<dramaSeries_C>) dramaSeries_cs);

        }

    }



    private void SeriesClick(ArrayList<dramaSeries_C> dramaSeries_cs) {

        dramaSeries_a = new dramaSeries_A(R.layout.item_dramaseries, DetailActivity.this, dramaSeries_cs);


        gv_series.setAdapter(dramaSeries_a);
        gv_series.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView episode = (TextView) view.findViewById(R.id.episode);//id
                final TextView playerurl = (TextView) view.findViewById(R.id.playerurl);//id

                /*
                * 点击后直接播放
                * */

                Intent intent=new Intent();
                intent.putExtra("playerurl",playerurl.getText().toString());
                intent.putExtra("player_type",type);
                intent.putExtra("player_title",tv_name.getText().toString());
                intent.putExtra("player_detail",tv_vamp.getText().toString());
                intent.putExtra("player_episode",episode.getText().toString());
                intent.setClass(DetailActivity.this, playerActivity.class);
                DetailActivity.this.startActivity(intent);

            }
        });
    }

}



