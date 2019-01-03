package io.ztz.simple.mq.client.api;

import static com.google.common.collect.Maps.newConcurrentMap;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import io.netty.channel.Channel;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端上下文，包含客户端运行需要的各种环境
 * @author Tony
 * 
 */
@Slf4j
public enum SimpleMsgClientContext {

	CONTEXT;
	
	/**
	 * 任务池
	 */
	private final ExecutorService service = Executors.newWorkStealingPool();
	
	/**
	 * 消息响应容器，阻塞等待
	 */
	private ConcurrentMap<String, SynchronousQueue<SimpleMsgResponse>> consumerRespContainer = newConcurrentMap();
	
	/**
	 * 客户端连接缓存，key为服务端地址
	 */
	private ConcurrentMap<String, Channel> channelCache = newConcurrentMap();
	
	/**
	 * 缓存客户端连接
	 * @param address
	 * @param channel
	 */
	public void cacheChannel(String address, Channel channel) {
		channelCache.put(address, channel);
	}
	
	/**
	 * 获得连接
	 * @return
	 */
	public Channel getChannelWithLB() {
		if (channelCache.size() == 0) {
			// TODO 发起连接或者请求注册中心获取连接
			return null;
		}
		
		int index = new Random().nextInt(100) % channelCache.size();
		return channelCache.values().toArray(new Channel[channelCache.size()])[index];
	}
	
	/**
	 * 进入队列，等待响应
	 * @param reqId
	 * @return
	 */
	public SynchronousQueue<SimpleMsgResponse> waitforMsgResponse(String reqId) {
		SynchronousQueue<SimpleMsgResponse> resp = new SynchronousQueue<>();
		consumerRespContainer.putIfAbsent(reqId, resp);
		return resp;
	}
	
	/**
	 * 响应客户端请求
	 * @param resp
	 */
	public void respondToWaitQueue(SimpleMsgResponse resp) {
		service.execute(() -> {
			Optional.ofNullable(resp).ifPresent(r -> {
				try {
					SynchronousQueue<SimpleMsgResponse> queue = consumerRespContainer.get(r.getMsgId());
					if (queue != null) {
						queue.put(r);
					}
				} catch (Exception e) {
					log.error("error when respond with msg->{}", resp);
				}
			});
		});
	}
}
