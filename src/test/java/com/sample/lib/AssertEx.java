package com.sample.lib;

import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class AssertEx {
    public static void assertMapEquals(Map expected, Map actual, String message) {
        boolean failed = false;
        String output = message;
        for (Object key : expected.keySet()) {
            if (!actual.containsKey(key)) {
                failed = true;
                message = message.concat(String.format("\nExpected key of \"%s\" wasn't found in actual results", key));
            } else if (!expected.get(key).equals(actual.get(key))) {
                failed = true;
                message = message.concat(
                    String.format("\nExpected value of \"%s\" key doesn't match actual results."
                            + " Expected: \"%s\". Actual: \"%s\"",
                            key,
                            expected.get(key),
                            actual.get(key)));
            }
        }
        for (Object key : actual.keySet()) {
            if (!expected.containsKey(key)) {
                failed = true;
                message = message.concat(String.format("\nActual results map has an extra entry of [\"%s\", \"%s\"]",
                        key,
                        actual.get(key))
                );
            }
        }
        if (failed) {
            Assertions.fail(message);
        }
    }
}
