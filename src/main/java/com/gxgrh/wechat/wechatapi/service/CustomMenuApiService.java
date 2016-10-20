package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.custommenu.CustomMenu;
import com.gxgrh.wechat.wechatapi.responseentity.custommenu.WeChatButton;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/9.
 */
@Service
public class CustomMenuApiService extends BaseApiService {
    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private final String getMenuPath = "/cgi-bin/menu/get";
    private final String deleteMenuPath = "/cgi-bin/menu/delete";
    private final String createMenuPath = "/cgi-bin/menu/create";
    private static final Logger logger = LogManager.getLogger(CustomMenuApiService.class);


    /**
     * 获取当前微信配置的自定义菜单
     * @return CustomMenu
     */
    public CustomMenu getCustomMenu(){
        CustomMenu customMenu = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getMenuPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            customMenu = gson.fromJson(jsonResponse, CustomMenu.class);
            if(null != customMenu.getErrcode()){
                //如果没有配置菜单 这里errcode是有值的
                String errMsg = "调用接口:"+url+"失败!原因:"+customMenu.getErrmsg();
                throw new Exception(errMsg);
            }
            for(WeChatButton weChatButton:customMenu.getMenu().getButton()){
                //调试查看
                logger.info(weChatButton.getName()+"||"+weChatButton.getSubButton().size());
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {


        }
        return customMenu;

    }


    /**
     *删除自定义菜单
     *
     * @return Map<String,String> 执行结果
     */
    public Map<String,String> deleteCustomMenu(){
        Map<String,String> resultMap = null;
            try{
                AccessToken accessToken = refreshAccessToken();
                uriBuilder.clearParameters();
                uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                        .setHost(Constants.WECHAT_API_HOST)
                        .setPath(this.deleteMenuPath)
                        .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
                String url = uriBuilder.build().toString();
                logger.info("use targetUrL:"+url);
                String jsonResponse = sendGet(url);
                logger.info("jsonResponse:"+jsonResponse);
                resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
                if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                    String errMsg = "调用接口:"+url+"失败!原因:"+resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                    throw new Exception(errMsg);
                }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;

    }


    /**
     * 创建自定义菜单接口方法
     *
     * @param menuJsonData 菜单的json信息
     * @return
     */
    public Map<String,String> createCustonMenu(String menuJsonData){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.createMenuPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+menuJsonData+"]");
            String jsonResponse =  sendPost(url,menuJsonData);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if("0" != resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE)){
                String errMsg = "调用接口:"+url+"失败!原因:"+resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;

    }


}
