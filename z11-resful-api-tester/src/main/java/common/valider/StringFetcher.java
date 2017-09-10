/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

import testcase.DemoData;

/**
 *
 * @author vietduc
 */
public class StringFetcher extends IsNotNullValider {
    String key;
    public StringFetcher(String key2) {
        key = key2;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        DemoData.instance().addData(key, content);
    }
}
