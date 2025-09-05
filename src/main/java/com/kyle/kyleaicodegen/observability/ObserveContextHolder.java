package com.kyle.kyleaicodegen.observability;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
public class ObserveContextHolder {
    private static final ThreadLocal<ObserveContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置上下文
     */
    public static void setContext(ObserveContext context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }

    /**
     *  获取上下文
     */
    public static ObserveContext getContext() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 清除上下文
     */
    public static void clearContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }
}
