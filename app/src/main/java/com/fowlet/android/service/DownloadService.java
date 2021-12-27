package com.fowlet.android.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    public static final String BROADCAST_ACTION = "com.example.android.threadsample.BROADCAST";
    public static final String EXTENDED_DATA_STATUS = "com.example.android.threadsample.STATUS";
    private LocalBroadcastManager mLocalBroadcastManager;
    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onHandleIntent(@Nullable Intent intent) {

        Log.e("DownloadService::", "开始下载");
        //获取下载地址
        String url = intent.getDataString();
        Log.i(TAG, url);
        //获取DownloadManager对象
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //指定APK缓存路径和应用名称,比如我这个路径就是/storage/emulated/0/Download/vooloc.apk
        //request.setDestinationInExternalPublicDir(CorePlayer.downLoad, "net_owlsmart_owlbox.apk");
        //设置文件存放路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS  , "owlsmart.apk" ) ;

        //设置网络下载环境为wifi或者移动数据
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //设置显示通知栏，下载完成后通知栏自动消失
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //设置通知栏标题
        request.setTitle("猫头鹰更新");
        //设置这个下载的描述，显示在通知中
        request.setDescription("如果未弹出安装界面，请到系统下载目录，手动点击安装");
        //设置类型为.apk
        request.setMimeType("application/vnd.android.package-archive");
        //获得唯一下载id
        long requestId = downloadManager.enqueue(request);
        //将id放进Intent
        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS, requestId);
        //查询下载信息
        DownloadManager.Query query = new DownloadManager.Query();
        //只包括带有给定id的下载。
        query.setFilterById(requestId);
        try {
            boolean isGoging = true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        //如果下载状态为成功
                        case DownloadManager.STATUS_SUCCESSFUL:
                            isGoging = false;
                            Log.e("DownloadService::", "下载完成");
                            //调用LocalBroadcastManager.sendBroadcast将intent传递回去
                            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                            mLocalBroadcastManager.sendBroadcast(localIntent);
                            break;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
