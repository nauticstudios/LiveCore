package com.github.nautic.velocity.utils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class YamlUtil {

    private YamlUtil() {}

    public static Map<String, Object> getSection(Map<String, Object> root, String key) {
        Object o = root.get(key);
        return o instanceof Map ? (Map<String, Object>) o : null;
    }

    public static String getString(Map<String, Object> root, String key) {
        Object o = root.get(key);
        return o instanceof String ? (String) o : null;
    }

    public static int getInt(Map<String, Object> root, String key, int def) {
        Object o = root.get(key);
        return o instanceof Number ? ((Number) o).intValue() : def;
    }

    public static List<String> getStringList(Map<String, Object> root, String key) {
        Object o = root.get(key);
        return o instanceof List ? (List<String>) o : null;
    }
}
