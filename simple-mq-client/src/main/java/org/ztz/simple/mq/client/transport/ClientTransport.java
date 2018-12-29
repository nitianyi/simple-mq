package org.ztz.simple.mq.client.transport;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.ztz.simple.mq.api.dto.SimpleMsgRequest;
import org.ztz.simple.mq.api.enums.MsgTypeEnum;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientTransport {

	private final String host;
	
	private final int port;

	public ClientTransport(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
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
			prepareMsg(future.channel());
//			future.channel().closeFuture().sync();
		} finally {
//		  group.shutdownGracefully();
		}
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
	
}
