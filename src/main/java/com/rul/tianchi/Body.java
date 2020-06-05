package com.rul.tianchi;

/**
 * 过滤节点与汇总节点通信信息封装
 *
 * @author RuL
 */
public class Body {
    String traceId;
    boolean isBadTrace;

    public Body(String traceId, boolean isBadTrace) {
        this.traceId = traceId;
        this.isBadTrace = isBadTrace;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isBadTrace() {
        return isBadTrace;
    }

    public void setBadTrace(boolean badTrace) {
        isBadTrace = badTrace;
    }
}
