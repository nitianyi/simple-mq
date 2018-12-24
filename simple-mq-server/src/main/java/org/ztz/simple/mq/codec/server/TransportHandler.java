package org.ztz.simple.mq.codec.server;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TransportHandler extends ChannelInboundHandlerAdapter {
	
	private final AtomicLong counter = new AtomicLong();
	
	private static Logger logger = LoggerFactory.getLogger(TransportHandler.class);

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接已注册,顺序->{}", counter.incrementAndGet());
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接取消注册,顺序->{}", counter.incrementAndGet());
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接已激活,顺序->{}", counter.incrementAndGet());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接非激活,顺序->{}", counter.incrementAndGet());
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("连接内容可读，内容->{},顺序->{}", msg, counter.incrementAndGet());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("写完刷新,顺序->{}", counter.incrementAndGet());
//		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.info("用户注册时间触发,顺序->{}", counter.incrementAndGet());
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		logger.info("通道写入状态发生改变,顺序->{}", counter.incrementAndGet());
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("异常了，原因->{}", cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

}
