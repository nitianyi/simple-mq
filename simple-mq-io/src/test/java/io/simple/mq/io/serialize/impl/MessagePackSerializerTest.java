package io.simple.mq.io.serialize.impl;

import org.junit.Test;

import io.simple.mq.io.serialize.BaseTest;
import io.simple.mq.io.serialize.impl.MessagePackSerializer;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.enums.MsgTypeEnum;
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
	
	@Test
	public void testEnumSerialize() {
		byte[] bytes = serializer.serialize(MsgTypeEnum.PRODUCE);
		log.info("{}", bytes.length);
		
		log.info("{}", serializer.deserialize(bytes, MsgTypeEnum.class));
		
		SimpleMsgRequest msg = SimpleMsgRequest.of("1", "msg", "topic", MsgTypeEnum.PRODUCE);
		bytes = serializer.serialize(msg);
		log.info("{}", bytes.length);
		
		log.info("{}", serializer.deserialize(bytes, SimpleMsgRequest.class));
	}
}
