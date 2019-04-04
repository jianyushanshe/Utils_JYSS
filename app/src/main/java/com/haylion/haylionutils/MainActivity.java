package com.haylion.haylionutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.haylion.haylionutil.TagUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化日志打印系统，根据gradle配置决定是否打印日志
        //IS_DEBUG=true打印日志 IS_DEBUG=false关闭日志
        TagUtil.initLogger(BuildConfig.IS_DEBUG);
        TagUtil.d("debug");
        TagUtil.e("error");
        TagUtil.w("warning");
        TagUtil.v("verbose");
        TagUtil.i("information");

    }
}
