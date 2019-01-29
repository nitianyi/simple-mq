package io.ztz.simple.mq.codec.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.io.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<SimpleMsgRequest> {

	/**
	 * 构造方法
	 * @param serializer 序列化实现实例
	 */
	public MessageEncoder(Serializer serializer) {
		this.serializer = serializer;
	}

	private final Serializer serializer;
	
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
