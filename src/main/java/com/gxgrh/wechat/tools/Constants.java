package com.gxgrh.wechat.tools;

/**常量类，集中存储微信项目用到的常量，项目中用到的全局常量最好在本类配置。
 * Created by Administrator on 2016/9/26.
 */
public class Constants {
    //public static final String WECHAT_GETACCESSTOKEN_GRANT_TYPE = "client_credential";
    public static final String WECHAT_GETACCESSTOKEN_GRANT_TYPE_NAME = "grant_type";
    public static final String WECHAT_ACCESS_TOKEN_NAME = "access_token";
    public static final String WECHAT_APPID_NAME = "appid";
    public static final String WECHAT_SECRET_NAME = "secret";
    public static final String WECHAT_ENCODING_AES_KEY_NAME = "encodingAesKey";
    public static final String WECHAT_TOKEN_NAME = "tokon";
    public static final String WECHAT_ACCESS_TOKEN_EXPIRE_TIME_NAME = "access_token_expire";
    public static final String WECHAT_JSAPI_TICKET_NAME = "jsapi_ticket";
    public static final String WECHAT_JSAPI_TICKET_EXPIRE_TIME_NAME = "jsapi_ticket_expire";

    public static final String WECHAT_API_HOST = "api.weixin.qq.com";
    public static final String WECHAT_GET_QRCODE_HOST = "mp.weixin.qq.com";
    public static final String WECHAT_API_SCHEME = "https";

    //微信xml请求的类型
    public static final String WECHAT_MSG_TYPE_TEXT = "text";
    public static final String WECHAT_MSG_TYPE_IMAGE = "image";
    public static final String WECHAT_MSG_TYPE_EVENT = "event";
    public static final String WECHAT_MSG_TYPE_VIDEO = "video";
    public static final String WECHAT_MSG_TYPE_VOICE = "voice";
    public static final String WECHAT_MSG_TYPE_MUSIC = "music";
    public static final String WECHAT_MSG_TYPE_NEWS = "news";



    //微信xml事件请求的类型
    public static final String WECHAT_EVENT_TYPE_CLICK = "click";


    //微信xml消息的tag
    public static final String WECHAT_MSG_XML_TAG_MSGTYPE = "MsgType";
    public static final String WECHAT_MSG_XML_TAG_ENVENT =  "Event";
    public static final String WECHAT_MSG_XML_TAG_TOUSERNAME = "ToUserName";
    public static final String WECHAT_MSG_XML_TAG_FROMUSERNAME = "FromUserName";
    public static final String WECHAT_MSG_XML_TAG_CREATETIME = "CreateTime";
    public static final String WECHAT_MSG_XML_TAG_CONTENT = "Content";
    public static final String WECHAT_MSG_XML_TAG_MEDIAID = "MediaId";
    public static final String WECHAT_MSG_XML_TAG_IMAGE = "Image";
    public static final String WECHAT_MSG_XML_TAG_VIDEO = "Video";
    public static final String WECHAT_MSG_XML_TAG_TITLE = "Title";
    public static final String WECHAT_MSG_XML_TAG_DESCRIPTION = "Description";
    public static final String WECHAT_MSG_XML_TAG_VOICE = "Voice";
    public static final String WECHAT_MSG_XML_TAG_MUSIC = "Music";
    public static final String WECHAT_MSG_XML_TAG_MUSICURL = "MusicUrl";
    public static final String WECHAT_MSG_XML_TAG_HQMUSICURL = "HQMusicUrl";
    public static final String WECHAT_MSG_XML_TAG_THUMBMEDIAID = "ThumbMediaId";
    public static final String WECHAT_MSG_XML_TAG_ARITCLECOUNT = "ArticleCount";
    public static final String WECHAT_MSG_XML_TAG_ARTICLES = "Articles";
    public static final String WECHAT_MSG_XML_TAG_ITEM = "item";
    public static final String WECHAT_MSG_XML_TAG_PICURL = "PicUrl";
    public static final String WECHAT_MSG_XML_TAG_URL = "Url";


    //微信接口返回json数据常用
    public static final String WECHAT_API_JSON_NODE_ERRCODE = "errcode";
    public static final String WECHAT_API_JSON_NODE_ERRMSG = "errmsg";




    //配置文件的名字
    public static final String WECHAT_PROPERTIES_FILENAME = "WeChatConfigInfo.properties";

}
