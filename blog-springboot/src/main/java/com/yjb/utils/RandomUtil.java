package com.yjb.utils;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();


    /**
     * 返回指定范围内的随机数
     *
     * @param start 开始 --包括
     * @param end 结束 -- 包括
     * @return
     */
    public static int nextInt(int start, int end) {
        return random.nextInt(end - start) + start + 1;  // +1:包括右界值
    }
}
