package com.fowlet.android.Cache;

import android.content.Context;

public class UserData {

    public static String getUid(Context context) {
        return  ACache.get(context).getAsString("Uid");
    }

    public static void setUid(Context context,String str) {
        ACache.get(context).put("Uid", str);
    }


}
