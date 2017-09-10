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
public class JsonArrayContainKeyValueValider extends IsJsonArrayValider {
    String key;
    String value;
    public JsonArrayContainKeyValueValider(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        
        JsonArray jsonContent = new JsonParser().parse(content).getAsJsonArray();
        for (int i = 0; i < jsonContent.size(); i++) {
            JsonObject obj = (JsonObject) jsonContent.get(i);
            if (obj.has(key)) {
                String value2 = "" + obj.get(key).getAsString();
                if (value2.equals(value)) {
                    return;
                }
            }
        }
        throw new Exception("JsonArrayContainKeyValueValider not found key/value = " + key + "/" + value);
    }
}