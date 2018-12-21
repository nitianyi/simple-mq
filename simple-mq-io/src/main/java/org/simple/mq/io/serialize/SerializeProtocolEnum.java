package org.simple.mq.io.serialize;

import lombok.Getter;

@Getter
public enum SerializeProtocolEnum {

	Json("jsonSerializer"), Protostuff("messagePackSerializer"), MessagePack("protostuffSerializer");
	
	SerializeProtocolEnum(String key) {
		this.serviceKey = key;
	}
	
	private String serviceKey;
	
}
