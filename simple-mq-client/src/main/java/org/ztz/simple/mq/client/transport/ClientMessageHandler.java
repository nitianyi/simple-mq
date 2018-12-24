package org.ztz.simple.mq.client.transport;

import org.ztz.simple.mq.api.dto.SimpleMsgResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<SimpleMsgResponse> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgResponse msg) throws Exception {
		log.debug("receives server msg resp->{}", msg);
	}

}
