package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.tools.Validate;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**有关消息处理的接口服务
 * Created by Administrator on 2016/10/12.
 */
@Service
public class MessageApiService  extends  BaseApiService{
    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private static final Logger logger = LogManager.getLogger(MessageApiService.class);

    private final String batchSendMessagePath = "/cgi-bin/message/mass/sendall";
    private final String deleteBatchMessageSended = "/cgi-bin/message/mass/delete";
    private final String previewBatchSendMessage = "/cgi-bin/message/mass/preview";
    private final String checkBatchSendMessageState = "/cgi-bin/message/mass/get";



    /**
     * 通过用户的标签进行消息的群发
     * @param messageType 群发的消息的种类
     *                    共有：mpnews(图文消息)、text（文本消息）、voice（语音消息）、
     *                    image（图片消息）、mpvideo（视频消息）、wxcard（卡券消息）共六类
     * @param tagId       群发目标用户的tagid
     * @param mediaId     除了文本消息外，其他消息需要其mediaId，发送文本消息时可置空null
     * @param content     单独为文本消息使用，发送其他消息时可置空null
     * @param isToAll     用于设定是否向全部用户发送，值为"true"或"false"，选择true该消息群发给所有用户，选择false可根据tag_id发送给指定群组的用户
     * @return Map<String,String> keys:errcode/errmsg/msg_id/ msg_data_id(only messageType='mpnews')
     */
    public Map<String,String> batchSendMessageByTags(String messageType, String tagId, String mediaId, String content,String isToAll){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchSendMessagePath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataForBatchSendMessageByTag(messageType, tagId, mediaId, content, isToAll);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
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
     *
     * 通过用户的openid进行群发
     * @param messageType 群发的消息的种类
     *                    共有：mpnews(图文消息)、text（文本消息）、voice（语音消息）、
     *                    image（图片消息）、mpvideo（视频消息）、wxcard（卡券消息）共六类
     * @param userOpenid  群发目标用户的openid
     * @param mediaId     除了文本消息外，其他消息需要其mediaId，发送文本消息时可置空null
     * @param content     单独为文本消息使用，发送其他消息时可置空null
     * @return Map<String,String> keys:errcode/errmsg/msg_id/ msg_data_id(only messageType='mpnews')
     */
    public Map<String,String> batchSendMessageByOpenid(String messageType, List<String> userOpenid, String mediaId, String content){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchSendMessagePath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataForBatchSendMessageByOpenId(messageType, userOpenid, mediaId, content);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
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
     * 群发之后，随时可以通过该接口删除群发。
     * @param msgId 发送出去的消息ID
     * 1、只有已经发送成功的消息才能删除
     * 2、删除消息是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
     * 3、删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
     * 4、如果多次群发发送的是一个图文消息，那么删除其中一次群发，就会删除掉这个图文消息也，导致所有群发都失效
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> deleteBatchMessageAlreadySend(String msgId){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.deleteBatchMessageSended)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("msg_id",msgId);
            String requestJson = gson.toJson(requestMap);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
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
     * 开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版
     * 订阅号与服务号认证后均可用
     * @param openid 用户的openid
     * @param type  消息的类型
     * @param mediaId 媒体mediaId,如果是发送文本消息，可以设置为null
     * @param content 文本消息内容，如果不是发送文本消息，可以设置为null
     * @param cardId  卡券id，如果不是发送卡券信息，可以设置为null
     * @param cardText 卡券text，如果不是发送卡券信息，可以设置为null
     * @return Map<String,String> keys:errcode/errmsg/msg_id
     */
    public Map<String,String> previewBatchSendMessage(String openid, String type ,String mediaId, String content,
                                                      String cardId, String cardText){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.previewBatchSendMessage)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataForPreviewBatchSendMessage(type, openid, mediaId, content, cardId, cardText);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
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
     * 查询群发消息发送状态【订阅号与服务号认证后均可用】
     * @param msgId 	群发消息后返回的消息id
     * @return Map<String,String> keys:msg_id/msg_status
     */
    public Map<String,String> checkBatchSendMessageState(String msgId){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.checkBatchSendMessageState)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("msg_id",msgId);
            String requestJson = gson.toJson(requestMap);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRMSG);
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;
    }



    private String makeJsonDataForBatchSendMessageByTag(String messageType, String tagId, String mediaId, String content, String isToAll) throws Exception{
        Map<String,Object> requestMap = new HashMap<String,Object>();
        if(!Validate.isString(messageType) || !Validate.isString(isToAll) || (!(Validate.isString(tagId)) &&  isToAll.equals("false"))){
            throw new Exception("参数不完整，无法制作群发消息。");
        }

        Map<String,Object> filterMap = new HashMap<String, Object>();
        Map<String,String> contentMap = new HashMap<String, String>();

        filterMap.put("is_to_all",Boolean.valueOf(isToAll));
        filterMap.put("tag_id",Integer.valueOf(tagId));
        if(messageType.trim().equalsIgnoreCase("mpnews")){
            contentMap.put("media_id",mediaId);
            requestMap.put("msgtype","mpnews");

            requestMap.put("mpnews",contentMap);
        }else if(messageType.trim().equalsIgnoreCase("text")){
            contentMap.put("content",content);
            requestMap.put("msgtype", "text");

            requestMap.put("text",contentMap);
        }else if(messageType.trim().equals("voice")){
            contentMap.put("media_id", mediaId);
            requestMap.put("msgtype", "voice");

            requestMap.put("voice",contentMap);
        }else if(messageType.trim().equals("image")){
            contentMap.put("media_id", mediaId);
            requestMap.put("msgtype", "image");

            requestMap.put("image",contentMap);

        }else if(messageType.trim().equals("mpvideo")){
            contentMap.put("media_id", mediaId);
            requestMap.put("msgtype", "mpvideo");

            requestMap.put("mpvideo",contentMap);
        }else if(messageType.trim().equals("wxcard")){
            contentMap.put("card_id", mediaId);
            requestMap.put("msgtype", "wxcard");

            requestMap.put("wxcard",contentMap);
        }

        requestMap.put("filter",filterMap);
        return gson.toJson(requestMap);
    }


    private String makeJsonDataForBatchSendMessageByOpenId(String messageType, List<String> userOpenidList, String mediaId, String content) throws Exception{
        Map<String,Object> requestMap = new HashMap<String,Object>();
        if(!Validate.isString(messageType) || (userOpenidList == null) || (userOpenidList.size() == 0)){
            throw new Exception("参数不完整，无法制作群发消息。");

        }

        Map<String,String> contentMap = new HashMap<String, String>();

        if(messageType.trim().equalsIgnoreCase("mpnews")){
            contentMap.put("media_id", mediaId);

            requestMap.put("mpnews", contentMap);
            requestMap.put("msgtype", "mpnews");

        }else if(messageType.trim().equalsIgnoreCase("text")){
            contentMap.put("content", content);

            requestMap.put("text", contentMap);
            requestMap.put("msgtype", "text");
        }else if(messageType.trim().equals("voice")){
            contentMap.put("media_id", mediaId);

            requestMap.put("voice", contentMap);
            requestMap.put("msgtype", "voice");
        }else if(messageType.trim().equals("image")){
            contentMap.put("media_id", mediaId);

            requestMap.put("image", contentMap);
            requestMap.put("msgtype", "image");

        }else if(messageType.trim().equals("mpvideo")){
            contentMap.put("media_id", mediaId);
            contentMap.put("title", "");
            contentMap.put("description", "");

            requestMap.put("mpvideo", contentMap);
            requestMap.put("msgtype", "mpvideo");
        }else if(messageType.trim().equals("wxcard")){
            contentMap.put("card_id", mediaId);

            requestMap.put("wxcard", contentMap);
            requestMap.put("msgtype", "wxcard");
        }
        requestMap.put("touser",userOpenidList);
        return gson.toJson(requestMap);

    }


    private String makeJsonDataForPreviewBatchSendMessage(String messageType, String openid, String mediaId, String content,
                                                          String cardId, String cardText) throws Exception{
        Map<String,Object> requestMap = new HashMap<String,Object>();
        if(!Validate.isString(messageType) || !Validate.isString(openid)
                || ( !Validate.isString(mediaId) && !Validate.isString(cardId) )){
            throw new Exception("参数不完整，无法制作群发消息。");
        }

        Map<String,String> contentMap = new HashMap<String, String>();
        if(messageType.trim().equalsIgnoreCase("mpnews")){
            contentMap.put("media_id", mediaId);

            requestMap.put("mpnews", contentMap);
            requestMap.put("msgtype", "mpnews");
        }else if(messageType.trim().equalsIgnoreCase("text")){
            contentMap.put("content", content);

            requestMap.put("text", contentMap);
            requestMap.put("msgtype", "text");

        }else if(messageType.trim().equals("voice")){
            contentMap.put("media_id", mediaId);

            requestMap.put("voice", contentMap);
            requestMap.put("msgtype", "voice");
        }else if(messageType.trim().equals("image")){
            contentMap.put("media_id", mediaId);

            requestMap.put("image", contentMap);
            requestMap.put("msgtype", "image");
        }else if(messageType.trim().equals("mpvideo")){
            contentMap.put("media_id", mediaId);

            requestMap.put("mpvideo", contentMap);
            requestMap.put("msgtype", "mpvideo");

        }else if(messageType.trim().equals("wxcard")){
            contentMap.put("card_id", cardId);
            contentMap.put("card_ext", cardText);


            requestMap.put("wxcard", contentMap);
            requestMap.put("msgtype", "wxcard");
        }
        requestMap.put("touser", openid);

        return gson.toJson(requestMap);
    }
}
