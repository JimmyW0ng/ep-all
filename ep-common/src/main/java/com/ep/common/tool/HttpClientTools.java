package com.ep.common.tool;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 19:14 2018/4/22
 */
@Slf4j
public class HttpClientTools {

    public static Map<String, Object> doGet(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                String jsonStr = EntityUtils.toString(response.getEntity());
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                            @Override
                            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                                if (src == src.longValue()) {
                                    return new JsonPrimitive(src.longValue());
                                }
                                return new JsonPrimitive(src);
                            }
                        }).create();
                Map<String, Object> map = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
                }.getType());
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }

    public static String doGetStr(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                String jsonStr = EntityUtils.toString(response.getEntity());
                return jsonStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * post请求 json数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static Map<String, Object> doPost(String url, String params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonStr = EntityUtils.toString(responseEntity);
                //防止gson将int转为double
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                            @Override
                            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                                if (src == src.longValue()) {
                                    return new JsonPrimitive(src.longValue());
                                }
                                return new JsonPrimitive(src);
                            }
                        }).create();
                Map<String, Object> map = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
                }.getType());
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Maps.newHashMap();
    }

    /**
     * post请求 json数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String doPostStr(String url, String params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonStr = EntityUtils.toString(responseEntity);
                return jsonStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
