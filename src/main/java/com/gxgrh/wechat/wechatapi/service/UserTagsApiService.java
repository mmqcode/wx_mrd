package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.AllTagsInOneUser;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.AllUserInOneTag;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.AllUserTags;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.Tag;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**用户标签相关的api实现
 * Created by Administrator on 2016/10/10.
 */
@Service
public class UserTagsApiService extends BaseApiService {

    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private static final Logger logger = LogManager.getLogger(CustomMenuApiService.class);

    private final String getAllUserTagsPath = "/cgi-bin/tags/get";
    private final String createUserTagPath = "/cgi-bin/tags/create";
    private final String updateUserTagPath = "/cgi-bin/tags/update";
    private final String deleteUserTagPath = "/cgi-bin/tags/delete";
    private final String batchAddTagPath = "/cgi-bin/tags/members/batchtagging";
    private final String userInOneTagPath = "/cgi-bin/user/tag/get";
    private final String batchUnTagUserPath = "/cgi-bin/tags/members/batchuntagging";
    private final String allTagsInOneUserPath = "/cgi-bin/tags/getidlist";


    /**
     *
     * @return 返回公众号当前所拥有的全部标签
     */
    public AllUserTags getAllUserTags(){
        AllUserTags allUserTags = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getAllUserTagsPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            allUserTags = gson.fromJson(jsonResponse, AllUserTags.class);
            if(null != allUserTags.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+allUserTags.getErrmsg();
                throw new Exception(errMsg);
            }
            for(Tag userTag:allUserTags.getAllUserTags()){
                logger.info("tag:"+userTag.getName()+"||"+userTag.getId()+"||"+userTag.getCount());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();

        }finally {

        }
        return allUserTags;

    }

    /**
     * 创建一个用户标签
     * 一个公众号，最多可以创建100个标签。
     *
     * @param tagToCreateJson
     * @return 返回创建成功的用户标签信息
     */
    public Tag createUserTag(String tagToCreateJson){
        Tag userTag = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.createUserTagPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+tagToCreateJson+"]");
            String jsonResponse = sendPost(url, tagToCreateJson);
            logger.info("jsonResponse:"+jsonResponse);
            userTag = gson.fromJson(jsonResponse, Tag.class);
            if(null != userTag.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+userTag.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return userTag;
    }

    /**
     *更新一个用户标签的名称,根据标签id进行更改
     *
     * @param tagToUpdateJson 要进行更新的用户标签的json信息
     * @return 结果map
     */

    public Map<String,String> updateUserTag(String tagToUpdateJson){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.updateUserTagPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+tagToUpdateJson+"]");
            String jsonResponse = sendPost(url, tagToUpdateJson);
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
     * 删除一个用户标签
     * @param tagToDeleteJson
     * @return
     */
    public Map<String,String> deleteUserTag(String tagToDeleteJson){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.deleteUserTagPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+tagToDeleteJson+"]");
            String jsonResponse = sendPost(url, tagToDeleteJson);
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
     *获取标签下粉丝列表
     * @param requestJson
     * @return
     */
    public AllUserInOneTag getAllUserInOneTag(String requestJson){
        AllUserInOneTag  allUserInOneTag = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.userInOneTagPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            allUserInOneTag = gson.fromJson(jsonResponse, AllUserInOneTag.class);
            if(null != allUserInOneTag.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+allUserInOneTag.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return allUserInOneTag;
    }



    /**
     * 批量为用户打标签
     *
     * @param userToTagJson
     * @return
     */
    public Map<String,String> batchAddUserToTag(String userToTagJson){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchAddTagPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+userToTagJson+"]");
            String jsonResponse = sendPost(url, userToTagJson);
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
     * 批量为用户取消标签
     *
     * @param requestJson
     * @return
     */
    public  Map<String,String> batchUnTagUser(String requestJson){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchUnTagUserPath)
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
     * 获取某个用户的所有标签
     * @param requestJson
     * @return
     */
    public AllTagsInOneUser getAllTagsInOneUser(String requestJson){
        AllTagsInOneUser allTagsInOneUser = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.allTagsInOneUserPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            allTagsInOneUser =  gson.fromJson(jsonResponse, AllTagsInOneUser.class);
            if(null != allTagsInOneUser.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+allTagsInOneUser.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return allTagsInOneUser;
    }

}
