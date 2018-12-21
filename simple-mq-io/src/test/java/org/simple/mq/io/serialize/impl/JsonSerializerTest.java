package org.simple.mq.io.serialize.impl;

import org.junit.Test;
import org.simple.mq.io.serialize.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Tony
 *
 */
@Slf4j
public class JsonSerializerTest extends BaseTest {

	JsonSerializer serializer = objenesis.newInstance(JsonSerializer.class);
	
	@Test
	public void testJsonSerializer() {
		Nothing nothing = new Nothing("noting", "nobody", 101);
		log.info("{}", nothing);
		
		byte[] bytes = serializer.serialize(nothing);
		log.info("{}", bytes.length);
		
		nothing = serializer.deserialize(bytes, Nothing.class);
		log.info("{}", nothing);
	}
}
