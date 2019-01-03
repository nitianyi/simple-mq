package io.ztz.simple.mq.codec.server;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.simple.mq.io.serialize.impl.ProtostuffSerializer;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<SimpleMsgResponse> {

	// TODO 
	Objenesis objenesis = new ObjenesisStd(true);
	ProtostuffSerializer serializer = objenesis.newInstance(ProtostuffSerializer.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, SimpleMsgResponse msg, ByteBuf out) throws Exception {
		log.debug("encoder receives msg is ->{}", msg);
		
		try {
			byte[] b = serializer.serialize(msg);
			out.writeBytes(b);
			log.debug("Msg resp ->{}->length->{} has been sent successfully", msg.getMsgId(), b.length);
		} catch (Exception e) {
			log.error("error when parsing message pack ->{}, cause ->{}", msg, e);
			e.printStackTrace();
		}
	}

}
