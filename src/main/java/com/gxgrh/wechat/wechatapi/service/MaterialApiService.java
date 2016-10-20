package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.tools.Validate;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.material.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**有关素材管理的api服务
 * Created by Administrator on 2016/10/11.
 */
@Service
public class MaterialApiService extends  BaseApiService {


    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();
    @Autowired
    private JsonTool jsonTool;
    private static final Logger logger = LogManager.getLogger(MaterialApiService.class);

    private final String upLoadTempMediaPath = "/cgi-bin/media/upload";
    private final String downloadMediaPath = "/cgi-bin/media/get";
    private final String upLoadPermanentNewsPath = "/cgi-bin/material/add_news";
    private final String upLoadOtherPermanentMaterialPath = "/cgi-bin/material/add_material";
    private final String uploadNewsImgPath = "/cgi-bin/media/uploadimg";
    private final String downloadPermanentMaterialPath = "/cgi-bin/material/get_material";
    private final String deletePermanentMaterialPath = "/cgi-bin/material/del_material";
    private final String updatePermanentMaterialPath = "/cgi-bin/material/update_news";
    private final String getMaterialCountPath = "/cgi-bin/material/get_materialcount";
    private final String batchGetMaterialPath = "/cgi-bin/material/batchget_material";

    private final String MEDIA_TYPE = "type";
    private final String MEDIA_ID = "media_id";


    /**
     * 新增临时素材
     * @param mediaType 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param filePath
     * @return
     */
    public TempMediaUploadRsp upLoadTempMedia(String mediaType, String filePath){
        TempMediaUploadRsp tempMediaUploadRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.upLoadTempMediaPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter(MEDIA_TYPE,mediaType);
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use file:["+filePath+"]");
            String jsonResponse =  sendMutiPartFormData(url,filePath,"file");
            logger.info("jsonResponse:"+jsonResponse);
            tempMediaUploadRsp = gson.fromJson(jsonResponse, TempMediaUploadRsp.class);
            if(null != tempMediaUploadRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:"+tempMediaUploadRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return tempMediaUploadRsp;

    }

    /**
     * 获取临时素材 公众号可以使用本接口获取临时素材（即下载临时的多媒体文件）。非视频素材
     * @param mediaId 素材的mediaid
     * @return Map<String,Object> 文件名 key:fileName(String) 数据key:data(byte[])
     */
    public Map<String,Object> downLoadTempMedia(String mediaId){
        Map<String,Object> resultData = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.downloadMediaPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter(MEDIA_ID,mediaId);
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            resultData = downloadMutiPartFormDataByGet(url);
            logger.info("responseFileName:"+resultData.get("fileName"));
            String result = new String((byte[])resultData.get("data"),"utf-8");
            if(result.contains("errcode")){
                String errMsg = "调用接口:"+url+"失败!原因:"+result;
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultData;

    }

    /**
     *上传永久图文素材
     * @param articles 图文素材实体类集合
     * @return Map<String,String> key:media_id
     */
    public Map<String,String> uploadPermanentNewsMaterial(List<Article> articles){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.upLoadPermanentNewsPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String uploadData = this.makeJsonDataToUploadPermanentNewsMaterial(articles);
            logger.info("use targetUrL:["+url+"],use data:["+uploadData+"]");
            String jsonResponse = sendPost(url,uploadData);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(resultMap.get("errcode") != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;

    }


    /**
     * 上传其他永久素材到微信服务器(除了视频文件)
     *
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、和缩略图（thumb）
     * @param filePath 文件路径
     * @return Map<String,String> key:media_id  (如果是图片,另有:key:url)
     */
    public Map<String,String> uploadOtherPermanentMaterial(String type, String filePath){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.upLoadOtherPermanentMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter(MEDIA_TYPE,type);
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use file:["+filePath+"]");
            String jsonResponse =  sendMutiPartFormData(url,filePath,"media");
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(resultMap.get("errcode") != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;

    }

    /**
     * 上传图文消息内的图片获取URL
     * 在图文消息的具体内容中，将过滤外部的图片链接，开发者可以通过下述接口上传图片得到URL，放到图文内容中使用。
     * @param imgFilePath 图片文件路径
     * @return Map<String,String> key:url
     */
    public Map<String,String> uploadNewsImg(String imgFilePath){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.uploadNewsImgPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"],use file:["+imgFilePath+"]");
            String jsonResponse =  sendMutiPartFormData(url,imgFilePath,"media");
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(resultMap.get("errcode") != null){
                String errMsg = "调用接口:"+url+"失败!原因:" + resultMap.get("errmsg");
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultMap;

    }

    /**
     * 获取图文消息永久素材
     * @param mediaId
     * @return
     */
    public ArticleDownloadRsp downloadArticleMaterial(String mediaId){
        ArticleDownloadRsp articleDownloadRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.downloadPermanentMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            if(!Validate.isString(mediaId)){
                String errorMSg = "mediaId为空。";
                throw new Exception(errorMSg);
            }
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataDownloadPermanentNewsMaterial(mediaId);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            String jsonResponse = sendPost(url, requestJson);
            logger.info("jsonResponse:"+jsonResponse);
            articleDownloadRsp = gson.fromJson(jsonResponse, ArticleDownloadRsp.class);
            if(null != articleDownloadRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:" + articleDownloadRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return articleDownloadRsp;

    }

    /**
     * 获取其他永久素材（非图文和视频）
     * @param mediaId 素材的mediaid
     * @return Map<String,Object> 文件名 key:fileName(String) 数据key:data(byte[])
     */
    public Map<String,Object> downloadOtherMaterial(String mediaId){
        Map<String,Object> resultData = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.downloadPermanentMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataDownloadPermanentNewsMaterial(mediaId);
            logger.info("use targetUrL:["+url+"],use data:["+requestJson+"]");
            resultData = downloadMutiPartFormDataByPost(url,requestJson);
            logger.info("responseFileName:"+resultData.get("fileName"));
            String result = new String((byte[])resultData.get("data"),"utf-8");
            if(result.contains("errcode")){
                String errMsg = "调用接口:"+url+"失败!原因:"+result;
                throw new Exception(errMsg);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return resultData;

    }

    /**
     * 删除永久素材
     *
     * @param mediaId
     * @return
     */
    public Map<String,String> deletePermanentMateria(String mediaId){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.deletePermanentMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            if(!Validate.isString(mediaId)){
                String errorMSg = "mediaId为空。";
                throw new Exception(errorMSg);
            }
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataDownloadPermanentNewsMaterial(mediaId);//一致的json数据
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

    @Override
    protected Map<String, Object> downloadMutiPartFormDataByGet(String urlString) throws Exception {
        return super.downloadMutiPartFormDataByGet(urlString);
    }

    /**
     * 修改永久图文素材
     * @param mediaId 要修改的图文消息的id
     * @param index 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
     * @param article 文章实体类实例
     * @return Map<String,String> errcode errmsg
     */
    public Map<String,String> updateArticlePermanentMateria(String mediaId,String index,Article article){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.updatePermanentMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            if(!Validate.isString(mediaId)){
                String errorMSg = "mediaId为空。";
                throw new Exception(errorMSg);
            }
            String url = uriBuilder.build().toString();
            String requestJson = this.makeJsonDataUpdatePermanentNewsMaterial(mediaId,index,article);
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
     * 获取永久素材的总数，也会计算公众平台官网素材管理中的素材
     *
     * @return Map<String,String> keys:voice_count video_count  image_count news_count
     *
     */
    public Map<String,String> getPermenantMaterialCount(){
        Map<String,String> resultMap = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.getMaterialCountPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:["+url+"]");
            String jsonResponse = sendGet(url);
            logger.info("jsonResponse:"+jsonResponse);
            resultMap = this.jsonTool.jsonToSimpleMap(jsonResponse);
            if(null != resultMap.get(Constants.WECHAT_API_JSON_NODE_ERRCODE)){
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
     *获取图文消息素材列表
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count  指定返回素材的数量，取值在1到20之间
     * @return
     */
    public ArticleBatchDownloadRsp batchGetArticleMaterial(String offset, String count){
        ArticleBatchDownloadRsp articleBatchDownloadRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchGetMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String jsonRquest = this.makeJsonDataBatchGetMaterial("news",offset,count);
            logger.info("use targetUrL:["+url+"],user data:["+jsonRquest+"]");
            String jsonResponse = sendPost(url,jsonRquest);
            logger.info("jsonResponse:"+jsonResponse);
            articleBatchDownloadRsp = gson.fromJson(jsonResponse, ArticleBatchDownloadRsp.class);
            if(null != articleBatchDownloadRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:" +  articleBatchDownloadRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return articleBatchDownloadRsp;

    }

    /**
     *获取媒体素材列表
     * @param type 素材的类型，图片（image）、视频（video）、语音 （voice）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 指定返回素材的数量，取值在1到20之间
     * @return
     */
    public MediaBatchDownloadRsp batchGetMediaMaterial(String type, String offset, String count){
        MediaBatchDownloadRsp mediaBatchDownloadRsp = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(this.batchGetMaterialPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token());
            String url = uriBuilder.build().toString();
            String jsonRquest = this.makeJsonDataBatchGetMaterial(type,offset,count);
            logger.info("use targetUrL:["+url+"],user data:["+jsonRquest+"]");
            String jsonResponse = sendPost(url,jsonRquest);
            logger.info("jsonResponse:"+jsonResponse);
            mediaBatchDownloadRsp = gson.fromJson(jsonResponse, MediaBatchDownloadRsp.class);
            if(null != mediaBatchDownloadRsp.getErrcode()){
                String errMsg = "调用接口:"+url+"失败!原因:" +  mediaBatchDownloadRsp.getErrmsg();
                throw new Exception(errMsg);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return mediaBatchDownloadRsp;

    }

    /**
     *制作新增图文素材的json数据
     * @param articles
     * @return
     * @throws Exception
     */
    private String makeJsonDataToUploadPermanentNewsMaterial(List<Article> articles) throws Exception{
        Map<String,List<Article>> resultMap = new HashMap<String, List<Article>>();
        if(null == articles || articles.size() == 0){
            throw new Exception("图文素材为空，无法添加！");
        }
        if(articles.size() > 1){
            for(Article obj:articles){
                obj.setDigest("");
            }
        }
        resultMap.put("articles",articles);
        String jsonResult = gson.toJson(resultMap);
        return jsonResult;
    }


    /**
     * 制作更新图文素材需要的json数据
     * @param mediaId
     * @param index
     * @param article
     * @return
     * @throws Exception
     */
    private String makeJsonDataUpdatePermanentNewsMaterial(String mediaId, String index, Article article) throws Exception{
        Map<String,Object> resultMap = new HashMap<String, Object>();
        if(mediaId == null || index == null || article == null){
            throw new Exception("信息不全，无法进行图文素材更新！");
        }

        resultMap.put("media_id",mediaId);
        resultMap.put("index",index);
        resultMap.put("articles",article);

        String jsonResult = gson.toJson(resultMap);
        return jsonResult;

    }

    private String makeJsonDataDownloadPermanentNewsMaterial(String mediaId){
        Map<String,String> requestMap = new HashMap<String, String>();
        requestMap.put("media_id",mediaId);
        String requestJson = gson.toJson(requestMap);
        return requestJson;
    }

    /**
     *
     * @param type  素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset
     * @param count
     * @return
     */
    private String makeJsonDataBatchGetMaterial(String type, String offset, String count){
        Map<String,String> requestMap = new HashMap<String, String>();
        if(!Validate.isString(type)
                || !Validate.isString(offset)
                || !Validate.isString(count)){

        }
        requestMap.put("type",type);
        requestMap.put("offset",offset);
        requestMap.put("count",count);
        return gson.toJson(requestMap);
    }

}
