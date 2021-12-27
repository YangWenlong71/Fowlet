package com.fowlet.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.fowlet.android.Cache.UserData;
import com.fowlet.android.R;
import com.fowlet.android.http.RequestCallback;
import com.fowlet.android.http.RequestListener;
import com.fowlet.android.utils.SystemUtil;
import com.fowlet.android.utils.ToastUtils;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory;
import xyz.doikki.videoplayer.player.VideoView;

public class playerActivity extends AppCompatActivity {

    private VideoView doikkiPlayer;

    private TextView tv_player_title;
    private TextView tv_player_detail;
    private TextView tv_video_title;
    private ImageView iv_collect;
    private Handler handler;
    private String playerurl,player_title,player_type,cuid;
    private String TAG = "playerActivity";
    boolean state = false;
    private String movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        cuid = UserData.getUid(playerActivity.this);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_player_title = findViewById(R.id.tv_player_title);
        tv_player_detail = findViewById(R.id.tv_player_detail);
        tv_video_title = findViewById(R.id.tv_video_title);
        iv_collect=findViewById(R.id.iv_collect);


        Intent intent=getIntent();
        playerurl=intent.getStringExtra("playerurl");
        player_title=intent.getStringExtra("player_title");
        player_type=intent.getStringExtra("player_type");
        String player_detail=intent.getStringExtra("player_detail");
        String player_episode=intent.getStringExtra("player_episode");

        tv_video_title.setText(player_episode);
        tv_player_title.setText(player_title);
        tv_player_detail.setText(player_detail);
        //页面初始化
        startPlayer(playerurl,player_title,player_detail);
        collect();

    }

    private void collect(){
        iv_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //收藏
                if(state==false){
                    RequestListener requestListener = new RequestListener();
                    requestListener.setPostCollectAffirm(cuid,player_title,player_type,playerurl,new RequestCallback() {
                        @Override
                        public void success(String result) {
                            Log.e(TAG,result);
                            state = true;
                            iv_collect.setImageDrawable(getDrawable(R.drawable.ic_collect_f));

                            //{"uid":89006175,"movieId":256199305692450816}

                            JSONObject data_key = JSONObject.parseObject(result);
                            movieId = data_key.getString("movieId");//拿到data数据

                            //Toast.makeText(playerActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void error(String error) {
                            Log.e(TAG,error);
                            ToastUtils.show(playerActivity.this,error);
                            //Toast.makeText(playerActivity.this, error, Toast.LENGTH_LONG).show();

                        }
                    });
                }else if(state==true){
                    RequestListener requestListener = new RequestListener();
                    requestListener.setPostCollectCancel(cuid,movieId,new RequestCallback() {
                        @Override
                        public void success(String result) {
                            Log.e(TAG,result);
                            state = false;
                            iv_collect.setImageDrawable(getDrawable(R.drawable.ic_collect));
                        }
                        @Override
                        public void error(String error) {
                            new Thread() {
                                @Override
                                public void run() {
                                    Toast.makeText(playerActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                        }
                    });
                }
            }
        });
    }







    //开始播放
    private void startPlayer(String player_url,String player_title,String player_detail){

        tv_player_title.setText(player_title);
        tv_player_detail.setText(player_detail);

        doikkiPlayer = findViewById(R.id.doikki_player);
        //使用ExoPlayer解码
        doikkiPlayer.setPlayerFactory(ExoMediaPlayerFactory.create());
       // doikkiPlayer.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
        doikkiPlayer.setUrl(player_url); //设置视频地址
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent(player_title, false);
        doikkiPlayer.setVideoController(controller); //设置控制器
        doikkiPlayer.start(); //开始播放，不调用则不自动播放
    }

    @Override
    protected void onPause() {
        super.onPause();
        doikkiPlayer.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        doikkiPlayer.resume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doikkiPlayer.release();
    }
    @Override
    public void onBackPressed() {
        if (!doikkiPlayer.onBackPressed()) {
            super.onBackPressed();
        }
    }

}