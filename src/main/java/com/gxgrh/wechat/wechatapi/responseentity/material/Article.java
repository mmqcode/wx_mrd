package com.gxgrh.wechat.wechatapi.responseentity.material;

import com.google.gson.annotations.SerializedName;

/**新增永久图文消息素材时，使用本类反序列化可快速获得需要的json数据
 * Created by Administrator on 2016/10/12.
 */
public class Article {

    private String title;//标题
    @SerializedName("thumb_media_id")
    private String thumbMediaId;//图文消息的封面图片素材id（必须是永久mediaID）

    private String author;//作者

    private String digest;//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空

    @SerializedName("show_cover_pic")
    private String showCoverPic;//是否显示封面，0为false，即不显示，1为true，即显示

    private String content;//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS

    @SerializedName("content_source_url")
    private String contentSourceUrl;//图文消息的原文地址，即点击“阅读原文”后的URL


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(String showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }
}
