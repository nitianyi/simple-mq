package org.ztz.simple.mq.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CoreTransport {

	private int port;
	
	private static Logger logger = LoggerFactory.getLogger(CoreTransport.class);
	
	public CoreTransport(int port) {
		this.port = port;
	}
	
	public void startup() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(port)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel channel) throws Exception {
						// 添加业务处理器
						channel.pipeline().addLast(new TransportHandler());
					}
			});
			ChannelFuture future = bootstrap.bind().sync();
			logger.info("Server starts successfully and listen on {}", future.channel().localAddress());
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("error when server running,cause->{}", e.getMessage());
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new CoreTransport(65456).startup();
	}
}
