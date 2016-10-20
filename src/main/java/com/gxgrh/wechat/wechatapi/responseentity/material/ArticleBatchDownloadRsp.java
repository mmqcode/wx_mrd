package com.gxgrh.wechat.wechatapi.responseentity.material;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取图文素材列表json数据对应的实体类
 * Created by Administrator on 2016/10/12
 */
public class ArticleBatchDownloadRsp {

    @SerializedName("total_count")
    private String totalCount;

    @SerializedName("item_count")
    private String itemCount;

    @SerializedName("item")
    private List<Item> itemList;

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

    class Item{

        @SerializedName("media_id")
        private String mediaId;
        private Content content;
        @SerializedName("update_time")
        private String updateTime;

        class Content{
            @SerializedName("news_item")

            private List<Article> newsItem;

            @SerializedName("update_time")
            private String updateTime;
            @SerializedName("create_time")
            private String createTime;

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public List<Article> getNewsItem() {
                return newsItem;
            }

            public void setNewsItem(List<Article> newsItem) {
                this.newsItem = newsItem;
            }
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }


    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
