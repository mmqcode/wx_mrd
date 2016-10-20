package com.gxgrh.wechat.tools.test;

import com.google.gson.Gson;
import com.gxgrh.wechat.tools.*;
import com.gxgrh.wechat.wechatapi.responseentity.system.WeChatServerIp;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ForTest {

    @org.junit.Test
    public void testWriteProperties(){
        Properties pro = new Properties();
        try{
            FileOutputStream fos = new FileOutputStream(ForTest.class.getClassLoader().getResource(Constants.WECHAT_PROPERTIES_FILENAME).getPath());
           // pro.setProperty("access_token","11RUfq5CDDJEGI8hv5u2Wc_hhQ0kfHmi_TqcDHthDQsTfUQ2_K4XxY3PgywE2SSR9YFSe5Cr12nIHtrC17L92GuHQYLSJ-To2xqG9zawoVfe3v7SrsZsyPCpAcveU5Zw_sZHBeAEAOWQ");
            pro.setProperty(Constants.WECHAT_ACCESS_TOKEN_EXPIRE_TIME_NAME,(System.currentTimeMillis()+7200*100L)+"");
            pro.store(fos,"update Access_token");
            fos.flush();
            fos.close();

            FileTool fileTool = new FileTool();
            System.out.println(fileTool.getParamByName("access_token",Constants.WECHAT_PROPERTIES_FILENAME));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testTimeTool(){
        String time = (TimeTool.getCurrentTime()+7200*1000L)+"";

        System.out.println(TimeTool.getCurrentTimeBySeconds(time));

    }

    @Test
    public  void TestJsonData() throws Exception{

        FileInputStream fos = new FileInputStream(ForTest.class.getClassLoader().getResource("test.json").getPath());
        IoTool ioTool = new IoTool();
        String jsonString = ioTool.getStringFromInputStream(fos);
        WeChatServerIp weChatServerIp = new Gson().fromJson(jsonString,WeChatServerIp.class);

        //System.out.println("不会吧！");
    }

    @Test
    public void testHttpTool() throws  Exception{
        HttpTool httpTool = new HttpTool();
        String content =  httpTool.sendPostMethod("http://192.168.1.170:8087/EasyUILearningII/login/applyToken","hha");


    }
}
