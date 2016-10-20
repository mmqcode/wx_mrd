package com.gxgrh.wechat.handle.service.impl;

import com.gxgrh.wechat.handle.service.ICoreHandler;
import com.gxgrh.wechat.handle.service.IEventHandler;
import com.gxgrh.wechat.handle.service.IMsgHandler;
import com.gxgrh.wechat.support.WechatSupportController;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.IoTool;
import com.gxgrh.wechat.tools.XmlTool;
import com.gxgrh.wechat.wxtools.CryptService;
import org.apache.commons.codec.digest.Crypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/27.
 */
@Service
public class CoreHandlerImpl implements ICoreHandler {

    @Autowired
    private XmlTool xmlTool;

    @Autowired
    private IEventHandler eventHandler;

    @Autowired
    private IMsgHandler msgHandler;
    @Autowired
    private CryptService cryptService;
    @Autowired
    private IoTool ioTool;

    private static final Logger logger = LogManager.getLogger(CoreHandlerImpl.class);



    public String beforeHandle(HttpServletRequest request) throws Exception {
        //xml解密
        InputStream inputStream = null;
        String requestXml = null;
        try{
            inputStream = request.getInputStream();
            String postData = this.ioTool.getStringFromInputStream(inputStream);
            String msgSignature = request.getParameter("msg_signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            //requestXml = this.cryptService.decrptInXml(msgSignature,timestamp,nonce,postData);
            requestXml = postData;
        }catch(IOException e){
            throw new Exception(e.getMessage());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }finally {
            if(null != inputStream){
                inputStream.close();
            }

        }
       return requestXml;
    }

    public String doHandle(String requetsXml) throws Exception {
        Map<String,String> requestMap = null;
        String responseXml = null;
        try{
            //先解析xml成map对象
            requestMap = this.xmlTool.wechatRequestXmlToMap(requetsXml);
            //确认请求是消息还是事件
            String requestType = requestMap.get(Constants.WECHAT_MSG_XML_TAG_MSGTYPE);

            logger.info("requestType:"+requestType);
            //事件请求的处理
            if(Constants.WECHAT_MSG_TYPE_EVENT.equalsIgnoreCase(requestType)){
                //事件类型
                String evnetType = requestMap.get(Constants.WECHAT_MSG_XML_TAG_ENVENT);
                //click事件处理
                if(Constants.WECHAT_EVENT_TYPE_CLICK.equalsIgnoreCase(evnetType)){

                    responseXml = this.eventHandler.processClickEvent(requestMap);

                }else{
                    // TODO: 2016/10/13 处理还未被识别的事件。

                }
                //文本消息的处理
            }else if(Constants.WECHAT_MSG_TYPE_TEXT.equalsIgnoreCase(requestType)){

                responseXml = this.msgHandler.processTextMsg(requestMap);

                //图片消息处理
            }else if(Constants.WECHAT_MSG_TYPE_IMAGE.equalsIgnoreCase(requestType)){

                responseXml = this.msgHandler.processImgMsg(requestMap);
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            throw new Exception(e.getMessage());
        }finally {

        }
        return responseXml;
    }

    public String afterHandle(String outputXml, HttpServletRequest request) throws Exception {
        //xml加密
        String resultXml = null;
        try{
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            //resultXml = this.cryptService.encryptOutXml(outputXml, timestamp, nonce);
            resultXml = outputXml;
        }catch(Exception e){
            e.printStackTrace();
            resultXml = outputXml;
        }

        return resultXml;
    }
}
