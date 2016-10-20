package com.gxgrh.wechat.tools;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/27.
 */
@Component
public class XmlTool {


    /**
     * 解析微信请求xml，返回节点值map
     * @return
     * @throws Exception
     */
    public Map<String,String> wechatRequestXmlToMap(String xmlString) throws Exception{
        SAXReader reader = new SAXReader();
        Document doc;
        Map<String,String> map = new HashMap<String, String>();
        InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
        try {
            doc = reader.read(is);
            Element root = doc.getRootElement();
            for(@SuppressWarnings("rawtypes")
                Iterator it = root.elementIterator(); it.hasNext();){
                Element element = (Element)it.next();
                map.put(element.getName(), element.getText());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally{
            if(null != is){
                is.close();
            }

        }
        return map;
    }

}
