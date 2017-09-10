/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import z11.samplestructs.StringHeader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author vietduc
 */
public class HttpClientUtil {
    
    public static HttpStringResponse getTextURL(CookieStore cookieStore, ArrayList<StringHeader> headers, String url) {
        HttpStringResponse httpStringResponse = null;
        try {
            
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultHeaders(headers).setDefaultCookieStore(cookieStore).build();
            HttpGet get = new HttpGet(url);
            
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setConnectionRequestTimeout(5000)
                    .setSocketTimeout(3000)
                    .build();
            get.setConfig(config);
            
            HttpResponse response = httpclient.execute(get);
            httpStringResponse = new HttpStringResponse(response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append(System.lineSeparator());
            }
            String result = resultBuilder.toString();
            try {
                rd.close();
            } catch (Exception e) {
            }
            
            httpStringResponse.content = result.trim();
            return httpStringResponse;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return httpStringResponse;
        }
    }
    
    public static HttpStringResponse deleteTextURL(CookieStore cookieStore, ArrayList<StringHeader> headers, String url) {
        HttpStringResponse httpStringResponse = null;
        try {
            
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultHeaders(headers).setDefaultCookieStore(cookieStore).build();
            HttpDelete httpDelete = new HttpDelete(url);
            
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setConnectionRequestTimeout(5000)
                    .setSocketTimeout(3000)
                    .build();
            httpDelete.setConfig(config);
            
            HttpResponse response = httpclient.execute(httpDelete);
            httpStringResponse = new HttpStringResponse(response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append(System.lineSeparator());
            }
            String result = resultBuilder.toString();
            try {
                rd.close();
            } catch (Exception e) {
            }
            
            httpStringResponse.content = result.trim();
            return httpStringResponse;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return httpStringResponse;
        }
    }
    
    public static HttpStringResponse postTextURL(CookieStore cookieStore, 
            ArrayList<StringHeader> headers, 
            String url,
            ArrayList<NameValuePair> postParameters) {
        //ex.printStackTrace();
        
        if (postParameters == null) {
            postParameters = new ArrayList<>();
        }
        HttpEntity entity = new UrlEncodedFormEntity(postParameters, Charset.forName("utf-8"));
        return postTextURL(cookieStore, headers, url, entity);
    }
    
    public static HttpStringResponse postTextURL(CookieStore cookieStore,
            ArrayList<StringHeader> headers,
            String url,
            String postData, ContentType contentType) {
        HttpEntity entity = new ByteArrayEntity(postData.getBytes(), contentType);
        return postTextURL(cookieStore, headers, url, entity);
    }
    
    public static HttpStringResponse postTextURL(CookieStore cookieStore, 
            ArrayList<StringHeader> headers, 
            String url,
            HttpEntity entity) {
        HttpStringResponse httpStringResponse = null;
        try {
            
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultHeaders(headers).setDefaultCookieStore(cookieStore).build();
            HttpPost post = new HttpPost(url);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setConnectionRequestTimeout(5000)
                    .setSocketTimeout(3000)
                    .build();
            post.setConfig(config);

            
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
            httpStringResponse = new HttpStringResponse(response.getStatusLine().getStatusCode());
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append(System.lineSeparator());
            }
            String result = resultBuilder.toString();
            try {
                rd.close();
            } catch (Exception e) {
            }
            
            httpStringResponse.content = result.trim();
            return httpStringResponse;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return httpStringResponse;
        }
    }
    
    
    public static HttpStringResponse putTextURL(CookieStore cookieStore, 
            ArrayList<StringHeader> headers, 
            String url,
            ArrayList<NameValuePair> postParameters) {
        HttpEntity entity = new UrlEncodedFormEntity(postParameters, Charset.forName("utf-8"));
        return putTextURL(cookieStore, headers, url, entity);
    }
    
    public static HttpStringResponse putTextURL(CookieStore cookieStore,
            ArrayList<StringHeader> headers,
            String url,
            String postData, ContentType contentType) {
        HttpEntity entity = new ByteArrayEntity(postData.getBytes(), contentType);
        return putTextURL(cookieStore, headers, url, entity);
    }
    
    public static HttpStringResponse putTextURL(CookieStore cookieStore, 
            ArrayList<StringHeader> headers, 
            String url,
            HttpEntity entity) {
        HttpStringResponse httpStringResponse = null;
        try {
            
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultHeaders(headers).setDefaultCookieStore(cookieStore).build();
            HttpPut httpPut = new HttpPut(url);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setConnectionRequestTimeout(5000)
                    .setSocketTimeout(3000)
                    .build();
            httpPut.setConfig(config);

            
            httpPut.setEntity(entity);
            HttpResponse response = httpclient.execute(httpPut);
            httpStringResponse = new HttpStringResponse(response.getStatusLine().getStatusCode());
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append(System.lineSeparator());
            }
            String result = resultBuilder.toString();
            try {
                rd.close();
            } catch (Exception e) {
            }
            
            httpStringResponse.content = result.trim();
            return httpStringResponse;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return httpStringResponse;
        }
    }
    
    
    
}
