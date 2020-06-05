package com.rul.tianchi.filter;

import com.rul.tianchi.Body;
import com.rul.tianchi.NodePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 发送数据到汇总节点
 *
 * @author RuL
 */
public class SendData {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendData.class);
    //容量为2的线程池
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    //本机URL前缀
    private static final String HOST = "http://localhost:";

    /**
     * 发送已经统计完成的traceId到汇总节点
     *
     * @param traceId 当前节点统计完成的traceId
     */
    public static void sendFinishedTraceIdToGather(String traceId) {
        pool.execute(() -> {
            Body body;
            //是badTraceId
            if (FilterData.badTraceIds.contains(traceId)) {
                body = new Body(traceId, true);
            } else {
                body = new Body(traceId, false);
            }
            RestTemplate template = new RestTemplate();
            template.postForObject(HOST + NodePort.GATHER_PORT + "/setFinishedTraceId", body, String.class);
        });
    }

    /**
     * 数据拉取成功，发送badTrace到汇总节点
     */
    public static void finishedPullData() {
        pool.execute(() -> {
            RestTemplate template = new RestTemplate();
            String result = template.postForObject(HOST + NodePort.GATHER_PORT + "/finishedPullData", FilterData.badTraceIds, String.class);
            LOGGER.info("finished pull data " + result);
        });
    }
}
