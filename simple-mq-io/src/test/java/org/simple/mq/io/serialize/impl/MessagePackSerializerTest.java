package org.simple.mq.io.serialize.impl;

import org.junit.Test;
import org.simple.mq.io.serialize.BaseTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessagePackSerializerTest extends BaseTest {

	MessagePackSerializer serializer = objenesis.newInstance(MessagePackSerializer.class);
	
	@Test
	public void testMessagePackSerializer() {
		Nothing nothing = new Nothing("noting", "nobody", 102);
		log.info("{}", nothing);
		
		byte[] bytes = serializer.serialize(nothing);
		log.info("{}", bytes.length);
		
		nothing = serializer.deserialize(bytes, Nothing.class);
		log.info("{}", nothing);
	}
}
