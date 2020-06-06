package com.rul.tianchi;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

/**
 * 工具类
 *
 * @author RuL
 */
public class Utils {

    private final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(50L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .build();

    public static Response callHttp(Request request) throws IOException {
        Call call = OK_HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    /**
     * 生成MD5
     *
     * @param key key
     * @return 由key生成的MD5
     */
    public static String MD5(String key) {
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析数据traceId
     */
    public static String parseTraceId(String line) {
        return line.split("\\|")[0];
    }

    /**
     * 解析数据tags
     */
    public static String parseTags(String line) {
        return line.split("\\|")[8];
    }

    /**
     * 解析数据startTime
     */
    public static long parseStartTime(String line) {
        return Long.parseLong(line.split("\\|")[1]);
    }

}
