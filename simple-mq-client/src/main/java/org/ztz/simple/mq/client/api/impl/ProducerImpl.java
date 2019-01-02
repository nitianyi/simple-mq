package org.ztz.simple.mq.client.api.impl;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;

import org.simple.mq.api.tools.IdGenerator;
import org.simple.mq.api.tools.PrintExceptionStacktrace;
import org.ztz.simple.mq.api.dto.SimpleMsgRequest;
import org.ztz.simple.mq.api.enums.MsgTypeEnum;
import org.ztz.simple.mq.client.api.Producer;
import org.ztz.simple.mq.client.api.SimpleMsgClientContext;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProducerImpl implements Producer {

	/** 
	 * 初始化参数也可以从配置读取
	 */
	private BlockingDeque<String[]> msgs = new LinkedBlockingDeque<>(10000);
	
	private static volatile ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
	
	public ProducerImpl () {
		executor.execute(() -> {
			String topic = "", m = "";
			while (true) {
				try {
					String[] msgArr = msgs.take();
					topic = msgArr[0];
					m = msgArr[1];
					sendMsg(topic, m);
				} catch (Exception e) {
					log.info("Error when executing tasks({}-{}) ->{}", topic, m, PrintExceptionStacktrace.getStacktrace(e));
				}
			}
		});
	}
	
	@Override
	public boolean sendMsg(String topic, String msg) {
		SimpleMsgRequest request = getMsgRequest(topic, msg);
		getChannel().write(request).addListener(new GenericFutureListener<Future<? super Void>>() {

			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				if (future.isSuccess()) {
					log.info("msg ->{} has been sent successfully", request);
				} else {
					log.error("msg ->{} fails to be sent, retry later", request);
					sendMsgAsync(topic, msg);
				}
				
			}
		});
		
		return Boolean.TRUE;
	}

	@Override
	public boolean sendMsgAsync(String topic, String msg) {
		msgs.push(new String[] {topic, msg});
		return Boolean.TRUE;
	}
	
	private SimpleMsgRequest getMsgRequest(String topic, String msg) {
		return SimpleMsgRequest.of(IdGenerator.generate(), msg, topic, MsgTypeEnum.PRODUCE);
	}
	
	private Channel getChannel() {
		return SimpleMsgClientContext.CONTEXT.getChannelWithLB();
	}

}
