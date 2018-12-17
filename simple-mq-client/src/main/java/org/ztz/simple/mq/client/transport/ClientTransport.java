package org.ztz.simple.mq.client.transport;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
						ch.pipeline().addLast(new TransportHandler());
					}
				});
			
			ChannelFuture future = bootstrap.connect().sync();
			future.channel().closeFuture().sync();
		} finally {
		  group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ClientTransport("localhost", 65456).start();
//		TimeUnit.SECONDS.sleep(5);
	}
}
