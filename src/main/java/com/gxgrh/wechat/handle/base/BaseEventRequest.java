package com.gxgrh.wechat.handle.base;

/**事件请求的基类
 * Created by Administrator on 2016/9/26.
 */
public class BaseEventRequest extends BaseRequest {
    private String eventType;//事件的种类


    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
