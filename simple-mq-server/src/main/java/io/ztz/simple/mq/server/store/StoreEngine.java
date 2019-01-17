package io.ztz.simple.mq.server.store;

import io.ztz.simple.mq.api.dto.SimpleMsg;
import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;

public interface StoreEngine {

	SimpleMsgRequest saveMsg(String topic, SimpleMsg data);
	
	SimpleMsgResponse getMsg(String topic, String group, long timestamp, long timeout);
	
}
