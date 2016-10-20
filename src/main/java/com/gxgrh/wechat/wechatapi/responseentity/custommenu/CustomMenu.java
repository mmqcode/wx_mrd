package com.gxgrh.wechat.wechatapi.responseentity.custommenu;

/** 自定义菜单json数据对应的实体类，还未包含个性化菜单功能
 * Created by Administrator on 2016/10/9.
 */

public class CustomMenu {

    private Menu menu;

    private String errcode;
    private String errmsg;

    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
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