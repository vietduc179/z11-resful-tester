/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import z11.samplestructs.StringHeader;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;

/**
 *
 * @author danny
 */
public class RequestTestCase {

    CookieStore cookieStore;
    String base_url;
    String uri;

    String method;

    ArrayList<NameValuePair> params;
    ArrayList<StringHeader> headers;

    public RequestTestCase(
            CookieStore cookieStore,
            String base_url,
            String uri,
            String method,
            ArrayList<NameValuePair> params,
            ArrayList<StringHeader> headers) {
        this.cookieStore = cookieStore;
        this.base_url = base_url;
        this.uri = uri;
        this.method = method;
        this.params = params;
        this.headers = headers;
    }

    public HttpStringResponse makeHttpRequest() throws Exception {
        //try {
            if (HttpApiTestCase.GET.equals(method)) {
                return HttpClientUtil.getTextURL(cookieStore, headers, base_url + uri);
            }
            if (HttpApiTestCase.POST.equals(method)) {
                return HttpClientUtil.postTextURL(cookieStore, headers, base_url + uri, params);
            }
            if (HttpApiTestCase.PUT.equals(method)) {
                return HttpClientUtil.putTextURL(cookieStore, headers, base_url + uri, params);
            }
            if (HttpApiTestCase.DELETE.equals(method)) {
                return HttpClientUtil.deleteTextURL(cookieStore, headers, base_url + uri);
            }
            throw new Exception("Unknow http method");
//        } catch (Exception e) {
//            return null;
//        }
    }
}
