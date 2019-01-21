package io.ztz.simple.mq.server.store;

import io.ztz.simple.mq.api.dto.SimpleMsg;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;

public interface StoreEngine {

	SimpleMsgResponse saveMsg(String topic, SimpleMsg data);
	
	SimpleMsgResponse getMsg(String topic, String group, long timestamp, long timeout);
	
}
