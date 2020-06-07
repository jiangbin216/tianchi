package com.rul.tianchi.gather;

import com.rul.tianchi.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 数据排序
 *
 * @author RuL
 */
public class SortData {
    /**
     * 合并两链并排序
     *
     * @return 合并后的有序链
     */
    public static String sortAndMergeTrace(ArrayList<String> list1, ArrayList<String> list2) {
        HashSet<String> spanSet = new HashSet<>();
        spanSet.addAll(list1);
        spanSet.addAll(list2);
        String spans = spanSet.stream().sorted(
                Comparator.comparing(Utils::parseStartTime)).collect(Collectors.joining("\n"));

        return spans;
    }

}
