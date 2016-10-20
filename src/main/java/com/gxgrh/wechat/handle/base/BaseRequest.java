package com.gxgrh.wechat.handle.base;

/**微信请求的抽象基类，主要是用于处理微信请求中共通的部分。
 * Created by Administrator on 2016/9/26.
 */
public class BaseRequest {
    private String toUserName;//开发者微信号
    private String fromUserName;// 请求发送方帐号（微信用户的OpenID，唯一）
    private long createTime;// 请求创建时间
    private String msgType;// 请求类型(类型的种类可以在微信开发文档中找到，常量类中也会存储有)


    public String getToUserName() {
        return toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
