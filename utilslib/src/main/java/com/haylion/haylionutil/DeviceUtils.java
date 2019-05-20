package com.haylion.haylionutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * Author:wangjianming
 * Time:2018/11/19
 * Description:设备相关工具类
 */
@SuppressLint("MissingPermission")
public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String LOGTAG = DeviceUtils.class.getSimpleName();
    private static WifiManager wifiMgr;
    private static TelephonyManager telMgr;
    public static String PHONE_MCUTRE = ""; // 手机制造商
    public static String PHONE_MODEL = ""; // 获取手机设备类型

    private static void initManager(Context context) {
        if (wifiMgr == null) {
            Object obj = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (obj != null) {
                wifiMgr = (WifiManager) obj;
            }
        }
        if (telMgr == null) {
            Object obj = context.getSystemService(Context.TELEPHONY_SERVICE);
            if (obj != null) {
                telMgr = (TelephonyManager) obj;
            }
        }
    }

    /**
     * 获得设备唯一标识
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        try {
            initManager(context);
            if (telMgr != null) {//IMEI

                String imei = null;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    imei = telMgr.getImei();
                } else {
                    imei = telMgr.getDeviceId();
                }
                if (!isEmpty(imei)) {
                    return imei;
                }
            }
            if (wifiMgr != null) {//MAC Addr
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                if (wifiInfo != null) {
                    String macAddr = wifiInfo.getMacAddress();
                    if (!isEmpty(macAddr)) {
                        return "MAC:" + macAddr;
                    }
                }
            }
            //SIM SN
            assert telMgr != null;
            String simSN = telMgr.getSimSerialNumber();
            if (!isEmpty(simSN)) {
                return "SIMSN:" + simSN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //UUID
        String uuid = UUID.randomUUID().toString();
        if (!isEmpty(uuid)) {
            return "UUID:" + uuid;
        }
        return null;
    }


    public static void getpPhoneInfo(Context context) {
        // 手机制造商
        PHONE_MCUTRE = Build.MANUFACTURER;
        PHONE_MODEL = Build.MODEL;
    }

    public static String getPhoneBrandAndModel(Context context) {
        // 手机制造商
        PHONE_MCUTRE = Build.MANUFACTURER;
        PHONE_MODEL = Build.MODEL;
        return PHONE_MCUTRE + "_" + PHONE_MODEL;//拼接
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


    private static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }
}
