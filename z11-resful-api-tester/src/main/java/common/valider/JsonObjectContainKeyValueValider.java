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
public class JsonObjectContainKeyValueValider extends JsonObjectContainKeyValider {
    String value;
    public JsonObjectContainKeyValueValider(String key, String value) {
        super(key);
        this.value = value;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonObject jsonContent = new JsonParser().parse(content).getAsJsonObject();
        if (!jsonContent.has(key)) {
            throw new Exception("JsonObjectContainKeyValueValider not found key:" + key);
        }
        String value2 = "" + jsonContent.get(key).getAsString();
        if (value2.equals(value)) {
        } else {
            throw new Exception("JsonObjectContainKeyValueValider not valid, key:" + key + ", value:" + value);
        }
    }
}