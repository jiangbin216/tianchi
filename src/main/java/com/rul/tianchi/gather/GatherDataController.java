package com.rul.tianchi.gather;

import com.rul.tianchi.Body;
import com.rul.tianchi.filter.SendData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 与过滤节点通信接口
 *
 * @author RuL
 */
@RestController
public class GatherDataController {
    @RequestMapping("/setFinishedTraceId")
    public String setFinishedTraceId(@RequestBody Body body) {
        //是badTrace
        if (body.isBadTrace()) {
            GatherData.badTraceIds.add(body.getTraceId());
        }

        if (GatherData.finishedTraceIds.contains(body.getTraceId()) && GatherData.badTraceIds.contains(body.getTraceId())) {
            //两个节点均统计完毕且是符合要求的trace,从两个过滤节点请求符合要求的数据
            ReqData.getTraceFromFilter(body.getTraceId());
        } else {
            GatherData.finishedTraceIds.add(body.getTraceId());
        }
        return "success";
    }

    @RequestMapping("/finished")
    public String finished() {
        if (GatherData.oneFinished) {
            ReqData.finish();
        } else {
            GatherData.oneFinished = true;
        }
        return "success";
    }
}
