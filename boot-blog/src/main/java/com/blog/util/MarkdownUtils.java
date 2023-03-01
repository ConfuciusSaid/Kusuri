package com.blog.util;

public class MarkdownUtils {
    public static String defend(String markdown) {
//        return markdown.replaceAll("<", "&lt;")
//                .replaceAll(">", "&gt;");
        return markdown.replaceAll("<[\\s\\S]+>", "");
    }
}
