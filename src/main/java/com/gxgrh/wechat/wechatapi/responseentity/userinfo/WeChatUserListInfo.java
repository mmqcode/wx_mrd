package com.gxgrh.wechat.wechatapi.responseentity.userinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取用户列表接口返回的json数据对应的实体
 * Created by Administrator on 2016/10/11.
 */
public class WeChatUserListInfo {

    private long total;
    private long count;
    @SerializedName("data")
    private OpenIdList openIdList;

    @SerializedName("next_openid")
    private String nextOpenId;


    private String errcode;
    private String errmsg;

    class OpenIdList{
        @SerializedName("openid")
        private List<String> openidList;

        public List<String> getOpenidList() {
            return openidList;
        }

        public void setOpenidList(List<String> openidList) {
            this.openidList = openidList;
        }
    }



    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public OpenIdList getOpenIdList() {
        return openIdList;
    }

    public void setOpenIdList(OpenIdList openIdList) {
        this.openIdList = openIdList;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
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
