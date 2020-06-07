package com.rul.tianchi.gather;

import com.alibaba.fastjson.JSON;
import com.rul.tianchi.CommonController;
import com.rul.tianchi.Utils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqData.class);
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
            String spans = SortData.sortAndMergeTrace(trace1, trace2);

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
        try {
            String result = JSON.toJSONString(GatherData.checkSum);
            RequestBody body = new FormBody.Builder()
                    .add("result", result).build();
            String url = String.format("http://localhost:%s/api/finished", CommonController.getDataSourcePort());
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = Utils.callHttp(request);
            if (response.isSuccessful()) {
                LOGGER.info("all finished");
                LOGGER.info("result:" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
