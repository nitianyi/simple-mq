package org.ztz.simple.mq.client.api.impl;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.simple.mq.api.tools.IdGenerator;
import org.simple.mq.api.tools.PrintExceptionStacktrace;
import org.springframework.stereotype.Service;
import org.ztz.simple.mq.api.dto.SimpleMsgRequest;
import org.ztz.simple.mq.api.dto.SimpleMsgResponse;
import org.ztz.simple.mq.api.enums.MsgTypeEnum;
import org.ztz.simple.mq.client.api.Consumer;

import com.google.common.collect.Maps;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerImpl implements Consumer {

	/**
	 * 消息响应容器，阻塞等待
	 */
	private ConcurrentMap<String, SynchronousQueue<SimpleMsgResponse>> respContainer = Maps.newConcurrentMap();
	
	@Override
	public SimpleMsgResponse pull(String topic) {
		return pull(topic, 0);
	}

	@Override
	public SimpleMsgResponse pull(String topic, long timeout) {
		String reqId = IdGenerator.generate();
		SimpleMsgRequest request = getConsumeRequest(reqId, topic, timeout);
		SynchronousQueue<SimpleMsgResponse> resp = new SynchronousQueue<>();
		respContainer.putIfAbsent(reqId, resp);
		try {
			getChannel().writeAndFlush(request);
			
			if (timeout > 0) {
				return resp.poll(timeout, TimeUnit.MILLISECONDS);
			}
			
			return resp.take();
		} catch (Exception e) {
			log.error("error when consuming msg, ->cause->{}", PrintExceptionStacktrace.getStacktrace(e));
			throw new RuntimeException(e);
		}
	}
	
	private SimpleMsgRequest getConsumeRequest(String reqId, String topic, long timeout) {
		SimpleMsgRequest request = SimpleMsgRequest.of(reqId, "", topic, MsgTypeEnum.CONSUME);
		request.setTimeout(timeout);
		return request;
	}
	
	private Channel getChannel() {
		return null;
	}

}
