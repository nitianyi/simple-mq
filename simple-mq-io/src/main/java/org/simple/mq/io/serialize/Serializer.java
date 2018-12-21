package org.simple.mq.io.serialize;

/**
 * 序列化接口
 * @author Tony
 *
 */
public interface Serializer {

	<T> byte[] serialize(T t);
	
	<T> T deserialize(byte[] bytes, Class<T> clazz);
}
