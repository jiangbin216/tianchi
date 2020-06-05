package com.rul.tianchi.gather;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 汇总节点数据封装
 *
 * @author RuL
 */
public class GatherData {

    public static HashSet<String> badTraceIds = new HashSet<>();
    public static HashSet<String> finishedTraceIds = new HashSet<>();

    //某个过滤节点拉取数据全部完成
    public static boolean oneFinished = false;

    public static HashMap<String, String> checkSum = new HashMap<>();
}
