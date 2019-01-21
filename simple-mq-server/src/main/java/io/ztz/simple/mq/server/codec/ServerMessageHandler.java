package io.ztz.simple.mq.server.codec;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.server.SimpleMQServerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServerMessageHandler extends SimpleChannelInboundHandler<SimpleMsgRequest> {
	
	private SimpleMQServerContext context;
	
	public ServerMessageHandler(SimpleMQServerContext ctx) {
		this.context = ctx;
	}
	
	private static final ExecutorService POOL = Executors.newWorkStealingPool(); 

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SimpleMsgRequest msg) throws Exception {
		log.info("server-side receives msg->{}", msg);
		
		POOL.execute(() -> {
			SimpleMsgResponse resp = context.chooseRequestProcessor(msg.getMsgType()).process(msg);
			Optional.ofNullable(resp).ifPresent(r -> r.setMsgId(msg.getMsgId()));
			ctx.writeAndFlush(resp);
		});
	}

}
