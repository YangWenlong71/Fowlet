package com.fowlet.android.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import com.fowlet.android.Cache.ACache;
import com.fowlet.android.Cache.UserData;
import com.fowlet.android.R;
import com.fowlet.android.utils.CheckPermissionUtils;
import com.fowlet.android.utils.ImageUtil;
import com.fowlet.android.utils.SystemUtil;

public class userActivity extends AppCompatActivity {
    private TextView tv_uuid;
    private Button btn_replace,btn_exit,btn_history;
    // request参数
    private static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    private static final int REQ_PERM_CAMERA = 11003; // 打开摄像头

    private Button btn_tv;
    private Button btn_image,btn_collect;

    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btn_tv= findViewById(R.id.btn_tv);
        btn_image= findViewById(R.id.btn_image);
        btn_exit= findViewById(R.id.btn_exit);
        tv_uuid= findViewById(R.id.tv_uuid);
        String cuid = UserData.getUid(userActivity.this);
        tv_uuid.setText(cuid);
        btn_replace= findViewById(R.id.btn_replace);
        btn_history= findViewById(R.id.btn_history);
        btn_collect= findViewById(R.id.btn_collect);
        //顶部框颜色
        SystemUtil.setStatusBarColor(this, this.getResources().getColor(R.color.top_background_color));
        SystemUtil.setAndroidNativeLightStatusBar(this,true);

        //初始化权限
        initPermission();

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache.get(userActivity.this).clear();
                Intent intent = new Intent();
                intent.setClass(userActivity.this, InitActivity.class);
                userActivity.this.startActivity(intent);

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache.get(userActivity.this).clear();
                Intent intent = new Intent();
                intent.setClass(userActivity.this, InitActivity.class);
                userActivity.this.startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache mCache = ACache.get(userActivity.this);
                mCache.remove("search_data");
                Toast.makeText(userActivity.this, "缓存清理完成", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //扫码
        btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQrCode();
            }
        });
        //系统相册选择二维码
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        //系统相册选择二维码
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(userActivity.this, collectActivity.class);
                userActivity.this.startActivity(intent);
            }
        });
    }


    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CAMERA)) {
                Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(userActivity.this, new String[]{Manifest.permission.CAMERA},REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(userActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQ_QR_CODE);
    }


    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == REQ_QR_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("qrcode-qr",result);
                    String macStr = result.split("\\&")[0];
                    String serverStr = result.split("\\&")[1];
                    //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(userActivity.this,DisposeActivity.class);
                    Bundle bd=new Bundle();
                    bd.putString("server", serverStr.split("=")[1]);
                    bd.putString("tvId", macStr.split("=")[1]);
                    intent.putExtras(bd);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(userActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                Log.e("ImageUtil", ImageUtil.getImageAbsolutePath(this, uri));
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(String result) {
                            Log.e("qrcode-Image",result);
                            if(result!=null&&result.contains("&")){
                                String macStr = result.split("\\&")[0];
                                String serverStr = result.split("\\&")[1];
                                Intent intent =new Intent(userActivity.this,DisposeActivity.class);
                                Bundle bd=new Bundle();
                                bd.putString("server", serverStr.split("=")[1]);
                                bd.putString("tvId", macStr.split("=")[1]);
                                intent.putExtras(bd);
                                startActivity(intent);
                            }else {
                                Toast.makeText(userActivity.this, "解析结果:"+result, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(userActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(userActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}