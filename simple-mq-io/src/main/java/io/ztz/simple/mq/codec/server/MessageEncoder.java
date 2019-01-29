package io.ztz.simple.mq.codec.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.io.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<SimpleMsgResponse> {

	/**
	 * 构造方法
	 * @param serializer 序列化实现实例
	 */
	public MessageEncoder(Serializer serializer) {
		this.serializer = serializer;
	}

	private final Serializer serializer;
	
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
