package org.ztz.simple.mq.api.dto;

import java.io.Serializable;

import org.ztz.simple.mq.api.enums.MsgTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class BaseMsg implements Serializable {

	@NonNull
	protected MsgTypeEnum msgType;
}
