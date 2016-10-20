package com.gxgrh.wechat.handle.requestentity;

import com.gxgrh.wechat.handle.base.BaseEventRequest;
import com.gxgrh.wechat.tools.Constants;

/**自定义菜单点击事件的请求类
 * Created by Administrator on 2016/9/27.
 */
public class ClickEventRequest extends BaseEventRequest {
    private String eventKey;//自定义的菜单key值

    public ClickEventRequest(String eventKey){
        this.eventKey = eventKey;
        setMsgType(Constants.WECHAT_MSG_TYPE_EVENT);
        setEventType(Constants.WECHAT_EVENT_TYPE_CLICK);
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
