package io.ztz.simple.mq.server.codec;

import java.util.List;

import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.simple.mq.io.serialize.impl.ProtostuffSerializer;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

	// TODO 
	Objenesis objenesis = new ObjenesisStd(true);
	ProtostuffSerializer serializer = objenesis.newInstance(ProtostuffSerializer.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int length = in.readableBytes();
		log.debug("received msg length ->{}", length);
		byte[] b = new byte[length];
		in.readBytes(b);
		try {
			SimpleMsgRequest msg = serializer.deserialize(b, SimpleMsgRequest.class);
			
			log.debug("parsed msg ->{} sucessfully", msg);
			out.add(msg);
		} catch (Throwable e) {
			log.error("error when parsing msg, cause is ->{}", e.getMessage());
			e.printStackTrace();
		}
	}

}
