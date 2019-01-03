package io.ztz.simple.mq.codec.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<SimpleMsgRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgRequest msg) throws Exception {
		log.info("server-side receives msg->{}", msg);
		
		//TODO process logic
		ctx.writeAndFlush(new SimpleMsgResponse(msg.getMsgId(), ""));
	}

}
