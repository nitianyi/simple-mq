package io.ztz.simple.mq.api.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class SimpleMsg implements Serializable {

	@NonNull
	String topic;
	
	@NonNull
	String msgId;
	
	@NonNull
	String data;
	
	long timeToLive; // unit -> millisecond
}
