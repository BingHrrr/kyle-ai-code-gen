package com.kyle.kyleaicodegen.core.parser;

/**
 * 代码解析器策略接口
 * @author Haoran Wang
 * @since 2025
 */

public interface CodeParser<T> {

    /**
     * 解析代码内容
     * 这是一个泛型方法，用于解析传入的代码内容，并返回指定类型的结果对象
     * @param codeContent 原始代码内容
     * @return 解析后的结果对象
     */
    T parseCode(String codeContent);
}

