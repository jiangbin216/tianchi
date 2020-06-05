package com.rul.tianchi.filter;

import com.rul.tianchi.CommonController;
import com.rul.tianchi.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

/**
 * 从数据流拉取数据
 *
 * @author RuL
 */
public class PullData {
    private static final Logger LOGGER = LoggerFactory.getLogger(PullData.class);

    public static void pullData() {
        //连接到数据源
        HttpURLConnection connection = null;
        try {
            connection = getHttpConnection();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("connect fail");
        }
        if (connection == null) {
            LOGGER.info("connection is null");
            return;
        }

        try {
            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            //记录当前数据的行数
            int count = 0;
            String traceId;
            String tags;
            String finishedTraceId;

            LOGGER.info("start pulling data");
            while ((line = reader.readLine()) != null) {
                traceId = Utils.parseTraceId(line);
                tags = Utils.parseTags(line);

                ArrayList<String> spans;
                if (FilterData.traces.get(traceId) == null) {
                    spans = new ArrayList<>();
                    //记录trace首次出现位置
                    FilterData.traceIndex.put(count, traceId);
                } else {
                    spans = FilterData.traces.get(traceId);
                }
                spans.add(line);
                //将数据添加到traces
                FilterData.traces.put(traceId, spans);

                //判断是否是符合条件的traceId
                if (tags.contains("error=1") ||
                        (tags.contains("http.status_code=") && !tags.contains("http.status_code=200"))) {
                    FilterData.badTraceIds.add(traceId);
                }

                //已经读完的traceId
                if (count >= 20000) {
                    finishedTraceId = FilterData.traceIndex.get(count - 20000);
                    if (finishedTraceId != null) {
                        //发送traceId到汇总节点
                        SendData.sendFinishedTraceIdToGather(finishedTraceId);
                    }
                }
                count++;
            }
            //当前节点数据拉取完成，发送badTraceIds到汇总节点
            SendData.finishedPullData();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("catch IOException");
        }
    }

    /**
     * 获取数据源http连接
     */
    private static HttpURLConnection getHttpConnection() throws Exception {
        String port = System.getProperty("server.port", "8080");
        String path;
        if ("8000".equals(port)) {
            path = "http://localhost:" + CommonController.getDataSourcePort() + "/trace1.data";
        } else if ("8001".equals(port)) {
            path = "http://localhost:" + CommonController.getDataSourcePort() + "/trace2.data";
        } else {
            path = "";
        }
        URL url = new URL(path);
        return (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
    }
}
