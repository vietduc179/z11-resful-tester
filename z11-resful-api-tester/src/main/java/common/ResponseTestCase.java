/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import common.valider.Valider;
import java.util.List;
import org.apache.http.client.CookieStore;

/**
 *
 * @author danny
 */
public class ResponseTestCase {
    CookieStore cookieStore;
    Valider validCode;
    List<Valider> validContent;

    public ResponseTestCase(
            CookieStore cookieStore,
            Valider validCode,
            List<Valider> validContent) {
        this.cookieStore = cookieStore;
        this.validCode = validCode;
        this.validContent = validContent;
    }

    public void validRequest(HttpStringResponse httpStringResponse) throws Exception {
        validCode.valid("" + httpStringResponse.code);

        for (int i = 0; i < validContent.size(); i++) {
            validContent.get(i).valid(httpStringResponse.content);
        }

    }
}
