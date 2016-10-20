package com.gxgrh.wechat.wechatapi.responseentity.custommenu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class WeChatButton {

    private String type;
    private String name;
    private String key;
    private String url;

    @SerializedName("media_id")
    private String mediaId;
    @SerializedName("sub_button")
    private List<WeChatButton> subButton;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public List<WeChatButton> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<WeChatButton> subButton) {
        this.subButton = subButton;
    }
}
