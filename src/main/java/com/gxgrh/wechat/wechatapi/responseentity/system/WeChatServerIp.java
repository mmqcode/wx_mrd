package com.gxgrh.wechat.wechatapi.responseentity.system;

import java.util.List;

/**对应获取微信ip地址的接口
 * 接口地址:https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN
 * Created by Administrator on 2016/9/28.
 */
public class WeChatServerIp {


    private List<String> ip_list;
    private String errcode;
    private String errmsg;


    public List<String> getIp_list() {
        return ip_list;
    }

    public void setIp_list(List<String> ip_list) {
        this.ip_list = ip_list;
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
