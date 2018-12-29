package org.ztz.simple.mq.client.api;

/**
 * 
 * @author Tony
 *
 */
public interface Consumer {

	public String pull(String topic);
	
	public String pull(String topic,  long timeout);
	
}
