package org.simple.mq.io.serialize;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * 序列化工厂
 * @author Tony
 *
 */
@Component
public class SerializerFactory implements ApplicationContextAware {

	private ApplicationContext context;
	
	private Map<String, Serializer> serializers = Maps.newConcurrentMap();
	
	public Serializer getSerializer(SerializeProtocolEnum protocol) {
		SerializeProtocolEnum defaultEnum = Optional.ofNullable(protocol).orElse(SerializeProtocolEnum.Protostuff);
		return serializers.get(defaultEnum.getServiceKey());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	@PostConstruct
	public void init() {
		serializers = context.getBeansOfType(Serializer.class);
	}

}
