package org.simple.mq.io.serialize.impl;

import org.simple.mq.io.serialize.Serializer;
import org.springframework.stereotype.Service;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Protostuff implementation
 * @author Tony
 *
 */
@Service
public class ProtostuffSerializer implements Serializer {

	@Override
	public <T> byte[] serialize(T t) {
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		
		try {
			@SuppressWarnings("unchecked")
			Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(t.getClass());
			
			return ProtostuffIOUtil.toByteArray(t, schema, buffer);
		} catch (Throwable e) {
			throw e;
		} finally {
			buffer.clear();
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(clazz);
		T t = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(bytes, t, schema);
		return t;
	}

}
