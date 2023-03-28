package com.zhangbyby.bfc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * constants
 *
 * @author zhangbyby
 */
public interface Constants {
    String PLUGIN_TITLE = "BeanFieldComparator";

    Map<String, Map<String, String>> SOURCE_GENERICS = new ConcurrentHashMap<>();
    Map<String, Map<String, String>> TARGET_GENERICS = new ConcurrentHashMap<>();
}
