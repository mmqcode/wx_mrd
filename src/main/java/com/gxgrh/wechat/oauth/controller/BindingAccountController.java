package com.gxgrh.wechat.oauth.controller;

import com.google.gson.annotations.SerializedName;
import com.gxgrh.wechat.oauth.service.OauthHandler;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.WeChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/17.
 */
@Controller
@RequestMapping("/WeChat/BindingAccountController")
public class BindingAccountController {


    @Autowired
    private OauthHandler oauthHandler;


    @RequestMapping(value="/showBindingPage", method = RequestMethod.GET)
    public ModelAndView showBindingPage(HttpServletRequest request){
        String targetPage = "oauth/binding/showBindingAccountPage";
        Map<String,Object> resultMap = null;
        try{
            resultMap = new HashMap<String, Object>();
            WeChatUserInfo userInfo = oauthHandler.AccessPageControl(request);

            resultMap.put("message", "Hello!");

            resultMap.put("userinfo", userInfo);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ModelAndView(targetPage,"model",resultMap);
    }
}
