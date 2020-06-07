package com.rul.tianchi.filter;

import java.util.*;

/**
 * 过滤节点数据封装
 *
 * @author RuL
 */
public class FilterData {

    //缓存trace
    public static List<HashMap<String, ArrayList<String>>> TRACE_CACHE = new ArrayList<>();
    //容量15避免ArrayList触发扩容机制
    public static final int CACHE_SIZE = 15;
    public static final int TRACE_MAP_SIZE = 20000;

    public static void initCache() {
        for (int i = 0; i < CACHE_SIZE; i++) {
            TRACE_CACHE.add(new HashMap<>());
        }
    }

    //保存所有符合条件的traceId
    public static Set<String> badTraceIds = new HashSet<>();

    //保存trace的偏移量
    public static HashMap<Long, String> traceIndex = new HashMap<>();

}
