/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 *
 * @author danny
 */
public class HttpStringResponse {
    public HttpStringResponse(int code_) {
        code = code_;
    }
    public int code;
    public String content;
    
    @Override
    public String toString() {
        String prettyString = content;
        try {
            JsonObject jsonContent = new JsonParser().parse(content).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            prettyString = gson.toJson(jsonContent);
            return "code: " + code + ", content:\n" + prettyString;
        } catch (Exception e) {
        }
        
        try {
            JsonElement jsonContent = new JsonParser().parse(content).getAsJsonArray();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            prettyString = gson.toJson(jsonContent);
            return "code: " + code + ", content:\n" + prettyString;
        } catch (Exception e) {
        }
        
        return "code: " + code + ", content: " + prettyString;
        
        
//        JsonObject json = new JsonObject();
//        json.addProperty("code", code);
//        json.addProperty("content", content);
//        return json.toString();
    }
}
