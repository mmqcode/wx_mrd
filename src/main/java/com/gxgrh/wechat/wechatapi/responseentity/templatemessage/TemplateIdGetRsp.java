package com.gxgrh.wechat.wechatapi.responseentity.templatemessage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**获取模板列表接口对应的json信息实体
 * Created by Administrator on 2016/10/17.
 */
public class TemplateIdGetRsp {

    @SerializedName("template_list")
    private List<Template> templateList;

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

    public List<Template> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<Template> templateList) {
        this.templateList = templateList;
    }

    class Template{
        public Template() {
        }

        @SerializedName("template_id")
        private String templateId;

        private String title;

        @SerializedName("primary_industry")
        private String primayIndustry;

        @SerializedName("deputy_industry")
        private String deputyIndustry;

        private String content;

        private String example;

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrimayIndustry() {
            return primayIndustry;
        }

        public void setPrimayIndustry(String primayIndustry) {
            this.primayIndustry = primayIndustry;
        }

        public String getDeputyIndustry() {
            return deputyIndustry;
        }

        public void setDeputyIndustry(String deputyIndustry) {
            this.deputyIndustry = deputyIndustry;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }
    }

}
