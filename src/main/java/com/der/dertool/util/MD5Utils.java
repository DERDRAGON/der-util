package com.der.dertool.util;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密util
 * @author caoshilong
 */
public class MD5Utils {

    /**
     * 系统自定义散列次数
     */
    private static final int SYS_HASH_ITERATIONS = 16;

    /**
     * 对source和盐进行加密
     * @param source 元数据
     * @param salt 盐
     * @return 加密后数据
     */
    public static String encode(String source, String salt) {
        if (StringUtils.isAnyBlank(source, salt)) {
            return null;
        }
        return new MD5Hash(source, salt, SYS_HASH_ITERATIONS).toHex();
    }

    /**
     * 获取一个随即盐
     * @return
     */
    public static String getNextSalt() {
        return RandomStringUtils.random(4, "1234567890abcdefghijklmnopqrstuvwxyz");
    }
}

/**
 * MD5 hash类
 */
class MD5Hash {

    /**
     * MD5加密元数据
     */
    private String source;

    /**
     * MD5加密盐
     */
    private String salt;

    /**
     * MD5散列次数
     */
    private int hashIterations = 1;

    /**
     * MD5加密后byte数组
     */
    private byte[] hash;

    public MD5Hash(String source, String salt) {
        if (StringUtils.isBlank(source)) {
            throw new NullPointerException("source is null");
        }
        if (StringUtils.isBlank(salt)) {
            throw new NullPointerException("source is null");
        }
        this.source = source;
        this.salt = salt;
    }

    public MD5Hash(String source, String salt, int hashIterations) {
        if (StringUtils.isBlank(source)) {
            throw new NullPointerException("source is null");
        }
        if (StringUtils.isBlank(salt)) {
            throw new NullPointerException("source is null");
        }
        this.source = source;
        this.salt = salt;
        this.hashIterations = Math.max(this.hashIterations, hashIterations);
        generateHash();
    }

    /**
     * 生成hash码
     */
    private void generateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(this.salt.getBytes(Charsets.UTF_8));
            byte[] hashed = digest.digest(this.source.getBytes(Charsets.UTF_8));
            int iterations = hashIterations - 1;
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            this.hash = hashed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Hex生成字符串
     * @return md5加密加盐后的字符串
     */
    public String toHex() {
        if (null == hash) {
            return null;
        }
        return new String(Hex.encodeHex(hash));
    }
}
