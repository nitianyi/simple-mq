package org.ztz.simple.mq.client.api.impl;

import static org.ztz.simple.mq.client.api.SimpleMsgClientContext.CONTEXT;
import static org.simple.mq.api.tools.PrintExceptionStacktrace.getStacktrace;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.simple.mq.api.tools.IdGenerator;
import org.springframework.stereotype.Service;
import org.ztz.simple.mq.api.dto.SimpleMsgRequest;
import org.ztz.simple.mq.api.dto.SimpleMsgResponse;
import org.ztz.simple.mq.api.enums.MsgTypeEnum;
import org.ztz.simple.mq.client.api.Consumer;
import org.ztz.simple.mq.client.api.SimpleMsgClientContext;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumerImpl implements Consumer {

	
	
	@Override
	public SimpleMsgResponse pull(String topic) {
		return pull(topic, 0);
	}

	@Override
	public SimpleMsgResponse pull(String topic, long timeout) {
		String reqId = IdGenerator.generate();
		SimpleMsgRequest request = buildConsumeRequest(reqId, topic, timeout);
		SynchronousQueue<SimpleMsgResponse> resp = CONTEXT.waitforMsgResponse(reqId);
		try {
			getChannel().writeAndFlush(request);
			
			if (timeout > 0) {
				return resp.poll(timeout, TimeUnit.MILLISECONDS);
			}
			
			return resp.take();
		} catch (Exception e) {
			log.error("error when consuming msg, ->cause->{}", getStacktrace(e));
			throw new RuntimeException(e);
		}
	}
	
	private SimpleMsgRequest buildConsumeRequest(String reqId, String topic, long timeout) {
		SimpleMsgRequest request = SimpleMsgRequest.of(reqId, "", topic, MsgTypeEnum.CONSUME);
		request.setTimeout(timeout);
		request.setTimestamp(System.currentTimeMillis());
		return request;
	}
	
	private Channel getChannel() {
		return SimpleMsgClientContext.CONTEXT.getChannelWithLB();
	}

}