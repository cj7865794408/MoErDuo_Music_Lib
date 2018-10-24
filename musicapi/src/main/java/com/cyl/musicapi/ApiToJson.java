package com.cyl.musicapi;


import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjh on 2017/5/11.
 */

public class ApiToJson {

    public static TypedInput getNewParmData(String paramKey,Object paramData, String apiName) {


        return getParmData(new String[]{paramKey},new Object[]{paramData},apiName);
    }
    public static TypedInput getParmData(String[] paramKey, Object[] paramValue, String apiName) {
        Map<String, Object> iData = httPutParam(paramKey, paramValue);
        if (iData == null)
            iData = new HashMap<>();
        TypedInput type = new TypedInput(apiName, iData);
        return type;
    }

    /**
     * 方法说明：保存Url参数 方法名称：httPutParam
     *
     * @param paramKey
     * @param paramValue
     */
    public static Map<String, Object> httPutParam(String[] paramKey, Object[] paramValue) {
        Map<String, Object> paramsData = new HashMap<>();
        if (paramKey == null || paramValue == null) {
            return null;
        }
        for (int i = 0; i < paramKey.length; i++) {
            String key = paramKey[i];
            if (paramValue[i] != null) {
                String val = String.valueOf(paramValue[i]);
                if (!TextUtils.isEmpty(val)) {
                        paramsData.put(key, Arrays.asList(paramValue[i]));
                }
            }
        }
        return paramsData;
    }
}
