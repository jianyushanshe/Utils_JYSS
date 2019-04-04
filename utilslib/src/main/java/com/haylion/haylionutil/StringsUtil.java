package com.haylion.haylionutil;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:wangjianming
 * Time:2018/11/19
 * Description:字符串操作工具类
 */
public class StringsUtil {
    public static String urlParse(String url) {
        int len1 = url.indexOf("&t=");
        int len2 = url.indexOf("&method=");
        if (len1 != -1 && len2 != -1) {
            String str1 = url.substring(len1 + 3, len2);
            String str2 = url.substring(len1, len2);
            url = url.replaceAll(str2, "");
            int len3 = url.indexOf("Index.aspx?");
            url = url.substring(0, len3) + str1 + "api/" + url.substring(len3);
        }
        url = url.replaceAll(" ", "%20").replaceAll("<", "%3C")
                .replaceAll(">", "%3E");
        return url;
    }

    /**
     * <p>
     *
     * @throws Exception
     * @功能：对String 进行encoding 操作
     * <p>
     * @param：
     * @return：
     */
    public static String getDecodedStr(String str) {
        try {
            return java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 方法描述： 	// 将二进制字符串转换为char
     * <p>
     *
     * @param
     * @return
     */
    public static char BinstrToChar(String binStr) {
        int[] temp = BinstrToIntArray(binStr);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[temp.length - 1 - i] << i;
        }
        return (char) sum;
    }


    /**
     * 方法描述：// 将初始二进制字符串转换成字符串数组，以空格相隔
     * <p>
     *
     * @param
     * @return
     */
    public static String[] StrToStrArray(String str) {
        return str.split(" ");
    }

    // 将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    /**
     * 方法描述： 去除EditText中的空字符
     * <p>
     *
     * @param
     * @return
     */
    public static String deleterTrim(String edit) {
//		String Str = "";
//
//		if (!TextUtils.isEmpty(edit)) {
//			for (int i = 0; i < edit.length(); i++) {
//				if (!edit.substring(i, i + 1).equals(" ")) {
//					Str = Str + edit.substring(i, i + 1);
//				}
//			}
//		}
//		return Str;

        if (TextUtils.isEmpty(edit))
            return null;
        return edit.replaceAll("\\s*", "");
    }

    /**
     * 验证身份证是否合法
     * <p>
     *
     * @throws Exception
     * @功能：
     * @param：身份证
     * @return：true 合法
     */
    public static boolean checkIdentityNumber(String identityNumber) {
        boolean flag = false;
        try {
            String check1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
            String check2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
            Pattern regex1 = Pattern.compile(check1);
            Pattern regex2 = Pattern.compile(check2);
            Matcher matcher1 = regex1.matcher(identityNumber);
            Matcher matcher2 = regex2.matcher(identityNumber);
            flag = matcher1.matches() || matcher2.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return true手机号正确
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^(1)\\d{10}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            Log.e("tag", "验证手机号码错误");
            flag = false;
        }
        return flag;
    }

    /**
     * 验证银行卡号
     * <p>
     *
     * @param
     * @return true银行卡号
     */
    public static boolean isNumberOfBankCard(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            Log.e("tag", "验证银行卡错误");
            flag = false;
        }
        return flag;
    }

    /**
     * 验证身份证号码
     * <p>
     *
     * @param
     * @return true验证身份证号码
     */
    public static boolean isNumberOrChar(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[A-Za-z0-9()]*$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            Log.e("tag", " 验证身份证号码错误");
            flag = false;
        }
        return flag;
    }

    /**
     * 验证车牌号(除前二位)是否合法
     * <p>
     *
     * @throws Exception
     * @功能：
     * @param：车牌号
     * @return：true 合法
     */
    public static boolean checkPlateNumber(String plateNumber) {
        boolean flag = false;
        try {
            String check1 = "^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$|^[a-zA-Z]{2}\\d{7}$";
            Pattern regex1 = Pattern.compile(check1);
            Matcher matcher1 = regex1.matcher(plateNumber);
            flag = matcher1.matches();
        } catch (Exception e) {
            Log.e("tag", "验证车牌号错误");
            flag = false;
        }
        return flag;
    }

    /**
     * @throws Exception
     * @功能：是否符合数字格式 <P>
     * @param：
     * @return：
     */
    public static boolean checkAmout(String amount) {
        Pattern p = Pattern
                .compile("^([1-9]\\d*\\.?\\d{0,2})|(0\\.\\d{0,2})|([1-9]\\d*)|0$");
        Matcher m = p.matcher(amount);
        return m.matches();
    }

    /**
     * 验证邮箱地址是否正确
     * <p>
     *
     * @param email
     * @return true 邮箱名正确
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            Log.e("tag", "验证邮箱地址错误");
            flag = false;
        }

        return flag;
    }


    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    /**
     * 方法描述：// 将字符串转换成二进制字符串，以空格相隔
     * <p>
     *
     * @param
     * @return
     */
    public static String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }


    /**
     * 方法描述：// 将二进制字符串转换成Unicode字符串
     * <p>
     *
     * @param
     * @return
     */
    public static String BinstrToStr(String binStr) {
        String[] tempStr = StrToStrArray(binStr);
        char[] tempChar = new char[tempStr.length];
        for (int i = 0; i < tempStr.length; i++) {
            tempChar[i] = BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }


    /**
     * @throws Exception
     * @功能：高亮显示搜索关键词
     * @param：textView:需要设置的textView searchKey:关键词 color:高亮显示的颜色 text:需要设置的句子
     * @return：
     */
    public static void setSearchKey(TextView textView, String text,
                                    String searchKey, int color) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(searchKey);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(s);

    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

}
