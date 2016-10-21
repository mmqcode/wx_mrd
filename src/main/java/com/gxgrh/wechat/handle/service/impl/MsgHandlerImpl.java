package com.gxgrh.wechat.handle.service.impl;

import com.google.gson.Gson;
import com.gxgrh.wechat.handle.requestentity.TextMsgRequest;
import com.gxgrh.wechat.handle.service.IMsgHandler;
import com.gxgrh.wechat.handle.service.ResponseXmlGenerator;
import com.gxgrh.wechat.tools.Constants;
import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.SystemUtils;
import com.gxgrh.wechat.tools.Validate;
import com.gxgrh.wechat.wechatapi.responseentity.custommenu.CustomMenu;
import com.gxgrh.wechat.wechatapi.responseentity.system.WeChatServerIp;
import com.gxgrh.wechat.wechatapi.responseentity.material.Article;
import com.gxgrh.wechat.wechatapi.responseentity.userinfo.WeChatUserInfo;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.Tag;
import com.gxgrh.wechat.wechatapi.responseentity.usertags.UserToTag;
import com.gxgrh.wechat.wechatapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**消息处理的实现类
 * Created by Administrator on 2016/9/27.
 */
@Service
public class MsgHandlerImpl implements IMsgHandler {

    @Autowired
    private SystemUtils systemUtils;

    @Autowired
    private ResponseXmlGenerator responseXmlGenerator;

    @Autowired
    private SystemApiService systemApiService;

    @Autowired
    private CustomMenuApiService CustomMenuService;

    @Autowired
    private UserTagsApiService userTagsService;

    @Autowired
    private UserInfoApiService userInfoService;

    @Autowired
    private MaterialApiService materialApiService;

    @Autowired
    private MessageApiService messageApiService;

    @Autowired
    private FileTool fileTool;

    @Autowired
    private KeFuApiService keFuApiService;

    private final String NULL_MSG = "";

    private Gson gson = new Gson();

    public String processTextMsg(Map<String, String> requestMap) throws Exception {

        String returnXml = null;
        String respponseContent = "您所发送的消息指令无法识别";
        String reponseType = "text";
        try{
            TextMsgRequest textMsgRequest = new TextMsgRequest(requestMap.get(Constants.WECHAT_MSG_XML_TAG_CONTENT));
            this.systemUtils.fillUpMsgRequest(textMsgRequest,requestMap);
            String content = textMsgRequest.getContent();
            if(null != content && content.equalsIgnoreCase("ok")){
                respponseContent = "ok";
            }else if(null != content && content.equalsIgnoreCase("ip")){
                //用户发送ip指令，回复微信服务器ip地址
                WeChatServerIp serverIp = this.systemApiService.getCallbackIp();
                if(null != serverIp) {
                    StringBuffer serverIpBuffer = new StringBuffer("serverIP:");
                    List<String> ipList = serverIp.getIp_list();
                    for(String ip:ipList){
                        serverIpBuffer.append("["+ip+"]");
                    }
                    respponseContent = serverIpBuffer.toString();
                }else{
                    respponseContent = "未能获取到服务器ip，请稍后尝试。";
                }
            }else if(Validate.isString(content) && content.equalsIgnoreCase("clearQuota")){
                //测试 清零接口
                Map<String,String> resultMap = this.systemApiService.clearQuota();
                if(null != resultMap){
                    respponseContent = resultMap.get("errmsg");
                }else{
                    respponseContent = NULL_MSG;
                }
            }else if(Validate.isString(content) && content.equalsIgnoreCase("getMenu")){
                //测试获取自定义菜单
                CustomMenu customMenu = this.CustomMenuService.getCustomMenu();
                if(null != customMenu){
                    respponseContent = "done!";
                }else{
                    respponseContent = NULL_MSG;
                }
            }else if(Validate.isString(content) && content.contains("updateTag")){
                //测试更新标签名称
                String[] contentArray = content.split("=");
                String tagName = contentArray[1];
                Tag childUserTag = new Tag();
                childUserTag.setName(tagName);
                childUserTag.setId("100");
                Tag userTag = new Tag();
                userTag.setUserTag(childUserTag);
                String tagToUpdateJson = new Gson().toJson(userTag);
                userTagsService.updateUserTag(tagToUpdateJson);
            }else if(Validate.isString(content) && content.contains("getAllTag")){
                //测试获取所有标签
                userTagsService.getAllUserTags();
            }else if(Validate.isString(content) && content.contains("delTag")){
                //测试删除标签
                String[] contentArray = content.split("=");
                String tagId = contentArray[1];
                Tag childUserTag = new Tag();
                childUserTag.setId(tagId);
                Tag userTag = new Tag();
                userTag.setUserTag(childUserTag);
                String tagToDeleteJson = new Gson().toJson(userTag);
                userTagsService.deleteUserTag(tagToDeleteJson);
            }else if(Validate.isString(content) && content.contains("addToTag")){
                //测试 添加用户到某个标签组
                String[] contentArray = content.split("=");
                String tagid = contentArray[1];
                String openid = textMsgRequest.getFromUserName();
                List<String> openidList = new ArrayList<String>();
                openidList.add(openid);
                UserToTag userToTag = new UserToTag();
                userToTag.setOpenidList(openidList);
                userToTag.setTagId(tagid);
                String userToTagJson = gson.toJson(userToTag);
                this.userTagsService.batchAddUserToTag(userToTagJson);
                //this.userTagsService.batchUnTagUser(userToTagJson);
                respponseContent = "done!";
            }else if(Validate.isString(content) && content.contains("getUserInTag")){
                //测试 获取某个标签下的所有用户
                String[] contentArray = content.split("=");
                String tagid = contentArray[1];
                Map<String,String> temp = new HashMap<String, String>();
                temp.put("tagid",tagid);
                temp.put("next_openid","");
                String requestJson = gson.toJson(temp);
                this.userTagsService.getAllUserInOneTag(requestJson);
            }else if(Validate.isString(content) && content.equals("mytags")){
                //测试获取当前用户所拥有的标签
                Map<String,String> temp = new HashMap<String, String>();
                temp.put("openid",textMsgRequest.getFromUserName());
                String requestJson = gson.toJson(temp);
                this.userTagsService.getAllTagsInOneUser(requestJson);
            }else if(Validate.isString(content) && content.equals("myinfo")){
                //测试获取微信用户信息
                WeChatUserInfo weChatUserInfo = this.userInfoService.getWeChatUserInfo(textMsgRequest.getFromUserName());
                respponseContent = weChatUserInfo.toString();
            }else if(Validate.isString(content) && content.equals("mutiinfo")){
                List<String> list = new ArrayList<String>();
                list.add("oWdNpwyt15q6gcNSDlMTzEJiAXEI");
                list.add("oWdNpw86n3E9F4sN7tz7FDOnESFo");
                this.userInfoService.getMutiWeChatUserInfo(list);
            }else if(Validate.isString(content) && content.equals("allUser")){
                //测试获取微信用户信息列表
                this.userInfoService.getWeChatUserListInfo("");
            }else if(Validate.isString(content) && content.equals("图片")){
                //测试返回图片消息
                reponseType = "image";
                returnXml = responseXmlGenerator.getResponseImageXml("j98tmFoS-xBoMkI0JBJwqzRHoB7tHmWKXLy_RGmPLtxEAzlMZjdbKPtGcE1Ub2qa",textMsgRequest);
            }else if(Validate.isString(content) && content.equals("addArticle")){
                //测试新增图文素材
                Article article = new Article();
                article.setDigest("test");
                article.setAuthor("mmq");
                article.setContent("士大夫撒个撒都干撒干撒的吩咐过12312");
                article.setContentSourceUrl("http://www.baidu.com");
                article.setShowCoverPic("1");
                article.setThumbMediaId("zaWs-lWK-AE0YJFUQuBm3ExFEcp9qGWYGJoeHp4fgO4");
                article.setTitle("测试图文");
                List<Article> list = new ArrayList<Article>();
                list.add(article);
                this.materialApiService.uploadPermanentNewsMaterial(list);
            }else if(Validate.isString(content) && content.equals("downloadArticle")){
                //测试获取永久图文素材
                this.materialApiService.downloadArticleMaterial("zaWs-lWK-AE0YJFUQuBm3J2PDUUw1xQHfvsQl2awpvs");

            }else if(Validate.isString(content) && content.equals("updateArticle")){
                //测试更新图文素材
                Article article = new Article();
                article.setDigest("test");
                article.setAuthor("mmq");
                article.setContent("testsetestestestestsetsetestseteste测试");
                article.setContentSourceUrl("http://www.baidu.com");
                article.setShowCoverPic("1");
                article.setThumbMediaId("zaWs-lWK-AE0YJFUQuBm3ExFEcp9qGWYGJoeHp4fgO4");
                article.setTitle("测试图文");
                this.materialApiService.updateArticlePermanentMateria("zaWs-lWK-AE0YJFUQuBm3J2PDUUw1xQHfvsQl2awpvs","0",article);

            }else if(Validate.isString(content) && content.equals("getAllMaterial")){
                //测试 获取素材总量
                this.materialApiService.getPermenantMaterialCount();

            }else if(Validate.isString(content) && content.equals("getAllArticle")){
                //测试 获取图文素材列表
                this.materialApiService.batchGetArticleMaterial("0","10");

            }else if(Validate.isString(content) && content.equals("getAllMediaMaterial")){

                //测试 获取图片素材列表
                this.materialApiService.batchGetMediaMaterial("image","0","10");
            }else if(Validate.isString(content) && content.equals("sendAll")){
                //尝试群发接口
                this.messageApiService.batchSendMessageByTags("text","101",null,"恭喜发财","true");

            }else if(Validate.isString(content) && content.equals("getQrcodeTicket")){
                //测试获取二维码图片
                Map<String,String> resultMap = this.systemApiService.getTicketToCreateQrcode("7200","0","123");
                String ticket = resultMap.get("ticket");
                byte[] qrcodeData = this.systemApiService.getQrcodeImgByTicket(ticket);
                String fileStoreDirectoryString = System.getProperty("Web.root")+"staticResources"+
                        File.separator+"downloadFile";
                String targetFile = fileStoreDirectoryString+File.separator+"tempQrcode.jpg";
                fileTool.byteArrayToFile(qrcodeData, new File(targetFile));
                String  resoucesUrl = "http://wxtest.gxit.ren/wx_mrd/staticResources";
                respponseContent = resoucesUrl+"/downloadFile"+"/tempQrcode.jpg";
            }else if(Validate.isString(content) && content.equals("getshorturl")){
                Map<String,String> map = this.systemApiService.getShortUrl("http://detail.koudaitong.com/show/goods?alias=128wi9shh&spm=h56083&redirect_count=1");
                respponseContent = map.get("short_url");
            }else if(Validate.isString(content) && content.equals("addkf_mmq")){
                //测试添加客服账号
                keFuApiService.addKFAccount("mmq","小莫");
            }
            if(reponseType == "text"){
                returnXml = responseXmlGenerator.getResponseTextXml(respponseContent,textMsgRequest);
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return returnXml;
    }

    public String processImgMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }


    public String processVoiceMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processVideoMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processShortvideoMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processLocationMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }

    public String processLinkMsg(Map<String, String> requestMap) throws Exception {
        return null;
    }
}
