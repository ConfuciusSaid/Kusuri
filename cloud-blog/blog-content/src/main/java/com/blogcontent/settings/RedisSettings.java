package com.blogcontent.settings;

public class RedisSettings {
    public static final String TAG_PREFIX = "content:tag:";
    public static final Long TAG_TTL = -1L;
    public static final String INFO_PREFIX = "content:info:";
    public static final Long INFO_TTL = 120L;
    public static final String CONTENT_PREFIX = "content:content:";
    public static final Long CONTENT_TTL = 120L;
    public static final String BLOG_PREFIX = "content:blog:";
    public static final Long BLOG_TTL = 120L;

    public static final String BLOGTMP_PREFIX = "content:review:";
    public static final Long BLOGTMP_TTL = -1L;

    public static final Long EMPTY_TTL = 10L;
}
