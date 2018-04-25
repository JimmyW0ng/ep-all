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
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";

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

    public static String mapToXmlString(Map<String, String> map) {
        String xmlResult = "";

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
//            System.out.println(key + "========" + map.get(key));

            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
            System.out.println();
        }
        sb.append("</xml>");
        xmlResult = sb.toString();

        return xmlResult;
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
