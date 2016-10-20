package com.gxgrh.wechat.handle.service;

import com.gxgrh.wechat.handle.base.BaseRequest;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.TimeTool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**制作返回给微信服务器的xml
 * Created by Administrator on 2016/9/29.
 */
@Service
public class ResponseXmlGenerator {

    private final int MAX_ARTICLE_NUM = 10;

    //返回文本消息
    public String getResponseTextXml(String content, BaseRequest baseRequest){
        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_TEXT+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_CONTENT+">")
                .append("<![CDATA["+content+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_CONTENT+">");
        sb.append(getXmlFoot());
        return sb.toString();
    }
    //返回图片消息
    public String getResponseImageXml(String mediaId, BaseRequest baseRequest){
        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_IMAGE+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_IMAGE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MEDIAID+">")
                .append("<![CDATA["+mediaId+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MEDIAID+">");

        sb.append("</"+Constants.WECHAT_MSG_XML_TAG_IMAGE+">");

        sb.append(getXmlFoot());
        return sb.toString();
    }

    //返回视频消息
    public String getResponseVideoXml(String mediaId, String title, String description, BaseRequest baseRequest){
        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_VIDEO+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_VIDEO+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MEDIAID+">")
                .append("<![CDATA["+mediaId+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MEDIAID+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_TITLE+">")
                .append("<![CDATA["+title+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_TITLE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">")
                .append("<![CDATA["+description+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">");

        sb.append("</"+Constants.WECHAT_MSG_XML_TAG_VIDEO+">");
        sb.append(getXmlFoot());
        return sb.toString();

    }

    //返回声音消息
    public String getResponseVoiceXml(String mediaId, BaseRequest baseRequest){

        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_VOICE+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_VOICE+">")
                .append("<![CDATA["+mediaId+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_VOICE+">");

        sb.append(getXmlFoot());
        return sb.toString();

    }

    //返回音乐消息
    public String getResponseMusiceXml(Map<String,String> musicDetailMap, BaseRequest baseRequest){

        if(null == musicDetailMap){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_MUSIC+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MUSIC+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_TITLE+">")
                .append("<![CDATA["+musicDetailMap.get(Constants.WECHAT_MSG_XML_TAG_TITLE)+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_TITLE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">")
                .append("<![CDATA["+musicDetailMap.get(Constants.WECHAT_MSG_XML_TAG_DESCRIPTION)+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MUSICURL+">")
                .append("<![CDATA["+musicDetailMap.get(Constants.WECHAT_MSG_XML_TAG_MUSICURL)+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MUSICURL+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_HQMUSICURL+">")
                .append("<![CDATA["+musicDetailMap.get(Constants.WECHAT_MSG_XML_TAG_HQMUSICURL)+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_HQMUSICURL+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_THUMBMEDIAID+">")
                .append("<![CDATA["+musicDetailMap.get(Constants.WECHAT_MSG_XML_TAG_THUMBMEDIAID)+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_THUMBMEDIAID+">");

        sb.append("</"+Constants.WECHAT_MSG_XML_TAG_MUSIC+">");
        return sb.toString();

    }

    //返回图文消息
    public String getResponseArticleXml(List<Map<String,String>> aticleList, BaseRequest baseRequest) throws Exception{
        if(null == aticleList){
            return null;

        }
        if(aticleList.size() > MAX_ARTICLE_NUM){
            throw new Exception("图文消息一次发送不能操作十条!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getXmlHead());
        makeCommonPart(sb, baseRequest);

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">")
                .append("<![CDATA["+Constants.WECHAT_MSG_TYPE_NEWS+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_MSGTYPE+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_ARITCLECOUNT+">")
                .append("<![CDATA["+aticleList.size()+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_ARITCLECOUNT+">");


        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_ARTICLES+">");
        for(Map<String,String> obj:aticleList){
            sb.append("<"+Constants.WECHAT_MSG_XML_TAG_ITEM+">");

            sb.append("<"+Constants.WECHAT_MSG_XML_TAG_TITLE+">")
                    .append("<![CDATA["+obj.get(Constants.WECHAT_MSG_XML_TAG_TITLE)+"]]>")
                    .append("</"+Constants.WECHAT_MSG_XML_TAG_TITLE+">");

            sb.append("<"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">")
                    .append("<![CDATA["+obj.get(Constants.WECHAT_MSG_XML_TAG_DESCRIPTION)+"]]>")
                    .append("</"+Constants.WECHAT_MSG_XML_TAG_DESCRIPTION+">");

            sb.append("<"+Constants.WECHAT_MSG_XML_TAG_PICURL+">")
                    .append("<![CDATA["+obj.get(Constants.WECHAT_MSG_XML_TAG_PICURL)+"]]>")
                    .append("</"+Constants.WECHAT_MSG_XML_TAG_PICURL+">");

            sb.append("<"+Constants.WECHAT_MSG_XML_TAG_URL+">")
                    .append("<![CDATA["+obj.get(Constants.WECHAT_MSG_XML_TAG_URL)+"]]>")
                    .append("</"+Constants.WECHAT_MSG_XML_TAG_URL+">");

            sb.append("</"+Constants.WECHAT_MSG_XML_TAG_ITEM+">");

        }

        sb.append("</"+Constants.WECHAT_MSG_XML_TAG_ARTICLES+">");
        return sb.toString();
    }


    //制作xml公共部分
    private void makeCommonPart(StringBuilder sb, BaseRequest baseRequest){

        //返回给微信服务器的FromUserName和ToUserName与请求的相反
        sb.append("<"+ Constants.WECHAT_MSG_XML_TAG_TOUSERNAME+">")
                .append("<![CDATA["+baseRequest.getFromUserName()+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_TOUSERNAME+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_FROMUSERNAME+">")
                .append("<![CDATA["+baseRequest.getToUserName()+"]]>")
                .append("</"+Constants.WECHAT_MSG_XML_TAG_FROMUSERNAME+">");

        sb.append("<"+Constants.WECHAT_MSG_XML_TAG_CREATETIME+">")
                .append(TimeTool.getCurrentTimeInSeconds())
                .append("</"+Constants.WECHAT_MSG_XML_TAG_CREATETIME+">");
    }



    private String getXmlHead(){
        return "<xml>";
    }
    private String getXmlFoot(){
        return "</xml>";
    }


}
