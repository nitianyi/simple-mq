package io.ztz.simple.mq.client.transport;

import static io.ztz.simple.mq.client.api.SimpleMsgClientContext.CONTEXT;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<SimpleMsgResponse> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgResponse msg) throws Exception {
		
		log.debug("receives server msg resp->{}", msg);
		CONTEXT.respondToWaitQueue(msg);
	}

}
