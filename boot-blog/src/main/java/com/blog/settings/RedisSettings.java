package com.blog.settings;

public class RedisSettings {

    public static final Long EMPTY_TTL = 10L;

    // blog content

    public static final String TAG_PREFIX = "content:tag:";
    public static final Long TAG_TTL = -1L;
    public static final String BLOG_INFO_PREFIX = "content:info:";
    public static final Long BLOG_INFO_TTL = 120L;
    public static final String CONTENT_PREFIX = "content:content:";
    public static final Long CONTENT_TTL = 120L;
    public static final String BLOG_PREFIX = "content:blog:";
    public static final Long BLOG_TTL = 120L;

    public static final String BLOGTMP_PREFIX = "content:review:";
    public static final Long BLOGTMP_TTL = -1L;

    // blog image

    public static final String IMAGE_PREFIX = "user:image:";

    // blog home

    public static final String DESC_PREFIX = "main:desc:";
    public static final Long DESC_TTL = 60L;

    public static final String INFO_PREFIX = "main:info:";
    public static final Long INFO_TTL = 60L;

    // forum user

    public static final String CODE_PREFIX = "user:code:";
    public static final Long CODE_TTL = 240L;

    public static final String TOKEN_PREFIX = "user:token:";
    public static final Long TOKEN_TTL = 259200L;

    public static final String USER_PREFIX = "user:";
    public static final Long USER_TTL = 60L;

    public static final String ADMIN_PREFIX = "user:admin:";

    // user comment

    public static final String USER_CMT_PREFIX = "user:comment:";
    public static final Long USER_CMT_TTL = 7200L;
}
