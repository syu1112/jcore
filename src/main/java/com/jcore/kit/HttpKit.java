package com.jcore.kit;

import java.util.*;

import com.jcore.util.codec.MD5Codec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpKit {
    private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private CloseableHttpResponse response;
    private HttpEntity entity;

    // public void a() {
    // HttpClientContext context = new HttpClientContext();
    // CookieStore cookieStore = new BasicCookieStore();
    // String[] curCookie = cookies[index].split("=", 2);
    // BasicClientCookie cookie = new BasicClientCookie(curCookie[0],
    // curCookie[1]);
    // cookie.setDomain(".baidu.com");
    // cookie.setPath("/");
    // cookieStore.addCookie(cookie);
    // context.setCookieStore(cookieStore);
    // }

    public static void main(String[] args) throws Exception {
        String url = "http://www.baidu.com/";

        HttpKit http = new HttpKit();
        http.createHttpGetProxy(url, "122.183.139.98", "8080");
        http.executeGet();
        System.out.println(http.getEntityString());
        http.close();

//        url = "http://res1.eqh5.com/group2/M00/96/AB/yq0KXlatCSSAcJm8AADCZwrwjlY444.jpg";
//        http = new HttpKit();
//        http.createHttpGet(url);
//        http.executeGet();
//        FileUtil.writeByteArrayToFile("c:/var/yq0KXlatCSSAcJm8AADCZwrwjlY444.jpg", http.getEntityFile());
//        http.close();
    }

    public HttpKit() {
        createHttpClient();
    }
    
    private CloseableHttpClient createHttpClient() {
        httpClient = HttpClients.createDefault();
        return httpClient;
    }
    
    public HttpGet createHttpGet(String url) {
        httpGet = new HttpGet(url);
        return httpGet;
    }

    public HttpPost createHttpPost(String url) throws Exception {
        httpPost = new HttpPost(url);
        return httpPost;
    }
    
    @SuppressWarnings("deprecation")
    public HttpPost createHttpPostProxy(String url, String hostname, String port) {
        httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setStaleConnectionCheckEnabled(true)
                .setProxy(new HttpHost(hostname, Integer.parseInt(port)))
                .build();
        httpPost.setConfig(requestConfig);
        System.out.println("当前使用代理："+hostname+":"+port);
        return httpPost;
    }

    @SuppressWarnings("deprecation")
    public HttpGet createHttpGetProxy(String url, String hostname, String port) {
        httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setStaleConnectionCheckEnabled(true)
                .setProxy(new HttpHost(hostname, Integer.parseInt(port)))
                .build();
        httpGet.setConfig(requestConfig);
        System.out.println("当前使用代理："+hostname+":"+port);
        return httpGet;
    }

    public HttpEntity executeGet() throws Exception {
        response = httpClient.execute(httpGet);
        entity = response.getEntity();
        return entity;
    }

    public HttpEntity executePost() throws Exception {
        response = httpClient.execute(httpPost);
        entity = response.getEntity();
        return entity;
    }

    public String getEntityString() throws Exception {
        return EntityUtils.toString(entity, "UTF-8");
    }

    public String getEntityStringGzip() throws Exception {
        return EntityUtils.toString(new GzipDecompressingEntity(entity), "UTF-8");
    }

    public byte[] getEntityFile() throws Exception {
        return EntityUtils.toByteArray(entity);
    }

    public void close() throws Exception {
        if (entity != null)
            EntityUtils.consume(entity);
        if (response != null)
            response.close();
    }

    public void jsonParams(Map<String, Object> params) {
        StringEntity entity = new StringEntity(JSONObject.toJSONString(params).toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
    }

    public void pairParams(Map<String, Object> params) throws Exception {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        Iterator<String> keyit = keySet.iterator();
        while (keyit.hasNext()) {
            String key = (String) keyit.next();
            formparams.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(uefEntity);
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpGet getHttpGet() {
        return httpGet;
    }

    public HttpPost getHttpPost() {
        return httpPost;
    }

    public CloseableHttpResponse getResponse() {
        return response;
    }

    public HttpEntity getEntity() {
        return entity;
    }

}
