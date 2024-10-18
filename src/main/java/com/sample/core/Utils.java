package com.sample.core;

import java.util.Map;

public class Utils {
    public static String stringify(Map input) {
        String output = "map = [";
        for (Object key : input.keySet()) {
            output = output.concat("\"" + key + "\"=\"" + input.get(key) + "\"");
        }
        return output + "]";
    }
}
