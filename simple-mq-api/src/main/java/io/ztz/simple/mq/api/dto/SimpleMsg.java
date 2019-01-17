package io.ztz.simple.mq.api.dto;

import java.io.Serializable;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class SimpleMsg implements Serializable {

	String topic;
	
	String msgId;
	
	String data;
	
	long timeToLive; // unit -> millisecond
}
