/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import testcase.DemoData;

/**
 *
 * @author vietduc
 */
public class JsonObjectFetchId extends IsJsonObjectValider {
    String key;
    public JsonObjectFetchId(String key2) {
        key = key2;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
        DemoData.instance().addData(key, jsonObject.get("id").getAsString());
    }
}
