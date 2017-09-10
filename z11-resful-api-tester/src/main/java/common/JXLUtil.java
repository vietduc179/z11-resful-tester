package common;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import z11.F_ile;
import org.json.JSONArray;
import org.json.JSONObject;

public class JXLUtil {
    
    public static Sheet getSheet(File file, int sheetNo) throws Exception {
        if ((file.exists()) && (file.length() > 0L)) {
            Workbook w = Workbook.getWorkbook(file);
            Sheet sheet = w.getSheet(sheetNo-1);
            return sheet;
        }
        throw new Exception();
    }
    
    public static Sheet[] getSheets(File file) throws Exception {
        if ((file.exists()) && (file.length() > 0L)) {
            Workbook w = Workbook.getWorkbook(file);
            return w.getSheets();
        }
        throw new Exception();
    }
    
    
    public static JSONObject exportJson(String file) throws Exception {
        return exportJson(new File(file));
    }
    
    public static JSONObject exportJson(File file) throws Exception {
        Sheet[] sheets = getSheets(file);
        JSONObject jsonobject_test_case_file = new JSONObject();
        
        jsonobject_test_case_file.put("base_url", sheets[0].getCell(0, 1).getContents());
        jsonobject_test_case_file.put("browsers", Integer.parseInt(sheets[0].getCell(1, 1).getContents()));
        
        JSONObject demo_data_obj = new JSONObject();

        for (int pi = 2; pi < sheets[0].getRows(); pi++) {
            String key = sheets[0].getCell(2, pi).getContents();
            String value = sheets[0].getCell(3, pi).getContents();
            if (key.trim().length() == 0) continue;
            demo_data_obj.put(key, value);
        }

        jsonobject_test_case_file.put("demo_data", demo_data_obj);
        jsonobject_test_case_file.put("headers", new JSONArray(sheets[0].getCell(4, 1).getContents()));
        
        JSONArray jsonarray_test_cases = new JSONArray();
        for (int sheet_i = 1; sheet_i < sheets.length; sheet_i++) {
            Sheet sheet = sheets[sheet_i];
            System.out.println("exportJson, reading sheet: " + sheet.getName());
            
            for (int row_i = 1; row_i < sheet.getRows(); row_i++) {
                if (sheet.getCell(0, row_i).getContents().trim().length() == 0) {
                    continue;
                }
                
                System.out.println("exportJson, reading row: " + (row_i + 1));
                JSONObject jsonobject_test_case = new JSONObject();
                jsonobject_test_case.put("id", "sheet:" + sheet.getName() + ", row:" + (row_i+1));
                jsonobject_test_case.put("uri", sheet.getCell(0, row_i).getContents());
                jsonobject_test_case.put("method", sheet.getCell(1, row_i).getContents());
                jsonobject_test_case.put("msg", sheet.getCell(2, row_i).getContents());
                
                if (sheet.getCell(3, row_i).getContents().trim().length() >= 2) {
                    jsonobject_test_case.put("params", new JSONObject(sheet.getCell(3, row_i).getContents()));
                } else {
                    jsonobject_test_case.put("params", new JSONObject());
                }
                
                if (sheet.getCell(4, row_i).getContents().trim().length() >= 2) {
                    String content = sheet.getCell(4, row_i).getContents();
                    if (content.startsWith("{")) {
                        jsonobject_test_case.put("valid_code", new JSONObject(content));
                    } else if (content.startsWith("!")) {
                        String valueS = content.replace("!", "");
                        int value = Integer.parseInt(valueS);
                        String json = "{\"type\": \"number_compare\",\"params\": {\"compare_type\": \"NE\", \"value\": " + value + "}}";
                        jsonobject_test_case.put("valid_code", new JSONObject(json));
                    } else {
                        int value = Integer.parseInt(content);
                        String json = "{\"type\": \"number_compare\",\"params\": {\"value\": " + value + "}}";
                        jsonobject_test_case.put("valid_code", new JSONObject(json));
                    }
                } else {
                    jsonobject_test_case.put("valid_code", new JSONObject());
                }
                
                if (sheet.getCell(5, row_i).getContents().trim().length() >= 2) {
                    String content = sheet.getCell(5, row_i).getContents();
                    
                    if (content.startsWith("{")) {
                        JSONObject validObj = new JSONObject(content);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("num:")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "number_compare");
                        JSONObject validParams = new JSONObject();
                        validParams.put("value", content.replace("num:", "").trim());
                        validObj.put("params", validParams);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("number")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "is_number");
                        validObj.put("params", new JSONObject());
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("jsonobject")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "is_json_object");
                        validObj.put("params", new JSONObject());
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("jsonarray")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "is_json_array");
                        validObj.put("params", new JSONObject());
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("jsonarrlength:")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "json_array_length");
                        JSONObject validParams = new JSONObject();
                        validParams.put("length", content.replace("jsonarrlength:", "").trim());
                        validObj.put("params", validParams);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("string:")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "string_contain");
                        JSONObject validParams = new JSONObject();
                        validParams.put("value", content.replace("string:", "").trim());
                        validObj.put("params", validParams);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("fetch:")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "string_fetch");
                        JSONObject validParams = new JSONObject();
                        validParams.put("value", content.replace("fetch:", "").trim());
                        validObj.put("params", validParams);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else if (content.startsWith("objfetchid:")) {
                        JSONObject validObj = new JSONObject();
                        validObj.put("type", "obj_fetch_id");
                        JSONObject validParams = new JSONObject();
                        validParams.put("value", content.replace("objfetchid:", "").trim());
                        validObj.put("params", validParams);
                        JSONArray validArray = new JSONArray();
                        validArray.put(validObj);
                        jsonobject_test_case.put("valid_content", validArray);
                    } else {
                        jsonobject_test_case.put("valid_content", new JSONArray(content));
                    }
                    
                } else {
                    jsonobject_test_case.put("valid_content", new JSONArray());
                }
                
                if (sheet.getCell(6, row_i).getContents().trim().length() >= 1) {
                    int br = Integer.parseInt(sheet.getCell(6, row_i).getContents());
                    jsonobject_test_case.put("browser", br);
                } else {
                    jsonobject_test_case.put("browser", 1);
                }
                if (sheet.getCell(7, row_i).getContents().trim().length() >= 1) {
                    jsonobject_test_case.put("stop", Integer.parseInt(sheet.getCell(7, row_i).getContents()));
                } else {
                    jsonobject_test_case.put("stop", 0);
                }
                
                if (sheet.getCell(8, row_i).getContents().trim().length() >= 1) {
                    jsonobject_test_case.put("enable", Integer.parseInt(sheet.getCell(8, row_i).getContents()));
                } else {
                    jsonobject_test_case.put("enable", 1);
                }
                
                if (sheet.getCell(9, row_i).getContents().trim().length() >= 1) {
                    jsonobject_test_case.put("oneshot", Integer.parseInt(sheet.getCell(9, row_i).getContents()));
                } else {
                    jsonobject_test_case.put("oneshot", 0);
                }
                //System.out.println("oneshot = " + jsonobject_test_case.getInt("oneshot"));
        	jsonarray_test_cases.put(jsonobject_test_case);
            }
        
        }
        
        jsonobject_test_case_file.put("test_case", jsonarray_test_cases);
        
        return jsonobject_test_case_file;
        
    }
    
    public static void importJson(String file, String excelFile) throws Exception {
        importJson(new File(file), excelFile);
    }

    public static void importJson(File file, String excelFile) throws Exception {

        WritableFont wfontStatus = new WritableFont(WritableFont.COURIER, 10);
        wfontStatus.setColour(Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
        cellFormat.setWrap(true);
        cellFormat.setAlignment(Alignment.LEFT);
        cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        WritableFont wfontStatus2 = new WritableFont(WritableFont.COURIER, 10);
        wfontStatus2.setColour(Colour.BLACK);
        wfontStatus2.setBoldStyle(WritableFont.BOLD);
        
        WritableCellFormat cellFormatHeader = new WritableCellFormat(wfontStatus2);
        cellFormatHeader.setWrap(true);
        cellFormatHeader.setAlignment(Alignment.CENTRE);
        cellFormatHeader.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormatHeader.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        
        WritableSheet sheet0;
        WritableWorkbook workbook;
        workbook = Workbook.createWorkbook(new File(excelFile));
        WritableSheet sheetMeta = workbook.createSheet("meta", 0);
        WritableSheet sheetTestcase1 = workbook.createSheet("testcase1", 1);
        
        JSONObject jsonobject_test_case_file = new JSONObject(F_ile.getContentOfFile(file));

        sheetMeta.addCell(new Label(0, 0, "base_url", cellFormatHeader));
        sheetMeta.addCell(new Label(0, 1, jsonobject_test_case_file.getString("base_url"), cellFormat));
        
        sheetMeta.addCell(new Label(1, 0, "browsers", cellFormatHeader));
        sheetMeta.addCell(new Label(1, 1, "" + jsonobject_test_case_file.getInt("browsers"), cellFormat));

        sheetMeta.addCell(new Label(2, 0, "demo_data", cellFormatHeader));
        sheetMeta.addCell(new Label(2, 1, "" + jsonobject_test_case_file.getJSONObject("demo_data").toString(4), cellFormat));
        
        sheetMeta.addCell(new Label(3, 0, "headers", cellFormatHeader));
        sheetMeta.addCell(new Label(3, 1, "" + jsonobject_test_case_file.getJSONArray("headers").toString(4), cellFormat));
        
        sheetMeta.addCell(new Label(4, 0, "validers", cellFormatHeader));
        sheetMeta.addCell(new Label(4, 1, "" + jsonobject_test_case_file.getJSONArray("validers").toString(4), cellFormat));
        
        JSONArray json_array_testcase = jsonobject_test_case_file.getJSONArray("test_case");
        
        sheetTestcase1.addCell(new Label(0, 0, "uri", cellFormatHeader));
        sheetTestcase1.addCell(new Label(1, 0, "method", cellFormatHeader));
        sheetTestcase1.addCell(new Label(2, 0, "msg", cellFormatHeader));
        sheetTestcase1.addCell(new Label(3, 0, "params", cellFormatHeader));
        sheetTestcase1.addCell(new Label(4, 0, "valid_code", cellFormatHeader));
        sheetTestcase1.addCell(new Label(5, 0, "valid_content", cellFormatHeader));
        sheetTestcase1.addCell(new Label(6, 0, "browsers", cellFormatHeader));
        sheetTestcase1.addCell(new Label(7, 0, "stop", cellFormatHeader));
        
        
        for (int testcase_i = 0; testcase_i < json_array_testcase.length(); testcase_i++) {
            JSONObject json_testcase = json_array_testcase.getJSONObject(testcase_i);
            
            if (!json_testcase.has("msg")) {
                json_testcase.put("msg", "");
            }
            if (!json_testcase.has("browsers")) {
                json_testcase.put("browsers", "1");
            }
            if (!json_testcase.has("stop")) {
                json_testcase.put("stop", "0");
            }
            
            sheetTestcase1.addCell(new Label(0, testcase_i + 1, json_testcase.getString("uri"), cellFormat));
            sheetTestcase1.addCell(new Label(1, testcase_i + 1, json_testcase.getString("method"), cellFormat));
            sheetTestcase1.addCell(new Label(2, testcase_i + 1, json_testcase.getString("msg"), cellFormat));
            sheetTestcase1.addCell(new Label(3, testcase_i + 1, json_testcase.getJSONObject("params").toString(4), cellFormat));
            sheetTestcase1.addCell(new Label(4, testcase_i + 1, json_testcase.getJSONObject("valid_code").toString(4), cellFormat));
            sheetTestcase1.addCell(new Label(5, testcase_i + 1, json_testcase.getJSONArray("valid_content").toString(4), cellFormat));
            sheetTestcase1.addCell(new Label(6, testcase_i + 1, json_testcase.getString("browsers"), cellFormat));
            sheetTestcase1.addCell(new Label(7, testcase_i + 1, "" + json_testcase.getInt("stop"), cellFormat));
        }
       
        workbook.write();
        workbook.close();

    }
    
    
    public static void main(String []arg) throws Exception{
    	JSONObject jsonobject_test_case_file = exportJson("/Users/danny/Desktop/testcase-z11-authapi-users.xls");
        System.out.println(jsonobject_test_case_file.toString(4));
        //importJson("/Users/danny/Desktop/auth_api_test_case.json", "/Users/danny/Desktop/testcase-z11-authapi-users1.xls");
    }
    
}
