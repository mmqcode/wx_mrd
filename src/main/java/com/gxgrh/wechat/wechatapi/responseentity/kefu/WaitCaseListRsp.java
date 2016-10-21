package com.gxgrh.wechat.wechatapi.responseentity.kefu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取未接入会话列表接口数据对应的实体
 * Created by Administrator on 2016/10/21.
 */
public class WaitCaseListRsp {

    private String count;

    @SerializedName("waitcaselist")
    private List<WaitCase> waitCaseList;

    private String errcode;
    private String errmsg;

    public List<WaitCase> getWaitCaseList() {
        return waitCaseList;
    }

    public void setWaitCaseList(List<WaitCase> waitCaseList) {
        this.waitCaseList = waitCaseList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

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

    class WaitCase{
        public WaitCase() {
        }

        @SerializedName("latest_time")
        private String lastTime;

        private String openid;

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }

}
