package ie.dbs.moviesapi.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tools {

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array){
        List<Object> list = new ArrayList<Object>();
        try{
            for(int i = 0; i < array.length(); i++) {
                Object value = array.get(i);
                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);
            }
        } catch (Exception ex){
            Log.e("Exception",ex.getMessage());
        }
        return  list;
    }
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    public static String formatDate(String date) {

        SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date oldDate = null;

        try {
            oldDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateString = outputFormat.format( oldDate );

        return dateString;
    }

}
