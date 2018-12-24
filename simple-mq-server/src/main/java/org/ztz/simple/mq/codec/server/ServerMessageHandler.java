package org.ztz.simple.mq.codec.server;

import org.ztz.simple.mq.api.dto.SimpleMsgRequest;
import org.ztz.simple.mq.api.dto.SimpleMsgResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<SimpleMsgRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgRequest msg) throws Exception {
		log.info("server-side receives msg->{}", msg);
		//TODO process logic
		ctx.writeAndFlush(new SimpleMsgResponse(msg.getMsgId()));
//		ctx.channel().writeAndFlush("propcessed msg->" + msg.getMsgId() + " successfully");
	}

}
