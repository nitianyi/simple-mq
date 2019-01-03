package io.ztz.simple.mq.client.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TransportHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private static final Logger logger = LoggerFactory.getLogger(TransportHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("异常了，cause->{}", cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		ctx.write(Unpooled.copiedBuffer("netty client", CharsetUtil.UTF_8));
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		logger.info("客户端收到消息 ->{}", ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes())));
		System.out.println("客户端收到消息 " + ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes())));
	}

}
