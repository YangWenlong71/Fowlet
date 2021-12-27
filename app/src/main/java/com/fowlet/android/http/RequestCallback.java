package com.fowlet.android.http;

/**
 * interface接口
 */
public interface RequestCallback {
 
    void success(String str);
    void error(String error);
}