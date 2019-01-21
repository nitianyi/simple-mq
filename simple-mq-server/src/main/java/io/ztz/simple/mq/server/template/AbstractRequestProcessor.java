package io.ztz.simple.mq.server.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;
import io.ztz.simple.mq.server.SimpleMQServerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public abstract class AbstractRequestProcessor implements RequestProcessor {

	@Autowired
	protected SimpleMQServerContext context;
	
	public SimpleMsgResponse process(SimpleMsgRequest request) {
		log.info("receive request->{}", request);
		// TODO some validation, debug, monitor code
		
		return execute(request);
	}

	/** 
	 * To extend
	 * @param request
	 * @return
	 */
	protected abstract SimpleMsgResponse execute(SimpleMsgRequest request);
}
