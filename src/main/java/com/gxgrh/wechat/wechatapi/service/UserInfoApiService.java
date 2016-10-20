package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.MutiWeChatUserInfo;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.WeChatUserInfo;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.WeChatUserListInfo;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**微信用户信息相关的api
 * Created by Administrator on 2016/10/10.
 */
@Service
public class UserInfoApiService extends BaseApiService {


    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private static final Logger logger = LogManager.getLogger(UserInfoApiService.class);

    private final String updateUserRemarkPath = "/cgi-bin/user/info/updateremark";
    private final String getWeChatUserInfoPath = "/cgi-bin/user/info";
    private final String getMutiWeChatUserInfoPath = "/cgi-bin/user/info/batchget";
    private final String getWeChatUserListInfoPath = "/cgi-bin/user/get";
    private final String getBlackListPath = "/cgi-bin/tags/members/getblacklist";
    private final String batchBlackListPath = "/cgi-bin/tags/members/batchblacklist";
    private final String batchUnBlackListPath = "/cgi-bin/tags/members/batchunblacklist";
    private final String getOauthInfoWhileAccessPagePath = "/sns/oauth2/access_token";
    private final String getUserInfoWhileAccessPagePath = "/sns/userinfo";
    private final String NEXT_OPENID = "next_openid";
    private final String BEGIN_OPENID = "begin_openid";
    private final String OPENED_LIST = "opened_list";
    private final String CODE = "code";
    private final String OPENID = "openid";


    /**
     *通过该接口对指定用户设置备注名，该接口暂时开放给微信认证的服务号
     * @param requestJson
     * @return
     */
    public Map<String,String> updateUserInfoRemark(String requestJson){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.updateUserRemarkPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
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
     *获取单个微信用户信息
     * @param openid 用户的openid
     * @return
     */
    public WeChatUserInfo getWeChatUserInfo(String openid){
        WeChatUserInfo weChatUserInfo = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getWeChatUserInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter("openid",openid)
                    .setParameter("lang","zh_CN");
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            weChatUserInfo = gson.fromJson(jsonResponse, WeChatUserInfo.class);
            if(null != weChatUserInfo.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+weChatUserInfo.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return weChatUserInfo;

    }

    /**
     * 批量获取用户基本信息,最多支持一次拉取100条
     * @param openidList
     * @return
     */
    public MutiWeChatUserInfo getMutiWeChatUserInfo(List<String> openidList){
        MutiWeChatUserInfo mutiWeChatUserInfo = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getMutiWeChatUserInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.getMutiWeChatUserInfoRequestJson(openidList);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url,requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            mutiWeChatUserInfo = gson.fromJson(jsonResponse, MutiWeChatUserInfo.class);
            if(null != mutiWeChatUserInfo.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+mutiWeChatUserInfo.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return mutiWeChatUserInfo;

    }

    /**
     *公众号可通过本接口来获取帐号的关注者列表，关注者列表由一串OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。
     * 一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。
     * @param nextOpenId 起始openid，不填写的话从头开始拉取
     * @return
     */
    public WeChatUserListInfo getWeChatUserListInfo(String nextOpenId){
        WeChatUserListInfo weChatUserListInfo = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getWeChatUserListInfoPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter(NEXT_OPENID,"");
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            weChatUserListInfo = gson.fromJson(jsonResponse,WeChatUserListInfo.class);
            if(null != weChatUserListInfo.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+weChatUserListInfo.getErrmsg();
                throw new Exception(errMsg);
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return weChatUserListInfo;
    }

    /**
     * 获取黑名单列表
     * @param beginOpenid
     * @return
     */
    public WeChatUserListInfo getBlackList(String beginOpenid){
        WeChatUserListInfo weChatUserListInfo = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getBlackListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeGetBlackListRequestJson(beginOpenid);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url,requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            weChatUserListInfo = gson.fromJson(jsonResponse, WeChatUserListInfo.class);
            if(null != weChatUserListInfo.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+weChatUserListInfo.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return weChatUserListInfo;
    }

    /**
     * 拉黑用户
     * @param openidList 需要拉黑的用户的openid集合 一次拉黑最多允许20个
     * @return
     */
    public Map<String,String> batchBlackList(List<String> openidList){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchBlackListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeBatchBlackListRequestJson(openidList);
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
     * 取消拉黑用户
     * @param openidList 需要取消拉黑的用户的openid集合 一次最多允许20个
     * @return
     */
    public Map<String,String> batchUnBlackList(List<String> openidList){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchUnBlackListPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeBatchBlackListRequestJson(openidList);
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
     *通过code换取网页授权access_token(和openid)
     * 如果授权方式是:snsapi_base,可以直接用这里获取到的opendid来获取用户数据
     * @param code 授权code参数
     * @return Map<String,String>
     *     <p>access_token:网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同</p>
     *     <p>expires_in:ccess_token接口调用凭证超时时间，单位（秒）</p>
     *     <p>refresh_token:用户刷新access_token</p>
     *     <p>openid:用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID</p>
     *     <p>scope:用户授权的作用域，使用逗号（,）分隔</p>
     *     <p>(errcode)</p>
     *     <p>(errmsg)</p>
     *
     */
    public Map<String,String> getOauthInfoWhileAccessPage(String code){
        Map<String,String> resultMap = null;
        try{
            String appid = fileTool.getParamByName(Constants.WECHAT_APPID_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
            String secret = fileTool.getParamByName(Constants.WECHAT_SECRET_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getOauthInfoWhileAccessPagePath)
                    .setParameter(Constants.WECHAT_APPID_NAME,appid)
                    .setParameter(Constants.WECHAT_SECRET_NAME,secret)
                    .setParameter(CODE,code)
                    .setParameter(Constants.WECHAT_GETACCESSTOKEN_GRANT_TYPE_NAME,"authorization_code");
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(null != resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE) && !"0".equals(resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE))){
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
     *拉取用户信息(网页授权方式为： snsapi_userinfo的情况)
     * @param openid    普通用户的标识，对当前公众号唯一
     * @param pageAccessToken 调用接口凭证
     * @return WeChatUserInfo
     */
    public WeChatUserInfo getUserInfoWhileAccessPage(String openid, String pageAccessToken){
        WeChatUserInfo weChatUserInfo = null;
        try{
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getUserInfoWhileAccessPagePath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,pageAccessToken)
                    .setParameter(OPENID,openid)
                    .setParameter("lang","zh_CN");
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            weChatUserInfo = gson.fromJson(jsonResponse, WeChatUserInfo.class);
            if(weChatUserInfo.getErrcode() != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + weChatUserInfo.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return weChatUserInfo;
    }

    /**
     * 制作获取多个用户基本信息接口需要的json信息
     * @param openidList
     * @return
     * @throws Exception
     */
    private String getMutiWeChatUserInfoRequestJson(List<String> openidList) throws Exception{
        Map<String,String> userMap = null;
        List<Map<String,String>> userList = new ArrayList<Map<String, String>>();
        for(String openid:openidList){
            userMap = new HashMap<String, String>();
            userMap.put("openid",openid);
            userMap.put("lang","zh-CN");
            userList.add(userMap);
        }
        Map<String,List<Map<String,String>>> requestMap = new HashMap<String, List<Map<String, String>>>();
        requestMap.put("user_list",userList);
        return gson.toJson(requestMap);
    }

    /**
     * 制作获取黑名单需要的json信息
     * @param beginOpenId
     * @return
     */
    private String makeGetBlackListRequestJson(String beginOpenId){
        Map<String,String> temp = new HashMap<String, String>();
        temp.put(BEGIN_OPENID,beginOpenId);
        return gson.toJson(temp);
    }

    /**
     * 制作拉黑用户或者取消拉黑用户需要的json信息
     * @param openidList
     * @return
     */
    private String makeBatchBlackListRequestJson(List<String> openidList){
        Map<String,List<String>> requestMap = new HashMap<String, List<String>>();
        requestMap.put(OPENED_LIST,openidList);
        return gson.toJson(requestMap);
    }

}
