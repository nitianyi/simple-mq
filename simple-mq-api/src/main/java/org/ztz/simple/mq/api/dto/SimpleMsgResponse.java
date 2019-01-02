package org.ztz.simple.mq.api.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class SimpleMsgResponse implements Serializable {

	@NonNull
	private String msgId;
	
	@NonNull
	private String msg;
}
