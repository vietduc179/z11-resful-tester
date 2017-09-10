/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 *
 * @author vietduc
 */
public class JsonArrayLengthValider extends IsJsonArrayValider {
    String compareType;
    String length;
    
    public JsonArrayLengthValider(String length) {
        this(length, CompareType.EQ);
    }
    
    public JsonArrayLengthValider(String length, String compareType) {
        this.length = length;
        this.compareType = compareType;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        JsonArray jsonContent = new JsonParser().parse(content).getAsJsonArray();
        new NumberCompareValider(length, compareType).valid(""+jsonContent.size());
    }
}
