package org.ztz.simple.mq.client.transport;

import java.io.ByteArrayOutputStream;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ztz.simple.mq.api.dto.SimpleMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<SimpleMsg> {

	private MessagePack msgPack = new MessagePack();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, SimpleMsg msg, ByteBuf out) throws Exception {
		LOGGER.debug("received msg is ->{}", msg);
		
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			Packer packer = msgPack.createPacker(bos);
			packer.write(msg);
			
			out.writeBytes(bos.toByteArray());
			LOGGER.debug("Msg ->{} has been sent successfully");
		} catch (Exception e) {
			LOGGER.error("error when parsing message pack ->{}, cause ->{}", msg, e);
		}
	}

}
