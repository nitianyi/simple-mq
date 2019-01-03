package io.ztz.simple.mq.client.transport;

import static io.ztz.simple.mq.client.api.SimpleMsgClientContext.CONTEXT;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.enums.MsgTypeEnum;
import io.ztz.simple.mq.client.api.ClientEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientTransport implements ClientEngine {

	private final String host;
	
	private final int port;
	
	private EventLoopGroup group;
	
	private Channel channel;

	public ClientTransport(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception {
		group = new NioEventLoopGroup();
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_REUSEADDR, true)
			.remoteAddress(host, port)
			.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline()
						.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
						.addLast(new LengthFieldPrepender(2))
						.addLast(new MessageDecoder())
						.addLast(new MessageEncoder())
						.addLast(new ClientMessageHandler());
				}
			});
		
		ChannelFuture future = bootstrap.connect().sync();
		channel = future.channel();
		CONTEXT.cacheChannel(host + ":" + port, channel);
		log.info("client has succeeded connecting servicer-({}:{})", host, port);
		prepareMsg(future.channel());
	}
	
	void prepareMsg(Channel channel) {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			channel.write(SimpleMsgRequest.of(String.valueOf(i), "Msg" + i, "Topic_" + i, MsgTypeEnum.PRODUCE));
		});
		channel.flush();
		log.info("finish sending msgs");
	}
	
	public static void main(String[] args) throws Exception {
		new ClientTransport("localhost", 65456).start();
		TimeUnit.SECONDS.sleep(10);
	}

	@Override
	public void close() throws Exception {
		if (channel != null && channel.isActive()) {
			channel.close().sync();
		}
		
		if (group != null && !group.isShutdown()) {
			group.shutdownGracefully().sync();
		}
	}
	
}
