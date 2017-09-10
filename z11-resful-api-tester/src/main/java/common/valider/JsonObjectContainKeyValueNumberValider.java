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
public class JsonObjectContainKeyValueNumberValider extends JsonObjectContainKeyValider {
    String compareType;
    String value;
    
    public JsonObjectContainKeyValueNumberValider(String key, String value) {
        this(key, value, CompareType.EQ);
    }
    public JsonObjectContainKeyValueNumberValider(String key, String value, String compareType) {
        super(key);
        this.value = value;
        this.compareType = compareType;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonObject jsonContent = new JsonParser().parse(content).getAsJsonObject();
        if (!jsonContent.has(key)) {
            throw new Exception("JsonObjectContainKeyValueNumberValider not found key:" + key);
        }
        String value2 = "" + jsonContent.get(key).getAsString();
        new NumberCompareValider(value, compareType).valid(value2);
    }
}