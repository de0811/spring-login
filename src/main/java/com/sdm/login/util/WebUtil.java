package com.sdm.login.util;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.*;
import java.util.*;

public class WebUtil {

    public static String safePathMerge(String _path_0, String _path_1) {
        if (_path_0.endsWith("/") && _path_1.startsWith("/")) {
            return _path_0 + _path_1.replaceFirst("/", "");
        } else if (_path_0.endsWith("/") || _path_1.startsWith("/")) {
            return _path_0 + _path_1;
        } else {
            return _path_0 + "/" + _path_1;
        }
    }

    private static String extractBodyToJson(HttpServletRequest request) {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
                body = stringBuilder.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return body;
    }

    private static String extractParamteterToJson(HttpServletRequest request) {
        String body = null;
        String parameter = null;
        // TODO Get 방식으로 파라미터 전달시 파라미터를 인식하지 못함.
        {
            for (String name : Collections.<String>list(request.getParameterNames())) {
                String value = request.getParameter(name);
                if (!value.equals("")) {
                    if (parameter == null) {
                        parameter = "{";
                    }
                    parameter += "\"" + name + "\"" + ":" + "\"" + value + "\"" + ",";
                }
            }
            if (parameter != null) {
                parameter = parameter.substring(0, parameter.length() - 1);
                parameter += "}";
            }

            body = parameter;
        }
        return body;
    }

    public static Map<String, Object> extractRequestToMap(HttpServletRequest request) {
        {
            try {
                String body = null;
                //X-WWW-Form-Urlencoded 방식으로 전달 되었더라도 Parameter 값으로 뽑을 수 있어서 우선 진행
                body = extractParamteterToJson(request);
                if (ObjectUtil.isEmpty(body)) { //만약 Json 으로 없을 경우 parameter 값으로 전달 되었는지 확인하여 추출
                    // Body 값을 JSON 값으로 보냈다면 그대로 사용
                    body = extractBodyToJson(request);
                }

                //Json To Map
                // TODO body가 null인 경우 체크 (PathVariable로 파라미터 받을경우 발생한다)
                if (body != null) {
                    Map<String, Object> jsonMap = JsonUtil.jsonToMap(body);
                    if (ObjectUtil.isEmpty(jsonMap)) jsonMap = new HashMap<>();
                    return jsonMap;
                } else {
                    return new HashMap<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        }
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }


    /*
        * 2020-07-09
        * 다중 정렬을 위한 메소드 추가
     */
    public static Sort strSortToSort(String strSort) {
        Sort sort = null;

        if( ObjectUtil.isEmpty(strSort) ) return null;

        String[] arrSort = strSort.split(",");
        String beforeTarget = "";
        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < arrSort.length; i++) {
            if( arrSort[i].equals("DESC") || arrSort[i].equals("ASC") ) {
                if(ObjectUtil.isEmpty(beforeTarget))continue;
                if (arrSort[i].equals("DESC")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, beforeTarget));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.ASC, beforeTarget));
                }
                beforeTarget = "";
            } else {
                if( ObjectUtil.isNotEmpty(beforeTarget) ) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, beforeTarget));
                }
                beforeTarget = arrSort[i];
            }
        }
        if( ObjectUtil.isNotEmpty(beforeTarget) ) {
            orders.add(new Sort.Order(Sort.Direction.DESC, beforeTarget));
        }
        sort = Sort.by(orders);
        return sort;
    }

    public static Pageable getPageable(Integer page, Integer size, String strSort) {
        Pageable pageable = null;
        {
            Sort sort = strSortToSort(strSort);
            if (ObjectUtil.isNotEmpty(page) && ObjectUtil.isNotEmpty(size) && ObjectUtil.isNotEmpty(sort)) {
                pageable = PageRequest.of(page, size, sort);
            } else if (ObjectUtil.isNotEmpty(page) && ObjectUtil.isNotEmpty(size)) {
                pageable = PageRequest.of(page, size);
            }
        }
        return pageable;
    }
}
