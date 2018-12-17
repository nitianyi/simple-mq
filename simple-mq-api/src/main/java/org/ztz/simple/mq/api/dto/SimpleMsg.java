package org.ztz.simple.mq.api.dto;

import java.io.Serializable;

import org.msgpack.annotation.Message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Message
@SuppressWarnings("serial")
public class SimpleMsg implements Serializable {

	private String msgId;
	
	private String msg;
	
	private String top;
}
