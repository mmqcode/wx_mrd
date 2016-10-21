package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.kefu.KeFuListRsp;
import com.gxgrh.wechat.wechatapi.responseentity.kefu.KeFuOnlineListRsp;
import com.gxgrh.wechat.wechatapi.responseentity.kefu.KeFuSessionListRsp;
import com.gxgrh.wechat.wechatapi.responseentity.kefu.WaitCaseListRsp;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**客服管理api的实现
 * Created by Administrator on 2016/10/21.
 */
@Service
public class KeFuApiService extends BaseApiService{

    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;

    @Autowired
    private FileTool fileTool;

    private static final Logger logger = LogManager.getLogger(KeFuApiService.class);


    private static final String getKFListPath = "/cgi-bin/customservice/getkflist";
    private static final String getOnlineKFListPath = "/cgi-bin/customservice/getonlinekflist";
    private static final String addKFAccountPath = "/customservice/kfaccount/add";
    private static final String inviteWorkerPath = "/customservice/kfaccount/inviteworker";
    private static final String updateKFAccountInfoPath = "/customservice/kfaccount/update";
    private static final String uploadKFHeadImgPath = "/customservice/kfaccount/uploadheadimg";
    private static final String delKFAccountPath = "/customservice/kfaccount/del";
    private static final String createKfSessionPath = "/customservice/kfsession/create";
    private static final String closeKfSessionPath = "/customservice/kfsession/close";
    private static final String getKfSessionInfoPath = "/customservice/kfsession/getsession";
    private static final String getKfSessionListPath = "/customservice/kfsession/getsessionlist";
    private static final String getWaitCaseListPath = "/customservice/kfsession/getwaitcase";




    /**
     * 获取客服基本信息
     *
     * @return KeFuListRsp
     */
    public KeFuListRsp getKFList(){
        KeFuListRsp keFuListRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getKFListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse =  sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            keFuListRsp = gson.fromJson(jsonResponse, KeFuListRsp.class);
            if(null != keFuListRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+keFuListRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return keFuListRsp;
    }


    /**
     * 获取在线客服基本信息
     * @return KeFuOnlineListRsp
     */
    public KeFuOnlineListRsp getKeFuOnlineListInfo(){
        KeFuOnlineListRsp keFuOnlineListRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getOnlineKFListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse =  sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            keFuOnlineListRsp = gson.fromJson(jsonResponse, KeFuOnlineListRsp.class);
            if(null != keFuOnlineListRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+keFuOnlineListRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return keFuOnlineListRsp;

    }

    /**
     * 添加客服帐号
     * @param account 客服账号，自定义
     * @param nickName 客服昵称，自定义
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> addKFAccount(String account,String nickName){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(addKFAccountPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            //公众号账号
            String GZHAccount = this.fileTool.getParamByName(Constants.WECHAT_ACCOUNT_NAME, Constants.WECHAT_PROPERTIES_FILENAME);
            String kfAccount =  account+"@"+GZHAccount;
            requestMap.put("kf_account", kfAccount);
            requestMap.put("nickname", nickName);
            String requestJson = this.jsonTool.mapToJsonString(requestMap);

            String jsonResponse = sendPost(url, requestJson);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *邀请绑定客服帐号
     * @param kfAccount  完整客服帐号，格式为：帐号前缀@公众号微信号
     * @param inviteWxAccount 接收绑定邀请的客服微信号
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> inviteWorker(String kfAccount, String inviteWxAccount){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(inviteWorkerPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();

            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("kf_account", kfAccount);
            requestMap.put("invite_wx", inviteWxAccount);

            String requestJson = this.jsonTool.mapToJsonString(requestMap);
            String jsonResponse = sendPost(url, requestJson);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;

    }

    /**
     *
     *设置客服信息
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @param nickName 客服昵称，最长16个字
     * @return Map<String,String>  keys:errcode/errmsg
     */
    public Map<String,String> updateKFAccountInfo(String kfAccount, String nickName){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(updateKFAccountInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();

            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("kf_account", kfAccount);
            requestMap.put("nickname", nickName);

            String requestJson = this.jsonTool.mapToJsonString(requestMap);
            String jsonResponse = sendPost(url, requestJson);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;

    }

    /**
     *上传客服头像
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @param imgPath 头像文件路径，文件大小为5M 以内
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> uploadKFHeadImg(String kfAccount, String imgPath){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(uploadKFHeadImgPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token())
                    .setParameter("kf_account",kfAccount);
            String url = uriBuilder.build().toString();
            String jsonResponse = sendMutiPartFormData(url, imgPath, "media");
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;

    }


    /**
     *删除客服帐号
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> delKFAccount(String kfAccount){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(delKFAccountPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token())
                    .setParameter("kf_account",kfAccount);
            String url = uriBuilder.build().toString();
            String jsonResponse = sendGet(url);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *创建会话
     * 此接口在客服和用户之间创建一个会话，如果该客服和用户会话已存在，则直接返回0。指定的客服帐号必须已经绑定微信号且在线。
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @param openid 粉丝的openid
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> createKfSession(String kfAccount, String openid){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(createKfSessionPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();

            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("kf_account", kfAccount);
            requestMap.put("openid", openid);
            String jsonRequest = this.jsonTool.mapToJsonString(requestMap);
            String jsonResponse = sendPost(url, jsonRequest);
            if("0".equals(jsonResponse)){
                resultMap = new HashMap<String, String>();
                requestMap.put("errcode","0");
                requestMap.put("errmsg","ok");
            }else{
                resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
                if(!"0".equals(resultMap.get("errcode"))){
                    String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                    throw new Exception(errMsg);
                }
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     *
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @param openid 粉丝的openid
     * @return Map<String,String> keys:errcode/errmsg
     */
    public Map<String,String> closeKfSession(String kfAccount, String openid){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(closeKfSessionPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            Map<String,String> requestMap = new HashMap<String, String>();
            requestMap.put("kf_account", kfAccount);
            requestMap.put("openid", openid);
            String jsonRequest = this.jsonTool.mapToJsonString(requestMap);
            String jsonResponse = sendPost(url, jsonRequest);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取客户会话状态
     * 此接口获取一个客户的会话，如果不存在，则kf_account为空。
     * @param openid 粉丝的openid
     * @return Map<String,String> keys:createtime/kf_account or errcode/errmsg
     */
    public Map<String,String> getKfSessionInfo(String openid){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getKfSessionListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String jsonResponse = sendGet(url);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(!"0".equals(resultMap.get("errcode"))){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     *获取客服会话列表
     * @param kfAccount 完整客服帐号，格式为：帐号前缀@公众号微信号
     * @return
     */
    public KeFuSessionListRsp getKfSessionList(String kfAccount){
        KeFuSessionListRsp keFuSessionListRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getKfSessionInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token())
                    .setParameter("kf_account",kfAccount);
            String url = uriBuilder.build().toString();
            String jsonResponse = sendGet(url);
            keFuSessionListRsp = gson.fromJson(jsonResponse, KeFuSessionListRsp.class);
            if(keFuSessionListRsp.getErrcode() != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + keFuSessionListRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return keFuSessionListRsp;
    }

    /**
     *获取未接入会话列表
     * @return waitCaseListRsp
     */
    public WaitCaseListRsp getWaitCaseList(){
        WaitCaseListRsp waitCaseListRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getKfSessionInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME, accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String jsonResponse = sendGet(url);
            waitCaseListRsp = gson.fromJson(jsonResponse, WaitCaseListRsp.class);
            if(waitCaseListRsp.getErrcode() != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + waitCaseListRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return waitCaseListRsp;
    }


}
