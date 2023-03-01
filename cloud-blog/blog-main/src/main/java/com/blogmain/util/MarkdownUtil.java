package com.blogmain.util;

public class MarkdownUtil {
    public static String defend(String markdown) {
        //return markdown.replaceAll("<[\\s\\S]+>[\\s\\S]+</[\\s\\S]+>", "");
        //        return markdown.replaceAll("<","&lt;")
        //                .replaceAll(">","&gt;");
        return markdown.replaceAll("<[\\s\\S]+>", "");
    }
}
