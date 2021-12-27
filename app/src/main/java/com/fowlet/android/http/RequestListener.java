package com.fowlet.android.http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import com.fowlet.android.OwlVar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 接口使用
 */
public class RequestListener {
 
    private RequestCallback requestListener;
    //打印的标识符
    private String TAG = "RequestListener";




    //根据不同的后台定义的不同关键字
    private static String
            _code="status",
            _msg = "msg",
            _data = "data";



    public void GetRequest(String str,RequestCallback requestListener) {
        this.requestListener = requestListener;
        setGetRequest(str);
    }
 
    public void setGetRequest(String Url) {
        Request request = new Request
                .Builder()
                .url(Url)//要访问的链接
                .build();
        RequestBase(request);

    }


    public void setPostCollectList(String Url,RequestCallback requestListener) {
        this.requestListener = requestListener;
        setGetRequest(Url);
    }



    public void setPostCollectAffirm(String uid,String movieName,String movieType,String movieUrl,RequestCallback requestListener) {
        this.requestListener = requestListener;
        PostCollectAffirm(uid,movieName,movieType,movieUrl);
    }
    private void PostCollectAffirm(String uid,String movieName,String movieType,String movieUrl){

        FormBody formBody = new FormBody.Builder()
                .add("uid",uid)
                .add("movieName",movieName)
                .add("movieType", movieType)
                .add("movieUrl", movieUrl)
                .build();
        Request request = new Request.Builder().url(OwlVar.collect_affirm).post(formBody).build();
        RequestBase(request);
    }


    public void setPostCollectCancel(String uid,String movieId,RequestCallback requestListener) {
        this.requestListener = requestListener;
        PostCollectCancel(uid,movieId);
    }
    private void PostCollectCancel(String uid,String movieId){

        FormBody formBody = new FormBody.Builder()
                .add("uid",uid)
                .add("movieId",movieId)
                .build();
        Request request = new Request.Builder().url(OwlVar.collect_cancel).post(formBody).build();
        RequestBase(request);
    }






    /*
     * 请求部分的耦合代码
     * */
    private void RequestBase(Request request){
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.getMessage());
                requestListener.error("{\""+_code+"\":404,\""+_msg+"\":\""+e.getMessage()+"\"}");
            }
            public void onResponse(Call call, Response response) throws IOException {

                    String result = response.body().string();
                  //  Log.e(TAG,result);
                    //解析json
                    JSONObject data_key1 = JSONObject.parseObject(result);
                    int code = data_key1.getInteger(_code);
                    if (code == 0) {
                        JSONObject data_key = JSONObject.parseObject(result);
                        String dataKeyString = data_key.getString(_data);//拿到data数据
                        requestListener.success(dataKeyString);
                    }else {
                        JSONObject data_key = JSONObject.parseObject(result);
                        String msg = data_key.getString(_msg);//拿到data数据
                        requestListener.error(msg);
                    }
                }

        });
    }
}