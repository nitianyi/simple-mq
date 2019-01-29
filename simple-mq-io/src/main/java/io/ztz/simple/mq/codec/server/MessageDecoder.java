package io.ztz.simple.mq.codec.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.io.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

	/**
	 * 构造方法
	 * @param serializer 序列化实现实例
	 */
	public MessageDecoder(Serializer serializer) {
		this.serializer = serializer;
	}

	private final Serializer serializer;
	
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
