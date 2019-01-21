package io.ztz.simple.mq.server.template;

import io.ztz.simple.mq.api.dto.SimpleMsgRequest;
import io.ztz.simple.mq.api.dto.SimpleMsgResponse;

public interface RequestProcessor {

	SimpleMsgResponse process(SimpleMsgRequest request);
}
