package com.sdm.login.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {
  public static Map<String, Object> jsonToMap(String json) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
    return map;
  }

  public static boolean isJson(String json) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(json);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}
