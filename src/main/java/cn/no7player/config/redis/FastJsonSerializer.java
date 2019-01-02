/*
 * Copyright 2017 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.no7player.config.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * FastJsonSerializer.java
 *
 * @author xuxiaowen
 */
public class FastJsonSerializer<T> implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return null;
        }
        try {
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }

    }

    @Override
    public T deserialize(byte[] data) throws SerializationException {
        if ((data == null || data.length == 0)) {
            return null;
        }
        try {
            return (T) JSON.parse(data);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

}
