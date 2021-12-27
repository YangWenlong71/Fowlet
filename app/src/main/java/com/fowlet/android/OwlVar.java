package com.fowlet.android;

import android.os.Environment;

import java.io.File;

public class OwlVar {

    private static final String domain_name = "http://www.owlsmart.net/app";

    //删除指定位置的文件
    public static final String downLoad = Environment.getExternalStorageDirectory()+ File.separator + "Download" + File.separator;

    public static final String update_URL = domain_name+"/UpdateApp.php";

    public static final String source_URL = domain_name+"/SourceList.php";


    public static  String url_server = "http://www.owlsmart.net:8089";
    public static  String collect_list = url_server+"/user/get_collection_list.do";
    public static  String collect_affirm = url_server+ "/user/collection_movie.do";
    public static  String collect_cancel = url_server+ "/user/cancel_collection_movie.do";

}
