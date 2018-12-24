package org.simple.mq.io.serialize;

import java.util.Arrays;

import lombok.Getter;
/**
 * 序列化协议枚举
 * @author Tony
 *
 */
@Getter
public enum SerializeProtocolEnum {

	Json("jsonSerializer"), Protostuff("messagePackSerializer"), MessagePack("protostuffSerializer");
	
	SerializeProtocolEnum(String key) {
		this.serviceKey = key;
	}
	
	private String serviceKey;
	
	public static SerializeProtocolEnum getByName(String name) {
		return Arrays.stream(SerializeProtocolEnum.values()).filter(e -> e.name().equalsIgnoreCase(name)).findAny().orElseThrow(() -> new RuntimeException("No protocol -> " + name + " exists"));
	}
	
}
