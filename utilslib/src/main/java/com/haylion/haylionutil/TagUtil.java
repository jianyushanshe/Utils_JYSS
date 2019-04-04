package com.haylion.haylionutil;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Author:wangjianming
 * Time:2018/11/19
 * Description:log日志打印
 * <p>
 * 保存日志文件具体路径是：/storage/emulated/0/logger
 * 每个文件最大为500K
 * private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file
 * <p>
 * 生成的文件名称为logs_0.csv 后面的数字会递增
 * newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
 */
public class TagUtil {

    /**
     * 初始化Logger日志系统
     *
     * @param isDebug
     */
    public static void initLogger(final boolean isDebug) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)         // （可选）要显示的方法行数。 默认2
                .methodOffset(5)        // （可选）隐藏内部方法调用到偏移量。 默认5
                //.tag("Haylion")//（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));//根据上面的格式设置logger相应的适配器
        Logger.addLogAdapter(new DiskLogAdapter());//保存日志信息,路径/storage/emulated/0/logger
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isDebug;
            }
        });
    }

    /**
     * 方法描述：等同于  Log.i
     * <p>
     *
     * @param
     * @return
     */
    public static void i(String o) {
        Logger.i(o);
    }

    /**
     * 方法描述：等同于  Log.i
     * <p>
     *
     * @param
     * @return
     */
    public static void i(String tag, String o) {
        Logger.i(tag, o);
    }

    /**
     * 方法描述：等同于  Log.i
     * <p>
     *
     * @param
     * @return
     */
    public static void i(String tag, String action, String des) {
        Logger.i(tag, "[ " + action + " ]" + ":" + des);
    }

    /**
     * 方法描述：等同于  Log.d
     * <p>
     *
     * @param
     * @return
     */
    public static void d(String o) {
        Logger.d(o);

    }

    /**
     * 方法描述：等同于  Log.d
     * <p>
     *
     * @param
     * @return
     */
    public static void d(String tag, String o) {
        Logger.d(tag, o);
    }

    /**
     * 方法描述：等同于  Log.d
     * <p>
     *
     * @param
     * @return
     */
    public static void d(String tag, String action, String des) {
        Logger.d(tag, "[ " + action + " ]" + ":" + des);
    }

    /**
     * 方法描述：等同于  Log.e
     * <p>
     *
     * @param
     * @return
     */
    public static void e(String o) {
        Logger.e(o);

    }

    /**
     * 方法描述：等同于  Log.e
     * <p>
     *
     * @param
     * @return
     */
    public static void e(String tag, String o) {
        Logger.e(tag, o);
    }

    /**
     * 方法描述：等同于  Log.e
     * <p>
     *
     * @param
     * @return
     */
    public static void e(String tag, String action, String des) {
        Logger.e(tag, "[ " + action + " ]" + ":" + des);
    }

    /**
     * 方法描述：等同于  Log.v
     * <p>
     *
     * @param
     * @return
     */
    public static void v(String o) {
        Logger.v(o);

    }

    /**
     * 方法描述：等同于  Log.v
     * <p>
     *
     * @param
     * @return
     */
    public static void v(String tag, String o) {
        Logger.v(tag, o);
    }

    /**
     * 方法描述：等同于  Log.v
     * <p>
     *
     * @param
     * @return
     */
    public static void v(String tag, String action, String des) {
        Logger.v(tag, "[ " + action + " ]" + ":" + des);
    }

    /**
     * 方法描述：等同于  Log.w
     * <p>
     *
     * @param
     * @return
     */
    public static void w(String w) {
        Logger.w(w);
    }

    /**
     * 方法描述：等同于  Log.w
     * <p>
     *
     * @param
     * @return
     */
    public static void w(String tag, String o) {
        Logger.w(tag, o);
    }

    /**
     * 方法描述：等同于  Log.w
     * <p>
     *
     * @param
     * @return
     */
    public static void w(String tag, String action, String des) {
        Logger.w(tag, "[ " + action + " ]" + ":" + des);
    }
    /**
     * 方法描述：输出结果为json格式
     * <p>
     *
     * @param
     * @return
     */
    public static void json(String json) {
        Logger.json(json);
    }

}
