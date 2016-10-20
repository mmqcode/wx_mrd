package com.gxgrh.wechat.wechatapi.responseentity.templatemessage;

import com.google.gson.annotations.SerializedName;

/**获取设置的行业信息的接口 返回的json数据的对应实体
 * Created by Administrator on 2016/10/17.
 */
public class IndustryGetRsp {

    @SerializedName("primary_industry")
    private IndustryClass primaryIndustry;
    @SerializedName("secondary_industry")
    private IndustryClass secondaryIndustry;

    class IndustryClass {
        public IndustryClass() {

        }

        @SerializedName("first_class")
        private String firstClass;

        @SerializedName("second_class")
        private String secondClass;

        public String getFirstClass() {
            return firstClass;
        }

        public void setFirstClass(String firstClass) {
            this.firstClass = firstClass;
        }

        public String getSecondClass() {
            return secondClass;
        }

        public void setSecondClass(String secondClass) {
            this.secondClass = secondClass;
        }
    }

    public IndustryClass getPrimaryIndustry() {
        return primaryIndustry;
    }

    public void setPrimaryIndustry(IndustryClass primaryIndustry) {
        this.primaryIndustry = primaryIndustry;
    }

    public IndustryClass getSecondaryIndustry() {
        return secondaryIndustry;
    }

    public void setSecondaryIndustry(IndustryClass secondaryIndustry) {
        this.secondaryIndustry = secondaryIndustry;
    }
}
