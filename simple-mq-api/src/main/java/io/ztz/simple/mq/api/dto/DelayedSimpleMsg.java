package io.ztz.simple.mq.api.dto;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DelayedSimpleMsg implements Delayed {

	@NonNull
	private SimpleMsg msg;
	
	private long timeout;
	
	public DelayedSimpleMsg(SimpleMsg msg) {
		this.msg = msg;
		this.timeout = now() + msg.getTimeToLive();
	}
	
	@Override
	public int compareTo(Delayed o) {
		if (this == o) {
			return 0;
		}
		if (o instanceof DelayedSimpleMsg) {
			DelayedSimpleMsg ds = (DelayedSimpleMsg)o;
			return Long.compare(timeout, ds.getTimeout());
		}
		return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(timeout - now(), TimeUnit.MILLISECONDS);
	}

	private long now() {
		return System.currentTimeMillis();
	}
}
