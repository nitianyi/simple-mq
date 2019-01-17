package io.ztz.simple.mq.server.template;

import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRequestProcessor {

	SimpleMsgResponse process(SimpleMsgRequest request) {
		log.info("receive request->{}", request);
		// TODO some validation op
		
		return execute(request);
	}

	/** 
	 * To extend
	 * @param request
	 * @return
	 */
	protected abstract SimpleMsgResponse execute(SimpleMsgRequest request);
}
