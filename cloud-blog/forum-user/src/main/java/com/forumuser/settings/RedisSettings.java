package com.forumuser.settings;

public class RedisSettings {
    public static final String CODE_PREFIX = "user:code:";
    public static final Long CODE_TTL = 240L;

    public static final String TOKEN_PREFIX = "user:token:";
    public static final Long TOKEN_TTL = 259200L;

    public static final String USER_PREFIX = "user:";
    public static final Long USER_TTL = 60L;

    public static final String ADMIN_PREFIX = "user:admin:";

    public static final Long EMPTY_TTL = 10L;
}
