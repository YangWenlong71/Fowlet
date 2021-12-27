package com.fowlet.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fowlet.android.APPAplication;
import com.fowlet.android.Cache.ACache;
import com.fowlet.android.Cache.UserData;
import com.fowlet.android.R;
import com.fowlet.android.adapter.movieA;
import com.fowlet.android.base.BaseActivity;
import com.fowlet.android.model.movie;
import com.fowlet.android.utils.ButtonUtils;
import com.fowlet.android.utils.SystemUtil;
import com.fowlet.android.utils.XmlToJson;
import com.fowlet.android.view.AutoNewLineLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {


    private List<movie> movieList = new ArrayList<movie>();
    private ListView lv_list;
    private EditText et_search;
    private ImageView iv_my;
    private APPAplication app;
    private TextView tv_uuid;
    private LinearLayout ll_search;
    private LinearLayout ll_hint;

    private String search_data;
    private AutoNewLineLayout anl_history;

    private ImageView iv_clear_editview,iv_clear_history;
    private TextView tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        app = (APPAplication) getApplication();
        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        lv_list = findViewById(R.id.lv_list);
        et_search = findViewById(R.id.et_search);
        iv_my = findViewById(R.id.iv_my);
        anl_history = findViewById(R.id.anl_history);
        tv_uuid= findViewById(R.id.tv_uuid);
        String cuid = UserData.getUid(SearchActivity.this);
        tv_uuid.setText(cuid);
        ll_search  =findViewById(R.id.ll_search);
        ll_hint  =findViewById(R.id.ll_hint);
        tv_hint =findViewById(R.id.tv_hint);

        iv_clear_editview=findViewById(R.id.iv_clear_editview);
        iv_clear_history=findViewById(R.id.iv_clear_history);


        iv_clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache mCache = ACache.get(SearchActivity.this);
                mCache.remove("search_data");
                showhistory();
            }
        });

        iv_clear_editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_search.getText().toString().equals("")){
                    ll_hint.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.GONE);
                    iv_clear_editview.setVisibility(View.GONE);
                    showhistory();
                }else {
                    ll_hint.setVisibility(View.GONE);
                    ll_search.setVisibility(View.VISIBLE);
                    iv_clear_editview.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Thandler.sendEmptyMessage(2);
                }
                return false;
            }
        });





        ImageView btn_search = findViewById(R.id.iv_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.iv_search)) {
                    //写你相关操作即可
                    Thandler.sendEmptyMessage(2);
                }
            }
        });

        ImageView iv_menu = findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.iv_menu)) {

                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, SourceActivity.class);
                    SearchActivity.this.startActivity(intent);
                }
            }
        });

        iv_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, userActivity.class);
                SearchActivity.this.startActivity(intent);
            }
        });


        showhistory();
    }


    private void showhistory(){

        anl_history.removeAllViews();

        ACache mCache = ACache.get(SearchActivity.this);   // Fragment里为getActivity()
        String MS = mCache.getAsString("search_data");
        if(MS==null){
            tv_hint.setVisibility(View.VISIBLE);
            iv_clear_history.setVisibility(View.GONE);
        }else {
            tv_hint.setVisibility(View.GONE);
            iv_clear_history.setVisibility(View.VISIBLE);
            search_data =  MS;
            //这个是列出来历史搜索的
            Message msg = new Message();
            msg.what = 26;
            Bundle bundle = new Bundle();
            bundle.putString("result", search_data);//往Bundle中存放数据
            msg.setData(bundle);
            Thandler.sendMessage(msg);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }//屏蔽返回键


    private void RequestHttp(String str) {
        movieList.clear();
        //get请求
        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(app.getPlayerSearch() + str)
                .build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 200 && response.code() < 300) {

                    String result = response.body().string();
                    /*
                     * 可能获取的数据有问题
                     * */
                    XmlToJson xmlToJson = new XmlToJson.Builder(result).build();
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

                case 2:
                    if(!et_search.getText().toString().equals("")){
                        RequestHttp(et_search.getText().toString());


                        ACache mCache = ACache.get(SearchActivity.this);   // Fragment里为getActivity()
                        //   MissionSearch = mCache.getAsString("mission_search");
                        search_data = mCache.getAsString("search_data");
                        if (search_data == null) {
                            mCache.put("search_data", et_search.getText().toString());
                        } else {
                            if (search_data.contains(et_search.getText().toString())) {
                                //如果包含，就不再存了
                            } else {
                                mCache.put("search_data", search_data + "," + et_search.getText().toString());
                            }
                        }
                    }else {
                        Toast.makeText(SearchActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 16:
                    String result = msg.getData().getString("result");//接受msg传递过来的参数
                    /*
                     * 解析
                     * */
                    String rss = JSONObject.parseObject(result).getString("rss");
                    String list = JSONObject.parseObject(rss).getString("list");
                    String video = JSONObject.parseObject(list).getString("video");

                    /*
                     * 读取列表
                     * */
                    String videoTemp = video;
                    if (video == null) {
                        et_search.setText("");
                        et_search.setHint("搜索不到内容");
                    } else {

                        if (video != null && videoTemp != null) {
                            System.out.println("Handler::::" + video);
                            if (video.contains("[") && video.contains("]")) {
                                getList(video);
                            } else {
                                getList("[" + video + "]");
                            }

                        }


                    }


                    break;
                case 21:
                    clickData((ArrayList<movie>) movieList);
                    break;
                case 26:
                    String result26 = msg.getData().getString("result");//接受msg传递过来的参数
                    if(result26.contains(",")){
                        String[] str = result26.split(",");
                        for (String string : str) {
                            //逐个new出来
                            newTextview(SearchActivity.this, string);
                        }
                    }else {
                        newTextview(SearchActivity.this, result26);
                    }
                    break;


            }
        }
    };




    /*
     * new控件
     * */
    private void newTextview(Context context, String str) {
        //设置大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout project = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_searchhistory, null);
        project.setLayoutParams(lp);
        LinearLayout ll_history = (LinearLayout) project.findViewById(R.id.ll_history);
        TextView tv_history = (TextView) ll_history.findViewById(R.id.tv_history);
        tv_history.setText(str);
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHttp(tv_history.getText().toString());
                //点击搜索，隐藏该布局
                ll_hint.setVisibility(View.GONE);
                ll_search.setVisibility(View.VISIBLE);
                et_search.setText(tv_history.getText().toString());
            }
        });
        anl_history.addView(project);
    }





    private movie mtv;

    /*
     * 读取列表
     * */
    private void getList(String jsonData) {
        List<movie> subc = JSON.parseArray(jsonData, movie.class);
        //avatar,job,nickname,status,user_id;
        for (movie pp : subc) {
            mtv = new movie();
            String last = pp.getLast();
            String id = pp.getId();
            String tid = pp.getTid();
            String name = pp.getName();
            String type = pp.getType();
            String dt = pp.getDt();
            String note = pp.getNote();

            mtv.setName(name + "#" + et_search.getText().toString());

            mtv.setLast(last);
            mtv.setId(id);
            mtv.setTid(tid);
            //mtv.setName(name);
            mtv.setType(type);
            mtv.setDt(dt);
            mtv.setNote(note);
            movieList.add(mtv);
        }
        Message msg = new Message();
        msg.what = 21;
        Thandler.sendMessage(msg);//用activity中的handler发送消息


    }

    private void clickData(ArrayList<movie> movieList) {

        movieA moviea = new movieA(R.layout.item_search, SearchActivity.this, movieList);
        lv_list.setAdapter(moviea);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView tv_id = (TextView) view.findViewById(R.id.tv_id);

                Intent intent = new Intent();
                intent.putExtra("movie_id", tv_id.getText().toString());
                intent.setClass(SearchActivity.this, DetailActivity.class);
                SearchActivity.this.startActivity(intent);
            }
        });
    }


    /**
     * Activity从后台重新回到前台时被调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        showhistory();
        System.out.println("------------->" + "onRestart is invoke!!!");
    }
    /**
     *Activity创建或者从后台重新回到前台时被调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("------------->" + "onStart is invoke!!!");
    }
    /**
     *Activity创建或者从被覆盖、后台重新回到前台时被调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("------------->" + "onResume is invoke!!!");
    }
    /**
     *  Activity被覆盖到下面或者锁屏时被调用
     */
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("------------->" + "onPause is invoke!!!");
    }
    /**
     *退出当前Activity或者跳转到新Activity时被调用
     */
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("------------->" + "onStop is invoke!!!");
    }
    /**
     *退出当前Activity时被调用,调用之后Activity就结束了
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("------------->" + "onDestroy is invoke!!!");
    }

}