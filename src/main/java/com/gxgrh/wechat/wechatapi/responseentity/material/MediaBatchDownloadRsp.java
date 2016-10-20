package com.gxgrh.wechat.wechatapi.responseentity.material;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取媒体素材列表json数据对应的实体类
 * Created by Administrator on 2016/10/12.
 */
public class MediaBatchDownloadRsp {


    @SerializedName("total_count")
    private String totalCount;

    @SerializedName("item_count")
    private String itemCount;

    @SerializedName("item")
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    private String errcode;
    private String errmsg;

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

        private String name;

        @SerializedName("update_time")
        private String updateTime;

        private String url;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
