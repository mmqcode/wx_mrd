package com.gxgrh.wechat.wechatapi.responseentity.usertags;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**将要进行打标签或者取消标签的实体信息
 * Created by Administrator on 2016/10/10.
 */
public class UserToTag {

    @SerializedName("openid_list")
    private List<String> openidList;//粉丝列表

    @SerializedName("tagid")
    private String tagId;

    public List<String> getOpenidList() {
        return openidList;
    }

    public void setOpenidList(List<String> openidList) {
        this.openidList = openidList;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
