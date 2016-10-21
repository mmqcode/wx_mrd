package com.gxgrh.wechat.wechatapi.responseentity.kefu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取聊天记录接口返回数据对应的实体
 * Created by Administrator on 2016/10/21.
 */
public class MsgListRsp {

    @SerializedName("recordlist")
    private List<Record> recordList;

    //number和msgid两个参数用以判断当前时间段内的消息是否拉取完毕，具体如何使用请参考：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1464937269_mUtmK&token=&lang=zh_CN
    private String number;
    private String msgid;

    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    class Record{

        public Record() {

        }

        private String openid; //用户标识
        private String opercode;// 	操作码，2002（客服发送信息），2003（客服接收消息）
        private String text;// 聊天记录
        private String time;//	操作时间，unix时间戳
        private String worker;//完整客服帐号，格式为：帐号前缀@公众号微信号

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getOpercode() {
            return opercode;
        }

        public void setOpercode(String opercode) {
            this.opercode = opercode;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWorker() {
            return worker;
        }

        public void setWorker(String worker) {
            this.worker = worker;
        }
    }


}
