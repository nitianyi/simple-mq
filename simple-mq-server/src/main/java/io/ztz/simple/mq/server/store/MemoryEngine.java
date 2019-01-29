package io.ztz.simple.mq.server.store;

import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import io.ztz.simple.mq.api.dto.DelayedSimpleMsg;
import io.ztz.simple.mq.api.dto.SimpleMsg;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Repository
public class MemoryEngine implements StoreEngine {

	private ConcurrentMap<String, BlockingDeque<SimpleMsg>> queueDB = Maps.newConcurrentMap();
	
	private DelayQueue<DelayedSimpleMsg> aliveMsgQueue = new DelayQueue<>();
	{
		new Thread(() -> {
			while (true) {
				// TODO 
				log.debug("The length of queueDB is {}", queueDB.size());
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public SimpleMsgResponse saveMsg(String topic, SimpleMsg data) {
		if (Strings.isNullOrEmpty(topic)) {
			return new SimpleMsgResponse(data.getMsgId(), 401, "Invalid param");
		}
		
		BlockingDeque<SimpleMsg> queue = null;
		if (!queueDB.containsKey(topic)) {
			queue = new LinkedBlockingDeque<SimpleMsg>();
			queueDB.putIfAbsent(topic, queue);
		} else {
			queue = queueDB.get(topic);
		}
		
		queue.push(data);
		return new SimpleMsgResponse(data.getMsgId());
	}

	@Override
	public SimpleMsgResponse getMsg(String topic, String group, long timestamp, long timeout) {
		// TODO get by group
		log.debug("input params->{} -{} -{} -{}", topic, group, timestamp, timeout);
		BlockingDeque<SimpleMsg> queue = queueDB.get(topic);
		if (queue == null) {
			log.warn("The request topic ->{} not exists...");
			return null;
		}
		
		long now = System.currentTimeMillis();
		long timeMillisToWait = timeout - (now - timestamp);
		SimpleMsg msg = null;
		try {
			
			if (timeMillisToWait > 0) {
				msg = queue.poll(timeMillisToWait, TimeUnit.MILLISECONDS);
			} else {
				msg = queue.takeFirst();
			}
			
		} catch (InterruptedException e) {
			log.warn("error when poll msg", e);
		}
		return Optional.ofNullable(msg).map(m -> new SimpleMsgResponse(m.getMsgId(), m.getData()))
				.orElseGet(() -> new SimpleMsgResponse(404, "No message exists"));
	}

}
