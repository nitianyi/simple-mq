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
public class SimpleMsgResponse implements Serializable {

	/**
	 * 
	 * @param requestId 请求id
	 */
	public SimpleMsgResponse(String requestId) {
		
		this.requestId = requestId;
		
	}
	
	/**
	 * 
	 * @param msgId
	 * @param msg
	 */
	public SimpleMsgResponse(String msgId, String msg) {
		
		this.msgId = msgId;
		
		this.msg = msg;
	}
	
	/**
	 * 
	 * @param requestId 请求id
	 * @param status 状态码
	 * @param reason 原因
	 */
	public SimpleMsgResponse(String requestId, int status, String reason) {
		
		this.requestId = requestId;
		
		this.status = status;
		
		this.reason = reason;
	}
	
	@NonNull
	private String requestId;
	
	@NonNull
	private String msgId;
	
	@NonNull
	private String msg;
	
	private int status;
	
	private String reason = "OK";
}
