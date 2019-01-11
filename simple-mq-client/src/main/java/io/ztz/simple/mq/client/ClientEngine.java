package io.ztz.simple.mq.client;

/**
 * 
 * @author tony
 *
 */
public interface ClientEngine {

	public void start() throws Exception;
	
	public void close() throws Exception;
}
