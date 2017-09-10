/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcase;


import common.HttpApiTestCase;
import z11.samplestructs.StringHeader;
import common.valider.IsJsonArrayValider;
import common.valider.IsJsonObjectValider;
import common.valider.IsNotNullValider;
import common.valider.IsNullValider;
import common.valider.IsNumberValider;
import common.valider.JsonArrayContainKeyValider;
import common.valider.JsonArrayContainKeyValueValider;
import common.valider.JsonArrayExceptKeyValider;
import common.valider.JsonArrayLengthValider;
import common.valider.JsonObjectContainKeyValider;
import common.valider.JsonObjectContainKeyValueNumberValider;
import common.valider.JsonObjectContainKeyValueValider;
import common.valider.JsonObjectExceptKeyValider;
import common.valider.JsonObjectFetchId;
import common.valider.JsonObjectFieldsValider;
import common.valider.NumberCompareValider;
import common.valider.StringContainValider;
import common.valider.StringFetcher;
import common.valider.StringLengthValider;
import common.valider.Valider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import z11.F_ile;
import z11.S_tring;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author danny
 */
public class RunTestCase {
    public static String getContentOfFile(String filePath){
        return getContentOfFile(new File(filePath));
    }
    
    public static String getContentOfFile(File file) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                resultBuilder.append(line).append(System.lineSeparator());
            }
            in.close();
            return resultBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    public static void main(String [] args) throws Exception {
        final long startTime = System.nanoTime();
        
        String test_case_json_file = "test_case/auth_api_test_case.json";
        if (!F_ile.checkFileExists(test_case_json_file)) {
            test_case_json_file = "test_case/auth_api_test_case_local.xls";
        }
        
        if (args.length > 0 ) {
            test_case_json_file = args[0];
        } else {
            System.out.println("Parsing run params error!");
            System.out.println("Use: java -jar tester.jar <path/to/file> [base/url]");
            return;
        }
        
        JSONObject file_object = null;
        if (test_case_json_file.endsWith(".xls")) {
            System.out.println("Read test case from excel file!");
            file_object = common.JXLUtil.exportJson(test_case_json_file);
            F_ile.writeStringToFile("test_case_readed.json", file_object.toString(4), false);

        } else if (test_case_json_file.endsWith(".json")) {
            System.out.println("Read test case from json file!");
            file_object = new JSONObject(getContentOfFile(test_case_json_file));
        }
        
        String baseUrl = file_object.getString("base_url");
        if ((baseUrl == null || baseUrl.length() < 5) && args.length > 1) {
            baseUrl = args[1];
        }
        System.out.println("test case for url:" + baseUrl);
        
        ArrayList<StringHeader> headerAcceptJson = new ArrayList<>();
        JSONArray json_headers = (JSONArray) file_object.get("headers");
        for (int header_i = 0; header_i < json_headers.length(); header_i++) {
            JSONObject json_header = (JSONObject) json_headers.get(header_i);
            headerAcceptJson.add(new StringHeader(json_header.getString("name"), json_header.getString("value")));
        }
        
        ArrayList<CookieStore> browsers = new ArrayList<>();
        int numOfBrowser = file_object.getInt("browsers");
        for (int i = 0; i < numOfBrowser; i++) {
            browsers.add(new BasicCookieStore());
        }
        
        JSONObject jsonobj_demo_data = (JSONObject) file_object.get("demo_data");
        System.out.println("json_demo_data:" + jsonobj_demo_data.toString(4));
        
        DemoData.instance().importData(jsonobj_demo_data);
        
        JSONArray json_test_cases = (JSONArray) file_object.get("test_case");
        
        boolean hasOneShot = false;
        for (int test_case_i = 0; test_case_i < json_test_cases.length(); test_case_i++) {
            JSONObject json_test_case = (JSONObject) json_test_cases.get(test_case_i);
            if (json_test_case.has("oneshot")) {
                if (json_test_case.getInt("oneshot") == 1) {
                    hasOneShot = true;
                }
            }
        }
        System.out.println("hasOneShot:" + hasOneShot);
        for (int test_case_i = 0; test_case_i < json_test_cases.length(); test_case_i++) {
            System.out.println();
            System.out.println(">>> Test Case " + (test_case_i+1) + "/" + json_test_cases.length() + ":");
            JSONObject json_test_case = (JSONObject) json_test_cases.get(test_case_i);
            
            String uri = S_tring.replacePattern(json_test_case.getString("uri"), DemoData.instance().getDictionary());
            String httpMethod = json_test_case.getString("method");

            if (json_test_case.has("enable")) {
                int enable = json_test_case.getInt("enable");
                if (enable == 0) {
                    continue;
                }
            }
            
            if (hasOneShot) {
                if (!json_test_case.has("oneshot")) {
                    System.out.println("not has oneshot: ");
                    continue;
                }
                
                if ((json_test_case.getInt("oneshot") == 1)) {
                    System.out.println("oneshot: " + 1);
                } else {
                    System.out.println("oneshot: " + json_test_case.getInt("oneshot"));
                    continue;
                }    
                
            }
            
            System.out.println("uri: " + httpMethod + " " + uri);

            if (json_test_case.has("msg")) {
                System.out.println("msg: " + json_test_case.getString("msg"));
            }
            
            ArrayList<NameValuePair> params = null;
            JSONObject json_params = json_test_case.getJSONObject("params");
            JSONArray json_params_names = json_params.names();
            if (json_params_names != null) {
                for(int param_i = 0; param_i < json_params_names.length(); param_i++){
                    if (params == null) {
                        params = new ArrayList<>();
                    }
                    String respkey = S_tring.replacePattern(json_params_names.getString(param_i), DemoData.instance().getDictionary());
                    String respvalue = S_tring.replacePattern(json_params.getString(respkey), DemoData.instance().getDictionary());
                    
                    params.add(new BasicNameValuePair(respkey, respvalue));
                }
            }

            // prepare valider for code
            Valider validCode = null;
            JSONObject jsonValidCode = json_test_case.getJSONObject("valid_code");
            String validCodeType = jsonValidCode.getString("type");
            JSONObject jsonValidCodeParam = jsonValidCode.getJSONObject("params");
            HashMap<String, String> validerCodeParams = new HashMap<>();
            JSONArray jsonValidCodeParam_fields = jsonValidCodeParam.names();
            if (jsonValidCodeParam_fields != null) {
                for (int param_i = 0; param_i < jsonValidCodeParam_fields.length(); param_i++) {
                    String prKey = jsonValidCodeParam_fields.getString(param_i);
                    String prValue = null;
                    try {
                        prValue = jsonValidCodeParam.getString(prKey);
                    } catch (JSONException je) {
                        prValue = "" + jsonValidCodeParam.getInt(prKey);
                    }
                    prValue = S_tring.replacePattern(prValue, DemoData.instance().getDictionary());
                    validerCodeParams.put(prKey, prValue);
                }
            }
            
            if ("number_compare".equals(validCodeType)) {
                if (jsonValidCodeParam.has("compare_type")) {
                    validCode = new NumberCompareValider(validerCodeParams.get("value"), validerCodeParams.get("compare_type"));
                } else {
                    validCode = new NumberCompareValider(validerCodeParams.get("value"));
                }
                
            } else {
                throw new Exception("Not support valid type for response code!");
            }
            
            // prepare valider for content
            ArrayList<Valider> validContents = new ArrayList<>();
            JSONArray jsonValidContents = json_test_case.getJSONArray("valid_content");
            for (int validContent_i = 0; validContent_i < jsonValidContents.length(); validContent_i++) {
                JSONObject jsonValidContent = jsonValidContents.getJSONObject(validContent_i);
                String validType = jsonValidContent.getString("type");
                JSONObject jsonValidParams = jsonValidContent.getJSONObject("params");
                
                HashMap<String, String> validerParams = new HashMap<>();
                
                JSONArray jsonValidParam_fields = jsonValidParams.names();
                if (jsonValidParam_fields != null) {
                    for (int param_i = 0; param_i < jsonValidParam_fields.length(); param_i++) {
                        String prKey = jsonValidParam_fields.getString(param_i);
                        String prValue = null;
                        try {
                            prValue = jsonValidParams.getString(prKey);
                        } catch (JSONException je) {
                            prValue = "" + jsonValidParams.getInt(prKey);
                        }
                        prValue = S_tring.replacePattern(prValue, DemoData.instance().getDictionary());
                        
                        validerParams.put(prKey, prValue);
                    }
                }
                
                if (null != validType) {
                    switch (validType) {
                        case "is_not_null": {
                            validContents.add(new IsNotNullValider());
                            break;
                        }
                        case "is_null": {
                            validContents.add(new IsNullValider());
                            break;
                        }
                        case "is_number": {
                            validContents.add(new IsNumberValider());
                            break;
                        }
                        case "is_json_array": {
                            validContents.add(new IsJsonArrayValider());
                            break;
                        }
                        case "is_json_object": {
                            validContents.add(new IsJsonObjectValider());
                            break;
                        }
                        case "json_array_contain_key": {
                            validContents.add(new JsonArrayContainKeyValider(validerParams.get("key")));
                            break;
                        }
                        case "json_array_contain_key_value": {
                            validContents.add(new JsonArrayContainKeyValueValider(validerParams.get("key"), validerParams.get("value")));
                            break;
                        }
                        case "json_array_except_key": {
                            validContents.add(new JsonArrayExceptKeyValider(validerParams.get("key")));
                            break;
                        }
                        case "json_array_length": {
                            if (validerParams.containsKey("compare_type")) {
                                validContents.add(
                                        new JsonArrayLengthValider(validerParams.get("length"), validerParams.get("compare_type")));
                            } else {
                                validContents.add(new JsonArrayLengthValider(validerParams.get("length")));
                            }
                            break;
                        }
                        case "json_object_contain_key": {
                            validContents.add(new JsonObjectContainKeyValider(validerParams.get("key")));
                            break;
                        }
                        case "json_object_except_key": {
                            validContents.add(new JsonObjectExceptKeyValider(validerParams.get("key")));
                            break;
                        }     
                        case "json_object_contain_key_value": {
                            validContents.add(new JsonObjectContainKeyValueValider(validerParams.get("key"), validerParams.get("value")));
                            break;
                        }
                        case "json_object_contain_key_value_number": {
                            if (validerParams.containsKey("compare_type")) {
                                validContents.add(new JsonObjectContainKeyValueNumberValider(validerParams.get("key"), validerParams.get("value"), validerParams.get("compare_type")));
                            } else {
                                validContents.add(new JsonObjectContainKeyValueNumberValider(validerParams.get("key"), validerParams.get("value")));
                            }
                            break;
                        }
                        case "json_object_field": {
                            if (validerParams.containsKey("compare_type")) {
                                validContents.add(new JsonObjectFieldsValider(validerParams.get("key"), validerParams.get("compare_type")));
                            } else {
                                validContents.add(new JsonObjectFieldsValider(validerParams.get("key")));
                            }
                            break;
                        }
                        
                        case "number_compare": {
                            if (validerParams.containsKey("compare_type")) {
                                validContents.add(new NumberCompareValider(validerParams.get("value"), validerParams.get("compare_type")));
                            } else {
                                validContents.add(new NumberCompareValider(validerParams.get("value")));
                            }
                            
                            break;
                        }
                        case "string_contain": {
                            validContents.add(new StringContainValider(validerParams.get("value")));
                            break;
                        }
                        
                        case "string_length": {
                            if (validerParams.containsKey("compare_type")) {
                                validContents.add(new StringLengthValider(validerParams.get("value"), validerParams.get("compare_type")));
                            } else {
                                validContents.add(new StringLengthValider(validerParams.get("value")));
                            }
                            break;
                        }
                        
                        case "string_fetch": {
                            validContents.add(new StringFetcher(validerParams.get("value")));
                            break;
                        }
                        
                        case "obj_fetch_id": {
                            validContents.add(new JsonObjectFetchId(validerParams.get("value")));
                            break;
                        }
                        
                        default: {
                            throw new Exception("Not support valid type for response code:" + validType);
                        }
                    }
                }
            }
            
            // setup browser
            CookieStore browserCookie = browsers.get(0);
            if (json_test_case.has("browser")) {
                int browser = json_test_case.getInt("browser");
                if (browser == 0) {
                    browserCookie = new BasicCookieStore();
                } else if (browser <= browsers.size()) {
                    browserCookie = browsers.get(browser - 1);
                } else {
                    throw new Exception("invalid browser parameter!");
                }
            }
            
            
            
            HttpApiTestCase httpApiTestCase = new HttpApiTestCase(browserCookie, baseUrl);
            httpApiTestCase.setRequestTestCase(uri, httpMethod, params, headerAcceptJson);
            httpApiTestCase.setResponseTestCase(validCode, validContents);
            
            final long startTestCaseTime = System.nanoTime();
            
            try {
                httpApiTestCase.doTest();
            } catch (Exception e) {
                System.out.println("json_test_case:" + json_test_case.toString(4));
                throw e;
            }
            
            System.out.println("time: " + (System.nanoTime() - startTestCaseTime)/1000000. + " ms");
            
            if (json_test_case.has("stop")) {
                if (json_test_case.getInt("stop") == 1) {
                    break;
                }
            }
        }
        
        System.out.println("\n\n\nPASSSS");

        final long duration = System.nanoTime() - startTime;
        
        System.out.println("Duration time: " + duration/1000000000. + " s");
        
    }
    
}
