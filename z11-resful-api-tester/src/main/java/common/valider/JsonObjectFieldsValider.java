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
public class JsonObjectFieldsValider extends IsJsonObjectValider {
    String compareType;
    String length;
    
    public JsonObjectFieldsValider(String length) {
        this(length, CompareType.EQ);
    }
    
    public JsonObjectFieldsValider(String length, String compareType) {
        this.length = length;
        this.compareType = compareType;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonObject jsonContent = new JsonParser().parse(content).getAsJsonObject();
        int countProperty = jsonContent.entrySet().size();
        new NumberCompareValider(length, compareType).valid(""+countProperty);
    }
}