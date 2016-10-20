package com.gxgrh.wechat.support;

import com.gxgrh.wechat.handle.service.ICoreHandler;
import com.gxgrh.wechat.tools.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 此类实现与微信服务器的认证和交互。
 * get方法实现认证过程，post方法处理微信消息和事件
 * Created by Administrator on 2016/9/26.
 */
@Controller
@RequestMapping("/WeChat")
public class WechatSupportController {


    @Autowired
    private ICoreHandler coreHandler;

    @Autowired
    private SystemUtils SystemUtils;

    private static final Logger logger = LogManager.getLogger(WechatSupportController.class);

    /**
     *
     * @param request
     * @param response
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字串
     */
    @RequestMapping(method={RequestMethod.GET})
    public void verify(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("echostr") String echostr){

        try{
            boolean flag = SystemUtils.checkSignature(signature,timestamp,nonce);
            if(flag){
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            }else{
                //不是微信服务器发来的请求，不做处理。
                logger.info("请求未通过验证。");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @RequestMapping(method = {RequestMethod.POST})
    public void handleRequest(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        String responseXmlEncryt = "";
        try{

            response.setContentType("text/xml;charset=UTF-8");
            out = response.getWriter();

            String requestXml = this.coreHandler.beforeHandle(request);
            logger.info("requestXml:"+requestXml);
            String responseXml = this.coreHandler.doHandle(requestXml);

            responseXmlEncryt = this.coreHandler.afterHandle(responseXml,request);

        }catch(Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }finally {

        }
        logger.info("responseXmlEncryt:"+responseXmlEncryt);
        if(responseXmlEncryt == null){
            responseXmlEncryt = "";
        }
        out.write(responseXmlEncryt);
        out.flush();
        out.close();
    }

}
