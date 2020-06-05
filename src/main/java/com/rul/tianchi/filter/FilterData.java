package com.rul.tianchi.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 过滤节点数据封装
 *
 * @author RuL
 */
public class FilterData {
    //保存所有的trace
    public static HashMap<String, ArrayList<String>> traces = new HashMap<>();

    //保存所有符合条件的traceId
    public static Set<String> badTraceIds = new HashSet<>();

    //保存trace的偏移量
    public static HashMap<Integer, String> traceIndex = new HashMap<>();

}
