package com.sageburner.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ryan on 10/30/2015.
 */
public class ApiUtils {
    /**
     *
     * @param input
     * @return
     */
    public static String convertToBase64(String input) {
        byte[] plainInputBytes = input.getBytes();
        byte[] base64InputBytes = Base64Utils.encode(plainInputBytes);
        return new String(base64InputBytes);
    }

    public static String toJSON(Object input) {
        ObjectMapper mapper = new ObjectMapper();

        //Object to JSON in String
        String json = new String();
        try {
            json =  mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Object fromJson(String input, String className) {
        ObjectMapper mapper = new ObjectMapper();

        //Object to JSON in String
        Object outputObject = new String();

        try {
            outputObject = mapper.readValue(input, Class.forName(className));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return outputObject;
    }
}
