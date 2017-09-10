/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author vietduc
 */
public class JsonObjectContainKeyValider extends IsJsonObjectValider {
    String key;
    public JsonObjectContainKeyValider(String key) {
        this.key = key;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonObject jsonContent = new JsonParser().parse(content).getAsJsonObject();
        if (!jsonContent.has(key)) {
            throw new Exception("JsonObjectContainKeyValider not found key:" + key);
        }
    }
}