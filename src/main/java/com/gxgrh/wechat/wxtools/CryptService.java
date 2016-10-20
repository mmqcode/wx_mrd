package com.gxgrh.wechat.wxtools;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.gxgrh.wechat.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.gxgrh.wechat.tools.FileTool;

@Component
public class CryptService {

    @Autowired
    private FileTool fileTool;

    //加密
    public String encryptOutXml(String outXml,String timestamp, String nonce) throws Exception{
        String encodingAesKey = fileTool.getParamByName(Constants.WECHAT_ENCODING_AES_KEY_NAME,
                Constants.WECHAT_PROPERTIES_FILENAME);
        String token = fileTool.getParamByName(Constants.WECHAT_TOKEN_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        String appId = fileTool.getParamByName(Constants.WECHAT_APPID_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String miwen = pc.encryptMsg(outXml, timestamp, nonce);
        return miwen;
    }
    //解密
    public String decrptInXml(String msgSignature, String timestamp, String nonce, String postData) throws Exception{
        String encodingAesKey = fileTool.getParamByName(Constants.WECHAT_ENCODING_AES_KEY_NAME,
                Constants.WECHAT_PROPERTIES_FILENAME);
        String token = fileTool.getParamByName(Constants.WECHAT_TOKEN_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        String appId = fileTool.getParamByName(Constants.WECHAT_APPID_NAME,Constants.WECHAT_PROPERTIES_FILENAME);
        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String mingwen = pc.decryptMsg(msgSignature, timestamp, nonce, postData);
        return mingwen;
    }
}
