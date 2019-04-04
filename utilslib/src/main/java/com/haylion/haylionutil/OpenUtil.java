package com.haylion.haylionutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

/**
 * Author:wangjianming
 * Time:2018/11/28 18:31
 * Description:OpenUtil 页面打开util
 */
public class OpenUtil {
    /**
     * 跳转到目标activity
     *
     * @param context 上下文
     * @param mClass  目标activity
     */
    public static void openActivity(Activity context, Class mClass) {
        Intent intent = new Intent();
        intent.setClass(context, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);

    }

    /**
     * 跳转到另外的Activity并且返回结果
     *
     * @param context     上下文
     * @param mClass      目标Activity
     * @param requestCode 请求码
     */
    public static void openActivityForResult(Activity context, Class mClass, int requestCode) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClass(context, mClass);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到其目标activity,并且携带bundle对象
     *
     * @param context 上下文
     * @param mClass  目标类
     * @param bundle  bundle对象
     */
    public static void openActivityWithBundle(Activity context, Class mClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtras(bundle);
        intent.setClass(context, mClass);
        context.startActivity(intent);
    }

    /**
     * 打开应用市场
     *
     * @param context
     */
    public static void openApplicationMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable throwable) {
        }
    }

}
