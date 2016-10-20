package com.gxgrh.wechat.handle.base;

/**消息请求的基类
 * Created by Administrator on 2016/9/26.
 */
public class BaseMsgRequest extends BaseRequest {
    private String msgId;//消息请求的id

    public String getMsgId(){
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }


}
