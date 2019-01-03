package io.ztz.simple.mq.api.dto;

import java.io.Serializable;

import org.msgpack.annotation.Message;

import io.ztz.simple.mq.api.enums.MsgTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Message
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor(staticName = "of")
@SuppressWarnings("serial")
public class SimpleMsgRequest implements Serializable {

	@NonNull
	private String msgId;
	
	@NonNull
	private String msg;
	
	@NonNull
	private String topic;
	
	@NonNull
	protected MsgTypeEnum msgType;
	
	private long timeout;
	
	private long timestamp;
	
}
