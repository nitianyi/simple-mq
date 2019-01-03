package io.ztz.simple.mq.client.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.client.api.SimpleMsgClientContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<SimpleMsgResponse> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgResponse msg) throws Exception {
		// TODO 
		log.debug("receives server msg resp->{}", msg);
		SimpleMsgClientContext.CONTEXT.respondToWaitQueue(msg);
	}

}
