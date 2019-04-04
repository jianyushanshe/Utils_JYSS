package com.haylion.haylionutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * Author:wangjianming
 * Time:2018/11/19
 * Description:文件操作工具类
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();
    private Context context;

    public FileUtil(Context context) {
        this.context = context;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @throws Exception
     * @功能：获取sd卡根目录
     * @param：
     * @return：目录
     */
    public static String getSdPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return "/sdcard/";
        }
    }

    /**
     * 删除指定路径的文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 文件是否存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * 得到文件的名称
     *
     * @param path
     * @param FileName
     * @return
     */
    public static String getFileName(String path, String FileName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return DataFormatUtil.addText(new StringBuilder(), path, FileName);
    }

    /**
     * 保存二进制流到指定路径
     *
     * @param instream
     * @param filepath
     */
    public void saveFile(InputStream instream, String filepath) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return;
        }

        File file = new File(filepath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int cnt = 0;

            while ((cnt = instream.read(buffer)) != -1) {
                fos.write(buffer, 0, cnt);
            }

            instream.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * 复制文件
     *
     * @param from origin file path
     * @param to   target file path
     */
    public void copyFile(String from, String to) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return;
        }

        File fileFrom = new File(from);
        File fileTo = new File(to);

        try {

            FileInputStream fis = new FileInputStream(fileFrom);
            FileOutputStream fos = new FileOutputStream(fileTo);
            byte[] buffer = new byte[1024];
            int cnt = 0;

            while ((cnt = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, cnt);
            }

            fis.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * 保存 JSON 字符串到指定文件
     *
     * @param json
     * @param filepath
     */
    public boolean saveJson(String json, String filepath) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return false;
        }

        File file = new File(filepath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * 删除指定的 JSON 文件
     *
     * @param filepath
     * @return
     */
    public boolean deleteJson(String filepath) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return false;
        }

        File file = new File(filepath);

        if (file.exists()) {
            file.delete();
        }

        return false;
    }

    /**
     * 从指定文件读取 JSON 字符串
     *
     * @param filepath
     * @return
     */
    public String readJson(String filepath) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return null;
        }

        File file = new File(filepath);
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();

        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }

        return sb.toString();
    }

    /**
     * 保存图片到制定路径
     *
     * @param filepath
     * @param bitmap
     */
    public void saveBitmap(String filepath, Bitmap bitmap) {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return;
        }

        if (bitmap == null) {
            return;
        }

        try {
            File file = new File(filepath);
            FileOutputStream outputstream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
            outputstream.flush();
            outputstream.close();
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * 删除 Files 目录下所有的图片
     *
     * @return
     */
    public boolean cleanCache() {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return false;
        }

        File dir = context.getExternalFilesDir(null);

        if (dir != null) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }

        return true;
    }

    /**
     * 计算 Files 目录下图片的大小
     *
     * @return
     */
    public String getCacheSize() {
        if (!isExternalStorageWritable()) {
            Log.i(TAG, "SD卡不可用，保存失败");
            return null;
        }

        long sum = 0;
        File dir = context.getExternalFilesDir(null);

        if (dir != null) {
            for (File file : dir.listFiles()) {
                sum += file.length();
            }
        }

        if (sum < 1024) {
            return sum + "字节";
        } else if (sum < 1024 * 1024) {
            return (sum / 1024) + "K";
        } else {
            return (sum / (1024 * 1024)) + "M";
        }
    }

    /**
     * 返回当前应用 SD 卡的绝对路径 like
     * /storage/sdcard0/Android/data/com.example.test/files
     */
    public String getAbsolutePath() {
        File root = context.getExternalFilesDir(null);

        if (root != null) {
            return root.getAbsolutePath();
        }

        return null;
    }

    /**
     * 返回当前应用的 SD卡缓存文件夹绝对路径 like
     * /storage/sdcard0/Android/data/com.example.test/cache
     */
    public String getCachePath() {
        File root = context.getExternalCacheDir();

        if (root != null) {
            return root.getAbsolutePath();
        }

        return null;
    }

    public boolean isBitmapExists(String filename) {
        File dir = context.getExternalFilesDir(null);
        File file = new File(dir, filename);

        return file.exists();
    }

    /**
     * 删除指定的 图片
     * <p>
     *
     * @param filepath
     * @return
     */
    public boolean deletepictur(String filepath) {
        if (!isExternalStorageWritable()) {
            return false;
        }

        File file = new File(filepath);

        if (file.exists()) {
            file.delete();
        }

        return false;
    }


    private static final File parentPath = Environment
            .getExternalStorageDirectory();
    private static String storagePath = "";


    /**
     * @功能：yuv转prg
     * @param：
     * @return：
     */
    public static int[] decodeYUV420SPrgb565(int[] rgb, byte[] yuv420sp,
                                             int width, int height) {
        final int frameSize = width * height;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 保存图片
     *
     * @param data
     * @param width
     * @param height
     * @param index
     * @param nDegree
     * @return
     */
    public static boolean SaveImage(byte[] data, int width, int height,
                                    int index, int nDegree) {

        int nImageWidth = width;
        int nImageHeight = height;

        String strPre = "C";

        // 1.采用NV21格式 YuvImage类进行保存 效率很高
        String fileName = strPre + nDegree + "_IMG_" + String.valueOf(index)
                + ".jpg";
        String fileDataName = strPre + nDegree + "_DATA_"
                + String.valueOf(index) + ".yuv";
        File sdRoot = Environment.getExternalStorageDirectory();
        String dir = "/photoss/";
        File mkDir = new File(sdRoot, dir);
        if (!mkDir.exists()) {
            mkDir.mkdirs();
        }

        File pictureFile = new File(sdRoot, dir + fileName);
        File dataFile = new File(sdRoot, dir + fileDataName);

        if (!pictureFile.exists()) {
            try {
                pictureFile.createNewFile();

                FileOutputStream filecon = new FileOutputStream(pictureFile);
                YuvImage image = new YuvImage(data, ImageFormat.NV21,
                        nImageWidth, nImageHeight, null);

                image.compressToJpeg(
                        new Rect(0, 0, image.getWidth(), image.getHeight()),
                        70, filecon); // 将NV21格式图片，以质量70压缩成Jpeg，并得到JPEG数据流

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!dataFile.exists()) {
            try {
                FileOutputStream fops = new FileOutputStream(dataFile);
                fops.write(data);
                fops.flush();
                fops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static String input2Str(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bs = new byte[1024];
        int i = 0;
        while ((i = in.read(bs)) != -1) {
            out.write(bs, 0, i);
        }
        in.close();
        out.close();

        byte[] bytesResult = out.toByteArray();
        String strResult = new String(bytesResult, "utf-8");
        return strResult;
    }

    public static String getCRC32(File mFile) {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = null;
        CheckedInputStream checkedinputstream = null;
        String crc = null;
        try {
            fileinputstream = new FileInputStream(mFile);
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
            byte[] buffer = new byte[4096];
            while (checkedinputstream.read(buffer) != -1) {
            }
            crc = String.valueOf(checkedinputstream.getChecksum().getValue());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileinputstream != null) {
                try {
                    fileinputstream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (checkedinputstream != null) {
                try {
                    checkedinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return crc;
    }


    /**
     * 读取文件中字符串
     * <p>
     *
     * @param file 目标文件
     * @return 结果字符
     */
    public static String readTextFile(File file) throws Exception {
        String result = "";
        if (file.exists()) {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                // 分行读取
                while ((line = buffreader.readLine()) != null) {
                    result += line;
                }
                instream.close();
            }
        }
        return result;
    }

    /**
     * 向文件写入数据
     * <p>
     *
     * @param file 目标文件
     * @param data 字符数据
     */
    public static void writeTextFile(File file, String data) throws Exception {
        FileWriter filerWriter = new FileWriter(file, false);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
        BufferedWriter bufWriter = new BufferedWriter(filerWriter);
        bufWriter.write(data);
        bufWriter.newLine();
        bufWriter.close();
        filerWriter.close();
    }

    /**
     * 将输入流转成输出流
     * <p>
     *
     * @param is
     * @param os
     */
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
            is.close();
            os.close();
        } catch (Exception ex) {
        }
    }

    /**
     * 读取输入数据流
     * <p>
     *
     * @param is 输入流
     * @return 字节流
     */
    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 读取SD下某路径的所有图片信息
     *
     * @return
     */

    public static String[] ListFile(String filePath) {

        File file = new File(filePath);
        File[] f = file.listFiles();
        String Path[] = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            Path[i] = f[i].getPath();
        }
        return Path;

    }


    /**
     * 将data转为bitmap
     *
     * @param data
     * @param camera
     * @return
     */
    public static Bitmap getBitmap(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;
        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(data, imageFormat, w, h, null);
        Bitmap bitmap = null;
        try {
            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            yuvImg.compressToJpeg(rect, 100, outputstream);
            bitmap = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
            // Rotate the Bitmap
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            // We rotate the same Bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);

            //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
            //img.compressToJpeg(rect, 100, bos);
            //bos.flush();
            //bos.close();
            camera.startPreview();
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 二进制转文件
     * fileName文件的全路径  by：二进制数据
     *
     * @param fileName
     * @param by
     * @return
     */
    public static File castByte2File(String fileName, byte[] by) {
        {
            FileOutputStream fileout = null;
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                fileout = new FileOutputStream(file);
                fileout.write(by, 0, by.length);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileout != null)
                        fileout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file;
        }
    }

}
