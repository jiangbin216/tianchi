package com.rul.tianchi.gather;

import com.alibaba.fastjson.JSON;
import com.rul.tianchi.CommonController;
import com.rul.tianchi.Utils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 从过滤节点请求数据
 *
 * @author RuL
 */
public class ReqData {
    //容量为2的线程池
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    //过滤节点port
    private static final int PORT1 = 8000;
    private static final int PORT2 = 8001;
    //本机URL前缀
    private static final String HOST = "http://localhost:";

    /**
     * 从两个过滤节点请求符合要求的数据
     *
     * @param traceId 符合要求的traceId
     */
    public static void getTraceFromFilter(String traceId) {
        pool.execute(() -> {
            RestTemplate template = new RestTemplate();
            ArrayList trace1 = template.postForObject(HOST + PORT1 + "/getTrace", traceId, ArrayList.class);
            ArrayList trace2 = template.postForObject(HOST + PORT2 + "/getTrace", traceId, ArrayList.class);
            //排序合并
            ArrayList trace = SortData.sortAndMergeTrace(trace1, trace2);

            StringBuffer spans = new StringBuffer();
            for (Object span : trace) {
                spans.append(span).append("\n");
            }
            //生成md5
            String md5 = Utils.MD5(new String(spans));
            GatherData.checkSum.put(traceId, md5);
            //将traceId从badTraceIds中删除
            GatherData.badTraceIds.remove(traceId);
        });
    }

    /**
     * 运行结束，数据上报
     */
    public static void finish() {
        String checkSumJSON = JSON.toJSONString(GatherData.checkSum);
        RestTemplate template = new RestTemplate();
        template.postForObject(HOST + CommonController.DATA_SOURCE_PORT + "/api/finished", checkSumJSON, Object.class);
    }
}
