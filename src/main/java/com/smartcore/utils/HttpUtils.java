package com.smartcore.utils;

import com.alibaba.fastjson.JSONObject;
import com.smartcore.common.HTTPResponse;
import com.smartcore.common.Response;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    //Http协议GET请求
    public static Response httpGet(String url, String token) throws Exception{
        //初始化HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpGet
        HttpGet httpGet = new HttpGet(url);

        if (token != "") {
            httpGet.addHeader("X-Auth-Token", token);
        }
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //获取请求状态码
        //response.getStatusLine().getStatusCode();
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String body = EntityUtils.toString(entity,"UTF-8");
        HTTPResponse httpResponse = new HTTPResponse();
        httpResponse.setBody(body);
        httpResponse.setCode(response.getStatusLine().getStatusCode());

        Map<String, Object> headerMap = new HashMap<>();
        Header[] header = response.getAllHeaders();
        for (Header headers : header) {
            String[] splitStr = headers.toString().split(":");
            headerMap.put(splitStr[0], splitStr[1].strip());
        }
        httpResponse.setHeader(JSONObject.toJSONString(headerMap));
        response.close();
        httpClient.close();
        return httpResponse;

    }

    //Http协议Post请求
    public static Response httpPost (String url, String json) throws Exception{
        //初始HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Post对象
        HttpPost httpPost = new HttpPost(url);
        //设置Content-Type
        httpPost.setHeader("Content-Type","application/json");
        //写入JSON数据
        httpPost.setEntity(new StringEntity(json));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取请求码
        //response.getStatusLine().getStatusCode();
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String body = EntityUtils.toString(entity,"UTF-8");

        StatusLine code = response.getStatusLine();
        HTTPResponse httpResponse = new HTTPResponse();
        httpResponse.setBody(body);
        if (url.contains("auth/token")) {
            Header header =  response.getFirstHeader("X-Subject-Token");
            String token = header.toString();
            Integer index = "X-Subject-Token:".length();
            httpResponse.setHeader(token.substring(index).strip());
        }
        httpResponse.setCode(code.getStatusCode());
        response.close();
        httpClient.close();
        return httpResponse;

    }

    public static Response httpPostWithHeader (String url, String token, String json) throws Exception{
        //初始HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Post对象
        HttpPost httpPost = new HttpPost(url);
        //设置Content-Type
        httpPost.setHeader("Content-Type","application/json");
        httpPost.setHeader("DataEncoding", "UTF-8");
        if (!token.equals("")) {
            httpPost.setHeader("X-Auth-Token", token);
        }
        //写入JSON数据
        httpPost.setEntity(new StringEntity(json));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取请求码
        //response.getStatusLine().getStatusCode();
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String body = EntityUtils.toString(entity,"UTF-8");

        StatusLine code = response.getStatusLine();
        HTTPResponse httpResponse = new HTTPResponse();
        httpResponse.setBody(body);
        httpResponse.setCode(code.getStatusCode());
        response.close();
        httpClient.close();
        return httpResponse;

    }

    public static String httpPut(String url, String token, String json) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("DataEncoding", "UTF-8");
        if (!token.equals("")) {
            httpPut.setHeader("X-Auth-Token", token);
        }

        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(json));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String httpDelete(String url, String token) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");
        if (!token.equals("")) {
            httpDelete.setHeader("X-Auth-Token", token);
        }

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) { return ""; }
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //请求head
    public static Response httpHeader(String url, String token) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(url);

        // 设置超时
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpHead.setConfig(requestConfig);

        //添加herder信息
        httpHead.setHeader("Content-type", "application/json");
        httpHead.setHeader("DataEncoding", "UTF-8");
        if (!token.equals("")) {
            httpHead.setHeader("X-Auth-Token", token);
        }

        CloseableHttpResponse response = httpClient.execute(httpHead);
        Map<String, Object> headerMap = new HashMap<>();

        Header[] header = response.getAllHeaders();
        for (Header headers : header) {
            String[] splitStr = headers.toString().split(":");
            headerMap.put(splitStr[0], splitStr[1].strip());
        }
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            System.out.println(url + response);
        }
        HTTPResponse httpResponse = new HTTPResponse();
        httpResponse.setBody(JSONObject.toJSONString(headerMap));
        httpResponse.setCode(response.getStatusLine().getStatusCode());
        response.close();
        httpClient.close();
        return httpResponse;
    }

    //请求head，返回指定head信息
    public static String getOneHeadValue(String url, String[] getHeaderName, String[] herderName, String[] headerValue) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(url);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        // 设置超时
        httpHead.setConfig(requestConfig);

        //添加herder信息
        for (int i = 0; i < herderName.length; i++) {
            httpHead.addHeader(herderName[i], headerValue[i]);
        }
        CloseableHttpResponse response = httpClient.execute(httpHead);
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            System.out.println(url + response);
        }
        StringBuilder header = new StringBuilder();
        for (String getHeaderNames : getHeaderName) {
            Header[] headerIndex = response.getHeaders(getHeaderNames);
            header.append(Arrays.toString(headerIndex)).append("\n");
        }
        response.close();
        httpClient.close();
        return String.valueOf(header);
    }

    //Https协议Get请求
    public static String httpsGet(String url) throws Exception{
        CloseableHttpClient hp = createSSLClientDefault();
        HttpGet hg = new HttpGet(url);
        CloseableHttpResponse response = hp.execute(hg);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity,"UTF-8");
        hp.close();
        return content;
    }
    //Https协议Post请求
    public static String httpsPost(String url, String json) throws Exception{

        CloseableHttpClient hp = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type","application/json");
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse response = hp.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity,"UTF-8");
        hp.close();
        return content;
    }


    public static CloseableHttpClient createSSLClientDefault() throws Exception{
        //如果下面的方法证书还是不过，报错的话试试下面第二种
        /* SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){
        //信任所有
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
        }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();*/

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
                NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();

    }


}