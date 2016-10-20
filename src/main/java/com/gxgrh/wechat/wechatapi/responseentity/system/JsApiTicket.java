package com.gxgrh.wechat.wechatapi.responseentity.system;

/**JSAPI_TICKET返回的json参数对应的实体类
 * Created by Administrator on 2016/9/30.
 */
public class JsApiTicket{

    private String errcode;
    private String errmsg;
    private String ticket;
    private long expires_in;

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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }
}
