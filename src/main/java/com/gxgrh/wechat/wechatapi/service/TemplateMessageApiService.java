package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.templatemessage.TemplateIdGetRsp;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**关于模板消息的接口服务
 * Created by Administrator on 2016/10/17.
 */
@Service
public class TemplateMessageApiService extends BaseApiService {

    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private static final Logger logger = LogManager.getLogger(TemplateMessageApiService.class);

    private  final String setIndustryPath = "/cgi-bin/template/api_set_industry";
    private  final String addTemplateIdPath = "/cgi-bin/template/api_add_template";
    private  final String getOwnTemplateIdPath = "/cgi-bin/template/get_all_private_template";
    private  final String delTemplateIdPath = "/cgi-bin/template/del_private_template";
    private  final String sendTemplateMessagePath = "/cgi-bin/message/template/send";



    /**
     *设置所属行业
     * 设置行业可在MP中完成，每月可修改行业1次，账号仅可使用所属行业中相关的模板
     * @param industryid1 行业id1
     * @param industryid2 行业id2
     * @return Map<Sring,String> keys: errmsg/errcode
     */
    public Map<String,String>   setIndustry(String industryid1,String industryid2){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.setIndustryPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonForSetIndustry(industryid1, industryid2);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *添加模板到微信账号,并获得该模板的id
     * @param templateIdShort 模板短id，这个id需要在微信公众号官网服务后台获得
     * @return Map<Sring,String> keys: errmsg/errcode
     */
    public Map<String,String> addTemplate(String templateIdShort){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.addTemplateIdPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("template_id_short",templateIdShort);
            String requestJson = this.gson.toJson(requestMap);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *
     *获取已添加至帐号下所有模板列表
     * @return TemplateIdGetRsp
     */
    public TemplateIdGetRsp getAllPrivateTemplate(){
        TemplateIdGetRsp templateIdGetRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getOwnTemplateIdPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            templateIdGetRsp = gson.fromJson(jsonResponse, TemplateIdGetRsp.class);
            if(templateIdGetRsp.getErrcode() != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + templateIdGetRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return templateIdGetRsp;
    }


    /**
     * 删除模板
     * @param templateId 模板id
     * @return Map<Sring,String> keys: errmsg/errcode
     */
    private Map<String,String> delTemplate(String templateId){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.delTemplateIdPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("template_id",templateId);
            String requestJson = this.gson.toJson(requestMap);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 发送模板消息
     * @param userOpenId
     * @param templateId
     * @param TempalteUrl
     * @param messageContent
     * @return Map<Sring,String> keys: errmsg/errcode/msgid
     */
    private Map<String,String> sendTemplateMessage(String userOpenId, String templateId, String TempalteUrl, String messageContent){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.sendTemplateMessagePath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataForSendTemplateMessage(userOpenId, templateId, TempalteUrl, messageContent);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }




    private String makeJsonDataForSendTemplateMessage(String userOpenId, String templateId, String TempalteUrl, String messageContent){
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("touser", userOpenId);
        requestMap.put("template_id",templateId);
        requestMap.put("url", TempalteUrl);
        requestMap.put("data", messageContent);
        return gson.toJson(requestMap);
    }


    private String makeJsonForSetIndustry(String industryid1, String industryid2){
        Map<String,String> requestMap  = new HashMap<String, String>();
        requestMap.put("industry_id1",industryid1);
        requestMap.put("industry_id2",industryid2);
        return this.gson.toJson(requestMap);
    }

}
