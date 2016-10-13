package edu.zhku.tools.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年7月16日 上午10:43:56
 * @since 0.1
 */
public class MD5Utils {

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 进行MD5加密
     *
     * @param val
     * @return
     * @author 夏集球
     * @time 2015年7月16日 上午10:51:54
     * @since 0.1
     */
    public static String md5Hex(final String val) {
        try {
            byte[] btInput = val.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return getDefaultStringFroMd5Exception(e);
        }
    }

    /**
     * 当获cache异常的时候返回空
     *
     * @param e
     * @return
     * @author 夏集球
     * @time 2015年7月16日 上午10:51:29
     * @since 0.1
     */
    private static String getDefaultStringFroMd5Exception(NoSuchAlgorithmException e) {
        return null;
    }

    private MD5Utils() {

    }
}
