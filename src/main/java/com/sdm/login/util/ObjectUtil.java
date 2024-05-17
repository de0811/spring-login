package com.sdm.login.util;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectUtil {
    public static boolean isEmpty(Object obj) {
        if(obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if(obj instanceof List) return obj == null || ((List)obj).isEmpty();
        else if(obj instanceof Map) return obj == null || ((Map)obj).isEmpty();
        else if(obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static Map objectToMap(Object obj){ try { //Field[] fields = obj.getClass().getFields(); //private field는 나오지 않음.
        Field[] fields = obj.getClass().getDeclaredFields();
        Map resultMap = new HashMap();
        for(int i=0; i<=fields.length-1;i++){
            fields[i].setAccessible(true);
            resultMap.put(fields[i].getName(), fields[i].get(obj));
        }
        return resultMap;
    } catch (IllegalArgumentException e) { e.printStackTrace(); }
    catch (IllegalAccessException e) { e.printStackTrace(); }
        return null;
    }

    public static Object mapToObject(Map map, Object objClass){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();
        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            try {
                Method[] methods = objClass.getClass().getDeclaredMethods();
                for(int i=0;i<=methods.length-1;i++){
                    if(methodString.equals(methods[i].getName())){
                        System.out.println("invoke : "+methodString);
                        methods[i].invoke(objClass, map.get(keyAttribute));
                    }
                }
            } catch (SecurityException e) { e.printStackTrace(); }
            catch (IllegalAccessException e) { e.printStackTrace(); }
            catch (IllegalArgumentException e) { e.printStackTrace(); }
            catch (InvocationTargetException e) { e.printStackTrace(); } }
        return objClass;
    }

    public static Boolean objectToBoolean(Object ob){
        Boolean bo = null;
        if(isEmpty(ob)) return bo;
        if( ob instanceof String )          bo = Boolean.parseBoolean((String) ob);
        else if( ob instanceof Boolean )       bo = ((Boolean) ob);
        return bo;
    }

    public static Long objectToLong(Object ob){
        Long lo = null;
        if(isEmpty(ob)) return lo;
        if( ob instanceof String )          lo = Long.parseLong((String) ob);
        else if( ob instanceof Long )       lo = ((Long) ob);
        else if( ob instanceof Integer )    lo = ((Integer)ob).longValue();
        else if( ob instanceof Double )     lo = ((Double) ob).longValue();
        return lo;
    }

    public static Integer objectToInteger(Object ob){
        Integer in = null;
        if( isEmpty(ob) ) return in;
        if( ob instanceof String )          in = Integer.parseInt((String) ob);
        else if( ob instanceof Long )       in = ((Long) ob).intValue();
        else if( ob instanceof Integer )    in = ((Integer) ob);
        else if( ob instanceof Double )     in = ((Double) ob).intValue();
        return in;
    }

    public static String objectToString(Object ob){
        String st = null;
        if( isEmpty(ob) ) return st;
        if( ob instanceof String )          st = ((String) ob);
        else if( ob instanceof Long )       st = ((Long) ob).toString();
        else if( ob instanceof Integer )    st = ((Integer) ob).toString();
        else if( ob instanceof Double )     st = ((Double) ob).toString();
        return st;
    }

}
