package io.ztz.simple.mq.client.api;

/**
 * 
 * @author tony
 *
 */
public interface ClientEngine {

	public void start() throws Exception;
	
	public void close() throws Exception;
}
