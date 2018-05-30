package ro.mindit.todo.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {


    /**
     * Extracts the JSON from the request as a Map
     * @param request
     * @return
     */
    public static Map<String,String> getJSONFromRequest(HttpServletRequest request) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        Map<String,Object> myMap = new HashMap<String, Object>();
        Map<String,String> stringMap = new HashMap<String, String>();
        System.out.println("In JSON parser");
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
            return stringMap;
        }
        System.out.println("String is " + jb.toString());
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            myMap = (Map<String, Object>) objectMapper.readValue(jb.toString().getBytes(), HashMap.class).get("params");

            if (myMap.containsKey("id")) {
                myMap.put("id", myMap.get("id") + "");
            }

            for (String key : myMap.keySet()) {
                stringMap.put(key, (String) myMap.get(key));
            }

            System.out.println("Returning a map that is " + myMap.toString());
            return stringMap;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Exception occured!");
        return stringMap;
    }

}
