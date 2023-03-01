package com.blogcontent.util;

public class MarkdownUtil {
    public static String defend(String markdown) {
//        return markdown.replaceAll("<", "&lt;")
//                .replaceAll(">", "&gt;");
        return markdown.replaceAll("<[\\s\\S]+>", "");
    }
}
