package org.ztz.simple.mq.client.transport;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.simple.mq.io.serialize.impl.ProtostuffSerializer;
import org.ztz.simple.mq.api.dto.SimpleMsgRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<SimpleMsgRequest> {

	// TODO 
	Objenesis objenesis = new ObjenesisStd(true);
	ProtostuffSerializer serializer = objenesis.newInstance(ProtostuffSerializer.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, SimpleMsgRequest msg, ByteBuf out) throws Exception {
		log.debug("received msg is ->{}", msg);
		
		try {
			byte[] b = serializer.serialize(msg);
			out.writeBytes(b);
			log.debug("Msg ->{}->length->{} has been sent successfully", msg.getMsgId(), b.length);
		} catch (Exception e) {
			log.error("error when parsing message pack ->{}, cause ->{}", msg, e);
			e.printStackTrace();
		}
	}

}
