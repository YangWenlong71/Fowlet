package com.fowlet.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class GetMac {



    public static final String getNetStatus(Context context) {
        int networkType;
        final ConnectivityManager connectivityManager= (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        networkType=networkInfo.getType();
        Log.d("获取当前数据", "network type:"+networkType);
        if(networkType == ConnectivityManager.TYPE_WIFI){
            return getMac_wlan0();
        }else if((networkType == ConnectivityManager.TYPE_ETHERNET)){
            return getMac_eth0();
        }else{
            return getMac_wlan0();
        }
    }



    //获取无线mac地址
    public static final String getMac_wlan0() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null || macBytes.length == 0) {
                    continue;
                }
                StringBuilder result = new StringBuilder();
                for (byte b : macBytes) {
                    result.append(String.format("%02X", b));
                }

                String s1 = result.toString().toUpperCase().replaceAll ("(.{2})", "$1:");//加入：

                return s1.substring(0,s1.length()-1);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return "";
    }

    //获取有线mac地址(有时可能获取不到)
    public static String getMac_eth0() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"eth0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null || macBytes.length == 0) {
                    continue;
                }
                StringBuilder result = new StringBuilder();
                for (byte b : macBytes) {
                    result.append(String.format("%02X", b));
                }
                String s1 = result.toString().toUpperCase().replaceAll ("(.{2})", "$1:");//加入：
                return s1.substring(0,s1.length()-1);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return "";
    }


}
