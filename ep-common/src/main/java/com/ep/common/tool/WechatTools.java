package com.ep.common.tool;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:05 2018/4/22
 */
public class WechatTools {
    public static final String PARAM_TOUSERNAME = "ToUserName";
    public static final String PARAM_FROMUSERNAME = "FromUserName";
    public static final String PARAM_CREATETIME = "CreateTime";
    public static final String PARAM_MSGTYPE = "MsgType";
    public static final String PARAM_CONTENT = "Content";
    public static final String PARAM_ACCESSTOKEN = "access_token";
    public static final String PARAM_ERRCODE = "errcode";
    public static final String PARAM_ERRMSG = "errmsg";
    public static final String PARAM_TOUSER = "touser";


    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";

    public static final String EVENT_SUB = "subscribe";
    public static final String EVENT_UNSUB = "unsubscribe";
    public static final String EVENT_CLICK = "click";
    public static final String EVENT_VIEW = "view";

    /**
     * xml转为map
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }

    /**
     * map转xml字符串
     *
     * @param map
     * @return
     */
    public static String mapToXmlString(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static Map<String, String> inputStreamToMap(InputStream ins) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }
}
