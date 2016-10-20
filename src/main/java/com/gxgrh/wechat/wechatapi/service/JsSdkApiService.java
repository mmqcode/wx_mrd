package com.gxgrh.wechat.wechatapi.service;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.TimeTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.AccessToken;
import com.gxgrh.wechat.wechatapi.responseentity.system.JsApiTicket;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**关于js_sdk的一些接口
 * Created by Administrator on 2016/9/29.
 */
@Service
public class JsSdkApiService extends BaseApiService {

    @Autowired
    private FileTool fileTool;
    private final String getJsApiTicketPath = "/cgi-bin/ticket/getticket";
    private final String type_name = "type";
    private final String jsapi_name = "jsapi";
    private URIBuilder uriBuilder = new URIBuilder();
    private Gson gson = new Gson();

    public static final Logger logger = LogManager.getLogger(JsSdkApiService.class);


    //要使用jssdk的页面需要这个jsapi_ticket进行网页的验证，
    //该方法提供jsapi_ticket的获取和刷新操作
    public JsApiTicket refreshJsApiTick() throws Exception{
        long alertTime = 5*30*1000L;
        JsApiTicket jsApiTicket = null;
        try{
            String ticketExpireTimeString = this.fileTool.getParamByName(Constants.WECHAT_JSAPI_TICKET_EXPIRE_TIME_NAME,
                    Constants.WECHAT_PROPERTIES_FILENAME);
            Long ticketExpireTimeLong = Long.parseLong(ticketExpireTimeString);
            long interval = ticketExpireTimeLong - TimeTool.getCurrentTime();
            if(interval < alertTime){
                jsApiTicket = getJsApiTick();
            }else{
                String ticketExpireTime = this.fileTool.getParamByName(Constants.WECHAT_JSAPI_TICKET_EXPIRE_TIME_NAME,
                        Constants.WECHAT_PROPERTIES_FILENAME);
                String ticket = this.fileTool.getParamByName(Constants.WECHAT_JSAPI_TICKET_NAME,
                        Constants.WECHAT_PROPERTIES_FILENAME);
                jsApiTicket = new JsApiTicket();
                jsApiTicket.setExpires_in(Long.parseLong(ticketExpireTime));
                jsApiTicket.setTicket(ticket);

            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        return jsApiTicket;

    }


    /**
     * 通过接口获取jsapip ticket
     * @return
     * @throws Exception
     */
    private JsApiTicket getJsApiTick() throws Exception{
        //目标 uri：https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
        JsApiTicket jsApiTicket = null;
        try{
            AccessToken accessToken = refreshAccessToken();
            uriBuilder.clearParameters();
            uriBuilder.setScheme(Constants.WECHAT_API_SCHEME)
                    .setHost(Constants.WECHAT_API_HOST)
                    .setPath(getJsApiTicketPath)
                    .setParameter(Constants.WECHAT_ACCESS_TOKEN_NAME,accessToken.getAccess_token())
                    .setParameter(type_name,jsapi_name);
            String url = uriBuilder.build().toString();
            logger.info("use targetUrL:"+url);
            String responseJson = sendGet(url);
            logger.info("jsonResponse:"+responseJson);
            jsApiTicket = gson.fromJson(responseJson,JsApiTicket.class);
            if(null != jsApiTicket.getErrcode() && !"0".equals(jsApiTicket.getErrcode())){
                String errMsg = "调用接口:"+url+"失败!原因:"+jsApiTicket.getErrmsg();
                logger.error(errMsg);
                throw new Exception(errMsg);
            }
            long expireTime = TimeTool.getCurrentTime()+jsApiTicket.getExpires_in()*100L;
            this.fileTool.updateProperties(Constants.WECHAT_JSAPI_TICKET_EXPIRE_TIME_NAME,expireTime+"",
                    Constants.WECHAT_PROPERTIES_FILENAME);
            this.fileTool.updateProperties(Constants.WECHAT_JSAPI_TICKET_NAME,jsApiTicket.getTicket(),
                    Constants.WECHAT_PROPERTIES_FILENAME);
            jsApiTicket.setExpires_in(expireTime);
            logger.info("jsapi_ticket已经重新获取：ticket["+jsApiTicket.getTicket()+"],expire_time["+expireTime+"]");
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return jsApiTicket;
    }

}
