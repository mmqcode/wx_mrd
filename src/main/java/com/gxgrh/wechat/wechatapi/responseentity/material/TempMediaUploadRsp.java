package com.gxgrh.wechat.wechatapi.responseentity.material;

import com.google.gson.annotations.SerializedName;

/**新增临时素材时返回的json数据实体
 * Created by Administrator on 2016/10/11.
 */
public class TempMediaUploadRsp {

    private String type;

    @SerializedName("media_id")
    private String mediaId;

    @SerializedName("created_at")
    private String creaetAt;

    private String errcode;
    private String errmsg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getCreaetAt() {
        return creaetAt;
    }

    public void setCreaetAt(String creaetAt) {
        this.creaetAt = creaetAt;
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
