/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author vietduc
 */
public class JsonArrayExceptKeyValider extends IsJsonArrayValider {
    String key;
    public JsonArrayExceptKeyValider(String key) {
        this.key = key;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        
        JsonArray jsonContent = new JsonParser().parse(content).getAsJsonArray();
        for (int i = 0; i < jsonContent.size(); i++) {
            JsonObject obj = (JsonObject) jsonContent.get(i);
            if (obj.has(key)) {
                throw new Exception("JsonArrayExceptKeyValider found key:" + key);
            }
        }
        
    }
}