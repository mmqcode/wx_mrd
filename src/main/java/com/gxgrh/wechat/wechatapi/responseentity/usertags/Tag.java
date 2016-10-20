package com.gxgrh.wechat.wechatapi.responseentity.usertags;

import com.google.gson.annotations.SerializedName;

/**标签信息实体
 * Created by Administrator on 2016/10/10.
 */
public class Tag {

    private String id;
    private String name;
    private String count;

    private String errcode;
    private String errmsg;

    @SerializedName("tag")
    private Tag userTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Tag getUserTag() {
        return userTag;
    }

    public void setUserTag(Tag userTag) {
        this.userTag = userTag;
    }
}
