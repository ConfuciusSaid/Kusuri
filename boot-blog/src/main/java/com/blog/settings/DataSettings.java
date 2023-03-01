package com.blog.settings;

public class DataSettings {

    // blog content

    public static final Long BLOG_TITLE = 4096L;
    public static final Long BLOG_CONTENT = 65536L;
    public static final Long BLOG_COUNT = 20L;
    public static final Long REVIEW_COUNT = 3L;
    public static final Long TAG_NAME = 32L;
    public static final Long TAG_COUNT = 10L;

    // blog image

    //public static final String IMAGE_PATH = "C:\\Users\\32124\\Desktop\\images\\";
    public static final String IMAGE_PATH = "/data/images/";
    public static final String SERVER_IMAGE_PATH = "http://www.kusuri.world/images/";
    public static final Long IMAGE_MAX = 10485760L;
    public static final Long IMAGE_COUNT = 10L;

    // blog home

    public static final Long DESC_MARKDOWN = 8192L;
    public static final Long INFO_MARKDOWN = 4096L;

    public static final Long DESC_COUNT = 20L;
    public static final Long INFO_COUNT = 5L;

    // user comment

    public static final Long USER_CMT_MARKDOWN = 2048L;
    public static final Long USER_CMT_COUNT = 15L;
}
