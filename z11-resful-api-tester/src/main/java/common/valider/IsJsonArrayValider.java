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
public class IsJsonArrayValider extends IsNotNullValider {
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        new JsonParser().parse(content).getAsJsonArray();
    }
}
