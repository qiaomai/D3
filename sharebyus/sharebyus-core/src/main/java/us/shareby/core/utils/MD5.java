package us.shareby.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/**
 * 封装好的MD5加密算法.
 *
 * @author chengdong
 */
@Component
public class MD5 {

    private static final byte[] HEXBYTES = {(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
            (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
            (byte) 'f'};

    private MessageDigest md5;

    private String tail = "MYtea'sgonecold,I'mwonderingwhyIGotoutofbedatall";
    /**
     * 构造函数.
     */
    public MD5() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
    }
    /**
     * 加密.
     * @param message 加密字符串
     * @return 加密的字符串
     */
    public String md5(String message) {
        try {
            return md5(message, "GB2312",true);
        } catch (UnsupportedEncodingException e) {
        }
        return message;
    }
    
    public String md5WithoutTail(String message) {
        try {
            return md5(message, "GB2312", false);
        } catch (UnsupportedEncodingException e) {
        }
        return message;
    }
    /**
     * 加密.
     * @param message 要加密的对象
     * @param charsetName 字符编码
     * @return 加密后的对象
     * @throws java.io.UnsupportedEncodingException 不支持字符编码
     */
    public String md5(String message, String charsetName,boolean withTail) throws UnsupportedEncodingException {
        byte[] result;
        synchronized (this) {
            if(withTail){
                result = md5.digest((message.toLowerCase() + tail).getBytes(charsetName));
            }else{
                result = md5.digest((message.toLowerCase()).getBytes(charsetName));
            }
        }
        return byteArrayToHexString(result);
    }

    /**
     * byte数组转换为十六进制字符串.
     * todo:特意设置为private，待重构后移至通用类
     * @param result byte数组
     * @return 字符串
     */
    private String byteArrayToHexString(byte[] result) {
        int len = result.length;
        char[] str = new char[len * 2];

        for (int i = 0, j = 0; i < len; i++) {
            int c = ((int) result[i]) & 0xff;

            str[j++] = (char) HEXBYTES[c >> 4 & 0xf];
            str[j++] = (char) HEXBYTES[c & 0xf];
        }

        return new String(str);
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
   

}
