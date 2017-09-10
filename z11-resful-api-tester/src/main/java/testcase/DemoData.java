/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcase;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author vietduc
 */
public class DemoData {
    public static DemoData demoData = null;
    public static DemoData instance() {
        if (demoData == null) {
            demoData = new DemoData();
        }
        return demoData;
    }
    
    private final HashMap<String, String> dictionary;
    private DemoData() {
        dictionary = new HashMap<>();
    }
    
    public void importData(JSONObject jsonobj_demo_data) {
        JSONArray demo_data_names = jsonobj_demo_data.names();
        for (int data_i = 0; data_i < demo_data_names.length(); data_i++) {
            String key = demo_data_names.getString(data_i);
            String value = jsonobj_demo_data.getString(key);
            dictionary.put(key, value);
        }
    }
    
    public void addData(String key, String value) {
        dictionary.put(key, value);
    }
    
    public HashMap<String, String> getDictionary() {
        return dictionary;
    }
    
}
