package com.cyl.musicapi;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by cjh on 2017/5/11.
 */

public class TypedInput implements Serializable {
    private String functionName;
    private Map<String,Object> parameters;

    public TypedInput(String functionName, Map<String,Object> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }


    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
//
//    public TestData getParameters() {
//        return parameters;
//    }
//
//    public void setParameters(TestData parameters) {
//        this.parameters = parameters;
//    }
}
