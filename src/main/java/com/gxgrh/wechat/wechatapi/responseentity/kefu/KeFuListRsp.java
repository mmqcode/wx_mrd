package com.gxgrh.wechat.wechatapi.responseentity.kefu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取客服基本信息返回的json数据对应的实体
 * Created by Administrator on 2016/10/21.
 */
public class KeFuListRsp {

    @SerializedName("kf_list")
    private List<KfInfo> kfList;

    private String errcode;
    private String errmsg;

    class KfInfo{
        public KfInfo() {
        }

        @SerializedName("kf_account")
        private String kfAccount; //完整客服帐号，格式为：帐号前缀@公众号微信号

        @SerializedName("kf_headimgurl")
        private String kfHeadimgUrl; //客服头像

        @SerializedName("kf_id")
        private String kfId; //客服编号

        @SerializedName("kf_nick")
        private String kfNickName; //	客服昵称

        @SerializedName("kf_wx")
        private String kfWx; //如果客服帐号已绑定了客服人员微信号 则此处显示微信号

        @SerializedName("invite_wx")
        private String inviteWx; //如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请,则此处显示绑定邀请的微信号

        @SerializedName("invite_expire_time")
        private String inviteExpireTime; //如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请,邀请的过期时间，为unix 时间戳

        @SerializedName("invite_status")
        private String inviteStatus; //邀请的状态，有等待确认“waiting”，被拒绝“rejected”，s过期“expired”

        public String getKfAccount() {
            return kfAccount;
        }

        public void setKfAccount(String kfAccount) {
            this.kfAccount = kfAccount;
        }

        public String getKfHeadimgUrl() {
            return kfHeadimgUrl;
        }

        public void setKfHeadimgUrl(String kfHeadimgUrl) {
            this.kfHeadimgUrl = kfHeadimgUrl;
        }

        public String getKfId() {
            return kfId;
        }

        public void setKfId(String kfId) {
            this.kfId = kfId;
        }

        public String getKfNickName() {
            return kfNickName;
        }

        public void setKfNickName(String kfNickName) {
            this.kfNickName = kfNickName;
        }

        public String getKfWx() {
            return kfWx;
        }

        public void setKfWx(String kfWx) {
            this.kfWx = kfWx;
        }

        public String getInviteWx() {
            return inviteWx;
        }

        public void setInviteWx(String inviteWx) {
            this.inviteWx = inviteWx;
        }

        public String getInviteExpireTime() {
            return inviteExpireTime;
        }

        public void setInviteExpireTime(String inviteExpireTime) {
            this.inviteExpireTime = inviteExpireTime;
        }

        public String getInviteStatus() {
            return inviteStatus;
        }

        public void setInviteStatus(String inviteStatus) {
            this.inviteStatus = inviteStatus;
        }
    }

    public List<KfInfo> getKfList() {
        return kfList;
    }

    public void setKfList(List<KfInfo> kfList) {
        this.kfList = kfList;
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
}
