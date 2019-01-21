package io.ztz.simple.mq.client.api.impl;

import static io.ztz.simple.mq.api.tools.PrintExceptionStacktrace.getStacktrace;
import static io.ztz.simple.mq.client.api.SimpleMsgClientContext.CONTEXT;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import io.netty.channel.Channel;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.api.enums.MsgTypeEnum;
import io.ztz.simple.mq.api.tools.IdGenerator;
import io.ztz.simple.mq.client.api.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ConsumerImpl implements Consumer {

	private static final String clientId;
	
	static {
		clientId = "";
	}
	
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
		SimpleMsgRequest request = SimpleMsgRequest.of(reqId, "", topic, MsgTypeEnum.Consume);
		request.setClientId("");
		request.setTimeout(timeout);
		request.setTimestamp(System.currentTimeMillis());
		return request;
	}
	
	private Channel getChannel() {
		return CONTEXT.getChannelWithLB();
	}

}
