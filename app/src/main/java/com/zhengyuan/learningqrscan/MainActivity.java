package com.zhengyuan.learningqrscan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_SCAN = 0;//请求码，返回时用来识别是谁请求扫码的

    private EditText result;
    private Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scan = findViewById(R.id.scan);
        result = findViewById(R.id.result);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //android6.0以上需要动态申请相机等权限，
                getPrimission();

                //启动扫码
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                //ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动
                //自选配置
                /*ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                config.setPlayBeep(true);//是否播放提示音
                config.setShake(true);//是否震动
                config.setShowAlbum(true);//是否显示相册
                config.setShowFlashLight(true);//是否显示闪光灯
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);*/

                startActivityForResult(intent, REQUEST_CODE_SCAN);//REQUEST_CODE_SCAN=0
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {//RESULT_OK=-1
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText(content);
            }
        }
    }

    //动态申请权限，我没有在androidManifest添加权限配置
    private void getPrimission() {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", "com.zhengyuan.learningqrscan"));
        if (permission) {
            //"有这个权限"
            Toast.makeText(MainActivity.this, "有权限", Toast.LENGTH_SHORT).show();
        } else {
            //"木有这个权限"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 15);
            }
        }


    }

}
