package io.ztz.simple.mq.server.template;

import org.springframework.stereotype.Repository;

import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.server.store.StoreEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ConsumeReqProcessorTemplate extends AbstractRequestProcessor {

	@Override
	protected SimpleMsgResponse execute(SimpleMsgRequest request) {
		StoreEngine engine = context.chooseStoreEngine();
		SimpleMsgResponse resp = engine.getMsg(request.getTopic(), "", request.getTimestamp(), request.getTimeout());
		return resp;
	}

}
