package com.rul.tianchi;

import java.security.MessageDigest;

/**
 * 工具类
 *
 * @author RuL
 */
public class Utils {

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
