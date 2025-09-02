package com.kyle.kyleaicodegen.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

/**
 * 用于生成redis的缓存key
 * @author Haoran Wang
 * @since 2025
 */

public class CacheKeyUtils {
    /**
     * 生成缓存键的方法
     *
     * @param obj 需要生成缓存键的对象
     * @return 返回对象的MD5哈希值
     */
    public static String generateCacheKey(Object obj){
        if(obj==null){
            return DigestUtil.md5Hex("null");
        }
        return DigestUtil.md5Hex(JSONUtil.toJsonStr(obj));
    }
}
