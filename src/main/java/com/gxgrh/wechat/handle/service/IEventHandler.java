package com.gxgrh.wechat.handle.service;

import java.util.Map;

/**处理事件的handler，每个方法处理一种事件类型。
 * Created by Administrator on 2016/9/26.
 */
public interface IEventHandler {


    /**
     * 处理自定义菜单点击拉取消息事件的方法。返回输出给微信服务器的xml
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processClickEvent(Map<String,String> requestMap) throws Exception;


    /**
     *
     *处理点击菜单跳转链接时的事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processViewEvent(Map<String,String> requestMap) throws Exception;


    /**
     *处理关注事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processSubscribeEvent(Map<String,String> requestMap) throws Exception;


    /**
     * 处理取消关注事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processUnsubscribeEvent(Map<String,String> requestMap) throws Exception;


    /**扫描带参数二维码事件
     * 用户未关注时，进行关注后的事件。
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processScanWhileNotSubEvent(Map<String,String> requestMap) throws Exception;


    /**
     * 扫描带参数二维码事件
     * 用户已关注状态下
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processScanWhileSubEvent(Map<String,String> requestMap) throws Exception;


    /**
     * 上报地理位置事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processLocationEvent(Map<String,String> requestMap) throws Exception;


    /**
     *群发消息后的结果事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processMassSendResultEvent(Map<String,String> requestMap) throws Exception;


    /**
     *
     * 模板消息发送后的结果事件
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processTemplateSendResultEvent(Map<String,String> requestMap) throws Exception;


}
