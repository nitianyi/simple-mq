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

	PRODUCE, PRODUCE_RESP, CONSUME, CONSUME_RESP, PING, PONG;
}
