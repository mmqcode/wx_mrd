package com.gxgrh.wechat.handle.service.impl;

import com.gxgrh.wechat.handle.service.IEventHandler;
import org.springframework.stereotype.Service;

import java.util.Map;

/**事件处理的实现类
 * Created by Administrator on 2016/9/27.
 */
@Service
public class EventHandlerImpl implements IEventHandler {

    public String processClickEvent(Map<String, String> requestMap) throws Exception {

        return null;
    }

    public String processViewEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processSubscribeEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processUnsubscribeEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processScanWhileNotSubEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processScanWhileSubEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processLocationEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processMassSendResultEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processTemplateSendResultEvent(Map<String, String> requestMap) throws Exception {
        return null;
    }
}
