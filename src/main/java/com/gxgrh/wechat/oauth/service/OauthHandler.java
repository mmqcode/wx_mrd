package com.gxgrh.wechat.oauth.service;

import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.Validate;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.WeChatUserInfo;
import com.gxgrh.wechat.wechatapi.service.UserInfoApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**网页授权统一处理类
 * Created by Administrator on 2016/10/17.
 */
@Service
public class OauthHandler {

    private static final Logger logger = LogManager.getLogger(OauthHandler.class);
    private static final String CODE = "code";
    private static final String STATE = "state";

    @Autowired
    private UserInfoApiService userInfoApiService;

    public WeChatUserInfo AccessPageControl(HttpServletRequest request){
        String openid = request.getParameter("openid");
        String code = null;
        WeChatUserInfo userInfo = null;
        String state = null;
        try{
            if(!Validate.isString(openid)){
                code = request.getParameter(this.CODE);
                state = request.getParameter(this.STATE);
            }

            Map<String,String> infoMap =  userInfoApiService.getOauthInfoWhileAccessPage(code);
            if(infoMap  != null){
                openid = infoMap.get("openid");
                String accessToken = infoMap.get("access_token");
                try{
                    //(snsapi_base)或者查询数据库，如果已经存在有记录，则认证完毕。
                    userInfo = userInfoApiService.getWeChatUserInfo(openid);
                    //将用户信息放到session中
                    request.getSession().setAttribute(Constants.WECHAT_PAGE_CURRENTUSER,userInfo);
                }catch (Exception e){
                    logger.info(e.getMessage());
                }

                if(null == userInfo){
                    userInfo = userInfoApiService.getUserInfoWhileAccessPage(openid, accessToken);//(snsapi_userinfo)方式
                    //将用户信息放到session中
                    request.getSession().setAttribute(Constants.WECHAT_PAGE_CURRENTUSER,userInfo);
                }
                logger.info("用户数据已经获取到:"+userInfo.toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return userInfo;
    }


}
