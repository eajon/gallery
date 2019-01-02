/*
 * Copyright 2017 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.no7player.config.redis;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * RedisCaches.java
 *
 * @author xuxiaowen
 */
public enum RedisCaches {
    PHOENIX_CACHE(0), PHOENIX_STATISTIC_CACHE(1800);

    private long expiration;

    /**
     * @param expiration
     */
    RedisCaches(long expiration) {
        this.expiration = expiration;
    }

    @SuppressWarnings("serial")
    public static Map<String, Long> mapValues() {
        final EnumSet<RedisCaches> rcts = EnumSet.allOf(RedisCaches.class);
        return new HashMap<String, Long>(rcts.size()) {
            {
                for (RedisCaches rct : rcts) {
                    put(rct.toString(), rct.getExpiration());
                }
            }
        };
    }

    public long getExpiration() {
        return expiration;
    }

}
