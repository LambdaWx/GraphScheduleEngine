package org.lambdawx.GraphScheduleEngine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = Logger.getLogger(JsonUtils.class);
    
    public static String getJson(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            return "{}";
        }
    }

    public static byte[] getJsonBytes(Object o) {
        try {
            return MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            return new byte[0];
        }
    }


    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            if(StringUtils.isEmpty(json)) {
                return null;
            } else {
                MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return MAPPER.readValue(json, clazz);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static List getDTOArray(String jsonString, Class clazz){
    	JSONArray array = JSONArray.fromObject(jsonString);
    	List list = new ArrayList();
    	for(int i=0; i<array.size(); i++){
    		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		list.add(JSONObject.toBean(array.getJSONObject(i), clazz)); 
    	}
    	return list;
    	} 
}
