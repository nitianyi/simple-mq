package org.simple.mq.io.serialize;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class BaseTest {

	protected final Objenesis objenesis = new ObjenesisStd(Boolean.TRUE);
	
	@Data
	@NoArgsConstructor
	@RequiredArgsConstructor
	public
	static class Nothing {
		@NonNull
		String non;
		
		@NonNull
		String no;
		
		@NonNull
		int nil;
	}
}
