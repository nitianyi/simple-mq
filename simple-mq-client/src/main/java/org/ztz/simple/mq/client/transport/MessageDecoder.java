package org.ztz.simple.mq.client.transport;

import java.util.List;

import org.simple.mq.io.serialize.impl.ProtostuffSerializer;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.ztz.simple.mq.api.dto.SimpleMsgResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

	// TODO 
	Objenesis objenesis = new ObjenesisStd(true);
	ProtostuffSerializer serializer = objenesis.newInstance(ProtostuffSerializer.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int length = in.readableBytes();
		log.debug("client decoder receives msg length ->{}", length);
		byte[] b = new byte[length];
		in.readBytes(b);
		try {
			SimpleMsgResponse msg = serializer.deserialize(b, SimpleMsgResponse.class);
			
			log.debug("parsed msg resp ->{} sucessfully", msg);
			out.add(msg);
		} catch (Throwable e) {
			log.error("error when parsing msg, cause is ->{}", e.getMessage());
			e.printStackTrace();
		}
	}

}