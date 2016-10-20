package com.gxgrh.wechat.tools;

import com.gxgrh.wechat.handle.base.BaseEventRequest;
import com.gxgrh.wechat.handle.base.BaseMsgRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**封装一些方法
 * Created by Administrator on 2016/9/27.
 */
@Component
public class SystemUtils {

    @Autowired
    private FileTool fileTool;

    private static final Logger logger = LogManager.getLogger(SystemUtils.class);

    public boolean checkSignature(String signature, String timestamp, String nonce) throws Exception{

        String token = this.fileTool.getParamByName("token",Constants.WECHAT_PROPERTIES_FILENAME);

        String[] arr = new String[] { token, timestamp, nonce };
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        content = null;
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;

    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public void fillUpMsgRequest(BaseMsgRequest baseMsgRequest, Map<String,String> requestMap){
        if(null == baseMsgRequest || null == requestMap){
            return;
        }
        try{
            baseMsgRequest.setMsgType(requestMap.get("MsgType"));
            baseMsgRequest.setMsgId(requestMap.get("MsgId"));
            baseMsgRequest.setCreateTime(Long.parseLong(requestMap.get("CreateTime")));
            baseMsgRequest.setFromUserName(requestMap.get("FromUserName"));
            baseMsgRequest.setToUserName(requestMap.get("ToUserName"));
        }catch (NullPointerException nullE) {

            logger.error("填充微信消息请求实体时发生空指针异常!请检查微信请求的xml");
            return;

        }catch(Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public void fillUpEventRequest(BaseEventRequest baseEventRequest, Map<String,String> requestMap){
        if(null == baseEventRequest || null == requestMap){
            return;
        }
        try{
            //MsgType和eventType已经在子类创建时确定了，这里不用赋值
            baseEventRequest.setCreateTime(Long.parseLong(requestMap.get("CreateTime")));
            baseEventRequest.setFromUserName(requestMap.get("FromUserName"));
            baseEventRequest.setToUserName(requestMap.get("ToUserName"));
        }catch (NullPointerException nullE) {
            logger.error("填充微信事件请求实体时发生空指针异常!请检查微信请求的xml");
            return;
        }catch(Exception e){
            e.printStackTrace();
        }finally {

        }

    }

}
