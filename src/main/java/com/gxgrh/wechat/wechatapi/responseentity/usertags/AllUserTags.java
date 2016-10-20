package com.gxgrh.wechat.wechatapi.responseentity.usertags;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**公众号所拥有的所有标签信息实体
 * Created by Administrator on 2016/10/10.
 */
public class AllUserTags {

    @SerializedName("tags")
    private List<Tag> AllUserTags;

    private String errcode;
    private String errmsg;

    public List<Tag> getAllUserTags() {
        return AllUserTags;
    }

    public void setAllUserTags(List<Tag> allUserTags) {
        AllUserTags = allUserTags;
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
