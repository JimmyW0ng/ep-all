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
    /**
     * 参数
     */
    public static final String PARAM_TOUSERNAME = "ToUserName";
    public static final String PARAM_FROMUSERNAME = "FromUserName";
    public static final String PARAM_CREATETIME = "CreateTime";
    public static final String PARAM_MSGTYPE = "MsgType";
    public static final String PARAM_CONTENT = "Content";
    public static final String PARAM_ACCESSTOKEN = "access_token";
    public static final String PARAM_ERRCODE = "errcode";
    public static final String PARAM_ERRMSG = "errmsg";
    public static final String PARAM_TOUSER = "touser";
    public static final String PARAM_EVENT = "Event";

    /**
     * 参数MsgType的类型
     */
    public static final String MSGTYPE_TEXT = "text";
    public static final String MSGTYPE_IMAGE = "image";
    public static final String MSGTYPE_VOICE = "voice";
    public static final String MSGTYPE_VIDEO = "video";
    public static final String MSGTYPE_LINK = "link";
    public static final String MSGTYPE_LOCATION = "location";
    public static final String MSGTYPE_EVENT = "event";

    /**
     * 参数Event的类型
     */
    public static final String EVENT_SUB = "subscribe";
    public static final String EVENT_UNSUB = "unsubscribe";
    public static final String EVENT_CLICK = "CLICK";
    public static final String EVENT_VIEW = "VIEW";

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

    /**
     * 输入流转map
     *
     * @param ins
     * @return
     * @throws Exception
     */
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
