package com.jfcore.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jfcore.web.vo.JsonRtn;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int DEFAULT_SOCKETTIMEOUT = 5000;// 默认等待响应时间(毫秒)
    private static final int DEFAULT_RETRY_TIMES = 0;// 默认执行重试的次数
    private static final int BUFFER_SIZE = 1024;

    public static String getUrlToString(String url) throws Exception {
        return Request.Get(url).execute().returnContent().asString();
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonRtn<T> getUrlToRtn(String url) throws Exception {
        return JSON.parseObject(Request.Get(url).execute().returnContent().asString(), JsonRtn.class);
    }

    public static CloseableHttpClient createHttpClient() {
        return createHttpClient(DEFAULT_RETRY_TIMES, DEFAULT_SOCKETTIMEOUT);
    }

    public static CloseableHttpClient createHttpClient(int retryTimes, int socketTimeout) {
        Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(5000);// 设置连接超时时间，单位毫秒
        builder.setConnectionRequestTimeout(1000);// 设置从connect
                                                  // Manager获取Connection
                                                  // 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
        builder.setSocketTimeout(socketTimeout);// 请求获取数据的超时时间，单位毫秒。
                                                // 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
        RequestConfig defaultRequestConfig = builder.setCookieSpec(CookieSpecs.STANDARD_STRICT).setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        // 创建可用Scheme
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE).build();
        // 创建ConnectionManager，添加Connection配置信息
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(connectionManager).setDefaultRequestConfig(defaultRequestConfig).build();
        return httpClient;
    }

    public static String doGet(CloseableHttpClient httpClient, String url, String reffer, String cookie, String charset, boolean closeHttpClient) throws IOException {
        CloseableHttpResponse httpResponse = null;
        try {
            if (httpClient == null) {
                httpClient = createHttpClient();
            }
            HttpGet get = new HttpGet(url);
            if (cookie != null && !"".equals(cookie)) {
                get.setHeader("Cookie", cookie);
            }
            if (reffer != null && !"".equals(reffer)) {
                get.setHeader("Reffer", reffer);
            }
            charset = getCharset(charset);

            httpResponse = httpClient.execute(get);
            return getResult(httpResponse, charset);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
            if (closeHttpClient && httpClient != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String doPost(String url, Object paramsObj) throws Exception {
        return doPost(HttpClients.createDefault(), url, paramsObj, null, null, null, true);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Object paramsObj, String reffer, String cookie, String charset, boolean closeHttpClient) throws IOException {
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost post = new HttpPost(url);
            if (cookie != null && !"".equals(cookie)) {
                post.setHeader("Cookie", cookie);
            }
            if (reffer != null && !"".equals(reffer)) {
                post.setHeader("Reffer", reffer);
            }
            charset = getCharset(charset);
            HttpEntity httpEntity = getEntity(paramsObj, charset);
            if (httpEntity != null) {
                post.setEntity(httpEntity);
            }
            httpResponse = httpClient.execute(post);
            return getResult(httpResponse, charset);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e2) {
                }
            }
            if (closeHttpClient && httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    public static String doDown(String remoteFileUrl, String localFilePath) {
        return doDown(remoteFileUrl, localFilePath, null);
    }

    public static String doDown(String remoteFileUrl, String localFilePath, String localFileName) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;

        int size = 0;
        byte[] buf = new byte[BUFFER_SIZE];
        String filename = localFileName;
        if (localFileName == null) {
            filename = FilenameUtils.getName(remoteFileUrl);
        }
        try {
            url = new URL(remoteFileUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(localFilePath + filename);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
        return null;
    }

    public static boolean doDown(CloseableHttpClient httpClient, String remoteFileUrl, String localFilePath, boolean closeHttpClient) throws ClientProtocolException, IOException {
        CloseableHttpResponse response = null;
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            HttpGet httpget = new HttpGet(remoteFileUrl);
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return false;
            }
            in = entity.getContent();
            File file = new File(localFilePath);
            fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
            }
            fout.flush();
            EntityUtils.consume(entity);
            return true;
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            if (closeHttpClient && httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        String location = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(30000);
            connection.setConnectTimeout(120000);
            // 建立实际的连接
            connection.connect();

            location = connection.getHeaderField("Location");
            // 获取所有响应头字段
            // Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            logger.error("exception sendGet,url:{}", url);
            // e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (!StringUtil.isEmpty(location)) {
            return location;
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url) {
        String param = url.split("?")[1];
        url = url.split("?")[0];

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            logger.error("exception sendPost,url:{}", url);
            // e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static HttpEntity getEntity(Object paramsObj, String charset) throws UnsupportedEncodingException {
        if (paramsObj == null) {
            return null;
        }
        if (Map.class.isInstance(paramsObj)) {// 当前是map数据
            @SuppressWarnings("unchecked")
            Map<String, String> paramsMap = (Map<String, String>) paramsObj;
            List<NameValuePair> list = getNameValuePairs(paramsMap);
            UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(list, charset);
            httpEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            return httpEntity;
        } else if (String.class.isInstance(paramsObj)) {// 当前是string对象，可能是
            String paramsStr = paramsObj.toString();
            StringEntity httpEntity = new StringEntity(paramsStr, charset);
            if (paramsStr.startsWith("{")) {
                httpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            } else if (paramsStr.startsWith("<")) {
                httpEntity.setContentType(ContentType.APPLICATION_XML.getMimeType());
            } else {
                httpEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            }
            return httpEntity;
        } else {
            logger.error("unknown HttpEntity type");
        }
        return null;
    }

    private static String getResult(CloseableHttpResponse httpResponse, String charset) throws ParseException, IOException {
        String result = null;
        if (httpResponse == null) {
            return result;
        }
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            return result;
        }
        logger.debug("StatusCode is " + httpResponse.getStatusLine().getStatusCode());
        result = EntityUtils.toString(entity, charset);
        EntityUtils.consume(entity);
        return result;
    }

    private static String getCharset(String charset) {
        return charset == null ? DEFAULT_CHARSET : charset;
    }

    private static List<NameValuePair> getNameValuePairs(Map<String, String> paramsMap) {
        List<NameValuePair> list = new ArrayList<>();
        if (paramsMap == null || paramsMap.isEmpty()) {
            return list;
        }
        for (Entry<String, String> entry : paramsMap.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return list;
    }

}
