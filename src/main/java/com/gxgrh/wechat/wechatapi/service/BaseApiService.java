package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.HttpTool;
import com.gxgrh.wechat.tools.TimeTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**微信api调用服务类的基类，继承此类可以提供统一的get、post的调用操作。
 * Created by Administrator on 2016/9/27.
 */
public class BaseApiService {

    @Autowired
    private HttpTool httpTool;

    @Autowired
    public FileTool fileTool;

    private final String getTokenPath = "/cgi-bin/token";
    private static final Logger logger = LogManager.getLogger(BaseApiService.class);
    /**
     * 刷新并返回token
     * @return
     * @throws Exception
     */
    protected AccessToken refreshAccessToken() throws Exception{
        //调用接口前需要进行access_token的更新操作，当然，如果access_token未失效则不用更新。
        // 在子类中直接使用该方法进行更新并获取access_token值。

        long alertTime = 5*60*1000L;//五分钟

        AccessToken accessToken = null;
        try{
            String tokenExpireTimeString = this.fileTool.getParamByName(Constants.WECHAT_ACCESS_TOKEN_EXPIRE_TIME_NAME,
                    Constants.WECHAT_PROPERTIES_FILENAME);
            Long tokenExpireTimeLong =  Long.parseLong(tokenExpireTimeString);
            long interval = tokenExpireTimeLong - TimeTool.getCurrentTime();
            if(interval < alertTime){
                //更新
                accessToken = getAccessToken();

            }else{
                String temp = this.fileTool.getParamByName(Constants.WECHAT_ACCESS_TOKEN_NAME,
                        Constants.WECHAT_PROPERTIES_FILENAME);
               // logger.info("从配置文件获取到的access_token:"+temp);
                accessToken = new AccessToken();
                accessToken.setAccess_token(temp);
                accessToken.setExpires_in(tokenExpireTimeLong);
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally{

        }
        if(null == accessToken){
            String errorMsg = "无法获取到access_token,调用接口失败！";
            throw new Exception(errorMsg);
        }
        return accessToken;
    }


    /**
     * 调用微信接口获取access_token.
     * @return
     * @throws Exception
     */
    private AccessToken getAccessToken() throws Exception{
        //目标 uri: https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String appid = this.fileTool.getParamByName(Constants.WECHAT_APPID_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        String secret = this.fileTool.getParamByName(Constants.WECHAT_SECRET_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)//https
                .setHost(Constants.WECHAT_API_HOST)// api.weixin.qq.com
                .setPath(this.getTokenPath)// /cgi-bin/token
                .setParameter(Constants.WECHAT_GETACCESSTOKEN_GRANT_TYPE_NAME, "client_credential")
                .setParameter(Constants.WECHAT_APPID_NAME, appid)
                .setParameter(Constants.WECHAT_SECRET_NAME,secret);
        AccessToken accessToken = null;
        try{

            String jsonResult = sendGet(uriBuilder.build().toString());
            accessToken = new Gson().fromJson(jsonResult,AccessToken.class);

            if(null != accessToken.getErrcode()){
                logger.error("请求AccessToken调用接口发生错误，errorCode["+accessToken.getErrcode()+"]" +
                        ",errorMsg["+ accessToken.getErrmsg()+"]");
                throw new Exception("AccessToken请求失败。");
            }
            //计算失效的时间点
            Long expire_time = TimeTool.getCurrentTime()+accessToken.getExpires_in()*1000L;

            //将获取到的access_token和失效时间点存入properties文件中
            this.fileTool.updateProperties(Constants.WECHAT_ACCESS_TOKEN_NAME,
                    accessToken.getAccess_token(),
                    Constants.WECHAT_PROPERTIES_FILENAME);
            this.fileTool.updateProperties(Constants.WECHAT_ACCESS_TOKEN_EXPIRE_TIME_NAME,
                    expire_time+"",
                    Constants.WECHAT_PROPERTIES_FILENAME);
            accessToken.setExpires_in(expire_time);//存储失效时间点
            logger.info("access_token已经重新获取:token["+accessToken.getAccess_token()+"],expire_time["+TimeTool.getCurrentTimeBySeconds(expire_time) +"]");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return accessToken;
    }



    /**
     * 对微信服务器指定api发送get请求
     * @param requestUrl 微信api的路径
     * @return 结果json的String串
     * @throws Exception
     */
    protected String sendGet(String requestUrl) throws Exception{
        String responseJson = null;
        try{
            responseJson =  httpTool.sendSSLGetMethod(requestUrl);

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseJson;
    }

    /**
     * 对微信服务器指定api发送post请求
     * @param requestUrl 微信api的路径
     * @param requestJson 请求需要的json数据
     * @return 结果json的String串
     * @throws Exception
     */
    protected String sendPost(String requestUrl,String requestJson) throws Exception{
        String responseJson = null;
        try{
            responseJson = httpTool.sendSSLPostMethod(requestUrl,requestJson);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseJson;
    }

    /**
     * 上传素材文件到微信服务器
     * @param urlString url
     * @param filePath 文件路径
     * @return 结果json的String串
     * @throws Exception
     */
    protected String sendMutiPartFormData(String urlString, String filePath, String formdataName) throws Exception{
        String responseJson = null;
        try{
            responseJson = httpTool.sendSSLMutiPartFormData(urlString,filePath,formdataName);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseJson;
    }

    /**
     * 下载微信临时素材
     * @param urlString url
     * @return
     * @throws Exception
     */
    protected Map<String,Object> downloadMutiPartFormDataByGet(String urlString) throws Exception{
        Map<String,Object> responseData = null;
        try{
            responseData = httpTool.sendSSLGetDownloadMedia(urlString);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseData;
    }

    /**
     * 下载微信永久素材
     * @param urlString url
     * @return
     * @throws Exception
     */
    protected Map<String,Object> downloadMutiPartFormDataByPost(String urlString, String postData) throws Exception{
        Map<String,Object> responseData = null;
        try{
            responseData = httpTool.sendSSLPostDownloadMedia(urlString, postData);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseData;
    }
}
