package org.ztz.simple.mq.client.api;

import org.ztz.simple.mq.api.dto.SimpleMsgResponse;

/**
 * 
 * @author Tony
 *
 */
public interface Consumer {

	public SimpleMsgResponse pull(String topic);
	
	public SimpleMsgResponse pull(String topic,  long timeout);
	
}
