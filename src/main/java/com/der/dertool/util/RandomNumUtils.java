package com.der.dertool.util;

import java.util.Random;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-14 13:57
 */
public class RandomNumUtils {

    /**
     * 随机数
     */
    private static final Random RANDOM = new Random();

    /**
     * 获取随机数，sync保证线程安全
     * @return int
     */
    public static synchronized int getNextInHundred() {
        return RANDOM.nextInt(100);
    }
}
