package org.ztz.simple.mq.client.transport;

import java.io.ByteArrayOutputStream;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.ztz.simple.mq.api.dto.SimpleMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<SimpleMsg> {

	private MessagePack msgPack = new MessagePack();
	
	@Override
	protected void encode(ChannelHandlerContext ctx, SimpleMsg msg, ByteBuf out) throws Exception {
		log.debug("received msg is ->{}", msg);
		
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			Packer packer = msgPack.createPacker(bos);
			packer.write(msg);
			
			out.writeBytes(bos.toByteArray());
			log.debug("Msg ->{} has been sent successfully");
		} catch (Exception e) {
			log.error("error when parsing message pack ->{}, cause ->{}", msg, e);
		}
	}

}
