package com.gxgrh.wechat.wechatapi.responseentity.kefu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取客服会话列表返回的数据的实体类
 * Created by Administrator on 2016/10/21.
 */
public class KeFuSessionListRsp {

    @SerializedName("sessionlist")
    private List<Session> sessionList;

    private String errcode;
    private String errmsg;

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
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

    class Session{

        private String createtime;
        private String openid;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }


}
