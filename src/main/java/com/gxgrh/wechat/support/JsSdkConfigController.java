package com.gxgrh.wechat.support;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.JsonTool;
import com.gxgrh.wechat.wechatapi.responseentity.system.JsApiTicket;
import com.gxgrh.wechat.wechatapi.service.JsSdkApiService;
import com.gxgrh.wechat.wxtools.Sign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**需要使用JSSDK的画面需要进行微信的验证，验证的参数是需要在后台计算的。本类提供了页面通过ajax获取参数的服务
 * Created by Administrator on 2016/9/29.
 */
@Controller
@RequestMapping("/WeChat/JsSdkConfigController")
public class JsSdkConfigController {

    private static final Logger logger = LogManager.getLogger(JsSdkConfigController.class);

    @Autowired
    private JsSdkApiService jsSdkApiService;

    @Autowired
    private JsonTool jsonTool;

    @Autowired
    private FileTool fileTool;

    private static final String exceptionCode = "0";
    private static final String successCode = "1";


    @RequestMapping(value="ajaxLoadConfigParam", produces = "text/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxLoadJsSdkConfigParam(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestParam("pageUrl") String pageUrl){
        Map<String,String> resultMap = null;
        String resultJson = null;
        try{
            JsApiTicket jsApiTicket = this.jsSdkApiService.refreshJsApiTick();
            resultMap = Sign.sign(jsApiTicket.getTicket(),pageUrl);
            String appid = this.fileTool.getParamByName(Constants.WECHAT_APPID_NAME, Constants.WECHAT_PROPERTIES_FILENAME);
            resultMap.put("appid",appid);
            resultMap.put("code",successCode);
            Gson gson =new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
            resultJson = gson.toJson(resultMap);
        }catch(Exception e){
            e.printStackTrace();
            resultJson = this.jsonTool.getSimpleMsgJson(e.getMessage(),exceptionCode);
        }finally{

        }
        return resultJson;
    }


}
