package com.rul.tianchi.filter;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 与汇总节点通信接口
 *
 * @author RuL
 */
@RestController
public class FilterDataController {
    /**
     * 根据traceId获取trace
     *
     * @param traceId traceId
     * @return traceId对应的trace
     */
    @RequestMapping("/getTrace")
    public ArrayList<String> getTrace(@RequestBody String traceId) {
        ArrayList<String> trace = new ArrayList<>();
        for (int i = 0; i < FilterData.CACHE_SIZE; i++) {
            ArrayList<String> traceIds = FilterData.TRACE_CACHE.get(i).get(traceId);
            if (traceIds != null) {
                trace.addAll(traceIds);
            }
        }
        FilterData.badTraceIds.remove(traceId);
        return trace;
    }

}
