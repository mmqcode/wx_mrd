package com.gxgrh.wechat.handle.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信交互信息的处理，统一处理聊天消息和事件。
 * Created by Administrator on 2016/9/26.
 */
public interface ICoreHandler {

    /**
     * 微信请求的预处理，如果信息是加密过的，在此解密。返回微信请求的明文xml信息。
     *
     * @param requets
     * @return 请求明文xml
     * @throws Exception
     */
    public String beforeHandle(HttpServletRequest requets) throws Exception;


    /**
     *处理微信请求，返回可以输出给微信服务器的xml报文。
     * @param requetsXml
     * @return
     * @throws Exception
     */
    public String doHandle(String requetsXml) throws Exception;


    /**
     * 请求处理完成后，如果需要对返回信息加密，可以在这里处理。
     * @param outputXml
     * @return
     * @throws Exception
     */
    public String afterHandle(String outputXml, HttpServletRequest request) throws Exception;

}
