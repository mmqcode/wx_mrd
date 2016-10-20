package com.gxgrh.wechat.wechatapi.responseentity.system;

/**请求access_token的api返回的json数据的实体类。
 * ===========对应好json数据设置适当的域即可直接用Gson工具类将数据直接转换成实体对象。=================
 * Created by Administrator on 2016/9/27.
 */
public class AccessToken {

    private String access_token;
    private long expires_in;
    private String errcode;
    private String errmsg;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
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
