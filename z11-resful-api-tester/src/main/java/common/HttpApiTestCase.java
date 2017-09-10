/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import z11.samplestructs.StringHeader;
import common.valider.Valider;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;

/**
 *
 * @author vietduc
 */
public class HttpApiTestCase {
    
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    
    RequestTestCase requestTestCase;
    ResponseTestCase responseTestCase;
    HttpStringResponse httpStringResponse;
    
    CookieStore cookieStore;
    String base_url; 
    
    public HttpApiTestCase (CookieStore cookieStore, String base_url) {
        requestTestCase = null;
        responseTestCase = null;
        this.cookieStore = cookieStore;
        this.base_url = base_url;
    }

    public void setRequestTestCase (
            String uri, 
            String method, 
            ArrayList<NameValuePair> params,
            ArrayList<StringHeader> headers) {
        requestTestCase = new RequestTestCase(cookieStore, base_url, uri, method, params, headers);
    }
    
    public void setResponseTestCase (
            Valider validCode,
            List<Valider> validContent) {
        responseTestCase = new ResponseTestCase(cookieStore, validCode, validContent);
    }
    
    public HttpStringResponse getHttpStringResponse() {
        return httpStringResponse;
    }
    
    public void doTest() throws Exception {
        try {
            httpStringResponse = requestTestCase.makeHttpRequest();
            if (httpStringResponse != null) {
                responseTestCase.validRequest(httpStringResponse);
            } else {
                throw new Exception("httpStringResponse is null!");
            }
        } catch (Exception e) {
            if (httpStringResponse != null) {
                System.out.println("httpStringResponse:" + httpStringResponse.toString());
            }
            throw e;
        }
    }

}
