package io.ztz.simple.mq.client.api;

import io.ztz.simple.mq.api.dto.SimpleMsgResponse;

/**
 * 
 * @author Tony
 *
 */
public interface Consumer {

	public SimpleMsgResponse pull(String topic);
	
	public SimpleMsgResponse pull(String topic,  long timeout);
	
}
