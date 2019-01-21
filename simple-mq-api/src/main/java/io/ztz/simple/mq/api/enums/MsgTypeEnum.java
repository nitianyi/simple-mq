/**
 * 
 */
package io.ztz.simple.mq.api.enums;

import org.msgpack.annotation.Message;

/**
 * @author Lenovo
 *
 */
@Message
public enum MsgTypeEnum {

	Send, Send_Resp, Consume, Consume_resp, Ping, Pong;
}
