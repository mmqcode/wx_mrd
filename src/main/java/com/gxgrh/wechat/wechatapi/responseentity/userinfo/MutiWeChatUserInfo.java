package com.gxgrh.wechat.wechatapi.responseentity.userinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MutiWeChatUserInfo {

    @SerializedName("user_info_list")
    private List<WeChatUserInfo> UserInfoList;

    private String errcode;
    private String errmsg;

    public List<WeChatUserInfo> getUserInfoList() {
        return UserInfoList;
    }

    public void setUserInfoList(List<WeChatUserInfo> userInfoList) {
        UserInfoList = userInfoList;
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
