package io.simple.mq.io.serialize.impl;

import org.junit.Test;

import io.simple.mq.io.serialize.BaseTest;
import io.simple.mq.io.serialize.impl.ProtostuffSerializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtostuffSerializerTest extends BaseTest {

	ProtostuffSerializer serializer = objenesis.newInstance(ProtostuffSerializer.class);
	
	@Test
	public void testProtostuffSerializer() {
		Nothing nothing = new Nothing("foo", "bar", 100);
		log.info("{}", nothing);
		
		byte[] data = serializer.serialize(nothing);
		log.info("{}", data.length);
		
		nothing = serializer.deserialize(data, Nothing.class);
		log.info("{}", nothing);
	}
	
}
