package com.gxgrh.wechat.wechatapi.responseentity.usertags;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**某个标签下所有用户信息的实体
 * Created by Administrator on 2016/10/10.
 */
public class AllUserInOneTag {

    private long count;
    @SerializedName("data")
    private UserData userData;//可以直接定义成内部类

    @SerializedName("next_openid")
    private String nexOpenid;

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

    class UserData{
        private List<String> openid;

        public List<String> getOpenid() {
            return openid;
        }

        public void setOpenid(List<String> openid) {
            this.openid = openid;
        }
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getNexOpenid() {
        return nexOpenid;
    }

    public void setNexOpenid(String nexOpenid) {
        this.nexOpenid = nexOpenid;
    }
}
