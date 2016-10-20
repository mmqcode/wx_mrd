package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gxgrh.wechat.tools.*;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.system.WeChatServerIp;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**该类实现的是关于一些微信系统性接口的调用方法
 * Created by Administrator on 2016/9/27.
 */
@Service
public class SystemApiService extends BaseApiService {

    private URIBuilder uriBuilder = new URIBuilder();

    private final String getIpPath = "/cgi-bin/getcallbackip";
    private final String clearQuotaPath = "/cgi-bin/clear_quota";
    private final String getTicketToCreateQrCodePath = "/cgi-bin/qrcode/create";
    private final String getQrcodeImgPath = "/cgi-bin/showqrcode";
    private final String getShortUrlPath = "/cgi-bin/shorturl";

    private final String ticket = "ticket";


    private Gson gson = new Gson();

    @Autowired
    private FileTool fileTool;

    @Autowired
    private JsonTool jsonTool;

    private static final Logger logger = LogManager.getLogger(SystemApiService.class);

    /**
     * 获取微信服务器IP地址
     * @return WeChatServerIp
     * @throws Exception
     */
    public WeChatServerIp getCallbackIp() throws Exception{

        WeChatServerIp weChatServerIp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getIpPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());

            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            weChatServerIp = gson.fromJson(jsonResponse, WeChatServerIp.class);
            if(null != weChatServerIp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+weChatServerIp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return weChatServerIp;

    }

    /**
     * 微信公众号接口清零接口
     * 每个帐号每月共10次清零操作机会，清零生效一次即用掉一次机会（10次包括了平台上的清零和调用接口API的清零）。
     * @return
     * @throws Exception
     */
    public Map<String,String> clearQuota() throws Exception{
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken  = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.clearQuotaPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestJsonMap = new HashMap<String, String>();
            String appid = this.fileTool.getParamByName(Constants.WECHAT_APPID_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
            requestJsonMap.put("appid",appid);
            String requestJson = jsonTool.mapToJsonString(requestJsonMap);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url,requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:"+resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
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
     *
     * @param expireSeconds 临时二维码有效时间，单位秒。
     * @param actionName    二维码的类型，“0”表示临时，“1”表示永久
     * @param senceStr       二维码场景值，用以区分二维码功能，自定义数字 临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @return Map<String,String> keys:ticket/expire_seconds/url
     * <br/>ticket:获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     * <br/>expire_seconds:该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。(临时二维码才有)
     * <br/>url:二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    public Map<String,String> getTicketToCreateQrcode(String expireSeconds, String actionName, String senceStr){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken  = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getTicketToCreateQrCodePath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = makeJsonDataForGetTicketToCreateQrcode(expireSeconds, actionName, senceStr);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url,requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = jsonTool.jsonToSimpleMap(jsonResponse);
            if(null != resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE) && !"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:"+resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *将一条长链接转成短链接。
     * @param longUrl 需要转换的长链接，支持http://、https://、weixin://wxpay 格式的url
     * @return Map<String,String> keys:errcode/errmsg/short_url
     */
    public Map<String,String> getShortUrl(String longUrl){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken  = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getShortUrlPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = makeJsondataForGetShortUrl(longUrl);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url,requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:"+resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 通过ticket换取二维码
     * @param ticket
     * @return
     */
    public byte[] getQrcodeImgByTicket(String ticket){
        byte[] resultBytes = null;
        try{
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_GET_QRCODE_HOST)
                    .setPath(this.getQrcodeImgPath)
                    //.setParameter(this.ticket, URLEncoder.encode(ticket, "utf-8"));
                    .setParameter(this.ticket, ticket);//uriBuilder自动encode
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            Map<String, Object> resultMap = downloadMutiPartFormDataByGet(url);
            if(resultMap != null){
                resultBytes = (byte[])resultMap.get("data");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultBytes;

    }

    /**
     * 生成ticket需要的数据
     *
     */
    private String makeJsonDataForGetTicketToCreateQrcode(String expireSeconds, String actionName, String senceStr) throws Exception{
        if(!Validate.isString(actionName)){
            throw new Exception("没有指定二维码的类别，无法获取ticket。");
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        if("0".equals(actionName)){
            //临时二维码
            resultMap.put("expire_seconds", Integer.parseInt(expireSeconds));
            resultMap.put("action_name", "QR_SCENE");
            Map<String,Integer> senceIdMap = new HashMap<String, Integer>();
            senceIdMap.put("scene_id",Integer.parseInt(senceStr));
            Map<String,Object> senceMap = new HashMap<String,Object>();
            senceMap.put("scene",senceIdMap);
            resultMap.put("action_info", senceMap);
        }else if("1".equals(actionName)){
            //永久二维码
            resultMap.put("action_name", "QR_LIMIT_STR_SCENE");
            Map<String,String> senceIdMap = new HashMap<String, String>();
            senceIdMap.put("scene_str",senceStr);
            Map<String,Object> senceMap = new HashMap<String,Object>();
            senceMap.put("scene",senceIdMap);
            resultMap.put("action_info", senceMap);
        }else{
            throw new Exception("二维码的类别指定错误！");
        }
        return gson.toJson(resultMap);

    }

    /**
     * 获取转换为短链接的json数据
     * @param longUrl 需要进行转换的url
     * @return
     * @throws Exception
     */
    private String makeJsondataForGetShortUrl(String longUrl) throws Exception{
        Map<String,String> requestMap = new HashMap<String, String>();
        requestMap.put("action","long2short");
        requestMap.put("long_url",longUrl);
        Gson gson =new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(requestMap);
    }

}
