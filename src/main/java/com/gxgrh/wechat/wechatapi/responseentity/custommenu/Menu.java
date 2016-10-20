package com.gxgrh.wechat.wechatapi.responseentity.custommenu;

import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class Menu {

    private List<WeChatButton> button;

    private String menuId;

    public List<WeChatButton> getButton() {
        return button;
    }

    public void setButton(List<WeChatButton> button) {
        this.button = button;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
