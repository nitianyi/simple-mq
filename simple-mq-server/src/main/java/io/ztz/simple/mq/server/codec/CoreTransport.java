package io.ztz.simple.mq.server.codec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.ztz.simple.mq.server.SimpleMQServerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoreTransport implements InitializingBean {

	@Autowired
	private Environment env;
	
	@Autowired
	private SimpleMQServerContext context;
	
	public void startup() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(Integer.valueOf(env.getProperty("server.msg.port")))
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel channel) throws Exception {
						// 添加业务处理器
						channel.pipeline()
							.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
							.addLast(new MessageDecoder())
							.addLast(new LengthFieldPrepender(2))
							.addLast(new MessageEncoder())
							.addLast(new ServerMessageHandler(context));
					}
			});
			ChannelFuture future = bootstrap.bind().sync();
			log.info("Server starts successfully and listen on {}", future.channel().localAddress());
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("error when server running,cause->{}", e.getMessage());
		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startup();
	}
	
}
