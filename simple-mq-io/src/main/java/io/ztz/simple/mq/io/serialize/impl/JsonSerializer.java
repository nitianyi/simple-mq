package io.ztz.simple.mq.io.serialize.impl;

import java.nio.charset.Charset;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import io.ztz.simple.mq.io.serialize.Serializer;
/**
 * Json implementation
 * @author Tony
 *
 */
@Service
public class JsonSerializer implements Serializer {

	private static final Charset utf8 = Charset.forName("UTF-8");
	
	@Override
	public <T> byte[] serialize(T t) {
		String objstr = JSON.toJSONString(t);
		return objstr.getBytes(utf8);
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		String jsonStr = new String(bytes, utf8);
		return JSON.parseObject(jsonStr, clazz);
	}

}
