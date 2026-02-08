package com.github.nautic.core.utils;

public final class URLUtil {

    public static String normalize(String input) {
        if (!input.startsWith("http://") && !input.startsWith("https://")) {
            return "https://" + input;
        }
        return input;
    }

    public static String extractDomain(String url) {
        String clean = url
                .replace("https://", "")
                .replace("http://", "");

        int slash = clean.indexOf('/');
        return (slash == -1 ? clean : clean.substring(0, slash)).toLowerCase();
    }
}
