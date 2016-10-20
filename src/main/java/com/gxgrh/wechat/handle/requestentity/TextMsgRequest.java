package com.gxgrh.wechat.handle.requestentity;

import com.gxgrh.wechat.handle.base.BaseMsgRequest;
import com.gxgrh.wechat.tools.Constants;

/**文本消息的请求类
 * Created by Administrator on 2016/9/26.
 */
public class TextMsgRequest extends BaseMsgRequest {
    private String content;//文本消息特有的节点

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextMsgRequest(String content){
        //super();
        this.content = content;
        setMsgType(Constants.WECHAT_MSG_TYPE_TEXT);
    }


}