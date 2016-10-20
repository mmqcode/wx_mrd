package com.gxgrh.wechat.wechatapi.responseentity.usertags;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**某个用户所拥有的标签的信息实体
 * Created by Administrator on 2016/10/10.
 */
public class AllTagsInOneUser {

    @SerializedName("tagid_list")
    private List<String> tagIdList;

    private String errcode;
    private String errmsg;

    public List<String> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(List<String> tagIdList) {
        this.tagIdList = tagIdList;
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
