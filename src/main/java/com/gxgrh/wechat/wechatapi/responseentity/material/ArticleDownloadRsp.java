package com.gxgrh.wechat.wechatapi.responseentity.material;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取永久素材中的图文消息的json实体
 * Created by Administrator on 2016/10/12.
 */
public class ArticleDownloadRsp {

    @SerializedName("news_item")
    private List<Article> newsItem;


    @SerializedName("create_time")
    private String creatTime;
    @SerializedName("update_time")
    private String updateTime;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String errcode;
    private String errmsg;

    public List<Article> getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(List<Article> newsItem) {
        this.newsItem = newsItem;
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
