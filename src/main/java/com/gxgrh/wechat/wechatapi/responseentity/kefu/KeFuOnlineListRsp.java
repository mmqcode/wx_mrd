package com.gxgrh.wechat.wechatapi.responseentity.kefu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取在线客服基本信息接口json数据对应的实体类
 * Created by Administrator on 2016/10/21.
 */
public class KeFuOnlineListRsp {


    @SerializedName("kf_online_list")
    private List<KeFuOnlineInfo> keFuOnlineInfoList;

    private String errmsg;
    private String errcode;

    public List<KeFuOnlineInfo> getKeFuOnlineInfoList() {
        return keFuOnlineInfoList;
    }

    public void setKeFuOnlineInfoList(List<KeFuOnlineInfo> keFuOnlineInfoList) {
        this.keFuOnlineInfoList = keFuOnlineInfoList;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    class KeFuOnlineInfo{
        public KeFuOnlineInfo() {


        }

        @SerializedName("kf_account")
        private String kfAccount; //完整客服帐号，格式为：帐号前缀@公众号微信号

        private String status; //客服在线状态，目前为：1、web 在线

        @SerializedName("kf_id")
        private String KfId; //	客服编号

        @SerializedName("accepted_case")
        private String acceptCase; // 客服当前正在接待的会话数

        public String getKfAccount() {
            return kfAccount;
        }

        public void setKfAccount(String kfAccount) {
            this.kfAccount = kfAccount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getKfId() {
            return KfId;
        }

        public void setKfId(String kfId) {
            KfId = kfId;
        }

        public String getAcceptCase() {
            return acceptCase;
        }

        public void setAcceptCase(String acceptCase) {
            this.acceptCase = acceptCase;
        }
    }

}
