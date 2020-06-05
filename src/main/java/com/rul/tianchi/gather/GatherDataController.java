package com.rul.tianchi.gather;

import com.rul.tianchi.Body;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * 与过滤节点通信接口
 *
 * @author RuL
 */
@RestController
public class GatherDataController {
    @RequestMapping("/setFinishedTraceId")
    public String setFinishedTraceId(@RequestBody Body body) {
        //是否是badTrace
        if (body.isBadTrace()) {
            GatherData.badTraceIds.add(body.getTraceId());
        }

        if (GatherData.finishedTraceIds.contains(body.getTraceId()) && GatherData.badTraceIds.contains(body.getTraceId())) {
            //两个节点均统计完毕且是符合要求的trace,从两个过滤节点请求符合要求的数据
            ReqData.getTraceFromFilter(body.getTraceId());
        } else if (GatherData.finishedTraceIds.contains(body.getTraceId()) && !GatherData.badTraceIds.contains(body.getTraceId())) {
            //两个节点均统计完毕且是符合要求的trace，从两个过滤节点删除数据
            ReqData.delTraceOnFilter(body.getTraceId());
        } else {
            //只有一个节点统计完毕
            GatherData.finishedTraceIds.add(body.getTraceId());
        }
        return "success";
    }

    /**
     * 某个节点统计完后剩余的badTraceIds
     *
     * @param badTraceIds badTraceIds
     * @return success
     */
    @RequestMapping("/finishedPullData")
    public String finishedPullData(@RequestBody HashSet<String> badTraceIds) {
        GatherData.badTraceIds.addAll(badTraceIds);
        //另一个过滤节点已经完成数据拉取操作
        if (GatherData.oneFinished) {
            ReqData.pullFinishedData();
        } else {
            GatherData.oneFinished = true;
        }
        return "success";
    }

}
