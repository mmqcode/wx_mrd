package com.gxgrh.wechat.handle.service;
import java.util.Map;

/**处理消息的handler，每个方法处理特定某一种消息类型。
 * Created by Administrator on 2016/9/26.
 */
public interface IMsgHandler {

    /**
     * 处理文本消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processTextMsg(Map<String,String> requestMap) throws Exception;


    /**
     * 处理图片消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processImgMsg(Map<String,String> requestMap) throws Exception;


    /**
     * 处理声音消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processVoiceMsg(Map<String,String> requestMap) throws Exception;


    /**
     *处理视频消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processVideoMsg(Map<String,String> requestMap) throws Exception;


    /**
     *处理短视频消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processShortvideoMsg(Map<String,String> requestMap) throws Exception;


    /**
     * 处理地理位置消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processLocationMsg(Map<String,String> requestMap) throws Exception;


    /**
     * 处理链接消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    public String processLinkMsg(Map<String,String> requestMap) throws Exception;
}
