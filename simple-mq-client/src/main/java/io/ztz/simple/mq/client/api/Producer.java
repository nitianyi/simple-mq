package io.ztz.simple.mq.client.api;

/**
 * 
 * @author Tony
 *
 */
public interface Producer {
	
	/**
	 * 同步发送消息
	 * @param topic
	 * @param msg
	 * @return
	 */
	public boolean sendMsg(String topic, String msg);
	
	/**
	 * 异步发送消息
	 * @param topic
	 * @param msg
	 * @return
	 */
	public boolean sendMsgAsync(String topic, String msg);
}
