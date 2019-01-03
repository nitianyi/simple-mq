package io.simple.mq.io.serialize.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.msgpack.MessagePack;
import org.springframework.stereotype.Service;

import io.simple.mq.io.serialize.Serializer;

/**
 * Message pack implementation
 * @author Tony
 *
 */
@Service
public class MessagePackSerializer implements Serializer {

	private static final MessagePack msgPack = new MessagePack();
	private static final ConcurrentMap<Class<?>, Integer> regMap = new ConcurrentHashMap<>();
	
	@Override
	public <T> byte[] serialize(T t) {
		register(t.getClass());
		
		try {
			return msgPack.write(t);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	void register(Class<?> clazz) {
		if (!regMap.containsKey(clazz)) {
			msgPack.register(clazz);
			regMap.put(clazz, 0);
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		try {
			return msgPack.read(bytes, clazz);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
